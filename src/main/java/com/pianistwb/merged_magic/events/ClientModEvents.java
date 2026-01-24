package com.pianistwb.merged_magic.events;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.client.model.MergedRenderTypeModelWrapper;
import com.pianistwb.merged_magic.geckolib.target_frisbee.TargetFrisbeeRenderer;
import com.pianistwb.merged_magic.registry.EntityTypeRegistry;

import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;

@EventBusSubscriber(modid = MergedMagic.MODID, value = Dist.CLIENT)
public class ClientModEvents {
	@SubscribeEvent
	public static void registerItemModels(RegisterItemModelsEvent event) {
		event.register(Identifier.fromNamespaceAndPath(MergedMagic.MODID, "merged_render_type"),
				MergedRenderTypeModelWrapper.Unbaked.MAP_CODEC);
	}

	@SubscribeEvent
	public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
		EntityRenderers.register(EntityTypeRegistry.TARGET_FRISBEE.get(), TargetFrisbeeRenderer::new);
	}
}
