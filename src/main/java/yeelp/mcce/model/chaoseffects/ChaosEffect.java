package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;

public interface ChaosEffect {
	
	boolean isInstant();
	
	void applyEffect(PlayerEntity player);
	
	String getName();
	
	void registerCallbacks();
	
	NbtCompound writeToNbt();
	
	void readNbt(NbtCompound nbt);
	
	int durationRemaining();
	
	int getDurationUntilNextActivation();
	
	void onEffectEnd(PlayerEntity player);
	
	void tickEffect(PlayerEntity player);
	
	boolean applicable(PlayerEntity player);
}
