package com.pianistwb.merged_magic.client;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ClientParticles {
	private static final List<Vec3> HERMES_RELATIVE_POSITIONS = new ArrayList<>();

	static {
		for (float deg = 0; deg < 360; deg += 22.5) {
			float rad = Mth.DEG_TO_RAD * deg;
			HERMES_RELATIVE_POSITIONS.add(new Vec3(Mth.cos(rad), 0, Mth.sin(rad)));
		}
	}
	
	public static final byte HERMES_BOOTS = 1;
	
	public static void runHermesBootsParticles(Level level, Vec3 pos) {
		double scale = 0.11D;
		HERMES_RELATIVE_POSITIONS.forEach(particlePos -> {
			level.addParticle(ParticleTypes.CLOUD, pos.x() + particlePos.x(), pos.y() - 0.4,
					pos.z() + particlePos.z(), scale * particlePos.x(), -scale, scale * particlePos.z());
		});
	}
}
