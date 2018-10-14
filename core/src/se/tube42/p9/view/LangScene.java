package se.tube42.p9.view;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;
import se.tube42.lib.util.*;
import se.tube42.lib.service.*;

import se.tube42.p9.data.*;
import se.tube42.p9.logic.*;

import static se.tube42.p9.data.Constants.*;

public class LangScene extends Scene
{
    private Layer l0;
	private ButtonItem []langs;

    public LangScene()
    {
		super("language");


		langs = new ButtonItem[ LANGUAGES.length];
		for(int i = 0; i < langs.length; i++)
			langs[i] = new ButtonItem(LANGUAGES[i], Assets.tex_lang, i);


		l0 = getLayer(0);
        l0.add(langs);
    }

	// --------------------------------------------------


    public void onShow()
    {
		// how big should it be?
		int n = 32;
		while( 6 * n < UIC.sw && langs.length * n * 3 < UIC.sh)
			n++;

		final int h = n, w = n * 2;

		final float gap = (UIC.sh - h * langs.length) /  ( 1 + langs.length);
		final float x0 = (UIC.sw - w) / 2;

		for(int i = 0; i < langs.length; i++) {
			final float y0 = gap + i * (gap + h);
			langs[i].setSize(w, h);
			langs[i].setPosition(-w, y0);
			langs[i].set(BaseItem.ITEM_X, x0)
				.configure(0.1f + i * 0.1f, TweenEquation.BACK_OUT);

		}
	}


	private void set_language(String lang) {
		GameService.setDict(lang);

		World.mgr.setScene( World.scene_menu);

		// can have this before we set the language
		World.mgr.setBackground( World.scene_bg);

		// System.out.println("LANG IS" + lang);
	}
    public boolean touch(int ptr, int x, int y, boolean down, boolean drag)
    {

        if(down && !drag) {
            ButtonItem hit = (ButtonItem) l0.hit(x, y);
            if(hit != null) {
				hit.press();
				set_language(LANGUAGES[ hit.getIndex()] );
            }
        }
        return true;
    }

}
