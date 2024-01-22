package yeelp.mcce.model.chaoseffects;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import yeelp.mcce.MCCE;
import yeelp.mcce.util.ChaosLib;

public final class ChaosEffectRegistry {
	
	private static final Map<String, Supplier<? extends ChaosEffect>> REGISTRY = Maps.newHashMap();
	private static final Map<String, ChaosEffect> EFFECT_INSTANCES = Maps.newHashMap();
	private static final Set<String> REPEATING_REGISTRY = Sets.newHashSet();
	private static final Random RNG = new Random();
	
	public static void registerEffects() {
		register(TwitchEffect::new, true);
		register(OofEffect::new, true);
		register(WitherEffect::new);
		register(CycleOfLifeEffect::new);
		register(ToTheMoonEffect::new);
		register(PressLToLevitateEffect::new);
		register(DoubleTimeEffect::new);
		register(DoubleTroubleEffect::new, true);
		register(NullEffect::new);
		register(InsomniaEffect::new);
		register(MemoryGameEffect::new);
		register(RainbowEffect::new);
		register(MidasTouchEffect::new);
		register(StickItToThemEffect::new);
		register(SuddenDeathEffect::new);
		register(GottaBlastEffect::new);
		register(LotteryEffect::new);
		register(IronManEffect::new);
		register(LavishLavaEffect::new);
		register(CrumbleEffect::new);
		register(BouncyEffect::new);
		register(IOUEffect::new);
		register(MyBodyAsAShieldEffect::new);
		register(InfestationEffect::new, true);
		register(ButterFingersEffect::new);
		register(QuiverEffect::new);  // Registered, but made to be never applicable due to motion sickness.
		register(SwitcherooEffect::new, true);
		register(ItemEvaporationEffect::new);
		register(WrapAroundEffect::new);
		register(GlintEffect::new, true);
		register(EnchantmentEffect::new);
		register(ColumnLikeYouSeeEmEffect::new);
		register(ChunkyEffect::new);
		register(PillagerDisguisesEffect::new);
		register(InverseEffect::new);
		register(BackToSquareOneEffect::new);
		register(TripleThreatEffect::new);
		register(MobVisionEffect::new);
		register(EquilibriumEffect::new);
		register(UpdateAquaticEffect::new);
		register(ItemRainEffect::new);
		register(MobRainEffect::new);
		register(SuperFishEffect::new, true);
		register(GiantEffect::new);
		register(BatBombEffect::new);
		register(UndeadEffect::new);
		register(ParticleEffect::new);
		register(ReorganizeEffect::new, true);
		register(EquipmentRandomizerEffect::new);
		register(GrummboneEffect::new);
		register(RepeatingEffect::new);
		register(MagnetEffect::new);
		register(ClippyEffect::new);
	}
	
	public static ChaosEffect register(Supplier<? extends ChaosEffect> effect) {
		ChaosEffect ce = effect.get();
		ce.registerCallbacks();
		String name = ce.getName();
		REGISTRY.put(name, effect);
		EFFECT_INSTANCES.put(name, ce);
		return ce;
	}
	
	public static void register(Supplier<? extends AbstractInstantChaosEffect> effect, boolean isValidForRepeating) {
		String name = register(effect).getName();
		if(isValidForRepeating) {
			REPEATING_REGISTRY.add(name);			
		}
	}

	public static boolean isEffectRegistered(String name) {
		return REGISTRY.containsKey(name);
	}
	
	public static ChaosEffect getEffect(String name) {
		return REGISTRY.get(name).get();
	}
	
	public static String getEffectValidForRepeating() {
		return ChaosLib.getRandomElementFrom(REPEATING_REGISTRY, RNG);
	}
	
	public static ChaosEffect getRandomEffect() {
		Iterator<Supplier<? extends ChaosEffect>> it = REGISTRY.values().iterator();
		int i = RNG.nextInt(REGISTRY.size());
		Supplier<? extends ChaosEffect> sup;
		for(sup = it.next(); i > 0; i--, sup = it.next());
		return sup.get();
	}
	
	public static ChaosEffect getRandomApplicableEffectForPlayer(PlayerEntity player) {
		List<String> lst = EFFECT_INSTANCES.values().stream().filter((s) -> s.applicable(player)).map(ChaosEffect::getName).toList();
		return getEffect(ChaosLib.getRandomElementFrom(lst, RNG));
	}
	
	public static ChaosEffect createEffectFromNbt(String name, NbtCompound nbt) {
		if(!isEffectRegistered(name)) {
			return getEffect(new NullEffect().getName());
		}
		ChaosEffect effect = getEffect(name);
		MCCE.LOGGER.info(nbt.toString());
		effect.readNbt(nbt);
		return effect;
	}
	
	public static Stream<ChaosEffect> getAllEffects() {
		return REGISTRY.values().stream().map(Supplier::get);
	}
	
}
