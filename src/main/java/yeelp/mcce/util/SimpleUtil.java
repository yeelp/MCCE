package yeelp.mcce.util;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;

import java.util.Objects;

/**
 * This is to simplify calls that are normally long-winded method calls.
 */
public abstract class SimpleUtil {

    private SimpleUtil() {
        throw new UnsupportedOperationException("Can not initialize");
    }

    public static ServerWorld getServerWorldFromEntity(Entity entity) {
        return Objects.requireNonNull(entity.getServer()).getWorld(entity.getWorld().getRegistryKey());
    }
}
