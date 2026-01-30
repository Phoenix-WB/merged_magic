package com.pianistwb.merged_magic.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record MergeLevel(int mergeLevel) {
	public static final MergeLevel LEVEL_ONE = new MergeLevel(1);
	public static final MergeLevel LEVEL_TWO = new MergeLevel(2);
	public static final MergeLevel LEVEL_THREE = new MergeLevel(3);

	public static final Codec<MergeLevel> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(Codec.INT.fieldOf("level").forGetter(MergeLevel::mergeLevel)).apply(instance, MergeLevel::new));

	public static final StreamCodec<ByteBuf, MergeLevel> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT,
			MergeLevel::mergeLevel, MergeLevel::new);
	
	public MergeLevel getLevelAbove() {
		if(this.mergeLevel() == 1) {
			return LEVEL_TWO;
		}
		
		return LEVEL_THREE;
	}
}
