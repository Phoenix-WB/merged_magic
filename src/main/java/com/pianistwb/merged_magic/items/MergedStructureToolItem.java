package com.pianistwb.merged_magic.items;

import java.util.function.Consumer;

import com.pianistwb.merged_magic.entity.monster.Trapjaw;
import com.pianistwb.merged_magic.registry.ItemRegistry;

import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;

public class MergedStructureToolItem extends Item {
	public MergedStructureToolItem(Properties properties) {
		super(properties);
	}

	@Override
	public void appendHoverText(ItemStack stack, TooltipContext context, TooltipDisplay tooltipDisplay,
			Consumer<Component> tooltipAdder, TooltipFlag flag) {
		tooltipAdder.accept(ItemRegistry.CREATIVE_MODE_ITEM);
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget,
			InteractionHand usedHand) {
		if (interactionTarget instanceof Trapjaw trapjaw) {
			trapjaw.withVillagerOwner();
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}
}
