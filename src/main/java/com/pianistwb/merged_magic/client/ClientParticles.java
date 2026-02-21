package com.pianistwb.merged_magic.client;

import java.util.ArrayList;
import java.util.List;

import com.pianistwb.merged_magic.networking.ParticlePayload;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.PacketDistributor;

public class ClientParticles {
	private static final List<Vec3> HERMES_RELATIVE_POSITIONS = new ArrayList<>();

	static {
		for (float deg = 0; deg < 360; deg += 22.5) {
			float rad = Mth.DEG_TO_RAD * deg;
			HERMES_RELATIVE_POSITIONS.add(new Vec3(Mth.cos(rad), 0, Mth.sin(rad)));
		}
	}

	public static final byte HERMES_SHOES = 1;

	public static final byte GUST = 2;

	public static void runHermesShoesParticles(Level level, Vec3 pos) {
		double scale = 0.11D;
		HERMES_RELATIVE_POSITIONS.forEach(particlePos -> {
			level.addParticle(ParticleTypes.CLOUD, pos.x() + particlePos.x(), pos.y() - 0.4D, pos.z() + particlePos.z(),
					scale * particlePos.x(), -scale, scale * particlePos.z());
		});
	}

	public static void runGustParticles(Level level, Vec3 pos) {
		level.addParticle(ParticleTypes.GUST, pos.x(), pos.y() + 1.8D, pos.z(), 0, 0, 0);
	}

	// Non-Packet
	public static void runRandomParticlesWithinBlock(ParticleOptions particle, Level level, Vec3 pos, int count,
			double xSpeed, double ySpeed, double zSpeed) {
		RandomSource rand = level.getRandom();
		for (int i = 0; i < count; i++) {
			level.addParticle(particle, pos.x() - 0.5D + randFloat(rand), pos.y() - 0.5D + randFloat(rand),
					pos.z() - 0.5D + randFloat(rand), xSpeed, ySpeed, zSpeed);
		}
	}

	public static void runRandomParticlesWithinBlockEdges(ParticleOptions particle, Level level, Vec3 pos, int count,
			double xSpeed, double ySpeed, double zSpeed) {
		RandomSource rand = level.getRandom();
		for (int i = 0; i < count; i++) {
			Vec3 face = getRandomFace(rand);

			level.addParticle(particle, pos.x() - 0.5D + face.x(), pos.y() - 0.5D + face.y(), pos.z() - 0.5D + face.z(),
					xSpeed, ySpeed, zSpeed);
		}
	}

	public static Vec3 getRandomFace(RandomSource random) {
		int direction = random.nextInt(6);
		switch (direction) {
		case 0:
			// North -Z
			return new Vec3(randFloat(random), randFloat(random), 0D);
		case 1:
			// South +Z
			return new Vec3(randFloat(random), randFloat(random), 1.0D);
		case 2:
			// East +X
			return new Vec3(1.0D, randFloat(random), randFloat(random));
		case 3:
			// West -X
			return new Vec3(0D, randFloat(random), randFloat(random));
		case 4:
			// Up +Y
			return new Vec3(randFloat(random), 1.0D, randFloat(random));
		case 5:
			// Down -Y
			return new Vec3(randFloat(random), 0D, randFloat(random));
		}

		return new Vec3(0, 0, 0);
	}

	public static void playParticles(Level level, Vec3 pos, byte particleID) {
		switch (particleID) {
		case 1:
			ClientParticles.runHermesShoesParticles(level, pos);
			break;
		case 2:
			ClientParticles.runGustParticles(level, pos);
			break;
		}
	}

	public static void sendOrPlayParticles(Entity entity, Vec3 pos, byte packetId) {
		if (entity.level().isClientSide()) {
			playParticles(entity.level(), pos, packetId);
		} else {
			PacketDistributor.sendToPlayersTrackingEntity(entity, new ParticlePayload(pos, packetId));
		}
	}

	private static float randFloat(RandomSource random) {
		return Mth.randomBetween(random, 0F, 1F);
	}
}
