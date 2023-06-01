package yeelp.mcce.model.chaoseffects;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.Nullable;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents;
import net.fabricmc.fabric.api.event.player.PlayerBlockBreakEvents.After;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.util.Tracker;

public final class LotteryEffect extends AbstractTriggeredChaosEffect {

	private static final Tracker AFFECTED_PLAYERS = new Tracker();
	private static final Map<Item, Float> WEIGHTED_ITEMS = Maps.newHashMap();
	private static final List<Map.Entry<Item, Float>> WEIGHTED_LIST;

	static {
		WEIGHTED_ITEMS.put(Items.DIAMOND, 5f);
		WEIGHTED_ITEMS.put(Items.HEART_OF_THE_SEA, 11f);
		WEIGHTED_ITEMS.put(Items.EMERALD, 7f);
		WEIGHTED_ITEMS.put(Items.NETHERITE_BLOCK, 6.5f);
		WEIGHTED_ITEMS.put(Items.QUARTZ, 15.5f);
		WEIGHTED_ITEMS.put(Items.ENDER_PEARL, 10f);
		WEIGHTED_ITEMS.put(Items.BLAZE_POWDER, 1f);
		WEIGHTED_ITEMS.put(Items.POLISHED_GRANITE, 12f);
		WEIGHTED_ITEMS.put(Items.DRIED_KELP_BLOCK, 25f);
		WEIGHTED_ITEMS.put(Items.BEDROCK, 0.5f);
		WEIGHTED_ITEMS.put(Items.REINFORCED_DEEPSLATE, 0.5f);
		WEIGHTED_ITEMS.put(Items.OBSIDIAN, 5f);
		WEIGHTED_ITEMS.put(Items.FOX_SPAWN_EGG, 1f);

		WEIGHTED_LIST = Lists.newArrayList(WEIGHTED_ITEMS.entrySet());
	}

	public LotteryEffect() {
		super(3000, 5000, 1);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		AFFECTED_PLAYERS.add(player);
	}

	@Override
	public String getName() {
		return "lottery";
	}

	@Override
	public void registerCallbacks() {
		PlayerBlockBreakEvents.AFTER.register(new AfterBlockBreakListener());
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		AFFECTED_PLAYERS.remove(player);
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
		return true;
	}

	private static final class AfterBlockBreakListener implements After {

		@Override
		public void afterBlockBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity) {
			MCCEAPI.mutator.modifyEffect(player, LotteryEffect.class, (ce) -> {
				if(ce.getTriggersRemaining() <= 0 || !AFFECTED_PLAYERS.tracked(player) || !player.getMainHandStack().getItem().isSuitableFor(state)) {
					return;
				}
				if(!state.streamTags().anyMatch((tag) -> tag == BlockTags.PICKAXE_MINEABLE)) {
					return;
				}
				int num = ce.getRNG().nextInt(1, 64);
				float weight = ce.getRNG().nextFloat(100.0f);
				Collections.shuffle(WEIGHTED_LIST);
				Iterator<Map.Entry<Item, Float>> it = WEIGHTED_LIST.iterator();
				Item item;
				do {
					Map.Entry<Item, Float> entry = it.next();
					weight -= entry.getValue();
					item = entry.getKey();
				} while(weight > 0);
				ItemStack stack = new ItemStack(item);

				while(num-- > 0) {
					ItemEntity entity = new ItemEntity(world, pos.getX(), pos.getY(), pos.getZ(), stack, ce.getRNG().nextDouble(-Math.E/10, Math.E/10), ce.getRNG().nextDouble(0, Math.E/10), ce.getRNG().nextDouble(-Math.E/10, Math.E/10));
					world.spawnEntity(entity);
				}
				ce.trigger();
			});
		}
	}

}
