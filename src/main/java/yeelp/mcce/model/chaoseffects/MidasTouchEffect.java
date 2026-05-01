package yeelp.mcce.model.chaoseffects;

import com.google.common.base.Predicates;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.event.OnBlockBreakingCallback;
import yeelp.mcce.event.OnBlockInteractCallback;
import yeelp.mcce.mixin.ServerWorldASMMixin;
import yeelp.mcce.util.Tracker;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

public final class MidasTouchEffect extends AbstractTimedChaosEffect implements OnBlockInteractCallback, OnBlockBreakingCallback {

	private static final Tracker AFFECTED_PLAYERS = new Tracker();
	private static final BlockState GOLD_STATE = Blocks.GOLD_BLOCK.getDefaultState();
	private static final BlockState GOLD_PRESSURE_PLATE_STATE = Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE.getDefaultState();
	private static final Map<Item, Set<Item>> ITEM_MAPPER = Maps.newHashMap();
	private static final Set<Item> BLACKLIST = Sets.newHashSet();
	private static final int DURATION_MIN = 700, DURATION_MAX = 1300;

	private interface GoldItemSupplier extends Supplier<Item> {
		Item getConvertedItem();

		@Override
		default Item get() {
			return this.getConvertedItem();
		}
	}

	private interface GoldData extends GoldItemSupplier {
		TagKey<Item> getTagToConvert();

		@SafeVarargs
        @SuppressWarnings("DataFlowIssue")
        static <T extends GoldData> Optional<T> determineGoldDataFrom(ItemStack stack, @NotNull T... data) {
			return Arrays.stream(data).filter(Predicates.compose(stack::isIn, GoldData::getTagToConvert)).findFirst();
		}
	}

	private enum GoldArmorData implements GoldData {
		HELMET(Items.GOLDEN_HELMET, ItemTags.HEAD_ARMOR, EquipmentSlot.HEAD),
		CHESTPLATE(Items.GOLDEN_CHESTPLATE, ItemTags.CHEST_ARMOR, EquipmentSlot.CHEST),
		LEGGINGS(Items.GOLDEN_LEGGINGS, ItemTags.LEG_ARMOR, EquipmentSlot.LEGS),
		BOOTS(Items.GOLDEN_BOOTS, ItemTags.FOOT_ARMOR, EquipmentSlot.FEET);

		private final Item item;
		private final TagKey<Item> tag;
		private final EquipmentSlot slot;

		GoldArmorData(Item item, TagKey<Item> tag, EquipmentSlot slot) {
			this.item = item;
			this.tag = tag;
			this.slot = slot;
		}

		@Override
		public TagKey<Item> getTagToConvert() {
			return this.tag;
		}

		@Override
		public Item getConvertedItem() {
			return this.item;
		}

		EquipmentSlot getSlot() {
			return this.slot;
		}

		static Optional<GoldArmorData> determineGoldArmor(ItemStack stack) {
			return GoldData.determineGoldDataFrom(stack, GoldArmorData.values());
		}
	}

	private enum GoldToolData implements GoldData {
		SWORD(Items.GOLDEN_SWORD, ItemTags.SWORDS),
		AXE(Items.GOLDEN_AXE, ItemTags.AXES),
		PICKAXES(Items.GOLDEN_PICKAXE, ItemTags.PICKAXES),
		SHOVELS(Items.GOLDEN_SHOVEL, ItemTags.SHOVELS),
		HOES(Items.GOLDEN_HOE, ItemTags.HOES),
		SPEARS(Items.GOLDEN_SPEAR, ItemTags.SPEARS);

		private final Item item;
		private final TagKey<Item> tag;

		GoldToolData(Item item, TagKey<Item> tag) {
			this.item = item;
			this.tag = tag;
		}

		@Override
		public Item getConvertedItem() {
			return item;
		}

		@Override
		public TagKey<Item> getTagToConvert() {
			return tag;
		}

		static Optional<GoldToolData> determineGoldTool(ItemStack stack) {
			return GoldData.determineGoldDataFrom(stack, GoldToolData.values());
		}
	}

