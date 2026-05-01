package yeelp.mcce.model.chaoseffects;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.CopyFrom;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import yeelp.mcce.api.MCCEAPI;

import java.util.List;
import java.util.Objects;

public abstract class AbstractAttributeChaosEffect extends AbstractTimedChaosEffect {

	protected AbstractAttributeChaosEffect(int durationMin, int durationMax) {
		super(durationMin, durationMax);
	}

	private List<AttributeModifierFactory> attributes;

    @Override
	public void applyEffect(PlayerEntity player) {
		AttributeContainer container = player.getAttributes();
		this.getFactories().forEach((factory) -> {
			EntityAttributeInstance instance = container.getCustomInstance(factory.getAttribute());
			if(Objects.requireNonNull(instance).hasModifier(factory.getInitialModifier().id())) {
				instance.removeModifier(factory.getID());
			}
			instance.addPersistentModifier(factory.getInitialModifier());
		});
	}

	@Override
	public void registerCallbacks() {
		ServerPlayerEvents.COPY_FROM.register(new AttributeCopyFromListener(this.getClass()));
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		AttributeContainer container = player.getAttributes();
		this.getFactories().forEach((factory) -> Objects.requireNonNull(container.getCustomInstance(factory.getAttribute())).removeModifier(factory.getID()));
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		this.getFactories().forEach((factory) -> {
			if(!factory.requiresUpdate()) {
				return;
			}
			EntityAttributeInstance instance = player.getAttributeInstance(factory.getAttribute());
			EntityAttributeModifier mod = Objects.requireNonNull(instance).getModifier(factory.getID());
			if(mod == null) {
				instance.addPersistentModifier(factory.getInitialModifier());
				return;
			}
			instance.removeModifier(mod.id());
			instance.addPersistentModifier(factory.tickAttribute(player, mod));
		});
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	protected abstract List<AttributeModifierFactory> getAttributeModifierFactories();

	private List<AttributeModifierFactory> getFactories() {
		return this.attributes == null ? this.attributes = this.getAttributeModifierFactories() : this.attributes;
	}

	protected static final class AttributeCopyFromListener implements CopyFrom {

		private final Class<? extends AbstractAttributeChaosEffect> clazz;

		public AttributeCopyFromListener(Class<? extends AbstractAttributeChaosEffect> clazz) {
			this.clazz = clazz;
		}

		@Override
		public void copyFromPlayer(@NotNull ServerPlayerEntity oldPlayer, @NotNull ServerPlayerEntity newPlayer, boolean alive) {
			MCCEAPI.accessor.getChaosEffect(oldPlayer, this.clazz).ifPresent((ce) -> {
				AttributeContainer oldContainer = oldPlayer.getAttributes();
				AttributeContainer newContainer = newPlayer.getAttributes();
				((AbstractAttributeChaosEffect) ce).getFactories().forEach((factory) -> {
					EntityAttributeInstance newInstance = Objects.requireNonNull(newContainer.getCustomInstance(factory.getAttribute()));
					EntityAttributeInstance oldInstance = Objects.requireNonNull(oldContainer.getCustomInstance(factory.getAttribute()));
					EntityAttributeModifier mod = Objects.requireNonNull(oldInstance).getModifier(factory.getID());
					if(mod != null) {
						newInstance.addPersistentModifier(mod);
					}
				});
			});

		}

	}

	protected abstract static class AttributeModifierFactory {
		protected final EntityAttributeModifier initial;
		protected final RegistryEntry<EntityAttribute> attribute;

		protected AttributeModifierFactory(RegistryEntry<EntityAttribute> attribute, EntityAttributeModifier initial) {
			this.initial = initial;
			this.attribute = attribute;
		}

		protected abstract boolean requiresUpdate();

		protected abstract EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute);

		protected final Identifier getID() {
			return this.initial.id();
		}

		protected final RegistryEntry<EntityAttribute> getAttribute() {
			return this.attribute;
		}

		protected final EntityAttributeModifier getInitialModifier() {
			return this.initial;
		}
	}

}
