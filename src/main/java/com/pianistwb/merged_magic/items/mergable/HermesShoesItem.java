package com.pianistwb.merged_magic.items.mergable;

import org.jspecify.annotations.Nullable;

import com.pianistwb.merged_magic.client.ClientParticles;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.LangUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class HermesShoesItem extends MergableItem {
	public final MergeStat<Integer> useDamage = new MergeStat<>(6, 3, 2);

	public HermesShoesItem(Properties properties) {
		super(ItemUtil.durableItem(properties, 6));
		addMergeStat(StatNames.USES, Component.empty(), useDamage, stat -> LangUtil.literal(6 / stat));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!player.onGround() && !player.isInWater() && ItemUtil.isItemHealthy(stack)
				&& !cooldowns.isOnCooldown(stack)) {
			int mergeLevel = ItemUtil.getMergeLevel(stack);

			Vec3 mov = player.getDeltaMovement();
			player.setDeltaMovement(mov.x(), 0.6D, mov.z());
			player.hurtMarked = true;

			Vec3 pos = player.position();
			if (!level.isClientSide()) {
				player.addEffect(new MobEffectInstance(MobEffects.SLOW_FALLING, 60));
			}

			ItemUtil.damageStack(stack, useDamage.getStat(mergeLevel));

			ClientParticles.sendOrPlayParticles(player, pos, ClientParticles.HERMES_SHOES, false);
			player.playSound(SoundEvents.ENDER_DRAGON_FLAP);

			cooldowns.addCooldown(stack, 10);

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}

	@Override
	public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
		if (entity.tickCount % 100 == 0) {
			int mergeLevel = ItemUtil.getMergeLevel(stack);
			ItemUtil.healStack(stack, useDamage.getStat(mergeLevel));
		}
	}

	@Override
	public ItemStack getMergedStack(ItemStack stackedOn, ItemStack carried) {
		stackedOn.setDamageValue(0);
		return stackedOn;
	}

	@Override
	public boolean isDurable() {
		return true;
	}
}
