package yeelp.mcce;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;

public final class ModConfig {

	private static ModConfig instance;
	private static final String PATH = MCCE.MODID + ".json";
	private static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.IDENTITY).setPrettyPrinting().excludeFieldsWithModifiers(Modifier.PRIVATE).create();
	
	public final GameEffectsOptions game = new GameEffectsOptions();
	
	public static class GameEffectsOptions {
		public boolean quiver = false;
		public boolean blockRain = false;
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
				e.printStackTrace();
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
				throw new RuntimeException(String.format("Parent path %s not a directory", parent.toString()));
			}
			writer.write(GSON.toJson(instance));
		}
		catch (IOException e) {
			e.printStackTrace();
			throw new RuntimeException("Could not write to config", e);
		}
			
	}
	
	public static ModConfig getInstance() {
		return instance == null ? instance = new ModConfig() : instance;
	}
}
