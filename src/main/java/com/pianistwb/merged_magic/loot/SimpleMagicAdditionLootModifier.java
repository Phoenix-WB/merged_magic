package com.pianistwb.merged_magic.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class SimpleMagicAdditionLootModifier extends LootModifier {
	public static final MapCodec<SimpleMagicAdditionLootModifier> CODEC = RecordCodecBuilder
			.mapCodec(inst -> LootModifier.codecStart(inst)
					.and(inst.group(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(mod -> mod.item),
							Codec.INT.fieldOf("rolls").forGetter(mod -> mod.rolls),
							Codec.INT.fieldOf("guarantees").forGetter(mod -> mod.guarantees),
							Codec.FLOAT.fieldOf("probability").forGetter(mod -> mod.probability)))
					.apply(inst, SimpleMagicAdditionLootModifier::new));

	private final Item item;
	private final float probability;
	private final int rolls;
	private final int guarantees;

	public SimpleMagicAdditionLootModifier(LootItemCondition[] conditionsIn, Item item, int rolls, int guarantees,
			float probability) {
		super(conditionsIn);
		this.item = item;
		this.rolls = rolls;
		this.guarantees = guarantees;
		this.probability = probability;
	}

	@Override
	public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		for (int i = 0; i < rolls; i++) {
			if (context.getRandom().nextFloat() < probability) {
				generatedLoot.add(item.getDefaultInstance());
			}
		}

		for (int i = 0; i < guarantees; i++) {
			generatedLoot.add(item.getDefaultInstance());
		}

		return generatedLoot;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
}
