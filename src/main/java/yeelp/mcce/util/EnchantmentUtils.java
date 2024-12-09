package yeelp.mcce.util;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;

public abstract class EnchantmentUtils {

    public static RegistryEntry<Enchantment> getEntry(RegistryKey<Enchantment> key, DynamicRegistryManager manager) {
        return manager.getOrThrow(RegistryKeys.ENCHANTMENT).getEntry(key.getValue()).orElseThrow();
    }
}
