package yeelp.mcce.model.chaoseffects;

import net.minecraft.block.Blocks;
import net.minecraft.block.entity.VaultBlockEntity;
import net.minecraft.block.vault.VaultConfig;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.LoreComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTable;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.text.Text;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockPos;
import yeelp.mcce.MCCE;

import java.util.List;
import java.util.Optional;

public final class LootBoxEffect extends AbstractInstantChaosEffect {

    private static final float ACTIVATION_RANGE_MIN = 4.0f, ACTIVATION_RANGE_MAX = 4.5f;
    private static final String LOOT_BOX_KEY = "lootbox";
    private static final String LORE = "Use to open a %s!".formatted(LootType.VAULT.getItemName());
    private static final RegistryKey<LootTable> LOOT_TABLE = RegistryKey.of(RegistryKeys.LOOT_TABLE, MCCE.createIdentifier("lootbox"));

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return player.getInventory().getEmptySlot() >= 0;
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        player.giveItemStack(LootType.values()[this.getRNG().nextBoolean() ? 0 : 1].createStack(player.getRegistryManager()));
    }

    @Override
    public String getName() {
        return "lootbox";
    }

    private enum LootType {
        VAULT(Items.VAULT, "Loot Box") {
            @Override
            protected void addData(ItemStack stack, WrapperLookup wrapper) {
                VaultBlockEntity vault = new VaultBlockEntity(new BlockPos(0, 0, 0), Blocks.VAULT.getDefaultState());
                VaultConfig config = new VaultConfig(LOOT_TABLE, ACTIVATION_RANGE_MIN, ACTIVATION_RANGE_MAX, KEY.createStack(wrapper), Optional.empty());
                vault.setConfig(config);
                vault.setStackNbt(stack, wrapper);
            }
        },
        KEY(Items.TRIAL_KEY, "Loot Box Key") {
            @Override
            protected void addData(ItemStack stack, WrapperLookup wrapper) {
                stack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(LootType.getKey()));
                stack.set(DataComponentTypes.LORE, new LoreComponent(List.of(Text.of(LORE))));
            }
        };

        private final Item item;
        private final String name;

        LootType(Item item, String name) {
            this.item = item;
            this.name = name;
        }

        String getItemName() {
            return this.name;
        }

        ItemStack createStack(WrapperLookup wrapper) {
            ItemStack stack = new ItemStack(this.item);
            this.addData(stack, wrapper);
            stack.set(DataComponentTypes.CUSTOM_NAME, Text.of(this.name));
            stack.set(DataComponentTypes.RARITY, Rarity.EPIC);
            return stack;
        }

        protected abstract void addData(ItemStack stack, WrapperLookup wrapper);

        static NbtCompound getKey() {
            NbtCompound nbt = new NbtCompound();
            nbt.putBoolean(LOOT_BOX_KEY, true);
            return nbt;
        }
    }
}
