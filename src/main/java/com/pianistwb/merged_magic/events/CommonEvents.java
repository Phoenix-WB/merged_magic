package com.pianistwb.merged_magic.events;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.items.MergableItem;
import com.pianistwb.merged_magic.items.TickInPlayerHand;
import com.pianistwb.merged_magic.registry.ItemRegistry;
import com.pianistwb.merged_magic.registry.MobEffectRegistry;
import com.pianistwb.merged_magic.util.ItemUtil;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.ItemStackedOnOtherEvent;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = MergedMagic.MODID)
public class CommonEvents {
	// Items
	@SubscribeEvent
	public static void onClickItem(ItemStackedOnOtherEvent event) {
		if (!event.getPlayer().level().isClientSide() || event.getPlayer().isCreative()) {
			ItemStack itemStacked = event.getStackedOnItem();
			ItemStack itemCarried = event.getCarriedItem();

			if (itemStacked.getItem() instanceof MergableItem stacked && stacked == itemCarried.getItem()
					&& ItemUtil.itemLevelsValid(itemStacked, itemCarried)) {
				ItemStack finalItem = stacked.getMergedStack(itemStacked, itemCarried);

				ItemUtil.levelItem(finalItem);
				event.getCarriedSlotAccess().set(ItemStack.EMPTY);
				event.getSlot().set(itemStacked);
				event.setCanceled(true);
				ItemUtil.triggerLevelSound(event.getPlayer(), finalItem);
			}
		}
	}

	// Entities
	@SubscribeEvent
	public static void onLivingIncomingDamage(LivingIncomingDamageEvent event) {
		LivingEntity living = event.getEntity();
		Level level = living.level();
		DamageSource source = event.getSource();

		if (source == level.damageSources().fall()) {
			if (living.hasEffect(MobEffectRegistry.CRUTCHED)) {
				level.playSound(null, living.blockPosition(), SoundEvents.ITEM_BREAK.value(), SoundSource.PLAYERS);

				living.removeEffect(MobEffectRegistry.CRUTCHED);

				event.setCanceled(true);
			}
		} else if (source.type() == level.registryAccess().getOrThrow(DamageTypes.ARROW).value()) {
			if (living.hasEffect(MobEffectRegistry.TARGETED)) {
				event.setAmount(event.getAmount() * 1.5F);
			}
		}
	}

	@SubscribeEvent
	public static void onPostPlayerHurt(LivingDamageEvent.Post event) {
		LivingEntity living = event.getEntity();

		if (!living.level().isClientSide() && living.hasEffect(MobEffectRegistry.ENDURING)) {
			DamageSource damage = event.getSource();
			Entity entity = damage.getDirectEntity();
			int amplifier = living.getEffect(MobEffectRegistry.ENDURING).getAmplifier();

			if (entity != null && entity instanceof LivingEntity attackingEntity) {
				double resistance = attackingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
				resistance = Math.max(0.0, 1.0 - resistance);
				double rawScale = ItemRegistry.TOTEM_OF_ENDURING.get().initialVelocity.getStat(Math.min(amplifier + 1, 3));
				attackingEntity.addDeltaMovement(new Vec3(0.0, rawScale * resistance, 0.0));

				living.level().playSound(null, living.blockPosition(), SoundEvents.IRON_GOLEM_ATTACK,
						SoundSource.PLAYERS);
			}
		}
	}

	@SubscribeEvent
	public static void onPostPlayerTick(PlayerTickEvent.Post event) {
		Player player = event.getEntity();
		Level level = player.level();

		ItemStack mainStack = player.getItemInHand(InteractionHand.MAIN_HAND);
		ItemStack offStack = player.getItemInHand(InteractionHand.OFF_HAND);

		if (mainStack.getItem() instanceof TickInPlayerHand tickMain) {
			tickMain.tickInHand(level, player, InteractionHand.MAIN_HAND, mainStack, offStack);
		}

		if (offStack.getItem() instanceof TickInPlayerHand tickOff) {
			tickOff.tickInHand(level, player, InteractionHand.OFF_HAND, offStack, mainStack);
		}
	}
}
