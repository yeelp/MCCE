package yeelp.mcce.model.chaoseffects;

import com.google.common.base.Functions;
import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.ClientSideSyncHandler;
import yeelp.mcce.event.PlayerTickCallback;
import yeelp.mcce.network.IconicPayload;
import yeelp.mcce.network.NetworkingPayloads.ChaosPayload;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;
import yeelp.mcce.util.Tracker;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public final class IconicEffect extends AbstractTimedChaosEffect {

    public enum HeartType {
        NORMAL,
        ABSORPTION("absorbing"),
        POISON("poisoned"),
        WITHER("withered"),
        FROST("frozen");

        private final String root;

        HeartType() {
            this("");
        }

        HeartType(String root) {
            this.root = root;
        }

        public Identifier swap(Identifier original, boolean hardcore) {
            String[] path = original.getPath().split("/");
            String[] heartArgs = path[2].split("_");
            int index = 0;
            for(HeartType type : HeartType.values()) {
                if(type.root.equals(heartArgs[0])) {
                    index = 1;
                    break;
                }
            }
            StringBuilder builder = new StringBuilder(this.root);
            if(hardcore) {
                builder.append("_hardcore");
            }
            for(; index < heartArgs.length; index++) {
                if(heartArgs[index].equals("hardcore")) {
                    continue;
                }
                builder.append("_").append(heartArgs[index]);
            }
            if(builder.charAt(0) == '_') {
                builder.deleteCharAt(0);
            }
            return Identifier.ofVanilla(String.format("%s/%s/%s", path[0], path[1], builder));
        }

        static HeartType getRandomHeartType() {
            return ChaosLib.getRandomElementFrom(HeartType.values());
        }
    }

    public enum HungerType {
        NORMAL {
            @Override
            String alterPathEnd(String path) {
                return path.substring(0, path.indexOf("_hunger"));
            }
        },
        HUNGER {
            @Override
            String alterPathEnd(String path) {
                return path + "_hunger";
            }
        };

        public Identifier swap(Identifier original) {
            boolean endsHunger = original.getPath().endsWith("hunger");
            if((endsHunger && this == HUNGER) || (!endsHunger && this == NORMAL)) {
                return original;
            }
            String[] path = original.getPath().split("/");
            return Identifier.ofVanilla(path[0] + "/" + this.alterPathEnd(path[1]));
        }

        abstract String alterPathEnd(String path);

        static HungerType getRandomHungerType() {
            return ChaosLib.getStaticRandomInstance().nextBoolean() ? NORMAL : HUNGER;
        }
    }

    private static final int DURATION_MIN = 1500, DURATION_MAX = 2200;
    private static final Tracker TRACKER = new Tracker();
    private static final String HEART_KEY = "heart", HUNGER_KEY = "hunger", HARDCORE_KEY = "hardcore";
    private static final Map<UUID, IconicPayload> ACTIVE_EFFECTS = Maps.newHashMap();

    private HeartType heartType;
    private HungerType hungerType;
    private boolean hardcore;

    public IconicEffect() {
        super(DURATION_MIN, DURATION_MAX);
        this.heartType = HeartType.getRandomHeartType();
        this.hungerType = HungerType.getRandomHungerType();
        this.hardcore = this.getRNG().nextBoolean();
    }

    @Override
    protected void tickAdditionalEffectLogic(PlayerEntity player) {
        //nothing
    }

    @Override
    protected boolean canStack() {
        return false;
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return !MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.MEMORY_GAME);
    }

    @Override
    public NbtCompound writeToNbt() {
        NbtCompound nbt = super.writeToNbt();
        nbt.putInt(HEART_KEY, this.heartType.ordinal());
        nbt.putInt(HUNGER_KEY, this.hungerType.ordinal());
        nbt.putBoolean(HARDCORE_KEY, this.hardcore);
        return nbt;
    }

    @Override
    public void readNbt(NbtCompound nbt) {
        super.readNbt(nbt);
        this.heartType = HeartType.values()[nbt.getInt(HEART_KEY).orElse(0)];
        this.hungerType = HungerType.values()[nbt.getInt(HUNGER_KEY).orElse(0)];
        this.hardcore = nbt.getBoolean(HARDCORE_KEY, false);
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        PlayerUtils.getServerPlayer(player).ifPresent(new IconicPayload((byte) this.heartType.ordinal(), (byte) this.hungerType.ordinal(), this.hardcore)::send);
        TRACKER.add(player);
    }

    @Override
    public String getName() {
        return "iconic";
    }

    @Override
    public void registerCallbacks() {
        PlayerTickCallback.EVENT.register(new ClientSideSyncHandler(ChaosEffects.ICONIC) {
            @Override
            protected Tracker getTracker() {
                return IconicEffect.TRACKER;
            }

            @Override
            protected ChaosPayload getPayload(PlayerEntity player, boolean isBeingRemoved) {
                if(!isBeingRemoved && ACTIVE_EFFECTS.containsKey(player.getUuid())) {
                    return ACTIVE_EFFECTS.get(player.getUuid());
                }
                return new IconicPayload((byte) HeartType.getRandomHeartType().ordinal(), (byte) HungerType.getRandomHungerType().ordinal(), ChaosLib.getStaticRandomInstance().nextBoolean());
            }
        });
    }

    @Override
    public void onEffectEnd(PlayerEntity player) {
        PlayerUtils.getServerPlayer(player).ifPresent(new IconicPayload()::send);
        TRACKER.remove(player);
    }

    public static void trackEffect(PlayerEntity player, IconicPayload payload) {
        UUID uuid = player.getUuid();
        if(payload.heartIcon() == -1) {
            ACTIVE_EFFECTS.remove(uuid);
        }
        else {
            ACTIVE_EFFECTS.put(uuid, payload);
        }
    }

    public static Optional<HeartType> getHeartType(PlayerEntity player) {
        //noinspection DataFlowIssue
        return getFromPayload(player, Functions.compose((i) -> HeartType.values()[i], IconicPayload::heartIcon));
    }

    public static Optional<HungerType> getHungerType(PlayerEntity player) {
        //noinspection DataFlowIssue
        return getFromPayload(player, Functions.compose((i) -> HungerType.values()[i], IconicPayload::hungerIcon));
    }

    public static Optional<Boolean> getHardcoreStatus(PlayerEntity player) {
        return getFromPayload(player, IconicPayload::hardcore);
    }

    private static <T> Optional<T> getFromPayload(PlayerEntity player, Function<IconicPayload, T> f) {
        return Optional.ofNullable(ACTIVE_EFFECTS.get(player.getUuid())).map(f);
    }
}
