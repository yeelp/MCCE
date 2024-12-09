package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import yeelp.mcce.MCCE;
import yeelp.mcce.util.ChaosLib;

import java.util.*;
import java.util.stream.Stream;

public final class ChaosEffectRegistry {

    private static final Map<String, ChaosEffectRegistryEntry> ENTRY_REGISTRY = Maps.newHashMap();

	private static final List<String> EFFECT_NAMES = Lists.newArrayList();
	private static final Set<String> REPEATING_REGISTRY = Sets.newHashSet();
	private static final Random RNG = new Random();

    public static void register(ChaosEffectRegistryEntry entry) {
        ENTRY_REGISTRY.put(entry.getName(), entry);
        entry.registerCallbacks();
        EFFECT_NAMES.add(entry.getName());
        if (entry.isValidForRepeating()) {
            REPEATING_REGISTRY.add(entry.getName());
        }
    }

	public static boolean isEffectRegistered(String name) {
		return ENTRY_REGISTRY.containsKey(name);
	}
	
	public static ChaosEffect getEffect(String name) {
		return ENTRY_REGISTRY.get(name).createChaosEffect();
	}

    public static ChaosEffectRegistryEntry getEntry(String name) {
        return ENTRY_REGISTRY.get(name);
    }

	public static ChaosEffectRegistryEntry getEntry(ChaosEffect effect) {
		return ENTRY_REGISTRY.get(effect.getName());
	}
	
	public static String getEffectValidForRepeating() {
		return ChaosLib.getRandomElementFrom(REPEATING_REGISTRY, RNG);
	}
	
	public static ChaosEffect getRandomEffect() {
		return getEffect(ChaosLib.getRandomElementFrom(EFFECT_NAMES, RNG));
	}
	
	public static ChaosEffect getRandomApplicableEffectForPlayer(PlayerEntity player) {
		Iterator<String> it = getShuffledNamesIterator();
		String name;
		for(name = it.next(); it.hasNext(); name = it.next()) {
			ChaosEffectRegistryEntry entry = getEntry(name);
			if(entry.isEnabled() && entry.isApplicable(player)) {
				return entry.createChaosEffect();
			}
		}
		return ChaosEffects.NULL.createChaosEffect();
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
	
	public static Stream<ChaosEffectRegistryEntry> getAllEffects() {
		return ENTRY_REGISTRY.values().stream();
	}
	
	private static Iterator<String> getShuffledNamesIterator() {
        Collections.shuffle(EFFECT_NAMES);
        return EFFECT_NAMES.iterator();
    }
	
}
