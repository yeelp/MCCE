package yeelp.mcce.model.chaoseffects;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.spongepowered.include.com.google.common.collect.ImmutableList;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.PickaxeItem;
import net.minecraft.item.ShovelItem;
import net.minecraft.item.SwordItem;
import net.minecraft.item.ToolItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.OnBlockBreakingCallback;
import yeelp.mcce.event.OnBlockInteractCallback;
import yeelp.mcce.mixin.ServerWorldASMMixin;
import yeelp.mcce.util.Tracker;

public final class MidasTouchEffect extends AbstractTimedChaosEffect implements OnBlockInteractCallback, OnBlockBreakingCallback {

	private static final Tracker AFFECTED_PLAYERS = new Tracker();
	private static final BlockState GOLD_STATE = Blocks.GOLD_BLOCK.getDefaultState();
	private static final BlockState GOLD_PRESSURE_PLATE_STATE = Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE.getDefaultState();
	private static final Map<Class<? extends ToolItem>, Item> TOOL_MAPPER = Maps.newHashMap();
	private static final Map<EquipmentSlot, Item> ARMOR_MAPPER = Maps.newHashMap();
	private static final Map<Item, Set<Item>> ITEM_MAPPER = Maps.newHashMap();
	private static final Set<Item> BLACKLIST = Sets.newHashSet();

	private static final Iterable<EquipmentSlot> ARMOR_SLOTS = ImmutableList.<EquipmentSlot>builder().add(EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET).build();

	static {
		TOOL_MAPPER.put(SwordItem.class, Items.GOLDEN_SWORD);
		TOOL_MAPPER.put(ShovelItem.class, Items.GOLDEN_SHOVEL);
		TOOL_MAPPER.put(PickaxeItem.class, Items.GOLDEN_PICKAXE);
		TOOL_MAPPER.put(AxeItem.class, Items.GOLDEN_AXE);
		TOOL_MAPPER.put(HoeItem.class, Items.GOLDEN_HOE);

		ARMOR_MAPPER.put(EquipmentSlot.HEAD, Items.GOLDEN_HELMET);
		ARMOR_MAPPER.put(EquipmentSlot.CHEST, Items.GOLDEN_CHESTPLATE);
		ARMOR_MAPPER.put(EquipmentSlot.LEGS, Items.GOLDEN_LEGGINGS);
		ARMOR_MAPPER.put(EquipmentSlot.FEET, Items.GOLDEN_BOOTS);
		

		ITEM_MAPPER.put(Items.GOLD_INGOT, ImmutableSet.<Item>builder().add(Items.COPPER_INGOT, Items.IRON_INGOT, Items.NETHERITE_INGOT, Items.BRICK, Items.NETHER_BRICK).build());
		ITEM_MAPPER.put(Items.GOLD_NUGGET, ImmutableSet.<Item>builder().add(Items.IRON_NUGGET, Items.NETHERITE_SCRAP).build());
		ITEM_MAPPER.put(Items.GOLDEN_HORSE_ARMOR, ImmutableSet.<Item>builder().add(Items.IRON_HORSE_ARMOR, Items.LEATHER_HORSE_ARMOR, Items.DIAMOND_HORSE_ARMOR).build());
		ITEM_MAPPER.put(Items.GOLDEN_CARROT, ImmutableSet.of(Items.CARROT));

		BLACKLIST.add(Items.BELL);
		BLACKLIST.add(Items.RAW_GOLD);
		BLACKLIST.add(Items.RAW_GOLD_BLOCK);
		BLACKLIST.add(Items.GOLD_BLOCK);
		BLACKLIST.add(Items.ENCHANTED_GOLDEN_APPLE);
		BLACKLIST.add(Items.LIGHT_WEIGHTED_PRESSURE_PLATE);
	}

	public MidasTouchEffect() {
		super(700, 1300);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		AFFECTED_PLAYERS.add(player.getUuid());
	}

	@Override
	public String getName() {
		return "midastouch";
	}

	@Override
	public void registerCallbacks() {
		OnBlockInteractCallback.EVENT.register(this);
		OnBlockBreakingCallback.EVENT.register(this);
	}

