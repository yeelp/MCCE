package yeelp.mcce.model;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.PersistentStateType;
import net.minecraft.world.World;
import yeelp.mcce.MCCE;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Server state that stores information about each player's state 
 * @author Yeelp
 *
 */
public final class ServerState extends PersistentState {

	private final Map<UUID, PlayerChaosEffectState> players = Maps.newHashMap();
	private final Map<UUID, DespawnTimer> timers = Maps.newHashMap();
	
	private static final String TIMERS_KEY = "despawnTimers";
	private static final Codec<ServerState> CODEC = NbtCompound.CODEC.comapFlatMap((nbt) -> DataResult.success(ServerState.createFromNbt(nbt)), ServerState::writeNbt);
	
	private static final PersistentStateType<ServerState> TYPE = new PersistentStateType<>(MCCE.MODID, ServerState::new, CODEC, DataFixTypes.LEVEL);

	public NbtCompound writeNbt() {
		NbtCompound tag = new NbtCompound();
		NbtCompound nested = new NbtCompound();
		
		this.players.forEach((uuid, pces) -> tag.put(uuid.toString(), pces.writeToNbt()));
		this.timers.forEach((uuid, timer) -> nested.put(uuid.toString(), timer.writeToNbt()));
		tag.put(TIMERS_KEY, nested);
		return tag;
	}
	
	/**
	 * Create a ServerState from stored NBT data
	 * @param tag the stored NBT data
	 * @return a ServerState reflecting the stored NBT data.
	 */
	public static ServerState createFromNbt(NbtCompound tag) {
		ServerState state = new ServerState();
		tag.getKeys().forEach((key) -> {
			if(key.equals(TIMERS_KEY)) {
				return;
			}
			UUID uuid = UUID.fromString(key);
			tag.getCompound(key).ifPresent((t) -> state.players.put(uuid, new PlayerChaosEffectState(t)));
		});
		tag.getCompound(TIMERS_KEY).map(NbtCompound::getKeys).ifPresent((keys) -> keys.forEach((key) -> {
			UUID uuid = UUID.fromString(key);
			tag.getCompound(key).ifPresent((t) -> state.timers.put(uuid, new DespawnTimer(t)));
		}));
		return state;
	}
	
	/**
	 * Gets a {@link PlayerChaosEffectState} for a specified UUID.
	 * @param uuid UUID of the player to get the state for
	 * @return The PlayerChaosEffectState for that UUID.
	 */
	public PlayerChaosEffectState getEffectState(UUID uuid) {
		return this.players.computeIfAbsent(uuid, (u) -> new PlayerChaosEffectState());
	}
	
	/**
	 * Gets a {@link DespawnTimer} for a specified UUID
	 * @param uuid UUID of the entity to get the despawn timer for
	 * @return The DespawnTimer for that UUID.
	 */
	public DespawnTimer getDespawnTimer(UUID uuid) {
		return this.timers.computeIfAbsent(uuid, (u) -> new DespawnTimer());
	}

	/**
	 * Checks if a UUID has a {@link DespawnTimer}.
	 * @param uuid UUID to check
	 * @return true if it has a timer.
	 */
	public boolean hasDespawnTimer(UUID uuid) {
		return this.timers.containsKey(uuid);
	}
	
	/**
	 * Remove a {@link DespawnTimer} for a specified UUID
	 * @param uuid the uuid of the despawn timer to remove.
	 */
	public void removeTimer(UUID uuid) {
		this.timers.remove(uuid);
	}
	
	/**
	 * Get the ServerState active on the server.
	 * @param server The server instance
	 * @return the active ServerState.
	 */
	public static ServerState getServerState(MinecraftServer server) {
		return Objects.requireNonNull(server.getWorld(World.OVERWORLD)).getPersistentStateManager().getOrCreate(TYPE);
	}

}
