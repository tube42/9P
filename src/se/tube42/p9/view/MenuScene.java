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

public class MenuScene extends Scene
{
    private Layer l0;
    private ButtonItem [] buttons;
    
    public MenuScene()
    {
        super("menu");
        
        buttons = new ButtonItem[3];
        buttons[0] = new ButtonItem("about");
        buttons[1] = new ButtonItem("settings");
        buttons[2] = new ButtonItem("play");
        
        l0 = getLayer(0);
        l0.add(buttons);
        l0.flags |= Layer.FLAG_TOUCHABLE;
    }
    
    // --------------------------------------------------
    
    private void position()
    {
        
        final int but_w = 4 * World.tile3_size;
        final int but_h = World.tile3_size;
        final int but_gap = World.tile3_size / 2;
        final int but_x0 = (World.sw - but_w) / 2;
        final int but_y0 = (World.sh - 3 * but_h - 2 * but_gap)/ 2;
        
        for(int i = 0; i < buttons.length; i++) {
            final BaseItem bi = buttons[i];
            final float t = RandomService.get(0.2f, 0.3f);
            bi.setSize(but_w, but_h);
            bi.setPosition(t, bi.x2 = but_x0,
                      bi.y2 = but_y0 + i * (but_gap + but_h));
        }
    }
    
    private void animate(boolean in_)
    {
        for(int i = 0; i < buttons.length; i++) {
            final BaseItem bi = buttons[i];
            final float t = 1f - i * 0.2f;
            
            bi.set(BaseItem.ITEM_A, in_, 0, 1, t, null);
            bi.set(BaseItem.ITEM_Y, in_, bi.y2 - World.sh, bi.y2, t, TweenEquation.QUAD_OUT);
            
            /*
               bi.set(BaseItem.ITEM_A, in_ ? 0 : 1,  in_ ? 1 : 0)
               .configure(t, null);
               bi.set(BaseItem.ITEM_Y, bi.y2 - (in_ ? World.sh: 0), 
               bi.y2 - (in_ ? 0: World.sh))
               .configure(t, TweenEquation.QUAD_OUT);
             */
        }   
    }
    
    public void resize(int w, int h)
    {
        super.resize(w, h);
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
    
    private void handle_button(BaseItem bi)
    {
        if(buttons[2] == bi) {
            World.mgr.setScene(World.scene_group);            
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
