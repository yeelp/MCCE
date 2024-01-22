package yeelp.mcce.model.chaoseffects;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import yeelp.mcce.network.NetworkingConstants;
import yeelp.mcce.network.ParticlePacket;
import yeelp.mcce.util.PlayerUtils;

public class ParticleEffect extends SimpleTimedChaosEffect {

	private int type;
	
	private static final String ID_KEY = "particle_id";

	public ParticleEffect() {
		super(2000, 3000);
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
		return true;
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
		if(this.getRNG().nextInt(3) != 0) {
			return;
		}
		PlayerUtils.getServerPlayer(player).ifPresent((sp) -> {
			float[] position = this.getPosition(player);
			new ParticlePacket((byte) this.type, position[0], position[1], position[2], 0.0f, 0.0f, 0.0f).sendPacket(sp);
		});
	}

	private final float[] getPosition(PlayerEntity player) {
		float x = (float) player.getX(), y = (float) player.getY(), z = (float) player.getZ();
		float rx = this.getRNG().nextFloat(-0.5f, 0.5f), rz = this.getRNG().nextFloat(-0.5f, 0.5f);
		switch((byte) this.type) {
			case NetworkingConstants.ParticlePacketConstants.SOUL:
				return new float[] {
					x + rx,
					y + this.getRNG().nextFloat(0.0f, 0.25f),
					z + rz
				};
			case NetworkingConstants.ParticlePacketConstants.DAMAGE_INDICATOR:
			case NetworkingConstants.ParticlePacketConstants.EXPLOSION:
			case NetworkingConstants.ParticlePacketConstants.TOTEM:
				return new float[] {
					x + rx,
					y + this.getRNG().nextFloat(0.25f, 0.75f),
					z + rz
				};
			case NetworkingConstants.ParticlePacketConstants.ASH:
			case NetworkingConstants.ParticlePacketConstants.NAUTILUS:
			case NetworkingConstants.ParticlePacketConstants.CAMPFIRE:
			case NetworkingConstants.ParticlePacketConstants.CHERRY:
			case NetworkingConstants.ParticlePacketConstants.HEART:
			case NetworkingConstants.ParticlePacketConstants.WITCH:
			case NetworkingConstants.ParticlePacketConstants.NOTE:
			case NetworkingConstants.ParticlePacketConstants.BUBBLE:
			case NetworkingConstants.ParticlePacketConstants.SPORE:
			case NetworkingConstants.ParticlePacketConstants.SPARK:
				return new float[] {
					x + rx,
					y + this.getRNG().nextFloat(1.0f, 1.8f),
					z + rz
				};
			case NetworkingConstants.ParticlePacketConstants.SONIC_BOOM:
				return new float[] {
						x,
						y + 0.5f,
						z
				};
			default:
				return new float[] {x, y, z};
		}
	}

}
