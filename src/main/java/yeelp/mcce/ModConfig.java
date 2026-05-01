package yeelp.mcce;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ModConfig {

	private static ModConfig instance;
	private static final String PATH = MCCE.MODID + ".json";
	private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).setPrettyPrinting().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
	
	public final GameEffectsOptions game = new GameEffectsOptions();
	public final PerformanceOptions performance = new PerformanceOptions();
	
	public static class GameEffectsOptions {
		public final boolean quiver = true;
		public final boolean blockrain = true;
		public final boolean xprandomizer = true;
		public final boolean paint = false;
	}

	public static class PerformanceOptions {
		public final boolean enableMassEntityDeletionWhenLagDetected = true;
		public final boolean enableEntityCaps = true;
		public final SpawnCaps spawnCaps = new SpawnCaps();
		public static class SpawnCaps {
			public final int localMobCap = 600;
			public final int localItemCap = 1000;
			public final int localFallingBlockCap = 600;
			public final int getRadius = 100;

			public int getLocalMobCap() {
				return this.localMobCap;
			}

			public int getLocalItemCap() {
				return this.localItemCap;
			}

			public int getLocalFallingBlockCap() {
				return this.localFallingBlockCap;
			}
		}
	}
	
	private ModConfig() {

	}
	
	public static void init() {
		Path path = FabricLoader.getInstance().getConfigDir().resolve(PATH);
		if(Files.exists(path)) {
			try(FileReader reader = new FileReader(path.toFile())) {
				instance = GSON.fromJson(reader, ModConfig.class);
			}
			catch(IOException e) {
				MCCE.LOGGER.error("Could not read from config", e);
				throw new RuntimeException("Could not read from config", e);
			}
		}
		else {
			instance = new ModConfig();
		}
		
		Path parent = path.getParent();
		try(FileWriter writer = new FileWriter(path.toFile())) {
			if(!Files.exists(parent)) {
				Files.createDirectories(parent);
			}
			else if(!Files.isDirectory(parent)) {
				throw new RuntimeException(String.format("Parent path %s not a directory", parent));
			}
			writer.write(GSON.toJson(instance));
		}
		catch (IOException e) {
			MCCE.LOGGER.error("Could not read from config", e);
			throw new RuntimeException("Could not write to config", e);
		}
			
	}
	
	public static ModConfig getInstance() {
		return instance == null ? instance = new ModConfig() : instance;
	}
}
