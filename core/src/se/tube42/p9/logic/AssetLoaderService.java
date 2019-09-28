
package se.tube42.p9.logic;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import se.tube42.lib.service.*;

import se.tube42.p9.data.*;
import static se.tube42.p9.data.Constants.*;

final class AssetLoaderService {
	public static void loadCommonAssets(int scale) {
		int ascale = scale;
		if (ascale == 3)
			ascale = 2;
		if (ascale > 4)
			ascale = 4;
		// final String ascale = "" + ascale;

		Texture tmp = AssetService.load(ascale + "/icons.png", true);
		Assets.tex_icons = AssetService.divide(tmp, 4, 4);

		tmp = AssetService.load(ascale + "/rect.png", true);
		Assets.tex_rect = new TextureRegion[] { new TextureRegion(tmp) };

		tmp = AssetService.load(ascale + "/splash.png", true);
		Assets.tex_splash = new TextureRegion[] { new TextureRegion(tmp) };

		tmp = AssetService.load(ascale + "/lang.png", true);
		Assets.tex_lang = AssetService.divide(tmp, 1, 4);

		Assets.sound_new = SoundService.loadSound("new");
		Assets.music = SoundService.loadMusic("music0");

		Assets.fonts_init = Assets.fonts2 = AssetService.createFonts("fonts/RobotoCondensed-Light.ttf",
				createCharset(true, false, LANGUAGES), scale * 30);
	}

	public static void loadLanguageAssets(WordList wl, int scale) {
		final int fscale = Math.min(4, scale);
		final String charset = createCharset(true, true, CHARSET_BASE, wl.getAlphabeth(false));

		Assets.fonts1 = AssetService.createFonts("fonts/Roboto-Regular.ttf", charset, fscale * 20, fscale * 40);
		Assets.fonts2 = AssetService.createFonts("fonts/RobotoCondensed-Light.ttf", charset, scale * 30);
	}

	private static String createCharset(boolean upper, boolean lower, String... chars) {
		String all = "", ret = "";
		for (String s : chars)
			all = all + s;

		for (char c : all.toCharArray()) {
			if (ret.indexOf("" + c) >= 0)
				continue;

			if (Character.isAlphabetic(c)) {
				if (upper)
					ret = ret + Character.toUpperCase(c);
				if (lower)
					ret = ret + Character.toLowerCase(c);
			} else {
				ret = ret + c;
			}
		}
		System.out.println("Language charset " + ret);
		return ret;
	}
}
