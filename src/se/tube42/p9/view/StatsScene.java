package se.tube42.p9.view;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.Input.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;
import se.tube42.lib.util.*;
import se.tube42.lib.service.*;

import se.tube42.p9.data.*;
import se.tube42.p9.logic.*;
import static se.tube42.p9.data.Constants.*;

public class StatsScene extends Scene
{
    private BarLayer l0;
    private Layer l1, l2;
    private StatsBarItem []bars;
    private SpriteItem [] stars;
    private BaseText text0;
    
    public StatsScene()
    {
        super("stats");
        
        
        text0 = new BaseText(Assets.fonts2[0]);  
        text0.setAlignment(-0.5f, 0.5f);
        text0.setColor(COLOR_FG);
        
        bars = new StatsBarItem[WORD_MAX_SIZE + 1 - WORD_MIN_SIZE];
        for(int i = 0; i < bars.length; i++) {
            bars[i] = new StatsBarItem("" + (i + WORD_MIN_SIZE));
        }
        
        stars = new SpriteItem[3];
        for(int i = 0; i < 3; i++) { 
            stars[i] = new SpriteItem(Assets.tex_icons, 0);
            stars[i].setColor(COLOR_FG);
        }
        
        addLayer( l0 = new BarLayer(true, 2));
        l0.setIcon(0, ICONS_BACK);
        l0.setIcon(1, ICONS_FORWARD);
        l0.setPosition(0, 0);
        l0.setPosition(1, 2);
        
        l1 = getLayer(1);
        l1.add(text0);
        l1.add(bars);
        l1.add(stars);
        
    }
    
    // --------------------------------------------------
    private void position()
    {
        final int w = World.sw;
        final int h = World.sh;
        
        // text
        text0.setPosition(w / 2, l0.getY() + l0.getSize() / 2);
        
        // bars
        final int cnt = bars.length;
        final int wstripe = World.tile3_size * 4 / cnt;
        final int w0 = wstripe / 2;
        final int h0 = h - World.tile1_size * 3;
        final int x0 = (w - (cnt -1) * wstripe - w0) / 2;
        final int y0 = (h - h0) / 2;
        
        for(int i = 0; i < cnt; i++) {
            bars[i].setSize(w0, h0);
            bars[i].setPosition(x0 + i * wstripe, y0);
        }
        
        // stars
        final int starstripe = World.tile2_size;        
        final int starsize = World.tile3_size;
        final int starx0 = (w - 2 * starstripe - starsize) / 2;
        final int stary0 = (y0 - starsize) / 2;
        
        for(int i = 0; i < stars.length; i++) {
            stars[i].setSize(starsize, starsize);
            stars[i].setPosition(starx0 + i * starstripe, stary0);
        }
    }
    
    private void animate(boolean in_)
    {
        if(in_) {
            TweenHelper.animate(bars, BaseItem.ITEM_A, 0, 1,
                      0.1f, 0.3f, 0.05f, 0.2f, null);
            TweenHelper.animate(stars, BaseItem.ITEM_A, 0, 1,
                      0.1f, 0.3f, 0.2f, 0.3f, null);
            text0.set(BaseItem.ITEM_A, 0, 1).configure(0.5f, null);
        } else {
            TweenHelper.animate(bars, BaseItem.ITEM_A, 1, 0, 0.1f, 0.3f, null);
            TweenHelper.animate(stars, BaseItem.ITEM_A, 1, 0, 0.1f, 0.3f, null);
            text0.set(BaseItem.ITEM_A, 1, 0).configure(0.5f, null);
        }
    }
    
    public void resize(int w, int h)
    {
    	super.resize(w, h);
        l0.resize(w, h);                
        position();
    }
    
    public void onShow()
    {
        update();
        
        position();
        l0.animate(true);
        animate(true);
    }
    
    public void onHide()
    {
        position();
        l0.animate(false);        
        animate(false);
    }
    
    private void update()
    {
        final Level l = World.level_curr;
        
        for(int i = 0; i < bars.length; i++) {
            final int n = i + WORD_MIN_SIZE;
            final int total = l.calcWords(n, n);
            final int found = l.calcWordsFound(n, n);
            bars[i].set(total, found);                          
        }
        
        final int cnt = l.calcStars();
        for(int i = 0; i < stars.length; i++) 
            stars[i].setIndex(cnt > i ? ICONS_STAR1 : ICONS_STAR0);
        
        text0.setText("level " + (l.id + 1));       
    }
    
    private void select(Level l)
    {
        if(l == null) return;
        
        GameService.setLevel(l);
        World.mgr.setScene(World.scene_game);
    }
    
    
    // ----------------------------------------------------
    
    private void go_back()
    {
        World.mgr.setScene(World.scene_level);
    }
    
    private void go_forward()
    {
        World.mgr.setScene(World.scene_game);
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
        switch(l0.touch(x, y, down, drag)) {
        case ICONS_BACK:
            go_back();
            break;
        case ICONS_FORWARD:
            go_forward();                        
            break;
        }
        return true;
    }
    
}
