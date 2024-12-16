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
	private static final float HORIZONTAL_OFFSET = 0.5f;

	public ParticleEffect() {
		super(DURATION_MIN, DURATION_MAX);
		this.type = this.getRNG().nextInt(PARTICLE_TYPES_AMOUNT);
	}

	public static final int PARTICLE_TYPES_AMOUNT;

	static {
		int i;
		for(i = 0; NetworkingConstants.ParticlePacketConstants.getParticle((byte) i) != null; i++);
		PARTICLE_TYPES_AMOUNT = i;
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

	@SuppressWarnings("MagicNumber")
    private float[] getPosition(PlayerEntity player) {
		float x = (float) player.getX(), y = (float) player.getY(), z = (float) player.getZ();
		float rx = this.getRNG().nextFloat(-HORIZONTAL_OFFSET, HORIZONTAL_OFFSET), rz = this.getRNG().nextFloat(-HORIZONTAL_OFFSET, HORIZONTAL_OFFSET);
        return switch ((byte) this.type) {
            case NetworkingConstants.ParticlePacketConstants.SOUL -> new float[]{
                    x + rx,
                    y + this.getRNG().nextFloat(0.0f, 0.25f),
                    z + rz
            };
            case NetworkingConstants.ParticlePacketConstants.DAMAGE_INDICATOR,
                 NetworkingConstants.ParticlePacketConstants.EXPLOSION,
                 NetworkingConstants.ParticlePacketConstants.TOTEM -> new float[]{
                    x + rx,
                    y + this.getRNG().nextFloat(0.25f, 0.75f),
                    z + rz
            };
            case NetworkingConstants.ParticlePacketConstants.ASH, NetworkingConstants.ParticlePacketConstants.NAUTILUS,
                 NetworkingConstants.ParticlePacketConstants.CAMPFIRE,
                 NetworkingConstants.ParticlePacketConstants.CHERRY, NetworkingConstants.ParticlePacketConstants.HEART,
                 NetworkingConstants.ParticlePacketConstants.WITCH, NetworkingConstants.ParticlePacketConstants.NOTE,
                 NetworkingConstants.ParticlePacketConstants.BUBBLE, NetworkingConstants.ParticlePacketConstants.SPORE,
                 NetworkingConstants.ParticlePacketConstants.SPARK -> new float[]{
                    x + rx,
                    y + this.getRNG().nextFloat(1.0f, 1.8f),
                    z + rz
            };
            case NetworkingConstants.ParticlePacketConstants.SONIC_BOOM -> new float[]{
                    x,
                    y + 0.5f,
                    z
            };
            default -> new float[]{x, y, z};
        };
	}

}
