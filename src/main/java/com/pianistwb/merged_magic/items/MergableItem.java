package com.pianistwb.merged_magic.items;

import java.util.function.Consumer;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public abstract class MergableItem extends Item {
	public MergableItem(Properties properties) {
		super(properties.stacksTo(1));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
			Consumer<Component> tooltipAdder, TooltipFlag flag) {
		tooltipAdder.accept(Component.translatable(this.getDesc()).withStyle(ChatFormatting.GREEN));
	}

	public abstract String getDesc();
}
