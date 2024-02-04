package yeelp.mcce.model;

import java.util.Map;
import java.util.UUID;

import com.google.common.collect.Maps;

import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import yeelp.mcce.MCCE;

/**
 * Server state that stores information about each player's state 
 * @author Yeelp
 *
 */
public final class ServerState extends PersistentState {

	private final Map<UUID, PlayerChaosEffectState> players = Maps.newHashMap();
	private final Map<UUID, DespawnTimer> timers = Maps.newHashMap();
	
	private static final String TIMERS_KEY = "despawnTimers";
	
	private static final Type<ServerState> TYPE = new Type<ServerState>(ServerState::new, ServerState::createFromNbt, null);
	
	@Override
	public NbtCompound writeNbt(NbtCompound var1) {
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
	public static final ServerState createFromNbt(NbtCompound tag) {
		MCCE.LOGGER.info(tag.toString());
		ServerState state = new ServerState();
		tag.getKeys().forEach((key) -> {
			if(key.equals(TIMERS_KEY)) {
				return;
			}
			UUID uuid = UUID.fromString(key);
			state.players.put(uuid, new PlayerChaosEffectState(tag.getCompound(key)));
		});
		tag.getCompound(TIMERS_KEY).getKeys().forEach((key) -> {
			UUID uuid = UUID.fromString(key);
			state.timers.put(uuid, new DespawnTimer(tag.getCompound(key)));
		});
		return state;
	}
	
	/**
	 * Gets a {@link PlayerChaosEffectState} for a specified UUID.
	 * @param uuid
	 * @return The PlayerChaosEffectState for that UUID.
	 */
	public PlayerChaosEffectState getEffectState(UUID uuid) {
		return this.players.computeIfAbsent(uuid, (u) -> new PlayerChaosEffectState());
	}
	
	/**
	 * Gets a {@link DespawnTimer} for a specified UUID
	 * @param uuid
	 * @return The DespawnTimer for that UUID.
	 */
	public DespawnTimer getDespawnTimer(UUID uuid) {
		return this.timers.computeIfAbsent(uuid, (u) -> new DespawnTimer());
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
	 * @param server
	 * @return the active ServerState.
	 */
	public static ServerState getServerState(MinecraftServer server) {
		return server.getWorld(World.OVERWORLD).getPersistentStateManager().getOrCreate(TYPE, MCCE.MODID);
	}

}
