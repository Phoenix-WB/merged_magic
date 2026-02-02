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
			level.addParticle(ParticleTypes.CLOUD, pos.x() + particlePos.x(), pos.y() - 0.4, pos.z() + particlePos.z(),
					scale * particlePos.x(), -scale, scale * particlePos.z());
		});
	}

	public static void runMiniCopperFlameParticles(Level level, Vec3 pos, float height) {
		level.addParticle(ParticleTypes.COPPER_FIRE_FLAME, pos.x(), pos.y() + height, pos.z(), 0, -0.05, 0);
	}

	public static void runCopperFlameSpireParticles(Level level, Vec3 pos) {
		for (int i = 2; i < 6; i++) {
			level.addParticle(ParticleTypes.COPPER_FIRE_FLAME,
					pos.x() - 0.5 + Mth.randomBetween(level.getRandom(), 0, 1), pos.y() + i * 0.2D,
					pos.z() - 0.5 + Mth.randomBetween(level.getRandom(), 0, 1), 0, -0.05, 0);
		}
	}
}
