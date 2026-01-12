package com.pianistwb.merged_magic.events;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.datacomponents.MergeLevel;
import com.pianistwb.merged_magic.items.MergableItem;
import com.pianistwb.merged_magic.registry.MobEffectRegistry;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;

@EventBusSubscriber(modid = MergedMagic.MODID)
public class ForgeEvents {
	// Items
	@SubscribeEvent
	public static void onClickItem(ItemStackedOnOtherEvent event) {
		if (!event.getPlayer().level().isClientSide() || event.getPlayer().isCreative()) {
			ItemStack itemStacked = event.getStackedOnItem();
			ItemStack itemCarried = event.getCarriedItem();

			if (itemStacked.getItem() instanceof MergableItem stacked && stacked == itemCarried.getItem()
					&& MergeLevel.itemLevelsValid(itemStacked, itemCarried)) {
				MergeLevel.levelItem(itemStacked);
				event.getCarriedSlotAccess().set(ItemStack.EMPTY);
				event.getSlot().set(itemStacked);
				event.setCanceled(true);
				MergeLevel.triggerLevelSound(event.getPlayer(), itemStacked);
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
}
