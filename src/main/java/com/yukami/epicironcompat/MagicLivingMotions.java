package com.yukami.epicironcompat;

import yesman.epicfight.api.animation.LivingMotion;

public enum MagicLivingMotions implements LivingMotion
{
	SPELL_CHANT_LONG,
	SPELL_CHANT_NONE,
	SPELL_CHANT_INST,
	SPELL_CHANT_CONT;
	final int id;

	MagicLivingMotions()
	{
		this.id = LivingMotion.ENUM_MANAGER.assign(this);
	}

	public int universalOrdinal()
	{
		return this.id;
	}
}
