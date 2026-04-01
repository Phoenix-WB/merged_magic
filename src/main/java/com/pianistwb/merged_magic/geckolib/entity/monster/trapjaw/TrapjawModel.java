package com.pianistwb.merged_magic.geckolib.entity.monster.trapjaw;

import com.geckolib.model.DefaultedEntityGeoModel;
import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.entity.monster.Trapjaw;

import net.minecraft.resources.Identifier;

public class TrapjawModel extends DefaultedEntityGeoModel<Trapjaw> {
	public TrapjawModel() {
		super(Identifier.fromNamespaceAndPath(MergedMagic.MODID, "monster/trapjaw"));
	}
}
