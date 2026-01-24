package com.pianistwb.merged_magic.util;

public class MergeStat<V> {
	private final V levelOne;
	private final V levelTwo;
	private final V levelThree;

	public MergeStat(V levelOne, V levelTwo, V levelThree) {
		this.levelOne = levelOne;
		this.levelTwo = levelTwo;
		this.levelThree = levelThree;
	}

	public V getStat(int level) {
		switch (level) {
		case 1:
			return levelOne;
		case 2:
			return levelTwo;
		case 3:
			return levelThree;
		default:
			return levelOne;
		}
	}
}
