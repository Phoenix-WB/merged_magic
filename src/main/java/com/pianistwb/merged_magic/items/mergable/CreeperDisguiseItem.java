package com.pianistwb.merged_magic.items.mergable;

import com.pianistwb.merged_magic.registry.MobEffectRegistry;
import com.pianistwb.merged_magic.util.LangUtil;
import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CreeperDisguiseItem extends MergableItem {
	public final MergeStat<Integer> effectDuration = new MergeStat<>(400, 800, 1800);
	public final MergeStat<Integer> cooldown = new MergeStat<>(800, 1600, 3600);

	public CreeperDisguiseItem(Properties properties) {
		super(properties);
		addMergeStat(StatNames.EFFECT_DURATION, StatUnits.SECONDS, effectDuration, stat -> LangUtil.literal(stat / 20));
		addMergeStat(StatNames.COOLDOWN, StatUnits.SECONDS, cooldown, stat -> LangUtil.literal(stat / 20));
	}

	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!cooldowns.isOnCooldown(stack)) {
			int mergeLevel = ItemUtil.getMergeLevel(stack);
			if (!level.isClientSide()) {
				player.addEffect(new MobEffectInstance(MobEffectRegistry.CREEPER_DISGUISED,
						effectDuration.getStat(mergeLevel), 0, false, false));
			}

			cooldowns.addCooldown(stack, cooldown.getStat(mergeLevel));
			
			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}
}
