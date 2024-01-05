package yeelp.mcce.model.chaoseffects;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import com.google.common.collect.Maps;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import yeelp.mcce.MCCE;

public final class ChaosEffectRegistry {
	
	private static final Map<String, Supplier<? extends ChaosEffect>> REGISTRY = Maps.newHashMap();
	private static final Random RNG = new Random();
	
	public static void registerEffects() {
		register(TwitchEffect::new);
		register(OofEffect::new);
		register(WitherEffect::new);
		register(CycleOfLifeEffect::new);
		register(ToTheMoonEffect::new);
		register(PressLToLevitateEffect::new);
		register(DoubleTimeEffect::new);
		register(DoubleTroubleEffect::new);
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
		register(InfestationEffect::new);
		register(ButterFingersEffect::new);
		//register(QuiverEffect::new);
		register(SwitcherooEffect::new);
		register(ItemEvaporationEffect::new);
		register(WrapAroundEffect::new);
		register(GlintEffect::new);
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
		register(SuperFishEffect::new);
	}
	
	public static void register(Supplier<? extends ChaosEffect> effect) {
		ChaosEffect ce = effect.get();
		ce.registerCallbacks();
		String name = ce.getName();
		REGISTRY.put(name, effect);
	}

	public static boolean isEffectRegistered(String name) {
		return REGISTRY.containsKey(name);
	}
	
	public static ChaosEffect getEffect(String name) {
		return REGISTRY.get(name).get();
	}
	
	public static ChaosEffect getRandomEffect() {
		Iterator<Supplier<? extends ChaosEffect>> it = REGISTRY.values().iterator();
		int i = RNG.nextInt(REGISTRY.size());
		Supplier<? extends ChaosEffect> sup;
		for(sup = it.next(); i > 0; i--, sup = it.next());
		return sup.get();
	}
	
	public static ChaosEffect getRandomApplicableEffectForPlayer(PlayerEntity player) {
		List<Supplier<? extends ChaosEffect>> lst = REGISTRY.values().stream().filter((s) -> s.get().applicable(player)).toList();
		return lst.get(RNG.nextInt(lst.size())).get();
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
