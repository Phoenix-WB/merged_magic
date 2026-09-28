package com.pianistwb.merged_magic.client.model;

import java.util.List;

import org.joml.Matrix4fc;
import org.jspecify.annotations.Nullable;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pianistwb.merged_magic.registry.ItemRegistry;
import com.pianistwb.merged_magic.util.ItemUtil;

import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.dispatch.BlockModelRotation;
import net.minecraft.client.renderer.item.CuboidItemModelWrapper;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.renderer.item.ModelRenderProperties;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.client.resources.model.geometry.BakedQuad;
import net.minecraft.client.resources.model.geometry.ItemQuads;
import net.minecraft.client.resources.model.sprite.TextureSlots;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Util;
import net.minecraft.world.entity.ItemOwner;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public record MergedRenderTypeModelWrapper(List<BakedQuad> quads, List<ItemTintSource> tints,
		ModelRenderProperties properties, RenderType type) implements ItemModel {

	@Override
	public void update(ItemStackRenderState renderState, ItemStack stack, ItemModelResolver itemModelResolver,
			ItemDisplayContext displayContext, @Nullable ClientLevel level, @Nullable ItemOwner owner, int seed) {
		renderState.appendModelIdentityElement(this);
		
		ItemStackRenderState.LayerRenderState layerState = renderState.newLayer();
		
		if (stack.hasFoil()) {
			layerState.setFoilType(ItemStackRenderState.FoilType.STANDARD);
			renderState.appendModelIdentityElement(ItemStackRenderState.FoilType.STANDARD);
			renderState.setAnimated();
		}
		
		int tintSize = this.tints.size();
		int[] tintLayers = new int[tintSize];
		
		for (int idx = 0; idx < tintSize; idx++) {
			int tintColor = this.tints.get(idx).calculate(stack, level, owner.asLivingEntity());
			tintLayers[idx] = tintColor;
			renderState.appendModelIdentityElement(tintColor);
		}
		
		layerState.setExtents(() -> CuboidItemModelWrapper.computeExtents(this.quads));
		
		this.properties.applyToLayer(layerState, displayContext);

		layerState.setQuads(ItemQuads.split(this.quads));

		if (displayContext == ItemDisplayContext.GUI || displayContext == ItemDisplayContext.GROUND || displayContext == ItemDisplayContext.ON_SHELF) {
			int mergeLevel = ItemUtil.getMergeLevel(stack);

			if (mergeLevel == 1) {
				itemModelResolver.appendItemLayers(renderState, ItemRegistry.BRONZE_STAR.get().getDefaultInstance(),
						displayContext, level, owner, seed);
			} else if (mergeLevel == 2) {
				itemModelResolver.appendItemLayers(renderState, ItemRegistry.SILVER_STAR.get().getDefaultInstance(),
						displayContext, level, owner, seed);
			} else if (mergeLevel == 3) {
				itemModelResolver.appendItemLayers(renderState, ItemRegistry.GOLD_STAR.get().getDefaultInstance(),
						displayContext, level, owner, seed);
			}
		}
	}

	public record Unbaked(Identifier model, List<ItemTintSource> tints, RenderType renderType)
			implements ItemModel.Unbaked {

		private static final BiMap<String, RenderType> RENDER_TYPES = Util.make(HashBiMap.create(), map -> {
			map.put("translucent_item", Sheets.translucentItemSheet());
			map.put("cutout_block", Sheets.cutoutBlockItemSheet());
		});

		private static final Codec<RenderType> RENDER_TYPE_CODEC = ExtraCodecs.idResolverCodec(Codec.STRING,
				RENDER_TYPES::get, RENDER_TYPES.inverse()::get);

		public static final MapCodec<MergedRenderTypeModelWrapper.Unbaked> MAP_CODEC = RecordCodecBuilder
				.mapCodec(instance -> instance
						.group(Identifier.CODEC.fieldOf("model").forGetter(MergedRenderTypeModelWrapper.Unbaked::model),
								ItemTintSources.CODEC.listOf().optionalFieldOf("tints", List.of())
										.forGetter(MergedRenderTypeModelWrapper.Unbaked::tints),
								RENDER_TYPE_CODEC.fieldOf("render_type")
										.forGetter(MergedRenderTypeModelWrapper.Unbaked::renderType))
						.apply(instance, MergedRenderTypeModelWrapper.Unbaked::new));

		@Override
		public void resolveDependencies(Resolver resolver) {
			resolver.markDependency(this.model);
		}

		@Override
		public ItemModel bake(BakingContext context, Matrix4fc transformation) {
			ModelBaker baker = context.blockModelBaker();
			ResolvedModel resolvedModel = baker.getModel(this.model);
			TextureSlots slots = resolvedModel.getTopTextureSlots();

			return new MergedRenderTypeModelWrapper(
					resolvedModel.bakeTopGeometry(slots, baker, BlockModelRotation.IDENTITY).getAll(), this.tints,
					ModelRenderProperties.fromResolvedModel(baker, resolvedModel, slots), this.renderType());
		}

		@Override
		public MapCodec<? extends net.minecraft.client.renderer.item.ItemModel.Unbaked> type() {
			return MAP_CODEC;
		}
	}
}
