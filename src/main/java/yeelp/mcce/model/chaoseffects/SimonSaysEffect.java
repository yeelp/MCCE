package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.text.Text;
import yeelp.mcce.MCCE;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.SimpleUtil;
import yeelp.mcce.util.Tracker;

import java.util.function.Predicate;

public final class SimonSaysEffect extends AbstractTriggeredChaosEffect {

    private static final int DURATION = 70;
    private static final float DAMAGE_AMOUNT = 2048.0f;
    private static final Tracker HAS_JUMPED = new Tracker();
    private static final String INTSTRUCTION_TYPE_KEY = "instruction";
    private static final RegistryKey<DamageType> SIMON_SAYS_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, MCCE.createIdentifier("simonsays"));
    private static final RegistryKey<DamageType> DISOBEY_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, MCCE.createIdentifier("disobey"));
    private static DamageSource simonSaysSource, disobeySource;
    private InstructionType type;

    private enum InstructionType {
        JUMP(HAS_JUMPED::tracked),
        SNEAK(PlayerEntity::isSneaking);

        private final Predicate<PlayerEntity> confirmation;
        private final String name;

        InstructionType(Predicate<PlayerEntity> confirmation) {
            this.confirmation = confirmation;
            char[] cs = this.name().toLowerCase().toCharArray();
            cs[0] = Character.toTitleCase(cs[0]);
            this.name = new String(cs);
        }

        boolean didFollowInstruction(PlayerEntity player) {
            return this.confirmation.test(player);
        }

        @Override
        public String toString() {
            return this.name;
        }

        byte encode() {
            return (byte) (this.ordinal() + 1);
        }

        static InstructionType decode(byte b) {
            int ordinal = b - 1;
            if(ordinal >= 0 && ordinal < InstructionType.values().length) {
                return InstructionType.values()[ordinal];
            }
            return ChaosLib.getRandomElementFrom(InstructionType.values(), ChaosLib.getStaticRandomInstance());
        }

        InstructionType getOpposite() {
            return this == SNEAK ? JUMP : SNEAK;
        }
    }

    public SimonSaysEffect() {
        super(DURATION, DURATION, 1);
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return !player.isSneaking();
    }

    @Override
    public NbtCompound writeToNbt() {
        NbtCompound nbt = super.writeToNbt();
        nbt.putByte(INTSTRUCTION_TYPE_KEY, this.type != null ? this.type.encode() : 0);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.type = InstructionType.decode(nbt.getByte(INTSTRUCTION_TYPE_KEY));
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        if(this.getTriggersRemaining() <= 0) {
            return;
        }
        if(this.type.didFollowInstruction(player)) {
            this.trigger();
        }
        if(this.type.getOpposite().didFollowInstruction(player)) {
            PlayerUtils.getServerPlayer(player).ifPresent((p) -> {
                this.trigger();
                p.damage(SimpleUtil.getServerWorldFromEntity(p), getDisobeySource(p.getRegistryManager()), DAMAGE_AMOUNT);
            });
        }
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        if(MCCEAPI.accessor.areAnyChaosEffectsActive(player, ChaosEffects.PRESS_L_TO_LEVITATE, ChaosEffects.CLIPPY, ChaosEffects.SHAKEWEIGHT, ChaosEffects.PING_PONG, ChaosEffects.TO_THE_MOON)) {
            this.type = InstructionType.SNEAK;
        }
        else {
            this.type = ChaosLib.getRandomElementFrom(InstructionType.values(), this.getRNG());
        }
        player.sendMessage(Text.literal("%s or perish!".formatted(this.type)), true);
    }

    @Override
    public String getName() {
        return "simonsays";
    }

    @Override
    public void registerCallbacks() {
        //no callbacks
    }

    @Override
    public void onEffectEnd(PlayerEntity player) {
        HAS_JUMPED.remove(player);
        if(this.getTriggersRemaining() > 0) {
            PlayerUtils.getServerPlayer(player).ifPresent((p) -> p.damage(SimpleUtil.getServerWorldFromEntity(p), getSimonSaysSource(p.getRegistryManager()), DAMAGE_AMOUNT));
        }
    }

    @Override
    public void onEffectRemoved(PlayerEntity player) {
        HAS_JUMPED.remove(player);
    }

    public static void trackJump(PlayerEntity player) {
        HAS_JUMPED.add(player);
    }

    private static DamageSource getSimonSaysSource(DynamicRegistryManager manager) {
        return simonSaysSource != null ? simonSaysSource : (simonSaysSource = new DamageSource(manager.getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(SIMON_SAYS_DAMAGE)));
    }

    private static DamageSource getDisobeySource(DynamicRegistryManager manager) {
        return disobeySource != null ? disobeySource : (disobeySource = new DamageSource(manager.getOrThrow(RegistryKeys.DAMAGE_TYPE).getOrThrow(DISOBEY_DAMAGE)));
    }
}
