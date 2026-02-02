package com.pianistwb.merged_magic.geckolib.entity.monster.trapjaw;

import com.pianistwb.merged_magic.entity.monster.Trapjaw;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class TrapjawRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<Trapjaw, R> {
	public TrapjawRenderer(EntityRendererProvider.Context context) {
		super(context, new TrapjawModel());
	}
}
