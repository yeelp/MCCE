package yeelp.mcce.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;

@Mixin(ServerWorld.class)
public interface ServerWorldASMMixin {
	@Accessor("server")
	public MinecraftServer getServer();
}
