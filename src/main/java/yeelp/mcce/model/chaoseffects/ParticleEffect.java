package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.ParticlePayload;
import yeelp.mcce.util.PlayerUtils;

public final class ParticleEffect extends SimpleTimedChaosEffect {

	private int type;
	
	private static final String ID_KEY = "particle_id";
	private static final int DURATION_MIN = 2000, DURATION_MAX = 3000;

	public ParticleEffect() {
		super(DURATION_MIN, DURATION_MAX);
		this.type = this.getRNG().nextInt(NetworkingConstants.ParticlePacketConstants.getTotalNumberOfGenerators());
	}

	@Override
	public String getName() {
		return "particle";
	}

	@Override
	protected boolean canStack() {
		return false;
	}

	@Override
	protected boolean isApplicableIgnoringStackability(PlayerEntity player) {
		return PlayerUtils.doesPlayerHaveValidPosition(player);
	}

	@Override
	public NbtCompound writeToNbt() {
		NbtCompound nbt = super.writeToNbt();
		nbt.putByte(ID_KEY, (byte) this.type);
		return nbt;
	}

	@Override
	public void readNbt(NbtCompound nbt) {
		super.readNbt(nbt);
		this.type = nbt.getByte(ID_KEY);
	}

	@Override
	public void applyEffect(PlayerEntity player) {
		if(!PlayerUtils.doesPlayerHaveValidPosition(player)) {
			return;
		}
		if(this.getRNG().nextInt(3) != 0) {
			return;
		}
		PlayerUtils.getServerPlayer(player).ifPresent((sp) -> {
			float[] position = this.getPosition(player);
			new ParticlePayload((byte) this.type, position[0], position[1], position[2], 0.0f, 0.0f, 0.0f).send(sp);
		});
	}

    private float[] getPosition(PlayerEntity player) {
        return NetworkingConstants.ParticlePacketConstants.getGeneratorById((byte) this.type).calculatePositionOffset((float) player.getX(), (float) player.getY(), (float) player.getZ());
	}

}
