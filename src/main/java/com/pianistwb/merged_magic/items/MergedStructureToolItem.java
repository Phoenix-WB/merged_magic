package com.pianistwb.merged_magic.items;

import java.util.function.Consumer;

import com.pianistwb.merged_magic.entity.monster.Trapjaw;
import com.pianistwb.merged_magic.registry.ItemRegistry;

import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.util.ParticleUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;

public class MergedStructureToolItem extends Item {
	private static final MutableComponent PERSISTENT = Component.translatable("chat.merged_magic.persistent")
			.withStyle(ChatFormatting.GREEN);
	private static final MutableComponent VILLAGER_OWNER = Component.translatable("chat.merged_magic.villager_owner")
			.withStyle(ChatFormatting.GREEN);

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
		Level level = player.level();

		if (player.isCrouching()) {
			if (interactionTarget instanceof Trapjaw trapjaw) {
				trapjaw.withVillagerOwner();

				if (level.isClientSide()) {
					player.sendOverlayMessage(VILLAGER_OWNER);
				}

				return InteractionResult.SUCCESS;
			}
		} else {
			if (interactionTarget instanceof Mob mob) {
				mob.setPersistenceRequired();

				if (level.isClientSide()) {
					ParticleUtils.spawnParticleInBlock(level, mob.blockPosition(), 15, ParticleTypes.HAPPY_VILLAGER);
					player.sendOverlayMessage(PERSISTENT);
				}

				return InteractionResult.SUCCESS;
			}
		}

		return InteractionResult.PASS;
	}
}
