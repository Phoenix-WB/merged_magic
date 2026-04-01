package com.pianistwb.merged_magic.geckolib.entity.monster.trapjaw;

import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.pianistwb.merged_magic.entity.monster.Trapjaw;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class TrapjawRenderer<R extends LivingEntityRenderState & GeoRenderState> extends GeoEntityRenderer<Trapjaw, R> {
	public TrapjawRenderer(EntityRendererProvider.Context context) {
		super(context, new TrapjawModel());
	}
}
