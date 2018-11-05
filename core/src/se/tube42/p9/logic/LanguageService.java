
package se.tube42.p9.logic;

import se.tube42.p9.data.*;
import static se.tube42.p9.data.Constants.*;
import static se.tube42.p9.data.World.*;

import com.badlogic.gdx.Gdx;

/* ppackage */ final class LanguageService
{
	public static void setLanguage(String lang)
    {
        try {
			World.lang = lang;
            World.words = IOService.loadWordList(lang);
			World.levels = IOService.loadLevels(World.words);
			World.translation = IOService.loadTranslation(lang);
        } catch(Exception e) {
            System.err.println(e.toString());
            Gdx.app.exit();
        }
    }

	public static String translate(String expr)
	{
		if(World.translation == null) {
			return expr;
		}

		String t = World.translation.get(expr);
		if(t != null) {
			return t;
		}

		if(World.lang != "en") {
			System.out.println("Expression '" + expr + "' not translated to " + World.lang);
		}

		World.translation.put(expr, expr);
		return expr;
	}
}
