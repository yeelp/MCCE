package yeelp.mcce;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.fabricmc.api.ModInitializer;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.command.ChaosCommand;
import yeelp.mcce.event.Callbacks;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistry;

public class MCCE implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "mcce";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	
	@Override
	public void onInitialize() {
		ModConfig.init();
		MCCEAPI.init();
		ChaosEffectRegistry.registerEffects();
		Callbacks.registerCallbacks();
		ChaosCommand.register();
	}
}
