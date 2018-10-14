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

public class SplashScene extends Scene
{
	private SpriteItem image;

    public SplashScene()
    {
		super("splash");

		image = new SpriteItem(Assets.tex_splash, 0);
		image.setAlpha(0);

		getLayer(0).add(image);
    }

	// --------------------------------------------------

    public void resize(int w, int h)
    {
		super.resize(w, h);
		final int n = Math.max(32, Math.min(w, h)) / 2;

		image.setSize(n, n);
		image.setPosition( (w - n ) / 2, (h - n) / 2);
    }

    public void onShow()
    {
		ColorHelper.set(World.bgc, COLOR_BG, 0.7f);

		final float y0 = (UIC.sh - image.getH() ) / 2;
		final float x0 = (UIC.sw - image.getW() ) / 2;
		image.setPosition(x0, y0);

		image.pause(BaseItem.ITEM_A, 0, 0.7f)
			.tail(1).configure(0.75f,null)
			.pause(1)
			.tail(0).configure(0.5f,null)
			.pause(0.75f)
			.finish(new Runnable(){

				@Override
				public void run() {
					World.mgr.setScene(new LangScene() );
				}
			});


    }

}
