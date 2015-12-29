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
    private Layer l0;
    private ButtonItem back;
    private BaseText aboutText;
    
    public AboutScene()
    {
        super("about");
                
        back = new ButtonItem("back");
        l0 = getLayer(0);
        l0.add(back);
        l0.flags |= Layer.FLAG_TOUCHABLE;
        
        
        aboutText = new BaseText(Assets.fonts2[0]);
        aboutText.setText(ABOUT_TEXT);
        aboutText.setAlignment(-0.5f, +0.5f);
        aboutText.setColor( COLOR_FG);
        
        getLayer(1).add(aboutText);
    }
    
    // --------------------------------------------------
    
    private void position()
    {
        
        final int but_w = 4 * World.tile3_size;
        final int but_h = World.tile3_size;        
        final int gap = World.tile3_size / 2;
        
        back.setSize(but_w, but_h);
        back.setPosition( (World.sw - but_w) / 2, gap);
        
        aboutText.setMaxWidth( World.sw - gap);
        aboutText.setPosition( World.sw / 2, World.sh / 2);
    }
    
    private void animate(boolean in_)
    {
        back.set(BaseItem.ITEM_A, in_, 0, 1, 0.5f, null);        
        aboutText.set(BaseItem.ITEM_A, in_, 0, 1, 1.0f, null);
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
        if(bi == back) {
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
