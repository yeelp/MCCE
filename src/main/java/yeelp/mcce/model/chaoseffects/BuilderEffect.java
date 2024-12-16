package yeelp.mcce.model.chaoseffects;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.ChunkSectionPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.feature.util.FeatureContext;
import net.minecraft.world.gen.structure.Structure;
import net.minecraft.world.gen.structure.StructureKeys;
import yeelp.mcce.util.ChaosLib;
import yeelp.mcce.util.PlayerUtils;

import java.util.*;
import java.util.Map.Entry;
import java.util.function.BiFunction;

@SuppressWarnings("MagicNumber")
public final class BuilderEffect extends AbstractInstantChaosEffect {

    private static final Map<RegistryKey<Structure>, Float> OPTIONS = Maps.newHashMap();
    private static final Map<Feature<? extends FeatureConfig>, FeatureRecord<?, ?>> FEATURES = Maps.newHashMap();
    private static final List<Entry<RegistryKey<Structure>, Float>> WEIGHTED_LIST;
    private static final float STRUCTURE_CHANCE = 0.6f;

    private record FeatureRecord<C extends FeatureConfig, F extends Feature<C>>(F feature, BiFunction<World, BlockPos, C> prep){
        void generate(BlockPos pos, ChunkGenerator generator, ServerWorld world) {
            this.feature.generate(new FeatureContext<>(Optional.empty(), world, generator, world.getRandom(), pos, this.prep().apply(world, pos)));
        }
    }

    static {
        OPTIONS.put(StructureKeys.BASTION_REMNANT, 6.5f);
        OPTIONS.put(StructureKeys.DESERT_PYRAMID, 4.5f);
        OPTIONS.put(StructureKeys.IGLOO, 9.0f);
        OPTIONS.put(StructureKeys.END_CITY, 2.0f);
        OPTIONS.put(StructureKeys.FORTRESS, 1.5f);
        OPTIONS.put(StructureKeys.JUNGLE_PYRAMID, 4.5f);
        OPTIONS.put(StructureKeys.MANSION, 5.0f);
        OPTIONS.put(StructureKeys.MINESHAFT_MESA, 6.0f);
        OPTIONS.put(StructureKeys.NETHER_FOSSIL, 13.0f);
        OPTIONS.put(StructureKeys.PILLAGER_OUTPOST, 5.0f);
        OPTIONS.put(StructureKeys.SWAMP_HUT, 13.5f);
        OPTIONS.put(StructureKeys.VILLAGE_PLAINS, 10.0f);
        OPTIONS.put(StructureKeys.TRAIL_RUINS, 11.0f);
        OPTIONS.put(StructureKeys.TRIAL_CHAMBERS, 8.5f);

        WEIGHTED_LIST = Lists.newArrayList(OPTIONS.entrySet());

        FEATURES.put(Feature.DESERT_WELL, new FeatureRecord<>(Feature.DESERT_WELL, (world, pos) -> {
            ChaosLib.forEachPos(pos.west(2).north(2), pos.east(2).south(2), (bPos) -> world.setBlockState(bPos, Blocks.SAND.getDefaultState()));
            return DefaultFeatureConfig.INSTANCE;
        }));
        FEATURES.put(Feature.GEODE, new FeatureRecord<>(Feature.GEODE, (world, pos) -> {
            GeodeFeatureConfig config = ((GeodeFeatureConfig) Objects.requireNonNull(world.getRegistryManager().getOrThrow(RegistryKeys.CONFIGURED_FEATURE).get(UndergroundConfiguredFeatures.AMETHYST_GEODE)).config());
            return new GeodeFeatureConfig(config.layerConfig, config.layerThicknessConfig, config.crackConfig, config.usePotentialPlacementsChance, config.useAlternateLayer0Chance, config.placementsRequireLayer0Alternate, config.outerWallDistance, config.distributionPoints, config.pointOffset, config.maxGenOffset, config.minGenOffset, config.noiseMultiplier, Integer.MAX_VALUE);
        }));
        FEATURES.put(Feature.HUGE_RED_MUSHROOM, new FeatureRecord<>(Feature.HUGE_RED_MUSHROOM, (world, pos) -> {
            world.setBlockState(pos.down(), Blocks.PODZOL.getDefaultState());
            ChaosLib.forEachPos(pos.west(2).north(2), pos.east(2).south(2).up(6), (bPos) -> ChaosLib.setToAir(world, bPos));
            return (HugeMushroomFeatureConfig) Objects.requireNonNull(world.getRegistryManager().getOrThrow(RegistryKeys.CONFIGURED_FEATURE).get(TreeConfiguredFeatures.HUGE_RED_MUSHROOM)).config();
        }));
        FEATURES.put(Feature.TREE, new FeatureRecord<>(Feature.TREE, (world, pos) -> {
            world.setBlockState(pos.down(), Blocks.DIRT.getDefaultState());
            ChaosLib.forEachPos(pos.west(3).north(3), pos.east(3).south(3).up(6), (bPos) -> ChaosLib.setToAir(world, bPos));
            return (TreeFeatureConfig) Objects.requireNonNull(world.getRegistryManager().getOrThrow(RegistryKeys.CONFIGURED_FEATURE).get(TreeConfiguredFeatures.SUPER_BIRCH_BEES)).config();
        }));
    }

