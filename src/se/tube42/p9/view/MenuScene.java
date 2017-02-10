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

public class MenuScene extends Scene implements TweenListener
{
    private Layer l0, l1;
    private ButtonItem [] buttons;
    private BaseText [] words;

    public MenuScene()
    {
        super("menu");

        l0 = getLayer(0);
        l1 = getLayer(1);
        l1.flags |= Layer.FLAG_TOUCHABLE;

        buttons = new ButtonItem[3];
        buttons[0] = new ButtonItem("about", Assets.tex_icons, ICONS_ABOUT);
        buttons[1] = new ButtonItem("settings", Assets.tex_icons, ICONS_SETTINGS);
        buttons[2] = new ButtonItem("play", Assets.tex_icons, ICONS_PLAY);
		for(int i = 0; i < buttons.length; i++) {
			buttons[i].setColor(Constants.COLOR_FG);
			buttons[i].setTextPosition(ButtonItem.TEXTPOS_BELOW);
		}
        l1.add(buttons);


        words = new BaseText[8];
        for(int i = 0; i < words.length; i++) {
            words[i] = new BaseText(Assets.fonts2[0]);
            words[i].setColor(Constants.COLOR_1);
            words[i].setAlignment(-0.5f, 0.5f);
        }

        l0.add(words);
    }

    // --------------------------------------------------

    private void position()
    {
	final int s0 = (~15) & World.tile3_size; // 6 * 9
	final int x0 = (World.sw - s0 * 6) / 2;
	final int y0 = (World.sh - s0 * 9) / 2;

	// play
	final ButtonItem p = buttons[2];
	p.setSize(s0 * 2, s0 * 2);
	p.setPosition(p.x2 = x0 + s0 * 2, p.y2 = y0 + s0 * 5);

	// about
	final ButtonItem a = buttons[0];
	a.setSize(s0, s0);
	a.setPosition(a.x2 = x0 + s0 * 1, a.y2 = y0 + s0 * 1);

	// settings
	final ButtonItem s = buttons[1];
	s.setSize(s0, s0);
	s.setPosition(s.x2 = x0 + s0 * 4, s.y2 = y0 + s0 * 1);
    }

    private void animate(boolean in_)
    {
        for(int i = 0; i < buttons.length; i++) {
            final BaseItem bi = buttons[i];
            final float t = 0.3f + i * 0.1f;

            bi.set(BaseItem.ITEM_A, in_, 0, 1, t, null);
            bi.set(BaseItem.ITEM_Y, in_, bi.y2 - World.sh, bi.y2, t, TweenEquation.QUAD_OUT);
        }


        if(in_) {
            for(int i = 0; i < words.length; i++) {
                final float t = RandomService.get(0.5f, 2.0f);
                words[i].setText("");
                words[i].set(BaseItem.ITEM_A, 0, 0.1f)
                    .configure(t, null)
                    .finish(this, i);
            }
        } else {
            for(int i = 0; i < words.length; i++) {
                words[i].set(BaseItem.ITEM_A, 0)
                    .configure(0.1f + 0.1f * i, null);

            }
        }

    }

    public void resize(int w, int h)
    {
        super.resize(w, h);
        position();

        final int h0 = h / 16;;
        final int h1 = ( h - h0 * 2) / (words.length - 1);
        for(int i = 0; i < words.length; i++) {
            words[i].setPosition(w / 2, h0 + h1 * i);
        }

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


    public void onFinish(Item item, int index, int msg)
    {
        final float t0 = RandomService.get(0.5f, 0.7f);
        final float t1 = RandomService.get(6.0f, 12.0f);
        final float t2 = RandomService.get(0.5f, 0.7f);

        final float x0 = UIC.sw * RandomService.get(0.2f, 0.8f);
        final float x1 = UIC.sw * RandomService.get(0.2f, 0.8f);
        final float x2 = UIC.sw * RandomService.get(0.2f, 0.8f);

        final int i = msg;

        words[i].setText(World.words.random());
        words[i].set(BaseItem.ITEM_X, x0, x1).configure(t0, null)
            .pause(t1)
            .tail(x2);

        words[i].set(BaseItem.ITEM_A, 0, 1.0f).configure(t0, null)
            .pause(t1)
            .tail(0).finish(this, i);

    }

    // ----------------------------------------------------

    private void handle_button(BaseItem bi)
    {
        if(buttons[2] == bi) {
            World.mgr.setScene(World.scene_group);
        } else if(bi == buttons[1]) {
            World.mgr.setScene(World.scene_settings);
        } else if(bi == buttons[0]) {
            World.mgr.setScene(World.scene_about);
        }
    }
    // ----------------------------------------------------

    public boolean type(int key, boolean down)
    {
        if(down && (key == Keys.BACK || key == Keys.ESCAPE)) {
            Gdx.app.exit();
        }

        return false;
    }

    public boolean touch(int ptr, int x, int y, boolean down, boolean drag)
    {

        if(down && !drag) {
            ButtonItem hit = (ButtonItem) l1.hit(x, y);
            if(hit != null) {
                hit.press();
                handle_button(hit);
            }
        }
        return true;
    }

}
