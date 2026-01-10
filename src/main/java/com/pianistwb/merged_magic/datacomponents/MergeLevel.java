package com.pianistwb.merged_magic.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record MergeLevel(int level) {
	public static final Codec<MergeLevel> BASIC_CODEC = RecordCodecBuilder.create(instance -> instance
			.group(Codec.INT.fieldOf("level").forGetter(MergeLevel::level)).apply(instance, MergeLevel::new));

	public static final StreamCodec<ByteBuf, MergeLevel> BASIC_STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT,
			MergeLevel::level, MergeLevel::new);

	public static boolean itemLevelsValid(ItemStack stack1, ItemStack stack2) {
		int level1 = stack1.get(MergedMagicDataComponents.BASIC_MERGE_LEVEL).level;
		return level1 == stack2.get(MergedMagicDataComponents.BASIC_MERGE_LEVEL).level && level1 < 3;
	}

	public static void levelItem(ItemStack stack) {
		int mergelevel = stack.get(MergedMagicDataComponents.BASIC_MERGE_LEVEL).level;
		stack.update(MergedMagicDataComponents.BASIC_MERGE_LEVEL, new MergeLevel(0),
				level -> new MergeLevel(mergelevel + 1));
	}

	public static void triggerLevelSound(Player player, ItemStack stack) {
		int level = stack.get(MergedMagicDataComponents.BASIC_MERGE_LEVEL).level;
		if (level == 3) {
			player.level().playSound(null, player.blockPosition(), SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 0.3F,
					1.3F);
		} else
			player.level().playSound(null, player.blockPosition(), SoundEvents.ANVIL_USE, SoundSource.PLAYERS, 0.7F,
					1.2F);
	}
}
