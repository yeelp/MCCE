package yeelp.mcce;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.command.ChaosCommand;
import yeelp.mcce.event.Callbacks;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistry;
import yeelp.mcce.model.chaoseffects.ChaosEffects;
import yeelp.mcce.network.NetworkingPayloads;
import yeelp.mcce.network.PolitePayload;

public final class MCCE implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final String MODID = "mcce";
	public static final Logger LOGGER = LoggerFactory.getLogger(MODID);
	
	@Override
	public void onInitialize() {
		ModConfig.init();
		MCCEAPI.init();
		ChaosEffects.registerEffects();
		Callbacks.registerCallbacks();
		ChaosCommand.register();
		NetworkingPayloads.initialize();
		ServerPlayNetworking.registerGlobalReceiver(PolitePayload.ID, (payload, context) -> {
			context.server().execute(() -> MCCEAPI.mutator.addNewChaosEffect(context.player(), ChaosEffectRegistry.getEffect(payload.effect())));
		});
	}

	public static Identifier createIdentifier(String name) {
		return Identifier.of(MODID, name);
	}
}
