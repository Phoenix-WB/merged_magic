package com.pianistwb.merged_magic.events;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.datacomponents.MergeLevel;
import com.pianistwb.merged_magic.items.MergableItem;

import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;

@EventBusSubscriber(modid = MergedMagic.MODID)
public class ForgeEvents {
	//Items
	@SubscribeEvent
	public static void onClickItem(ItemStackedOnOtherEvent event) {
		if(!event.getPlayer().level().isClientSide()) {
			ItemStack itemStacked = event.getStackedOnItem();
			ItemStack itemCarried = event.getCarriedItem();

			if(itemStacked.getItem() instanceof MergableItem stacked && stacked == itemCarried.getItem() && MergeLevel.itemLevelsValid(itemStacked, itemCarried)) {				
				MergeLevel.levelItem(itemStacked);
				event.getCarriedSlotAccess().set(ItemStack.EMPTY);
				event.getSlot().set(itemStacked);
				event.setCanceled(true);
				MergeLevel.triggerLevelSound(event.getPlayer(), itemStacked);
			}
		}
	}
}
