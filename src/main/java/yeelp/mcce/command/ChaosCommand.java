package yeelp.mcce.command;

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
import net.minecraft.command.permission.Permission;
import net.minecraft.command.permission.PermissionLevel;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;
import yeelp.mcce.api.MCCEAPI;
import yeelp.mcce.model.chaoseffects.ChaosEffect;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistry;
import yeelp.mcce.model.chaoseffects.ChaosEffectRegistryEntry;

import java.util.concurrent.CompletableFuture;

public final class ChaosCommand {

	private static final EffectSuggestionProvider EFFECT_SUGGESTIONS = new EffectSuggestionProvider();
	private static final String EFFECT_ARG_NAME = "effect";
	private static final RequiredArgumentBuilder<ServerCommandSource, String> EFFECT_ARG_NODE = CommandManager.argument(EFFECT_ARG_NAME, StringArgumentType.word()).suggests(EFFECT_SUGGESTIONS);

	public static void register() {
		CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, env) -> dispatcher.register(CommandManager.literal("chaos").requires((ctx) -> ctx.getPermissions().hasPermission(new Permission.Level(PermissionLevel.ADMINS)))
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
				.then(CommandManager.literal("applicable")
						.then(EFFECT_ARG_NODE
								.executes((ctx) -> applicable(ctx.getSource(), getEffectArg(ctx))))
						.executes((ctx) -> applicable(ctx.getSource(), null)))
		));
	}

	@SuppressWarnings("SameReturnValue")
    private static int applyEffect(ServerCommandSource src, String effect) throws CommandSyntaxException {
		ChaosEffect ce = effect == null ? ChaosEffectRegistry.getRandomEffect() : getChaosEffectEntryOrThrow(effect).createChaosEffect();
		MCCEAPI.mutator.addNewChaosEffect(src.getPlayer(), ce);
		src.sendFeedback(() -> Text.literal("Success!"), false);
		return Command.SINGLE_SUCCESS;
	}

	private static int applicable(ServerCommandSource src, String effect) throws CommandSyntaxException {
		StringBuilder result;
		boolean applicable;
		if(effect == null) {
			applicable = true;
			result = ChaosEffectRegistry.getAllEffects().filter((entry) -> entry.isApplicable(src.getPlayer())).map(ChaosEffectRegistryEntry::getName).reduce(new StringBuilder(), (sb, s2) -> sb.append(", ").append(s2), StringBuilder::append);
			result.delete(0, 2);
		}
        else {
			applicable = getChaosEffectEntryOrThrow(effect).isApplicable(src.getPlayer());
            result = new StringBuilder(applicable ? "Yes!": "No");
        }
        src.sendFeedback(() -> Text.literal(result.toString()), false);
		return applicable ? Command.SINGLE_SUCCESS : 0;
	}

	private static int checkEffect(ServerCommandSource src, String effect) throws CommandSyntaxException {
		boolean active = MCCEAPI.accessor.isChaosEffectActive(src.getPlayer(), getChaosEffectEntryOrThrow(effect));
		src.sendFeedback(() -> active ? Text.literal("Yes!") : Text.literal("No"), false);
		return active ? Command.SINGLE_SUCCESS : 0;
	}
	
	private static int removeEffect(ServerCommandSource src, String effect) throws CommandSyntaxException {
		PlayerEntity player = src.getPlayer();
		int result;
		String msg = "Success!";
		if(effect == null) {
			int count = Iterators.size(MCCEAPI.accessor.getPlayerChaosEffectState(player).iterator());
			MCCEAPI.mutator.clear(player);
			result = count;
		}
		else if(MCCEAPI.mutator.removeChaosEffect(player, getChaosEffectEntryOrThrow(effect))) {
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

	private static ChaosEffectRegistryEntry getChaosEffectEntryOrThrow(String effect) throws CommandSyntaxException {
		if(ChaosEffectRegistry.isEffectRegistered(effect)) {
			return ChaosEffectRegistry.getEntry(effect);
		}
		throw new DynamicCommandExceptionType((name) -> Text.literal("The effect: " + name + " doesn't exist!")).create(effect);
	}

	private static final class EffectSuggestionProvider implements SuggestionProvider<ServerCommandSource> {

		@Override
		public CompletableFuture<Suggestions> getSuggestions(CommandContext<ServerCommandSource> context, SuggestionsBuilder builder) {
			ChaosEffectRegistry.getAllEffects().map(ChaosEffectRegistryEntry::getName).forEach(builder::suggest);
			return builder.buildFuture();
		}

	}
}
