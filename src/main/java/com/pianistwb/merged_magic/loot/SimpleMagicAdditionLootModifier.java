package com.pianistwb.merged_magic.loot;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class SimpleMagicAdditionLootModifier extends LootModifier {
	public static final MapCodec<SimpleMagicAdditionLootModifier> CODEC = RecordCodecBuilder
			.mapCodec(inst -> LootModifier.codecStart(inst)
					.and(inst.group(BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(lm -> lm.item),
							Codec.FLOAT.fieldOf("probability").forGetter(lm -> lm.probability)))
					.apply(inst, SimpleMagicAdditionLootModifier::new));

	private final Item item;
	private final float probability;

	public SimpleMagicAdditionLootModifier(LootItemCondition[] conditionsIn, Item item, float probability) {
		super(conditionsIn);
		this.item = item;
		this.probability = probability;
	}

	@Override
	public ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
		if (context.getRandom().nextFloat() <= probability) {
			generatedLoot.add(item.getDefaultInstance());
		}
		
		generatedLoot.add(Items.DIRT.getDefaultInstance());

		return generatedLoot;
	}

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec() {
		return CODEC;
	}
}
