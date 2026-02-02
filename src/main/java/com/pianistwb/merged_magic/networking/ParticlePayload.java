package com.pianistwb.merged_magic.networking;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.client.ClientParticles;
import com.pianistwb.merged_magic.util.ClientUtil;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record ParticlePayload(Vec3 pos, byte particleID) implements CustomPacketPayload {
	public static final CustomPacketPayload.Type<ParticlePayload> TYPE = new CustomPacketPayload.Type<ParticlePayload>(
			Identifier.fromNamespaceAndPath(MergedMagic.MODID, "particle_payload"));

	public static final StreamCodec<ByteBuf, ParticlePayload> STREAM_CODEC = StreamCodec.composite(Vec3.STREAM_CODEC,
			ParticlePayload::pos, ByteBufCodecs.BYTE, ParticlePayload::particleID, ParticlePayload::new);

	public static void handleDataOnClient(final ParticlePayload payload, final IPayloadContext context) {
		context.enqueueWork(() -> {
			Level level = ClientUtil.getClientLevel();

			if (level == null) {
				return;
			}

			Vec3 pos = payload.pos();
			ClientParticles.playParticles(level, pos, payload.particleID());
		});
	}

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return TYPE;
	}
}
