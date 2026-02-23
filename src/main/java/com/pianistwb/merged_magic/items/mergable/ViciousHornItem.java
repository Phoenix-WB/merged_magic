package com.pianistwb.merged_magic.items.mergable;

import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.LangUtil;
import com.pianistwb.merged_magic.util.MagicUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.wolf.Wolf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUseAnimation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;

public class ViciousHornItem extends MergableItem {
	public static final SoundEvent FEEL = SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(3).value();

	public final MergeStat<Double> radius = new MergeStat<>(5.0D, 8.0D, 15.0D);
	public final MergeStat<Integer> cooldown = new MergeStat<>(440, 400, 320);

	public ViciousHornItem(Properties properties) {
		super(properties);
		addMergeStat(StatNames.RADIUS, StatUnits.BLOCKS, radius, stat -> LangUtil.literal(stat));
		addMergeStat(StatNames.COOLDOWN, StatUnits.SECONDS, cooldown, stat -> LangUtil.literal(stat / 20));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!cooldowns.isOnCooldown(stack)) {
			int mergeLevel = ItemUtil.getMergeLevel(stack);

			if (!level.isClientSide()) {
				AABB aabb = MagicUtil.getSquareRadius(player.blockPosition().above(), radius.getStat(mergeLevel));
				level.getEntitiesOfClass(Wolf.class, aabb).forEach(wolf -> {
					if (wolf.getOwner().is(player)) {
						wolf.addEffect(new MobEffectInstance(MobEffects.RESISTANCE, 300, 1));
						wolf.addEffect(new MobEffectInstance(MobEffects.STRENGTH, 300));

						Entity target = MagicUtil
								.getTarget(player,
										tg -> tg instanceof LivingEntity
												&& !(tg instanceof TamableAnimal tame && tame.getOwner().is(player)),
										20.0D);
						if (target != null) {
							wolf.setTarget((LivingEntity) target);
						}
					}
				});
			}

			player.startUsingItem(hand);
			player.playSound(FEEL);

			cooldowns.addCooldown(stack, cooldown.getStat(mergeLevel));

            return InteractionResult.CONSUME;
		}

		return InteractionResult.PASS;
	}

	@Override
	public int getUseDuration(ItemStack stack, LivingEntity entity) {
		return 140;
	}
	
	@Override
	public ItemUseAnimation getUseAnimation(ItemStack stack) {
		return ItemUseAnimation.TOOT_HORN;
	}
}
