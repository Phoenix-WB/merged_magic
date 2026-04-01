package com.pianistwb.merged_magic.geckolib.entity.projectile.target_frisbee;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.entity.projectile.TargetFrisbee;

import net.minecraft.resources.Identifier;

public class TargetFrisbeeModel extends DefaultedEntityGeoModel<TargetFrisbee> {
	public TargetFrisbeeModel() {
		super(Identifier.fromNamespaceAndPath(MergedMagic.MODID, "projectile/target_frisbee"));
	}
}
