package se.tube42.p9;

import java.util.*;
import com.badlogic.gdx.*;
import com.badlogic.gdx.assets.loaders.SoundLoader;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.ks.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.util.*;
import se.tube42.lib.service.*;

import se.tube42.p9.data.*;
import se.tube42.p9.view.*;
import se.tube42.p9.logic.*;

import static se.tube42.p9.data.Constants.*;

public class P9 extends BaseApp
{

    public P9()
    {
    }

    public void onCreate(SceneManager mgr, Item bgc)
    {
        // force one first resize!
        onResize(World.sw, World.sh);

        ServiceProvider.init();
        IOService.loadSettings();
        load_assets();

        // another one to update the compo right away
        onResize(World.sw, World.sh);

		World.mgr = mgr;

		// background color
		World.bgc = bgc;
		World.bgc.set(0, 0);
		World.bgc.set(1, 0);
		World.bgc.set(2, 0);

		// World.mgr.setScene( World.scene_menu);  // first screen

		// first screen
		World.mgr.setScene( new SplashScene());


        // TEMP until we fix the code handling back:
        Gdx.input.setCatchBackKey(false);
    }

    public void onUpdate(float dt, long dtl)
    {
        ServiceProvider.service(dtl); // this will update job manager, tween manager and so on
    }

    public void onResize(int w, int h)
    {
        LayoutService.resize(w, h);
        System.out.println("RESIZE: " + w + "x" + h);
    }

    // ------------------------------------------------------------
    public void load_assets()
    {
        final int fscale = Math.min(4, World.ui_scale);
        int ascale = World.ui_scale;
        if(ascale == 3) ascale = 2;
        if(ascale > 4)  ascale = 4;
        // final String ascale = "" + ascale;

        Assets.fonts1 = AssetService.createFonts(
                  "fonts/Roboto-Regular.ttf",
                  CHARSET_ALPHA,
                  fscale * 20,
                  fscale * 40
                  );

        Assets.fonts2 = AssetService.createFonts(
                  "fonts/RobotoCondensed-Light.ttf",
                  CHARSET_FULL,
                  World.ui_scale * 30
                  );
        Texture tmp = AssetService.load(ascale + "/icons.png", true);
        Assets.tex_icons = AssetService.divide(tmp, 4, 4);

        tmp = AssetService.load(ascale + "/rect.png", true);
		Assets.tex_rect = new TextureRegion [] { new TextureRegion(tmp) };

		tmp = AssetService.load(ascale + "/splash.png", true);
		Assets.tex_splash = new TextureRegion [] { new TextureRegion(tmp) };

		tmp = AssetService.load(ascale + "/lang.png", true);
        Assets.tex_lang = AssetService.divide(tmp, 1, 4);


		Assets.sound_new = SoundService.loadSound("new");
		Assets.music = SoundService.loadMusic("music0");
	}


}
