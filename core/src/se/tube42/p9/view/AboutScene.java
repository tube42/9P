package se.tube42.p9.view;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
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

public class AboutScene extends Scene
{
    private BarLayer l0;
    private BaseText aboutText;

    public AboutScene()
    {
        super("about");

        addLayer( l0 = new BarLayer(true, 1));
        l0.setPosition(0, 0);
        l0.setIcon(0, ICONS_BACK);


        aboutText = new BaseText(Assets.fonts2[0]);
        aboutText.setText(ABOUT_TEXT);
        aboutText.setAlignment(-0.5f, +0.5f);
        aboutText.setColor( COLOR_FG);

        getLayer(1).add(aboutText);
    }

    // --------------------------------------------------

    private void position()
    {
        final int gap = World.tile3_size / 2;

        aboutText.setMaxWidth( World.sw - gap);
        aboutText.setPosition( World.sw / 2, World.sh / 2);
    }

    private void animate(boolean in_)
    {
        aboutText.set(BaseItem.ITEM_A, in_, 0, 1, 1.0f, null);
        l0.animate(in_);
    }

    public void resize(int w, int h)
    {
        super.resize(w, h);
        l0.resize(w, h);
        position();
    }

    public void onShow()
    {
        position();
        animate(true);
    }

    public void onHide()
    {
        position();
        animate(false);
    }

    // ----------------------------------------------------

    private void go_back()
    {
        World.mgr.setScene(World.scene_menu);
    }

    // ----------------------------------------------------

    public boolean type(int key, boolean down)
    {
        if(down && (key == Keys.BACK || key == Keys.ESCAPE)) {
            go_back();
        }

        return false;
    }

    public boolean touch(int ptr, int x, int y, boolean down, boolean drag)
    {
        switch(l0.touch(x, y, down, drag)) {
        case ICONS_BACK:
            go_back();
            return true;
        }

        return true;
    }

}
