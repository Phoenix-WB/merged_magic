package com.pianistwb.merged_magic.items.mergable;

import com.pianistwb.merged_magic.util.ItemUtil;
import com.pianistwb.merged_magic.util.LangUtil;
import com.pianistwb.merged_magic.util.MagicUtil;
import com.pianistwb.merged_magic.util.MergeStat;
import com.pianistwb.merged_magic.util.StatNames;
import com.pianistwb.merged_magic.util.StatUnits;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemCooldowns;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BroomItem extends MergableItem {
	public final MergeStat<Double> initial_velocity = new MergeStat<>(0.6D, 0.9D, 1.1D);
	public final MergeStat<Integer> burnTime = new MergeStat<>(300, 600, 1200);
	public final MergeStat<Integer> cooldown = new MergeStat<>(90, 70, 50);

	public BroomItem(Properties properties) {
		super(properties);
		addMergeStat(StatNames.LAUNCH_VELOCITY, StatUnits.BLOCKS_PER_TICK, initial_velocity,
				stat -> LangUtil.literal(stat));
		addMergeStat(StatNames.COOLDOWN, StatUnits.SECONDS, cooldown, stat -> LangUtil.literal(stat / 20));
	}

	@Override
	public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget,
			InteractionHand usedHand) {
		ItemCooldowns cooldowns = player.getCooldowns();

		if (!cooldowns.isOnCooldown(stack)) {
			int mergeLevel = ItemUtil.getMergeLevel(stack);

			double resistance = interactionTarget.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
			resistance = Math.max(0D, 1D - resistance);
			Vec3 newMotion = MagicUtil.getRelativeToEntityView(player, resistance * initial_velocity.getStat(mergeLevel), 0.3D);
			interactionTarget.addDeltaMovement(newMotion);

			player.playSound(SoundEvents.BRUSH_GENERIC, 1.0F, 0.65F);

			cooldowns.addCooldown(stack, cooldown.getStat(mergeLevel));

			return InteractionResult.SUCCESS;
		}

		return InteractionResult.PASS;
	}
}