	private enum GoldPetArmorData implements GoldItemSupplier {
		HORSE_ARMOR(Items.GOLDEN_HORSE_ARMOR, EntityTypeTags.CAN_WEAR_HORSE_ARMOR),
		NAUTILUS_ARMOR(Items.GOLDEN_NAUTILUS_ARMOR, EntityTypeTags.CAN_WEAR_NAUTILUS_ARMOR);

		private final Item item;
		private final TagKey<EntityType<?>> tag;
		private static final RegistryEntryLookup<EntityType<?>> LOOKUP = Registries.createEntryLookup(Registries.ENTITY_TYPE);

		GoldPetArmorData(Item item, TagKey<EntityType<?>> tag) {
			this.item = item;
			this.tag = tag;
		}

		@Override
		public Item getConvertedItem() {
			return this.item;
		}

		static Optional<GoldPetArmorData> determineGoldPetArmor(ItemStack stack) {
			EquippableComponent equip = stack.getComponents().get(DataComponentTypes.EQUIPPABLE);
			if(equip == null) {
				return Optional.empty();
			}
			return equip.allowedEntities().flatMap((entities) -> Arrays.stream(GoldPetArmorData.values()).filter((data) -> {
				RegistryEntryList<EntityType<?>> list = LOOKUP.getOrThrow(data.tag);
				if(list.size() == entities.size()) {
					boolean same = true;
					for(RegistryEntry<EntityType<?>> type : entities) {
						if(!list.contains(type)) {
							same = false;
							break;
						}
					}
					return same;
				}
				return false;
			}).findFirst());
		}

	}

	static {

		ITEM_MAPPER.put(Items.GOLD_INGOT, ImmutableSet.<Item>builder().add(Items.COPPER_INGOT, Items.IRON_INGOT, Items.NETHERITE_INGOT, Items.BRICK, Items.NETHER_BRICK).build());
		ITEM_MAPPER.put(Items.GOLD_NUGGET, ImmutableSet.<Item>builder().add(Items.IRON_NUGGET, Items.NETHERITE_SCRAP, Items.COPPER_NUGGET).build());
		ITEM_MAPPER.put(Items.GOLDEN_CARROT, ImmutableSet.of(Items.CARROT));

		BLACKLIST.add(Items.BELL);
		BLACKLIST.add(Items.RAW_GOLD);
		BLACKLIST.add(Items.RAW_GOLD_BLOCK);
		BLACKLIST.add(Items.GOLD_BLOCK);
		BLACKLIST.add(Items.ENCHANTED_GOLDEN_APPLE);
		BLACKLIST.add(Items.GOLDEN_APPLE);
		BLACKLIST.add(Items.LIGHT_WEIGHTED_PRESSURE_PLATE);

		//This initializes the enum classes early.
        //noinspection ResultOfMethodCallIgnored
        GoldArmorData.values();
		//noinspection ResultOfMethodCallIgnored
		GoldPetArmorData.values();
		//noinspection ResultOfMethodCallIgnored
		GoldToolData.values();
	}