	@Override
	public void onEffectEnd(PlayerEntity player) {
		AFFECTED_PLAYERS.remove(player.getUuid());
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return !MCCEAPI.accessor.isChaosEffectActive(player, CrumbleEffect.class);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		BlockPos pos = player.getBlockPos();
		World world = player.getWorld();
		boolean secondPass = false;
		while(true) {
			final BlockPos p = pos;
			final boolean b = secondPass;
			BlockState state = world.getBlockState(p);
			getGoldenBlockState(state).ifPresent((s) -> {
				if((!b && s == GOLD_PRESSURE_PLATE_STATE) || (b && s == GOLD_STATE)) {
					world.setBlockState(p, s);
				}
			});
			if(secondPass) {
				break;
			}
			pos = pos.down();
			secondPass = !secondPass;
		}
		for(Hand hand : Hand.values()) {
			ItemStack stack = player.getStackInHand(hand);
			if(!stack.isEmpty() && !BLACKLIST.contains(stack.getItem())) {
				if(stack.getItem() instanceof ToolItem) {
					Item itemToSet = TOOL_MAPPER.get(stack.getItem().getClass());
					if(itemToSet != null && !stack.getItem().equals(itemToSet)) {
						player.setStackInHand(hand, makeGold(stack, itemToSet));
					}
				}
				else if(stack.getItem() instanceof ArmorItem) {
					Item armor = ARMOR_MAPPER.get(((ArmorItem) stack.getItem()).getSlotType());
					if(armor != null && !armor.equals(stack.getItem())) {
						player.setStackInHand(hand, makeGold(stack, armor));
					}
				}
				else if(stack.getItem() == Items.APPLE) {
					if(stack.hasGlint()) {
						player.setStackInHand(hand, makeGold(stack, Items.ENCHANTED_GOLDEN_APPLE));
					}
					else {
						player.setStackInHand(hand, makeGold(stack, Items.GOLDEN_APPLE));
					}
				}
				else if(stack.getItem() instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock().getDefaultState().isIn(BlockTags.PRESSURE_PLATES)) {
					player.setStackInHand(hand, makeGold(stack, Items.LIGHT_WEIGHTED_PRESSURE_PLATE));
				}
				else {
					boolean transmuted = false, needsTransmuting = true;

					for(Item item : ITEM_MAPPER.keySet()) {
						if(stack.getItem().equals(item)) {
							needsTransmuting = false;
							break;
						}
						if(ITEM_MAPPER.get(item).contains(stack.getItem())) {
							transmuted = true;
							player.setStackInHand(hand, makeGold(stack, item));
							break;
						}
					}
					if(!transmuted && needsTransmuting) {
						if(stack.getItem() instanceof BlockItem && doesTurnToGold(((BlockItem) stack.getItem()).getBlock().getDefaultState())) {
							player.setStackInHand(hand, makeGold(stack, Items.GOLD_BLOCK));
						}
						else {
							player.setStackInHand(hand, makeGold(stack, Items.RAW_GOLD));
						}
					}
				}
			}
		}
		for(EquipmentSlot slot : ARMOR_SLOTS) {
			ItemStack stack = player.getEquippedStack(slot);
			if(!stack.isEmpty() && !stack.getItem().equals(ARMOR_MAPPER.get(slot))) {
				player.getInventory().armor.set(slot.getEntitySlotId(), makeGold(stack, ARMOR_MAPPER.get(slot)));
			}
		}
	}

	@Override
	public void onBlockInteract(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult) {
		if(MCCEAPI.accessor.isChaosEffectActive(player, MidasTouchEffect.class) && AFFECTED_PLAYERS.tracked(player.getUuid())) {
			BlockPos pos = hitResult.getBlockPos();
			getGoldenBlockState(world.getBlockState(pos)).ifPresent((s) -> world.setBlockState(pos, s));
		}
	}

	@Override
	public void onBlockBreaking(ServerWorld world, int entityId, BlockPos pos, int progress) {
		((ServerWorldASMMixin) world).getServer().getPlayerManager().getPlayerList().stream().filter((player) -> player.getId() == entityId).findFirst().ifPresent((player) -> {
			if(MCCEAPI.accessor.isChaosEffectActive(player, MidasTouchEffect.class) && AFFECTED_PLAYERS.tracked(player.getUuid())) {
				getGoldenBlockState(world.getBlockState(pos)).ifPresent((s) -> world.setBlockState(pos, s));
			}
		});
	}

	private static ItemStack makeGold(ItemStack stackToChange, Item gold) {
		ItemStack stack = new ItemStack(gold);
		stack.setCount(stackToChange.getCount());
		if(stackToChange.hasNbt()) {
			stack.setNbt(stackToChange.getNbt());
		}
		if(stackToChange.isDamageable()) {
			float damageRatio = (float) stackToChange.getDamage() / stackToChange.getMaxDamage();
			stack.setDamage((int) (damageRatio * stack.getMaxDamage()));
		}
		return stack;
	}

	@SuppressWarnings("deprecation")
	private static final boolean doesTurnToGold(BlockState state) {
		//works, should find suitable replacement for blocksMovement()
		return !(!state.blocksMovement() || state.getBlock() == Blocks.RAW_GOLD_BLOCK || state.getBlock() == Blocks.GOLD_BLOCK || state.getBlock() == Blocks.BELL || state.isIn(BlockTags.PRESSURE_PLATES)) && state.getFluidState().isEmpty();
	}
	
	private static final Optional<BlockState> getGoldenBlockState(BlockState state) {
		if(state.isIn(BlockTags.PRESSURE_PLATES) && state.getBlock() != Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE) {
			return Optional.of(GOLD_PRESSURE_PLATE_STATE);
		}
		else if(doesTurnToGold(state)) {
			return Optional.of(GOLD_STATE);
		}
		return Optional.empty();
	}

}
