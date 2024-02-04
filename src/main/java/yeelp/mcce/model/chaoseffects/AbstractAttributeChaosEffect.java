package yeelp.mcce.model.chaoseffects;

import java.util.List;
import java.util.UUID;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents.CopyFrom;
import net.minecraft.entity.attribute.AttributeContainer;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import yeelp.mcce.api.MCCEAPI;

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
			if(instance.hasModifier(factory.getInitialModifier())) {
				instance.removeModifier(factory.getUUID());
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
		this.getFactories().forEach((factory) -> {
			container.getCustomInstance(factory.getAttribute()).removeModifier(factory.getUUID());
		});
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		AttributeContainer container = player.getAttributes();
		this.getFactories().forEach((factory) -> {
			if(!factory.requiresUpdate()) {
				return;
			}
			EntityAttributeInstance instance = container.getCustomInstance(factory.getAttribute());
			EntityAttributeModifier mod = instance.getModifier(factory.getUUID());
			if(mod == null) {
				instance.addPersistentModifier(factory.getInitialModifier());
				return;
			}
			instance.removeModifier(mod.getId());
			instance.addPersistentModifier(factory.tickAttribute(player, mod));
		});
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	protected abstract List<AttributeModifierFactory> getAttributeModifierFactories();

	private final List<AttributeModifierFactory> getFactories() {
		return this.attributes == null ? this.attributes = this.getAttributeModifierFactories() : this.attributes;
	}

	protected static final class AttributeCopyFromListener implements CopyFrom {

		private final Class<? extends AbstractAttributeChaosEffect> clazz;

		public AttributeCopyFromListener(Class<? extends AbstractAttributeChaosEffect> clazz) {
			this.clazz = clazz;
		}

		@Override
		public void copyFromPlayer(ServerPlayerEntity oldPlayer, ServerPlayerEntity newPlayer, boolean alive) {
			MCCEAPI.accessor.getChaosEffect(oldPlayer, this.clazz).ifPresent((ce) -> {
				AttributeContainer oldContainer = oldPlayer.getAttributes();
				AttributeContainer newContainer = newPlayer.getAttributes();
				((AbstractAttributeChaosEffect) ce).getFactories().forEach((factory) -> {
					newContainer.getCustomInstance(factory.getAttribute()).setFrom(oldContainer.getCustomInstance(factory.getAttribute()));
				});
			});

		}

	}

	protected abstract static class AttributeModifierFactory {
		protected final EntityAttributeModifier initial;
		protected final EntityAttribute attribute;

		protected AttributeModifierFactory(EntityAttribute attribute, EntityAttributeModifier initial) {
			this.initial = initial;
			this.attribute = attribute;
		}

		protected abstract boolean requiresUpdate();

		@Nullable
		protected abstract EntityAttributeModifier tickAttribute(PlayerEntity player, EntityAttributeModifier attribute);

		protected final UUID getUUID() {
			return this.initial.getId();
		}

		protected final EntityAttribute getAttribute() {
			return this.attribute;
		}

		protected final EntityAttributeModifier getInitialModifier() {
			return this.initial;
		}
	}

}
