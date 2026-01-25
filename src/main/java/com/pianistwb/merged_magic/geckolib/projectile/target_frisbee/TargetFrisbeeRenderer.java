package com.pianistwb.merged_magic.geckolib.projectile.target_frisbee;

import com.pianistwb.merged_magic.entity.projectile.TargetFrisbee;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class TargetFrisbeeRenderer<R extends LivingEntityRenderState & GeoRenderState>
		extends GeoEntityRenderer<TargetFrisbee, R> {
	public TargetFrisbeeRenderer(EntityRendererProvider.Context context) {
		super(context, new TargetFrisbeeModel());
	}
}
