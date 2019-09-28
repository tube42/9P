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

import java.security.KeyStore.TrustedCertificateEntry;

public class SettingsScene extends Scene
{
    private BarLayer l0;
    private Layer l1;
    private ButtonItem [] settings;

	private boolean funoff = true; // ;)
	private int delstate = 0;

    public SettingsScene()
    {
        super("settings");

        addLayer( l0 = new BarLayer(true, ICONS_BACK));

        settings = new ButtonItem[3];
        settings[0] = new ButtonItem("");
		settings[1] = new ButtonItem("");
		settings[2] = new ButtonItem("");

        l1 = getLayer(1);
        l1.add(settings);
        l1.flags |= Layer.FLAG_TOUCHABLE;
    }

    // --------------------------------------------------

    private void position()
    {

        final int w0 = 4 * World.tile3_size;
        final int h0 = World.tile3_size;
        final int gap = World.tile3_size / 2;


        final int cnt = settings.length;
        final int y0 = (World.sh - cnt * (h0 + gap)) / 2;

        for(int i = 0; i < cnt; i++) {
            final ButtonItem bi = settings[cnt - 1 - i];
            bi.setSize(w0, h0);
            bi.setPosition( bi.x2 = (World.sw - w0) / 2,
                      bi.y2 = y0 + i * (h0 + gap) );
        }
    }

    private void animate(boolean in_)
    {
        l0.animate(in_);

        for(int i = 0; i < settings.length; i++) {
            final ButtonItem bi = settings[i];
            bi.set(BaseItem.ITEM_Y, in_, bi.y2 - World.sh, bi.y2, 0.3f, null);
            bi.set(BaseItem.ITEM_A, in_, 0, 1, 0.5f + i * 0.05f, null);
        }
    }

    private void update()
    {
        settings[0].setText( ServiceProvider.translate(Settings.sound_on ? "sound on" : "sound off"));
		settings[1].setText( ServiceProvider.translate(funoff ? "fun off" : "boring on"));
		settings[2].setText( ServiceProvider.translate(delstate == 0 ? "delete progress" :
			delstate == 1 ? "confirm" : "deleted"));
    }

    public void resize(int w, int h)
    {
        super.resize(w, h);
        l0.resize(w, h);

        position();
    }

    public void onShow()
    {
		delstate = 0;
        update();
        position();
        animate(true);
    }

    public void onHide()
    {
        position();
        animate(false);
    }

    // ----------------------------------------------------

    private void handle_button(BaseItem bi)
    {
        if(bi == settings[0]) {
            Settings.sound_on = !Settings.sound_on;
            ServiceProvider.saveSettings();
            update();
        } else if(bi == settings[1]) {
            funoff = !funoff;
            update();
        } else if(bi == settings[2]) {
			if(delstate == 1) {
				ServiceProvider.deleteSavedLevels();
			}
			if(delstate < 2) {
				delstate++;
				update();
			}
		}
    }

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
