package com.pianistwb.merged_magic.items;

import com.pianistwb.merged_magic.datacomponents.MergedMagicDataComponents;
import com.pianistwb.merged_magic.effect.MergedMagicMobEffects;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class SpringyPistonItem extends MergableItem {
	public SpringyPistonItem(Properties properties) {
		super(properties);
	}
	
	@Override
	public InteractionResult use(Level level, Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		ItemCooldowns cooldowns = player.getCooldowns();
		
		if(!cooldowns.isOnCooldown(stack) && player.onGround()) {
			int mergeLevel = stack.get(MergedMagicDataComponents.MERGE_LEVEL).mergeLevel();
			
			player.addDeltaMovement(new Vec3(0,0.8+0.1*mergeLevel,0));
			player.hurtMarked = true;
			
			cooldowns.addCooldown(stack, 150 - (mergeLevel-1)*25);
			
			player.playSound(SoundEvents.PISTON_EXTEND);
			
			if(!level.isClientSide()) {
				player.addEffect(new MobEffectInstance(MergedMagicMobEffects.CRUTCHED, 40*mergeLevel));
			}
			
			return InteractionResult.SUCCESS;
		}
		
		return InteractionResult.PASS;
	}

	@Override
	public String getDesc() {
		return "tooltip.merged_magic.springy_piston";
	}
}
