package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Sets;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public final class ChaosEffects {

    public static final ChaosEffectRegistryEntry AMBIDEXTROUS;
    public static final ChaosEffectRegistryEntry ARMORER;
    public static final ChaosEffectRegistryEntry BACK_TO_SQUARE_ONE;
    public static final ChaosEffectRegistryEntry BAT_BOMB;
    public static final ChaosEffectRegistryEntry BLOCK_RAIN;
    public static final ChaosEffectRegistryEntry BOUNCY;
    public static final ChaosEffectRegistryEntry BUILDER;
    public static final ChaosEffectRegistryEntry BUTTER_FINGERS;
    public static final ChaosEffectRegistryEntry CHUNKY;
    public static final ChaosEffectRegistryEntry CLIPPY;
    public static final ChaosEffectRegistryEntry COLUMN_LIKE_YOU_SEE_EM;
    public static final ChaosEffectRegistryEntry COOKIE_CUTTER;
    public static final ChaosEffectRegistryEntry CRUMBLE;
    public static final ChaosEffectRegistryEntry CYCLE_OF_LIFE;
    public static final ChaosEffectRegistryEntry DOUBLE_TIME;
    public static final ChaosEffectRegistryEntry DOUBLE_TROUBLE;
    public static final ChaosEffectRegistryEntry ENCHANTMENT;
    public static final ChaosEffectRegistryEntry ENDER_DRIVE_THRU;
    public static final ChaosEffectRegistryEntry EQUILIBRIUM;
    public static final ChaosEffectRegistryEntry EQUIPMENT_RANDOMIZER;
    public static final ChaosEffectRegistryEntry FISH_LAUNCHER;
    public static final ChaosEffectRegistryEntry FOOD_CHAIN;
    public static final ChaosEffectRegistryEntry GASP;
    public static final ChaosEffectRegistryEntry GHAST;
    public static final ChaosEffectRegistryEntry GIANT;
    public static final ChaosEffectRegistryEntry GLINT;
    public static final ChaosEffectRegistryEntry GOTTA_BLAST;
    public static final ChaosEffectRegistryEntry GRUMMBONE;
    public static final ChaosEffectRegistryEntry HEARTY;
    public static final ChaosEffectRegistryEntry INFESTATION;
    public static final ChaosEffectRegistryEntry INSOMNIA;
    public static final ChaosEffectRegistryEntry INTERACTION_INTERACTOR;
    public static final ChaosEffectRegistryEntry INVERSE;
    public static final ChaosEffectRegistryEntry IOU;
    public static final ChaosEffectRegistryEntry IRON_MAN;
    public static final ChaosEffectRegistryEntry ITEM_EVAPORATION;
    public static final ChaosEffectRegistryEntry ITEM_RAIN;
    public static final ChaosEffectRegistryEntry LAVISH_LAVA;
    public static final ChaosEffectRegistryEntry LOOK_INVERSION;
    public static final ChaosEffectRegistryEntry LOOT_BOX;
    public static final ChaosEffectRegistryEntry LOTTERY;
    public static final ChaosEffectRegistryEntry LOVABLE_PHANTOM;
    public static final ChaosEffectRegistryEntry MAGNET;
    public static final ChaosEffectRegistryEntry MEMORY_GAME;
    public static final ChaosEffectRegistryEntry MIDAS_TOUCH;
    public static final ChaosEffectRegistryEntry MOB_RAIN;
    public static final ChaosEffectRegistryEntry MOB_VISION;
    public static final ChaosEffectRegistryEntry MY_BODY_AS_A_SHIELD;
    public static final ChaosEffectRegistryEntry NETHERITE_TRANSMUTATION;
    public static final ChaosEffectRegistryEntry NULL;
    public static final ChaosEffectRegistryEntry OOF;
    public static final ChaosEffectRegistryEntry PARTICLE;
    public static final ChaosEffectRegistryEntry PARTING_GIFT;
    public static final ChaosEffectRegistryEntry PILLAGER_DISGUISES;
    public static final ChaosEffectRegistryEntry PING_PONG;
    public static final ChaosEffectRegistryEntry PRESS_L_TO_LEVITATE;
    public static final ChaosEffectRegistryEntry QUIVER;
    public static final ChaosEffectRegistryEntry RAINBOW;
    public static final ChaosEffectRegistryEntry REORGANIZE;
    public static final ChaosEffectRegistryEntry REPEATING;
    public static final ChaosEffectRegistryEntry SCRAMBLED;
    public static final ChaosEffectRegistryEntry SHAKEWEIGHT;
    public static final ChaosEffectRegistryEntry SIMON_SAYS;
    public static final ChaosEffectRegistryEntry SIZE_EM_UP;
    public static final ChaosEffectRegistryEntry SLUGGISH;
    public static final ChaosEffectRegistryEntry SMACK_DOWN;
    public static final ChaosEffectRegistryEntry SOUND;
    public static final ChaosEffectRegistryEntry STICK_IT_TO_THEM;
    public static final ChaosEffectRegistryEntry STUTTER_SOUND;
    public static final ChaosEffectRegistryEntry SUDDEN_DEATH;
    public static final ChaosEffectRegistryEntry SUPERFISH;
    public static final ChaosEffectRegistryEntry SWITCHEROO;
    public static final ChaosEffectRegistryEntry TO_THE_MOON;
    public static final ChaosEffectRegistryEntry TRUE_CHAOS;
    public static final ChaosEffectRegistryEntry TRIPLE_THREAT;
    public static final ChaosEffectRegistryEntry TWITCH;
    public static final ChaosEffectRegistryEntry UNBREAKABLE;
    public static final ChaosEffectRegistryEntry UNDEAD;
    public static final ChaosEffectRegistryEntry UPDATE_AQUATIC;
    public static final ChaosEffectRegistryEntry WITHER;
    public static final ChaosEffectRegistryEntry WRAP_AROUND;
    public static final ChaosEffectRegistryEntry XP_RANDOMIZER;

    private static final Set<ChaosEffectRegistryEntry> BLACKLIST = Sets.newHashSet();

    static {
        AMBIDEXTROUS = createRegistryEntry(AmbidextrousEffect::new);
        ARMORER = createRegistryEntry(ArmorerEffect::new);
        BACK_TO_SQUARE_ONE = createRegistryEntry(BackToSquareOneEffect::new);
        BAT_BOMB = createRegistryEntry(BatBombEffect::new);
        BLOCK_RAIN = createRegistryEntry(BlockRainEffect::new);
        BOUNCY = createRegistryEntry(BouncyEffect::new);
        BUILDER = createRepeatingRegistryEntry(BuilderEffect::new);
        BUTTER_FINGERS = createRegistryEntry(ButterFingersEffect::new);
        CHUNKY = createRegistryEntry(ChunkyEffect::new);
        CLIPPY = createRegistryEntry(ClippyEffect::new);
        COLUMN_LIKE_YOU_SEE_EM = createRegistryEntry(ColumnLikeYouSeeEmEffect::new);
        COOKIE_CUTTER = createRepeatingRegistryEntry(CookieCutterEffect::new);
        CRUMBLE = createRegistryEntry(CrumbleEffect::new);
        CYCLE_OF_LIFE = createRegistryEntry(CycleOfLifeEffect::new);
        DOUBLE_TIME = createRegistryEntry(DoubleTimeEffect::new);
        DOUBLE_TROUBLE = createRepeatingRegistryEntry(DoubleTroubleEffect::new);
        ENCHANTMENT = createRegistryEntry(EnchantmentEffect::new);
        ENDER_DRIVE_THRU = createRegistryEntry(EnderDriveThruEffect::new);
        EQUILIBRIUM = createRegistryEntry(EquilibriumEffect::new);
        EQUIPMENT_RANDOMIZER = createRegistryEntry(EquipmentRandomizerEffect::new);
        FISH_LAUNCHER = createRegistryEntry(FishLauncherEffect::new);
        FOOD_CHAIN = createRegistryEntry(FoodChainEffect::new);
        GASP = createRegistryEntry(GaspEffect::new);
        GHAST = createRepeatingRegistryEntry(GhastEffect::new);
        GIANT = createRegistryEntry(GiantEffect::new);
        GLINT = createRepeatingRegistryEntry(GlintEffect::new);
        GOTTA_BLAST = createRegistryEntry(GottaBlastEffect::new);
        GRUMMBONE = createRegistryEntry(GrummboneEffect::new);
        HEARTY = createRegistryEntry(HeartyEffect::new);
        INFESTATION = createRepeatingRegistryEntry(InfestationEffect::new);
        INSOMNIA = createRegistryEntry(InsomniaEffect::new);
        INTERACTION_INTERACTOR = createRegistryEntry(InteractionInteractorEffect::new);
        INVERSE = createRegistryEntry(InverseEffect::new);
        IOU = createRegistryEntry(IOUEffect::new);
        IRON_MAN = createRegistryEntry(IronManEffect::new);
        ITEM_EVAPORATION = createRegistryEntry(ItemEvaporationEffect::new);
        ITEM_RAIN = createRegistryEntry(ItemRainEffect::new);
        LAVISH_LAVA = createRegistryEntry(LavishLavaEffect::new);
        LOOK_INVERSION = createRegistryEntry(LookInversionEffect::new);
        LOOT_BOX = createRepeatingRegistryEntry(LootBoxEffect::new);
        LOTTERY = createRegistryEntry(LotteryEffect::new);
        LOVABLE_PHANTOM = createRegistryEntry(LovablePhantomEffect::new);
        MAGNET = createRegistryEntry(MagnetEffect::new);
        MEMORY_GAME = createRegistryEntry(MemoryGameEffect::new);
        MIDAS_TOUCH = createRegistryEntry(MidasTouchEffect::new);
        MOB_RAIN = createRegistryEntry(MobRainEffect::new);
        MOB_VISION = createRegistryEntry(MobVisionEffect::new);
        MY_BODY_AS_A_SHIELD = createRegistryEntry(MyBodyAsAShieldEffect::new);
        NULL = createRegistryEntry(NullEffect::new);
        NETHERITE_TRANSMUTATION = createRegistryEntry(NetheriteTransmutation::new);
        OOF = createRepeatingRegistryEntry(OofEffect::new);
        PARTICLE = createRegistryEntry(ParticleEffect::new);
        PARTING_GIFT = createRegistryEntry(PartingGiftEffect::new);
        PILLAGER_DISGUISES = createRegistryEntry(PillagerDisguisesEffect::new);
        PING_PONG = createRegistryEntry(PingPongEffect::new);
        PRESS_L_TO_LEVITATE = createRegistryEntry(PressLToLevitateEffect::new);
        QUIVER = createRegistryEntry(QuiverEffect::new);
        RAINBOW = createRegistryEntry(RainbowEffect::new);
        REORGANIZE = createRepeatingRegistryEntry(ReorganizeEffect::new);
        REPEATING = new ChaosEffectRegistryEntry(RepeatingEffect::new, RepeatingEffect.getDummyInstance());
        SCRAMBLED = createRepeatingRegistryEntry(ComponentCompensationEffect::new);
        SHAKEWEIGHT = createRegistryEntry(ShakeweightEffect::new);
        SIMON_SAYS = createRegistryEntry(SimonSaysEffect::new);
        SIZE_EM_UP = createRegistryEntry(SizeEmUpEffect::new);
        SLUGGISH = createRegistryEntry(SluggishEffect::new);
        SMACK_DOWN = createRegistryEntry(SmackDownEffect::new);
        SOUND = createRepeatingRegistryEntry(SoundEffect::new);
        STICK_IT_TO_THEM = createRegistryEntry(StickItToThemEffect::new);
        STUTTER_SOUND = createRegistryEntry(StutterSoundEffect::new);
        SUDDEN_DEATH = createRegistryEntry(SuddenDeathEffect::new);
        SUPERFISH = createRegistryEntry(SuperFishEffect::new);
        SWITCHEROO = createRegistryEntry(SwitcherooEffect::new);
        TO_THE_MOON = createRegistryEntry(ToTheMoonEffect::new);
        TRUE_CHAOS = createRegistryEntry(TrueChaosEffect::new);
        TRIPLE_THREAT = createRegistryEntry(TripleThreatEffect::new);
        TWITCH = createRepeatingRegistryEntry(TwitchEffect::new);
        UNBREAKABLE = createRegistryEntry(UnbreakableEffect::new);
        UNDEAD = createRegistryEntry(UndeadEffect::new);
        UPDATE_AQUATIC = createRegistryEntry(UpdateAquaticEffect::new);
        WITHER = createRegistryEntry(WitherEffect::new);
        WRAP_AROUND = createRegistryEntry(WrapAroundEffect::new);
        XP_RANDOMIZER = createRegistryEntry(XPRandomizerEffect::new);

        BLACKLIST.add(RAINBOW);
    }

    public static void registerEffects() {
        for(Field f : ChaosEffects.class.getFields()) {
            if (Modifier.isStatic(f.getModifiers())) {
                try {
                    Object o = f.get(null);
                    if (o instanceof ChaosEffectRegistryEntry entry && !BLACKLIST.contains(entry)) {
                        if (!ChaosEffectRegistry.isEffectRegistered(entry.getName())) {
                            ChaosEffectRegistry.register(entry);
                        }
                        else {
                            throw new UnsupportedOperationException("Tried to register a Chaos Effect with the same name as another! "+entry.getName());
                        }
                    }
                }
                catch (IllegalArgumentException | IllegalAccessException e) {
                    throw new RuntimeException("Unable to register Chaos Effects! Check log!");
                }
            }
        }
    }

    private static ChaosEffectRegistryEntry createRegistryEntry(Supplier<ChaosEffect> generator) {
        return new ChaosEffectRegistryEntry(generator);
    }

    private static <E extends AbstractInstantChaosEffect> ChaosEffectRegistryEntry createRepeatingRegistryEntry(Supplier<E> generator) {
        return new ChaosEffectRegistryEntry(generator, true);
    }
}
