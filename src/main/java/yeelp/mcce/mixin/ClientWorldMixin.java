package yeelp.mcce.mixin;

import java.util.function.Supplier;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.DynamicRegistryManager;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.profiler.Profiler;
import net.minecraft.world.MutableWorldProperties;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;
import yeelp.mcce.client.event.StutterSoundSoundHandler;

@Mixin(ClientWorld.class)
public abstract class ClientWorldMixin extends World {

	public ClientWorldMixin(MutableWorldProperties properties, RegistryKey<World> registryRef, DynamicRegistryManager registryManager, RegistryEntry<DimensionType> dimensionEntry, Supplier<Profiler> profiler, boolean isClient, boolean debugWorld, long biomeAccess, int maxChainedNeighborUpdates) {
		super(properties, registryRef, registryManager, dimensionEntry, profiler, isClient, debugWorld, biomeAccess, maxChainedNeighborUpdates);
	}

	@SuppressWarnings({
			"resource",
			"static-method"})
	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getSoundManager()Lnet/minecraft/client/sound/SoundManager;"), method = "playSound(DDDLnet/minecraft/sound/SoundEvent;Lnet/minecraft/sound/SoundCategory;FFZJ)V")
	private void playSound(@SuppressWarnings("unused") double x, @SuppressWarnings("unused") double y, @SuppressWarnings("unused") double z, SoundEvent sound, @SuppressWarnings("unused") SoundCategory category, float volume, float pitch, @SuppressWarnings("unused") boolean useDistance, @SuppressWarnings("unused") long seed, @SuppressWarnings("unused") CallbackInfo info) {
		StutterSoundSoundHandler.setSound(MinecraftClient.getInstance().player, sound, volume, pitch);
	}
}
