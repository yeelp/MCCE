package yeelp.mcce.model.chaoseffects;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.jetbrains.annotations.Nullable;

import com.google.common.base.Functions;
import com.google.common.collect.Sets;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.ModifyBlockDrops;

public final class IOUEffect extends AbstractTimedChaosEffect {

	protected IOUEffect() {
		super(4000, 4500);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		return;
	}

	@Override
	public String getName() {
		return "iou";
	}

	@Override
	public void registerCallbacks() {
		ModifyBlockDrops.EVENT.register(new BlockDropChanger());
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		return;
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		return;
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, MidasTouchEffect.class);
	}
	
	private static final class BlockDropChanger implements ModifyBlockDrops {
		private static final Set<TagKey<Block>> OVERWORLD_ORES = Sets.newHashSet();
		private static final Set<Block> NETHER_ORES = Sets.newHashSet();
		
		static {
			OVERWORLD_ORES.add(BlockTags.COAL_ORES);
			OVERWORLD_ORES.add(BlockTags.IRON_ORES);
			OVERWORLD_ORES.add(BlockTags.GOLD_ORES);
			OVERWORLD_ORES.add(BlockTags.LAPIS_ORES);
			OVERWORLD_ORES.add(BlockTags.REDSTONE_ORES);
			OVERWORLD_ORES.add(BlockTags.COPPER_ORES);
			OVERWORLD_ORES.add(BlockTags.EMERALD_ORES);
			OVERWORLD_ORES.add(BlockTags.DIAMOND_ORES);
			
			NETHER_ORES.add(Blocks.NETHER_QUARTZ_ORE);
			NETHER_ORES.add(Blocks.ANCIENT_DEBRIS);
		}
		
		@Override
		public boolean changeBlockDrops(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
			if(world.isClient || !(player instanceof ServerPlayerEntity) || !MCCEAPI.accessor.isChaosEffectActive(player, IOUEffect.class)) {
				return false;
			}
			if(NETHER_ORES.contains(state.getBlock()) || OVERWORLD_ORES.stream().anyMatch((t) -> state.isIn(t))) {
				String name = state.getBlock().getName().getString();
				world.spawnEntity(new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), getIOU(name)));
				return true;
			}
			return false;
		}
		
		private static ItemStack getIOU(String oreName) {
			ItemStack stack = new ItemStack(Items.PAPER);
			stack.setCustomName(Text.empty().append("IOU").formatted(Formatting.BOLD).formatted(Formatting.RED));
			NbtCompound nbt = stack.getNbt();
			NbtCompound display = nbt.getCompound(ItemStack.DISPLAY_KEY);
			NbtList lore = new NbtList();
			lore.addAll(getTextWithColor("Sorry!", Formatting.LIGHT_PURPLE));
			lore.addAll(getTextWithColor(oreName, Formatting.GOLD));
			lore.addAll(getTextWithColor("is currently out of stock! Try again later!", Formatting.LIGHT_PURPLE));
			display.put(ItemStack.LORE_KEY, lore);
			nbt.put(ItemStack.DISPLAY_KEY, display);
			return stack;
		}
		
		private static final List<NbtString> getTextWithColor(String text, Formatting formatting) {
			return Text.of(text).getWithStyle(Style.EMPTY.withColor(formatting)).stream().map(Functions.compose(NbtString::of, Text.Serializer::toJson)).collect(Collectors.toList());
		}
	}

}
