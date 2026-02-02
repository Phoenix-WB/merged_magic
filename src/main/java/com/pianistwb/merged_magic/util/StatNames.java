package com.pianistwb.merged_magic.util;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

public class StatNames {	
	public static final MutableComponent RADIUS = register("radius");
	public static final MutableComponent DAMAGE_INCREASE = register("damage_increase");
	public static final MutableComponent LAUNCH_VELOCITY = register("launch_velocity");
	public static final MutableComponent BURN_TIME = register("burn_time");
	public static final MutableComponent COOLDOWN = register("cooldown");
	public static final MutableComponent EFFECT_DURATION = register("effect_duration");
	public static final MutableComponent USES = register("uses");
	public static final MutableComponent REPAIR = register("repair");
	public static final MutableComponent SPAWN_PLACEMENT = register("spawn_placement");
	public static final MutableComponent SUMMON_COUNT = register("summon_count");
	public static final MutableComponent ACCELERATION = register("acceleration");
	public static final MutableComponent REQUIRED_HEALTH = register("required_health");
	public static final MutableComponent AMPLIFIER = register("amplifier");
	public static final MutableComponent PROJECTILES = register("projectiles");
	public static final MutableComponent DURATION = register("duration");
	public static final MutableComponent HELD_DURATION = register("held_duration");

	private static MutableComponent register(String name) {
		return Component.translatable("statName.merged_magic." + name);
	}
}