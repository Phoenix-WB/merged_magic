package com.pianistwb.merged_magic.items;

import java.util.function.Consumer;

import com.pianistwb.merged_magic.datacomponents.MergeLevel;
import com.pianistwb.merged_magic.registry.DataComponentRegistry;

import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class MergableItem extends Item {
	Identifier descriptionId;
	MutableComponent desc;

	public MergableItem(Properties properties) {
		super(properties.stacksTo(1).component(DataComponentRegistry.MERGE_LEVEL, new MergeLevel(1)));
	}
	
	public ItemStack getMergedStack(ItemStack stackedOn, ItemStack carried) {
		return stackedOn;
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
			Consumer<Component> tooltipAdder, TooltipFlag flag) {
		tooltipAdder.accept(this.getDesc().withStyle(ChatFormatting.GREEN));
	}

	public MutableComponent getDesc() {
		if (this.desc == null) {
			this.desc = Component.translatable(
					"tooltip." + getIdentifier().getNamespace() + "." + getIdentifier().getPath() + "." + "desc");
		}
		return desc;
	}

	public Identifier getIdentifier() {
		if (this.descriptionId == null) {
			this.descriptionId = BuiltInRegistries.ITEM.getKey(this);
		}
		return this.descriptionId;
	}
}
