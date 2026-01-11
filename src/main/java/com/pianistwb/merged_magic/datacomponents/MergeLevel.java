package com.pianistwb.merged_magic.datacomponents;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public record MergeLevel(int mergeLevel) {
	public static final Codec<MergeLevel> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(Codec.INT.fieldOf("level").forGetter(MergeLevel::mergeLevel)).apply(instance, MergeLevel::new));

	public static final StreamCodec<ByteBuf, MergeLevel> STREAM_CODEC = StreamCodec.composite(ByteBufCodecs.INT,
			MergeLevel::mergeLevel, MergeLevel::new);

	public static boolean itemLevelsValid(ItemStack stack1, ItemStack stack2) {
		int level1 = stack1.get(MergedMagicDataComponents.MERGE_LEVEL).mergeLevel;
		return level1 == stack2.get(MergedMagicDataComponents.MERGE_LEVEL).mergeLevel && level1 < 3;
	}

	public static void levelItem(ItemStack stack) {
		int mergelevel = stack.get(MergedMagicDataComponents.MERGE_LEVEL).mergeLevel;
		stack.update(MergedMagicDataComponents.MERGE_LEVEL, new MergeLevel(1), level -> new MergeLevel(mergelevel + 1));
	}

	public static void triggerLevelSound(Player player, ItemStack stack) {
		int level = stack.get(MergedMagicDataComponents.MERGE_LEVEL).mergeLevel;
		if (level == 3) {
			player.playSound(SoundEvents.TOTEM_USE, 0.15F, 1.3F);
		} else
			player.playSound(SoundEvents.ANVIL_USE, 0.7F, 1.2F);
	}
}
