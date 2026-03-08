package com.pianistwb.merged_magic.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record IntegerData(int value) {
	public static final IntegerData DEFAULT = new IntegerData(0);

	public static final Codec<IntegerData> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(Codec.INT.fieldOf("state").forGetter(IntegerData::value)).apply(instance, IntegerData::create));

	public static final StreamCodec<ByteBuf, IntegerData> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT,
			IntegerData::value, IntegerData::create);

	public static IntegerData create(int state) {
		if (state == 0) {
			return DEFAULT;
		}

		return new IntegerData(state);
	}
}
