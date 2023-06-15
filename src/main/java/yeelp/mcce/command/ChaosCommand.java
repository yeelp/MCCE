package yeelp.mcce.command;

import java.util.concurrent.CompletableFuture;

import com.google.common.collect.Iterators;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.model.chaoseffects.ChaosEffect;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistry;

public final class ChaosCommand {

	private static final EffectSuggestionProvider EFFECT_SUGGESTIONS = new EffectSuggestionProvider();
	private static final String EFFECT_ARG_NAME = "effect";
	private static final RequiredArgumentBuilder<ServerCommandSource, String> EFFECT_ARG_NODE = CommandManager.argument(EFFECT_ARG_NAME, StringArgumentType.word()).suggests(EFFECT_SUGGESTIONS);

	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, env) -> dispatcher.register(CommandManager.literal("chaos").requires((ctx) -> ctx.hasPermissionLevel(4))
				.then(CommandManager.literal("apply")
						.then(EFFECT_ARG_NODE
								.executes((ctx) -> applyEffect(ctx.getSource(), getEffectArg(ctx))))
						.executes((ctx) -> applyEffect(ctx.getSource(), null)))
				.then(CommandManager.literal("isActive")
						.then(EFFECT_ARG_NODE
								.executes((ctx) -> checkEffect(ctx.getSource(), getEffectArg(ctx)))))
				.then(CommandManager.literal("clear")
						.then(EFFECT_ARG_NODE
								.executes((ctx) -> removeEffect(ctx.getSource(), getEffectArg(ctx))))
						.executes((ctx) -> removeEffect(ctx.getSource(), null)))
				));
	}

	private static int applyEffect(ServerCommandSource src, String effect) throws CommandSyntaxException {
		ChaosEffect ce = effect == null ? ChaosEffectRegistry.getRandomEffect() : getChaosEffectOrThrow(effect);
		MCCEAPI.mutator.addNewChaosEffect(src.getPlayer(), ce);
		src.sendFeedback(() -> Text.literal("Success!"), false);
		return Command.SINGLE_SUCCESS;
	}

	private static int checkEffect(ServerCommandSource src, String effect) throws CommandSyntaxException {
		ChaosEffect ce = getChaosEffectOrThrow(effect);
		boolean active = MCCEAPI.accessor.isChaosEffectActive(src.getPlayer(), ce.getClass());
		src.sendFeedback(() -> active ? Text.literal("Yes!") : Text.literal("No"), false);
		return active ? Command.SINGLE_SUCCESS : 0;
	}
	
	private static int removeEffect(ServerCommandSource src, String effect) throws CommandSyntaxException {
		ChaosEffect ce = effect == null ? null : getChaosEffectOrThrow(effect);
		PlayerEntity player = src.getPlayer();
		int result;
		String msg = "Success!";
		if(ce == null) {
			int count = Iterators.size(MCCEAPI.accessor.getPlayerChaosEffectState(player).iterator());
			MCCEAPI.mutator.clear(player);
			result = count;
		}
		else if(MCCEAPI.mutator.removeChaosEffect(player, ce.getClass())) {
			result = Command.SINGLE_SUCCESS;
		}
		else {
			msg = "Effect not present...";
			result = 0;
		}
		final Text text = Text.literal(msg);
		src.sendFeedback(() -> text, false);
		return result;
	}
	
	private static String getEffectArg(CommandContext<ServerCommandSource> ctx) {
		return StringArgumentType.getString(ctx, EFFECT_ARG_NAME);
	}

	private static ChaosEffect getChaosEffectOrThrow(String effect) throws CommandSyntaxException {
		if(ChaosEffectRegistry.isEffectRegistered(effect)) {
			return ChaosEffectRegistry.getEffect(effect);
		}
		throw new DynamicCommandExceptionType((name) -> Text.literal("The effect: " + (String) name + " doesn't exist!")).create(effect);
	}

	private static final class EffectSuggestionProvider implements SuggestionProvider<ServerCommandSource> {

		@Override
		public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) throws CommandSyntaxException {
			ChaosEffectRegistry.getAllEffects().map(ChaosEffect::getName).forEach(builder::suggest);
			return builder.buildFuture();
		}

	}
}
