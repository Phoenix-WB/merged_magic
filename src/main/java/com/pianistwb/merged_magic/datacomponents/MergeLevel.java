package com.pianistwb.merged_magic.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MergeLevel(int mergeLevel) {
	public static final Codec<MergeLevel> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(Codec.INT.fieldOf("level").forGetter(MergeLevel::mergeLevel)).apply(instance, MergeLevel::new));

	public static final StreamCodec<ByteBuf, MergeLevel> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT,
			MergeLevel::mergeLevel, MergeLevel::new);
}
