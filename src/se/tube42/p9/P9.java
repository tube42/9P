package se.tube42.p9;

import com.badlogic.gdx.*;
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

        // temp debug init:
        GameService.setDict("en");


        World.mgr = mgr;
        World.bgc = bgc;

        ColorHelper.set(bgc, COLOR_BG, 0.7f);

        // first screen
        World.scene_menu = new MenuScene();
        World.scene_settings = new SettingsScene();
        World.scene_about = new AboutScene();
        World.scene_game = new GameScene();
        World.scene_stats = new StatsScene();
        World.scene_group = new GroupScene();
        World.scene_level = new LevelScene();
        World.mgr.setScene( World.scene_menu);

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
        final String aname = "" + ascale;

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
        Texture tmp = AssetService.load(aname + "/icons.png", true);
        Assets.tex_icons = AssetService.divide(tmp, 4, 4);

        tmp = AssetService.load(aname + "/rect.png", true);
        Assets.tex_rect = new TextureRegion [] { new TextureRegion(tmp) };
    }
}
