package com.pianistwb.merged_magic.items;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface TickInPlayerHand {	
	void tickInHand(Level level, Player player, InteractionHand hand, ItemStack thisHand, ItemStack otherHand);
}
