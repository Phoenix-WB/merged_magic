package com.pianistwb.merged_magic.events;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.items.MergableItem;
import com.pianistwb.merged_magic.items.TicksInPlayerHand;
import com.pianistwb.merged_magic.registry.MobEffectRegistry;
import com.pianistwb.merged_magic.util.MergeUtil;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = MergedMagic.MODID)
public class ForgeEvents {
	// Items
	@SubscribeEvent
	public static void onClickItem(ItemStackedOnOtherEvent event) {
		if (!event.getPlayer().level().isClientSide() || event.getPlayer().isCreative()) {
			ItemStack itemStacked = event.getStackedOnItem();
			ItemStack itemCarried = event.getCarriedItem();

			if (itemStacked.getItem() instanceof MergableItem stacked && stacked == itemCarried.getItem()
					&& MergeUtil.itemLevelsValid(itemStacked, itemCarried)) {
				ItemStack finalItem = stacked.getMergedStack(itemStacked, itemCarried);
				
				MergeUtil.levelItem(finalItem);
				event.getCarriedSlotAccess().set(ItemStack.EMPTY);
				event.getSlot().set(itemStacked);
				event.setCanceled(true);
				MergeUtil.triggerLevelSound(event.getPlayer(), finalItem);
			}
		}
	}

	// Entities
	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
		LivingEntity living = event.getEntity();

		if (living.hasEffect(MobEffectRegistry.CRUTCHED)
				&& event.getSource() == living.level().damageSources().fall()) {
			living.level().playSound(null, living.blockPosition(), SoundEvents.ITEM_BREAK.value(), SoundSource.PLAYERS);

			living.removeEffect(MobEffectRegistry.CRUTCHED);

			event.setCanceled(true);
		}
	}

	@SubscribeEvent
	public static void onPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		Level level = player.level();

		ItemStack mainStack = player.getItemInHand(InteractionHand.MAIN_HAND);
		ItemStack offStack = player.getItemInHand(InteractionHand.OFF_HAND);

		if (mainStack.getItem() instanceof TicksInPlayerHand tickMain) {
			tickMain.tickInHand(level, player, mainStack);
		}

		if (offStack.getItem() instanceof TicksInPlayerHand tickOff) {
			tickOff.tickInHand(level, player, offStack);
		}
	}
}
