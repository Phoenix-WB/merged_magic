package com.pianistwb.merged_magic.geckolib.projectile.target_frisbee;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.entity.projectile.TargetFrisbee;

import net.minecraft.resources.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class TargetFrisbeeModel extends DefaultedEntityGeoModel<TargetFrisbee> {
	public TargetFrisbeeModel() {
		super(Identifier.fromNamespaceAndPath(MergedMagic.MODID, "projectile/target_frisbee"));
	}
}
