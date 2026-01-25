package com.pianistwb.merged_magic.geckolib.monster.trapjaw;

import com.pianistwb.merged_magic.MergedMagic;
import com.pianistwb.merged_magic.entity.monster.Trapjaw;

import net.minecraft.resources.Identifier;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class TrapjawModel extends DefaultedEntityGeoModel<Trapjaw> {
	public TrapjawModel() {
		super(Identifier.fromNamespaceAndPath(MergedMagic.MODID, "monster/trapjaw"));
	}
}
