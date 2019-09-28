
package se.tube42.p9.logic;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import se.tube42.lib.tweeny.*;

/*
 * Color functions for RGB8 (mostly without alpha).
 *
 * Some of these are stolen from imagelib:
 * http://github.com/tube42/imagelib
 */
public final class ColorHelper {
	public static final int blend(final int c1, final int c2, final int sel8) {

		final int v1 = sel8 & 0xFF;
		final int c1_RB = c1 & 0x00FF00FF;
		final int c2_RB = c2 & 0x00FF00FF;
		final int c1_AG = (c1 >>> 8) & 0x00FF00FF;
		final int c2_AG_org = c2 & 0xFF00FF00;
		final int c2_AG = (c2_AG_org) >>> 8;

		// the world-famous tube42 blend with one mult per two components:
		final int rb = (c2_RB + (((c1_RB - c2_RB) * v1) >> 8)) & 0x00FF00FF;
		final int ag = (c2_AG_org + ((c1_AG - c2_AG) * v1)) & 0xFF00FF00;
		return ag | rb;
	}

	public static int darker(int c) {
		return (c >>> 1) & ~0x80808080;
	}

	public static int lighter(int c) {
		return blend(c, 0x00FFFFFF, 0x80);
	}

	public static float extract(int n, int index) {
		n = n >> (index * 8);
		return (n & 0xFF) / 255f;
	}

	public static void set(Item bgr, int color, float time) {
		for (int i = 0; i < 3; i++) {
			final float c = extract(color, 2 - i);
			bgr.set(i, 1, c).configure(time, null);
		}
	}

	public static void set(BitmapFont font, int color, float alpha) {
		font.setColor(extract(color, 2), extract(color, 1), extract(color, 0), alpha);
	}

	public static void set(SpriteBatch sb, int color, float alpha) {
		sb.setColor(extract(color, 2), extract(color, 1), extract(color, 0), alpha);
	}
}
