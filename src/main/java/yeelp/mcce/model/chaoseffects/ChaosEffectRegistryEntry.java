package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public final class ChaosEffectRegistryEntry {

    private final Supplier<? extends ChaosEffect> generator;
    private final String name;
    private final String displayName;
    private final boolean validForRepeating;
    private final ChaosEffect instance;
    private final BooleanSupplier isOptionalAndEnabled;

    public ChaosEffectRegistryEntry(Supplier<? extends ChaosEffect> generator, boolean validForRepeating) {
        this(generator, generator.get(), validForRepeating);
    }

    public ChaosEffectRegistryEntry(Supplier<? extends ChaosEffect> generator) {
        this(generator, false);
    }

    public ChaosEffectRegistryEntry(Supplier<? extends ChaosEffect> generator, ChaosEffect instance, boolean validForRepeating) {
        this.generator = generator;
        this.validForRepeating = validForRepeating;
        this.instance = instance;
        this.name = instance.getName();
        this.displayName = instance.getDisplayName();
        this.isOptionalAndEnabled = this.instance instanceof OptionalEffect o ? o::enabled : () -> true;
    }

    public ChaosEffectRegistryEntry(Supplier<? extends ChaosEffect> generator, ChaosEffect instance) {
        this(generator, instance, false);
    }

    public ChaosEffect createChaosEffect() {
        return this.generator.get();
    }

    public boolean is(ChaosEffect effect) {
        return this.name.equals(effect.getName());
    }

    public String getName() {
        return this.name;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public boolean isValidForRepeating() {
        return this.validForRepeating;
    }

    public boolean isEnabled() {
        return this.isOptionalAndEnabled.getAsBoolean();
    }

    public boolean isApplicable(PlayerEntity player) {
        return this.instance.applicable(player);
    }

    public void registerCallbacks() {
        this.instance.registerCallbacks();
    }
}
