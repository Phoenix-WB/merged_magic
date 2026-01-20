package com.pianistwb.merged_magic.util;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

public class ClientUtil {
	public static Level getClientLevel() {
		Minecraft minecraft = Minecraft.getInstance();
		return minecraft.level;
	}
}
