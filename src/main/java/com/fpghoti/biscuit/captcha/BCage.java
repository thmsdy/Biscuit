package com.fpghoti.biscuit.captcha;

import java.awt.Font;
import java.util.Random;

import com.github.cage.Cage;
import com.github.cage.ObjectRoulette;
import com.github.cage.image.EffectConfig;
import com.github.cage.image.Painter;
import com.github.cage.image.RgbColorGenerator;
import com.github.cage.image.ScaleConfig;
import com.github.cage.token.RandomCharacterGeneratorFactory;
import com.github.cage.token.RandomTokenGenerator;

public class BCage extends Cage {

	private static int width = 300;
	private static int height = 100;
	private static int fontHeight = 80;
	private static String chars = "abcdefhijkmnoprstuvwxyzABCDEFGHJKLMNOPQRSTUVWXYZ123456789";
	private static int length = 6;

	public BCage() {
		this(new Random());
	}

	protected BCage(Random rnd) {
		super(new Painter(width, height, null, null, new EffectConfig(true,
				false, true, false, new ScaleConfig(0.85f, 0.85f)), rnd),
				new ObjectRoulette<Font>(rnd, new Font(Font.SERIF, Font.PLAIN, fontHeight)), //
				new RgbColorGenerator(rnd), null, null, 
				new RandomTokenGenerator(rnd, new RandomCharacterGeneratorFactory(chars.toCharArray(), null, rnd), length, 0), rnd);
	}
}