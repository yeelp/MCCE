package yeelp.mcce.model.chaoseffects;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;
import yeelp.mcce.util.EnchantmentUtils;

import java.util.Map;
import java.util.Optional;

public abstract class AbstractEnchantedItemChaosEffect extends AbstractInstantChaosEffect {

    @Override
    protected final boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return player.getInventory().getEmptySlot() >= 0;
    }

    @Override
    public final void applyEffect(PlayerEntity player) {
        ItemStack stack = new ItemStack(this.getItem());
        DynamicRegistryManager manager = player.getRegistryManager();
        this.getEnchantments().forEach((enchant, level) -> stack.addEnchantment(EnchantmentUtils.getEntry(enchant, manager), level));
        this.getCustomName().ifPresent((name) -> stack.set(DataComponentTypes.CUSTOM_NAME, name));
        this.getLore().ifPresent((lore) -> stack.set(DataComponentTypes.LORE, lore));
        player.giveItemStack(stack);
    }

    protected abstract Item getItem();

    protected abstract Map<RegistryKey<Enchantment>, Integer> getEnchantments();

    protected abstract Optional<Text> getCustomName();

    protected abstract Optional<LoreComponent> getLore();
}
