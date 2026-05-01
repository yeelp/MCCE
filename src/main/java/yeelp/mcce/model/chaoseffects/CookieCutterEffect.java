package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.text.Text;

import java.util.Map;
import java.util.Optional;

public final class CookieCutterEffect extends AbstractEnchantedItemChaosEffect {

    private static final Map<RegistryKey<Enchantment>, Integer> ENCHANTS = Maps.newHashMap();

    static {
        ENCHANTS.put(Enchantments.SHARPNESS, 9);
        ENCHANTS.put(Enchantments.VANISHING_CURSE, 1);
    }

    @Override
    protected Item getItem() {
        return Items.COOKIE;
    }

    @Override
    protected Map<RegistryKey<Enchantment>, Integer> getEnchantments() {
        return ENCHANTS;
    }

    @Override
    protected Optional<Text> getCustomName() {
        return Optional.empty();
    }

    @Override
    protected Optional<LoreComponent> getLore() {
        return Optional.of(new LoreComponent(Lists.newArrayList(Text.empty().append("It's a real cookie cutter."))));
    }

    @Override
    public String getName() {
        return "cookiecutter";
    }

    @Override
    public String getDisplayName() {
        return "Cookie Cutter";
    }
}
