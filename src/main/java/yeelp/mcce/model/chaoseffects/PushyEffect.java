package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.ClientSideSyncHandler;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;
import yeelp.mcce.network.PushyPayload;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.Tracker;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public final class PushyEffect extends AbstractTimedChaosEffect {
    private static final int DURATION_MIN = 1500, DURATION_MAX = 1700;
    private static final String VELOCITY_KEY = "velocity", LOCKED_KEY = "locked";
    private static final Map<UUID, PushyPayload> ACTIVE_EFFECTS = Maps.newHashMap();
    private static final Tracker TRACKER = new Tracker();
    private boolean locked;
    private Vec3d direction;

    public PushyEffect() {
        super(DURATION_MIN, DURATION_MAX);
        this.locked = this.getRNG().nextBoolean();
        this.direction = this.getRandomDirection();
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.SLUGGISH, ChaosEffects.GOTTA_BLAST);
    }

    @Override
    public NbtCompound writeToNbt() {
        NbtCompound nbt = super.writeToNbt();
        nbt.put(VELOCITY_KEY, Vec3d.CODEC, this.direction);
        nbt.putBoolean(LOCKED_KEY, this.locked);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.direction = nbt.get(VELOCITY_KEY, Vec3d.CODEC).orElseGet(this::getRandomDirection);
        this.locked = nbt.getBoolean(LOCKED_KEY).orElseGet(this.getRNG()::nextBoolean);
    }

    @Override
    public void onEffectEnd(PlayerEntity player) {
        PlayerUtils.getServerPlayer(player).ifPresent(new PushyPayload(false, null)::send);
        TRACKER.remove(player);
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        PlayerUtils.getServerPlayer(player).ifPresent(new PushyPayload(this.locked, this.direction)::send);
        TRACKER.add(player);
    }

    @Override
    public String getName() {
        return "pushy";
    }

    @Override
    public void registerCallbacks() {
        PlayerTickCallback.EVENT.register(new ClientSideSyncHandler(ChaosEffects.PUSHY) {
            @Override
            protected Tracker getTracker() {
                return PushyEffect.TRACKER;
            }

            @Override
            protected ChaosPayload getPayload(PlayerEntity player, boolean isBeingRemoved) {
                if(!isBeingRemoved && ACTIVE_EFFECTS.containsKey(player.getUuid())) {
                    return ACTIVE_EFFECTS.get(player.getUuid());
                }
                return new PushyPayload(ChaosLib.getStaticRandomInstance().nextBoolean(), isBeingRemoved ? null : new Vec3d(ChaosLib.getStaticRandomInstance().nextFloat(-1, 1), 0, ChaosLib.getStaticRandomInstance().nextFloat(-1, 1)));
            }
        });
    }

    private Vec3d getRandomDirection() {
        return new Vec3d(this.getRNG().nextFloat(2) - 1, 0, this.getRNG().nextFloat(2) - 1).normalize().multiply(this.getRNG().nextFloat(2));
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        //nothing
    }

    public static void trackEffect(PlayerEntity player, PushyPayload payload) {
        UUID uuid = player.getUuid();
        if(payload.direction() == null) {
            ACTIVE_EFFECTS.remove(uuid);
        }
        else {
            ACTIVE_EFFECTS.put(uuid, payload);
        }
    }

    public static Optional<Vec3d> getDirection(PlayerEntity player) {
        PushyPayload payload = ACTIVE_EFFECTS.get(player.getUuid());
        //noinspection DataFlowIssue
        return Optional.ofNullable(payload).map((p) -> p.locked() ? p.direction() : p.direction().rotateY(ChaosLib.convertToRadians(player.getYaw())));
    }
}
