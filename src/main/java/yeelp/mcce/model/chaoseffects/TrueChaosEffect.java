package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.SimpleUtil;


public final class TrueChaosEffect extends SimpleTimedChaosEffect implements PlayerTickCallback {

    private static final int DURATION_MIN = 1000, DURATION_MAX = 2000;
    private static final int THRESHOLD_MIN = 10, THRESHOLD_MAX = 20;
    private static final int KILL_RADIUS = 32;
    private static final float PERCENT_APPLY_CHANCE = 0.6f;

    private boolean hasDied = false;
    private int deaths = 0;

    private static final String HAS_DIED_KEY = "hasDied", DEATHS_KEY = "deaths";

    public TrueChaosEffect() {
        super(DURATION_MIN, DURATION_MAX);
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return this.getRNG().nextFloat() < PERCENT_APPLY_CHANCE;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        MCCEAPI.mutator.modifyEffectState(player, (pces) -> {
            int newDuration;
            if(pces.getDurationUntilNextEffect() > (newDuration = this.getRNG().nextInt(THRESHOLD_MIN, THRESHOLD_MAX))) {
                pces.setDurationUntilNextEffect(newDuration);
            }
        });
    }

    @Override
    public String getName() {
        return "truechaos";
    }

    @Override
    public NbtCompound writeToNbt() {
        NbtCompound nbt = super.writeToNbt();
        nbt.putBoolean(HAS_DIED_KEY, this.hasDied);
        nbt.putInt(DEATHS_KEY, this.deaths);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.hasDied = nbt.getBoolean(HAS_DIED_KEY);
        this.deaths = nbt.getInt(DEATHS_KEY);
    }

    @Override
    public void registerCallbacks() {
        PlayerTickCallback.EVENT.register(this);
    }

    @Override
    public boolean canBeFirstEffect() {
        return false;
    }

    @Override
    public void tick(PlayerEntity player) {
        if(player.getWorld().isClient || !(player instanceof ServerPlayerEntity)) {
            return;
        }
        if(player.isDead() && MCCEAPI.accessor.getChaosEffect(player, TrueChaosEffect.class).filter((e) -> !e.hasDied).isPresent()) {
            MCCEAPI.mutator.modifyEffect(player, TrueChaosEffect.class, (effect) -> {
                effect.hasDied = true;
                if(++effect.deaths >= 2) {
                    ServerWorld world = SimpleUtil.getServerWorldFromEntity(player);
                    world.getEntitiesByClass(Entity.class, ChaosLib.getBoxCenteredOnPlayerWithRadius(player, KILL_RADIUS), (e) -> !(e instanceof PlayerEntity)).forEach((e) -> e.kill(world));
                    MCCEAPI.mutator.clear(player);
                }
            });
        }
        else {
            MCCEAPI.mutator.modifyEffect(player, TrueChaosEffect.class, (effect) -> effect.hasDied = player.isDead());
        }
    }
}
