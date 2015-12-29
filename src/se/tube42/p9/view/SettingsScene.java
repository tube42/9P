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

public class SettingsScene extends Scene
{
    private Layer l0;
    private ButtonItem back;
    private ButtonItem [] settings;
    
    private boolean funoff = true; // ;)
    
    public SettingsScene()
    {
        super("settings");
        
        settings = new ButtonItem[2];
        settings[0] = new ButtonItem("");
        settings[1] = new ButtonItem("");
        
        back = new ButtonItem("back");
        
        l0 = getLayer(0);
        l0.add(settings);
        l0.add(back);
        l0.flags |= Layer.FLAG_TOUCHABLE;
    }
    
    // --------------------------------------------------
    
    private void position()
    {
        final int w0 = 4 * World.tile3_size;
        final int h0 = World.tile3_size;
        final int gap = World.tile3_size / 2;
                
        back.setSize(w0, h0);
        back.setPosition( (World.sw - w0) / 2, gap);
        
        final int cnt = settings.length;
        final int d0 = (int)(back.getH() + back.getY() + gap);
        final int y0 = d0 + (World.sh - d0 - cnt * (h0 + gap)) / 2;
        
        for(int i = 0; i < cnt; i++) {
            final ButtonItem bi = settings[cnt - 1 - i];
            bi.setSize(w0, h0);
            bi.setPosition( bi.x2 = (World.sw - w0) / 2, 
                      bi.y2 = y0 + i * (h0 + gap) );
        }
    }
    
    private void animate(boolean in_)
    {
        back.set(BaseItem.ITEM_A, in_, 0, 1, 0.5f, null);
        
        for(int i = 0; i < settings.length; i++) {
            final ButtonItem bi = settings[i];
            bi.set(BaseItem.ITEM_Y, in_, bi.y2 - World.sh, bi.y2, 0.3f, null);
            bi.set(BaseItem.ITEM_A, in_, 0, 1, 0.5f + i * 0.05f, null);
        }
    }
    
    private void update()
    {
        settings[0].setText( Settings.sound_on ? "sound on" : "sound off");
        settings[1].setText( funoff ? "fun off" : "boring on");
    }
    
    public void resize(int w, int h)
    {
        super.resize(w, h);
        position();
    }

    public void onShow()
    {
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
            IOService.saveSettings();
            update();
        } else if(bi == settings[1]) {
            funoff = !funoff;
            update();
        } else if(bi == back) {
            go_back();
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
    
    public boolean touch(int x, int y, boolean down, boolean drag)
    {
        
        if(down && !drag) {
            ButtonItem hit = (ButtonItem) l0.hit(x, y);
            if(hit != null) {
                hit.press();
                handle_button(hit);
            }
        }
        return true;
    }
    
}