	public MidasTouchEffect() {
		super(DURATION_MIN, DURATION_MAX);
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
	public String getDisplayName() {
		return "Midas Touch";
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
		return MCCEAPI.accessor.areChaosEffectsNotActive(player, ChaosEffects.CRUMBLE, ChaosEffects.NETHERITE_TRANSMUTATION);
	}

	@Override
	protected void tickAdditionalEffectLogic(PlayerEntity player) {
		BlockPos pos = player.getBlockPos();
		World world = player.getEntityWorld();
		boolean secondPass = false;
		do {
			final BlockPos p = pos;
			final boolean b = secondPass;
			BlockState state = world.getBlockState(p);
			getGoldenBlockState(state).ifPresent((s) -> {
				if((!b && s == GOLD_PRESSURE_PLATE_STATE) || (b && s == GOLD_STATE)) {
					world.setBlockState(p, s);
				}
			});
			pos = pos.down();
			secondPass = !secondPass;
		}while(secondPass);
		for(Hand hand : Hand.values()) {
			ItemStack stack = player.getStackInHand(hand);
			Item stackItem = stack.getItem();
			if(!stack.isEmpty() && !BLACKLIST.contains(stackItem)) {
				Optional<GoldArmorData> goldArmor;
				Optional<GoldToolData> goldTool;
				Optional<GoldPetArmorData> goldPet;
				if((goldArmor = GoldArmorData.determineGoldArmor(stack)).isPresent()) {
					tryConvertToGoldFromSupplier(player, hand, stack, stackItem, goldArmor.get());
				}
				else if((goldTool = GoldToolData.determineGoldTool(stack)).isPresent()) {
					tryConvertToGoldFromSupplier(player, hand, stack, stackItem, goldTool.get());
				}
				else if ((goldPet = GoldPetArmorData.determineGoldPetArmor(stack)).isPresent()) {
					tryConvertToGoldFromSupplier(player, hand, stack, stackItem, goldPet.get());
				}
				else if(stackItem == Items.APPLE) {
					if(stack.hasGlint()) {
						player.setStackInHand(hand, makeGold(stack, Items.ENCHANTED_GOLDEN_APPLE));
					}
					else {
						player.setStackInHand(hand, makeGold(stack, Items.GOLDEN_APPLE));
					}
				}
				else if(stackItem instanceof BlockItem && ((BlockItem) stack.getItem()).getBlock().getDefaultState().isIn(BlockTags.PRESSURE_PLATES)) {
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
		for(MidasTouchEffect.GoldArmorData data : GoldArmorData.values()) {
			EquipmentSlot slot = data.getSlot();
			ItemStack stack = player.getEquippedStack(slot);
			Item goldItem = data.getConvertedItem();
			if(!stack.isEmpty() && !stack.getItem().equals(goldItem)) {
				player.equipStack(slot, makeGold(stack, goldItem));
			}
		}
	}

	@Override
	public void onBlockInteract(ServerPlayerEntity player, World world, ItemStack stack, Hand hand, BlockHitResult hitResult) {
		if(MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.MIDAS_TOUCH) && AFFECTED_PLAYERS.tracked(player.getUuid())) {
			BlockPos pos = hitResult.getBlockPos();
			getGoldenBlockState(world.getBlockState(pos)).ifPresent((s) -> world.setBlockState(pos, s));
		}
	}

	@Override
	public void onBlockBreaking(ServerWorld world, int entityId, BlockPos pos, int progress) {
		((ServerWorldASMMixin) world).getServer().getPlayerManager().getPlayerList().stream().filter((player) -> player.getId() == entityId).findFirst().ifPresent((player) -> {
			if(MCCEAPI.accessor.isChaosEffectActive(player, ChaosEffects.MIDAS_TOUCH) && AFFECTED_PLAYERS.tracked(player.getUuid())) {
				getGoldenBlockState(world.getBlockState(pos)).ifPresent((s) -> world.setBlockState(pos, s));
			}
		});
	}

	private static ItemStack makeGold(ItemStack stackToChange, Item gold) {
		ItemStack stack = stackToChange.copyComponentsToNewStack(gold, stackToChange.getCount());
		if(stackToChange.isDamageable()) {
			float damageRatio = (float) stackToChange.getDamage() / stackToChange.getMaxDamage();
			stack.setDamage((int) (damageRatio * stack.getMaxDamage()));
		}
		return stack;
	}

	@SuppressWarnings("deprecation")
	private static boolean doesTurnToGold(BlockState state) {
		//works, should find suitable replacement for blocksMovement()
		return !(!state.blocksMovement() || state.getBlock() == Blocks.RAW_GOLD_BLOCK || state.getBlock() == Blocks.GOLD_BLOCK || state.getBlock() == Blocks.BELL || state.isIn(BlockTags.PRESSURE_PLATES)) && state.getFluidState().isEmpty();
	}
	
	private static Optional<BlockState> getGoldenBlockState(BlockState state) {
		if(state.isIn(BlockTags.PRESSURE_PLATES) && state.getBlock() != Blocks.LIGHT_WEIGHTED_PRESSURE_PLATE) {
			return Optional.of(GOLD_PRESSURE_PLATE_STATE);
		}
		else if(doesTurnToGold(state)) {
			return Optional.of(GOLD_STATE);
		}
		return Optional.empty();
	}

	private static void tryConvertToGoldFromSupplier(PlayerEntity player, Hand hand, ItemStack stack, Item stackItem, GoldItemSupplier supplier) {
		Item gold = supplier.getConvertedItem();
		if(stackItem.equals(gold)) {
			return;
		}
		player.setStackInHand(hand, makeGold(stack, gold));
	}

}