    @Override
    protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
        return player.isOnGround() && !player.getWorld().getDimension().hasCeiling();
    }

    @Override
    public void applyEffect(PlayerEntity player) {
        PlayerUtils.getServerPlayer(player).ifPresent((p) -> {
            BlockPos pos = player.getBlockPos();
            ServerWorld world = p.getServerWorld();
            ServerChunkManager chunkManager = world.getChunkManager();
            ChunkGenerator generator = chunkManager.getChunkGenerator();
            DynamicRegistryManager registryManager = player.getRegistryManager();
            if(this.getRNG().nextFloat() <= STRUCTURE_CHANCE) {
                RegistryKey<Structure> key;
                float rand = this.getRNG().nextFloat(100.0f);
                Collections.shuffle(WEIGHTED_LIST, this.getRNG());
                Iterator<Entry<RegistryKey<Structure>, Float>> it = WEIGHTED_LIST.iterator();
                do {
                    Entry<RegistryKey<Structure>, Float> entry = it.next();
                    key = entry.getKey();
                    rand -= entry.getValue();
                } while(it.hasNext() && rand > 0);
                StructureStart start = Objects.requireNonNull(registryManager.getOrThrow(RegistryKeys.STRUCTURE).get(key)).createStructureStart(registryManager, generator, generator.getBiomeSource(), chunkManager.getNoiseConfig(), world.getStructureTemplateManager(), this.getRNG().nextLong(), new ChunkPos(pos), 0, world, (biome) -> true);
                if(!start.hasChildren()) {
                    this.generateFeature(pos, generator, world);
                }
                else {
                    BlockBox box = start.getBoundingBox();
                    ChunkPos posMin = new ChunkPos(ChunkSectionPos.getSectionCoord(box.getMinX()), ChunkSectionPos.getSectionCoord(box.getMinZ()));
                    ChunkPos posMax = new ChunkPos(ChunkSectionPos.getSectionCoord(box.getMaxX()), ChunkSectionPos.getSectionCoord(box.getMaxZ()));
                    ChunkPos.stream(posMin, posMax).forEach((chunk) -> start.place(world, world.getStructureAccessor(), generator, world.getRandom(), new BlockBox(chunk.getStartX(), world.getBottomY(), chunk.getStartZ(), chunk.getEndX(), world.getTopYInclusive() + 1, chunk.getEndZ()), chunk));
                }
            }
            else {
                this.generateFeature(pos, generator, world);
            }
        });

    }

    private void generateFeature(BlockPos pos, ChunkGenerator generator, ServerWorld world) {
        FEATURES.get(ChaosLib.getRandomElementFrom(FEATURES.keySet(), this.getRNG())).generate(pos, generator, world);
    }

    @Override
    public String getName() {
        return "builder";
    }

    @Override
    public boolean canBeFirstEffect() {
        return false;
    }
}
