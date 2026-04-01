package com.pianistwb.merged_magic.geckolib.entity.projectile.target_frisbee;

import com.geckolib.renderer.GeoEntityRenderer;
import com.geckolib.renderer.base.GeoRenderState;
import com.pianistwb.merged_magic.entity.projectile.TargetFrisbee;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

public class TargetFrisbeeRenderer<R extends LivingEntityRenderState & GeoRenderState>
		extends GeoEntityRenderer<TargetFrisbee, R> {
	public TargetFrisbeeRenderer(EntityRendererProvider.Context context) {
		super(context, new TargetFrisbeeModel());
	}
}
