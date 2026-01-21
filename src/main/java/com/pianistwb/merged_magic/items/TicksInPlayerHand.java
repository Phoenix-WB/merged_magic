package com.pianistwb.merged_magic.items;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface TicksInPlayerHand {	
	void tickInHand(Level level, Player player, ItemStack stack);
}
