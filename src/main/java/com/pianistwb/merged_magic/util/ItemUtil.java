package com.pianistwb.merged_magic.util;

import java.util.Optional;

import org.jspecify.annotations.Nullable;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.datacomponents.IntegerData;
import com.pianistwb.merged_magic.datacomponents.MergeLevel;
import com.pianistwb.merged_magic.registry.DataComponentRegistry;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;

public class ItemUtil {
	public static int getMergeLevel(ItemStack stack) {
		return stack.get(DataComponentRegistry.MERGE_LEVEL).mergeLevel();
	}

	public static boolean itemLevelsValid(ItemStack stack1, ItemStack stack2) {
		int level1 = getMergeLevel(stack1);
		return level1 < 3 && level1 == getMergeLevel(stack2);
	}

	public static void levelItem(ItemStack stack) {
		stack.update(DataComponentRegistry.MERGE_LEVEL, MergeLevel.LEVEL_ONE, level -> level.getLevelAbove());
	}

	public static void triggerLevelSound(Player player, ItemStack stack) {
		int level = getMergeLevel(stack);
		if (level == 3) {
			player.playSound(SoundEvents.TOTEM_USE, 0.15F, 1.3F);
		} else
			player.playSound(SoundEvents.ANVIL_USE, 0.7F, 1.2F);
	}

	public static int getMergeState(ItemStack stack) {
		return stack.get(DataComponentRegistry.MERGE_STATE).value();
	}

	public static void setMergeState(ItemStack stack, int mergeState) {
		stack.set(DataComponentRegistry.MERGE_STATE, IntegerData.create(mergeState));
	}

	public static int getMergeIndex(ItemStack stack) {
		return stack.get(DataComponentRegistry.MERGE_INDEX).value();
	}

	public static void setMergeIndex(ItemStack stack, int mergeIndex) {
		stack.set(DataComponentRegistry.MERGE_INDEX, IntegerData.create(mergeIndex));
	}

	public static boolean isItemHealthy(ItemStack stack) {
		return getItemHealth(stack) > 0;
	}

	public static boolean isItemFullhealth(ItemStack stack) {
		return stack.getDamageValue() == 0;
	}

	public static int getItemHealth(ItemStack stack) {
		return stack.getMaxDamage() - stack.getDamageValue();
	}

	public static void damageStack(ItemStack stack, int amount) {
		stack.setDamageValue(Math.min(stack.getDamageValue() + amount, stack.getMaxDamage()));
	}

	public static void healStack(ItemStack stack, int amount) {
		stack.setDamageValue(Math.max(stack.getDamageValue() - amount, 0));
	}

	public static Item.Properties durableItem(Item.Properties properties, int maxDamage) {
		properties.component(DataComponents.MAX_DAMAGE, maxDamage);
		properties.component(DataComponents.DAMAGE, 0);
		return properties;
	}

	public static Item.Properties brokenItem(Item.Properties properties, int maxDamage) {
		properties.component(DataComponents.MAX_DAMAGE, maxDamage);
		properties.component(DataComponents.DAMAGE, maxDamage);
		return properties;
	}

	public static void saveEntityToStack(Entity entity, ItemStack stack, String entityID, @Nullable String removeTag) {
		try (ProblemReporter.ScopedCollector collector = new ProblemReporter.ScopedCollector(entity.problemPath(),
				MergedMagic.LOGGER)) {
			TagValueOutput tagvalueoutput = TagValueOutput.createWithContext(collector, entity.registryAccess());
			entity.saveWithoutId(tagvalueoutput);

			CustomData.update(DataComponentRegistry.STORED_ENTITIES.get(), stack, tag -> {
				if (removeTag != null) {
					CompoundTag saveTag = tagvalueoutput.buildResult();
					saveTag.remove(removeTag);
					tag.put(entityID, saveTag);
				} else {
					tag.put(entityID, tagvalueoutput.buildResult());
				}
			});
		}
	}

	@Nullable
	public static Optional<Entity> loadEntityFromStack(Level level, ItemStack stack, String entityID,
			boolean removeEntity) {
		CompoundTag entityTag = stack.get(DataComponentRegistry.STORED_ENTITIES).copyTag().getCompoundOrEmpty(entityID);
		if (removeEntity) {
			clearEntityFromStack(entityID, stack);
		}

		if (entityTag.isEmpty()) {
			return Optional.empty();
		}

		try (ProblemReporter.ScopedCollector collector = new ProblemReporter.ScopedCollector(MergedMagic.LOGGER)) {
			return EntityType.create(TagValueInput.create(collector, level.registryAccess(), entityTag), level,
					EntitySpawnReason.TRIGGERED);
		} catch (Exception e) {
			return Optional.empty();
		}
	}

	public static void clearEntityFromStack(String entityID, ItemStack stack) {
		CustomData.update(DataComponentRegistry.STORED_ENTITIES.get(), stack, tag -> {
			tag.remove(entityID);
		});
	}
}
