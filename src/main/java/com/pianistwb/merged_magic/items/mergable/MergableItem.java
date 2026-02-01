package com.pianistwb.merged_magic.items.mergable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import com.pianistwb.merged_magic.datacomponents.MergeLevel;
import com.pianistwb.merged_magic.registry.DataComponentRegistry;
import com.pianistwb.merged_magic.util.MergeStat;

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
	private Identifier descriptionId;
	private MutableComponent desc;
	private MutableComponent itemName;

	private static final MutableComponent HOLD_SHIFT = Component
			.translatable("tooltip.merged_magic.mergable_item.hold_shift").withStyle(ChatFormatting.GRAY);
	private static final MutableComponent HOLD_CTRL = Component
			.translatable("tooltip.merged_magic.mergable_item.hold_ctrl").withStyle(ChatFormatting.GRAY);
	private static final MutableComponent DURABLE = Component.translatable("tooltip.merged_magic.mergable_item.durable")
			.withStyle(ChatFormatting.RED);
	private final List<MutableComponent> mergeStats = new ArrayList<>();

	public MergableItem(Properties properties) {
		super(properties.stacksTo(1).component(DataComponentRegistry.MERGE_LEVEL, new MergeLevel(1)));
	}

	@Override
	public boolean isCombineRepairable(ItemStack stack) {
		return false;
	}

	public ItemStack getMergedStack(ItemStack stackedOn, ItemStack carried) {
		return stackedOn;
	}

	public boolean isDurable() {
		return false;
	}

	public <R> void addMergeStat(MutableComponent statName, MutableComponent statUnit, MergeStat<R> stat,
			Function<R, MutableComponent> func) {
		mergeStats.add(Component.translatable("tooltip.merged_magic.mergable_item.merge_stat", statName,
				func.apply(stat.getStat(1)).withColor(0xbe8628), func.apply(stat.getStat(2)).withColor(0xa3b6b0),
				func.apply(stat.getStat(3)).withColor(0xfde55f), statUnit));
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
			Consumer<Component> tooltipAdder, TooltipFlag flag) {
		if (flag.hasShiftDown()) {
			tooltipAdder.accept(this.getDesc().withStyle(ChatFormatting.GREEN));
			tooltipAdder
					.accept(Component.translatable("tooltip.merged_magic.mergable_item.merge_stack", this.getItemName())
							.withStyle(ChatFormatting.LIGHT_PURPLE));
			if (isDurable()) {
				tooltipAdder.accept(DURABLE);
			}
		} else if (flag.hasControlDown()) {
			mergeStats.forEach(tooltipAdder);
		} else {
			tooltipAdder.accept(HOLD_SHIFT);
			tooltipAdder.accept(HOLD_CTRL);
		}
	}

	public MutableComponent getDesc() {
		if (this.desc == null) {
			this.desc = Component.translatable(
					"tooltip." + getIdentifier().getNamespace() + "." + getIdentifier().getPath() + "." + "desc");
		}
		return desc;
	}

	public MutableComponent getItemName() {
		if (this.itemName == null) {
			this.itemName = Component
					.translatable("item." + getIdentifier().getNamespace() + "." + getIdentifier().getPath());
		}
		return itemName;
	}

	public Identifier getIdentifier() {
		if (this.descriptionId == null) {
			this.descriptionId = BuiltInRegistries.ITEM.getKey(this);
		}
		return this.descriptionId;
	}
}
