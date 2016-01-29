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
import static se.tube42.p9.data.WordList.*;

public class GameScene extends Scene
{
    private BarLayer l0;
    private Layer l1, l2, l4;
    private ParticleLayer l3;
    private SpriteItem back0;
    private StarItem []stars;
    private IconItem seen;
    
    private int big_size, big_stripe, big_x0, big_y0;
    private int sel_size, sel_stripe, sel_x0, sel_y0;
    
    
    private BrickItem brick_sel;
    
    public GameScene()
    {
        super("game");
        
        World.board = new Board();
        
        addLayer( l0 = new BarLayer(true, 3));
        l0.setPosition(0, 0);
        l0.setIcon(0, ICONS_BACK, false);
        
        l0.setPosition(1, 1);
        l0.setIcon(1, -1, false);
        l0.getButton(1).button = false;
        l0.setPosition(2, 2);
        l0.setIcon(2, ICONS_SHUFFLE, false);
        seen = l0.getButton(1);
        
        l1 = getLayer(1);
        l1.add( back0 = new SpriteItem(Assets.tex_rect, 0));
        back0.setColor(ColorHelper.lighter(COLOR_1));
        back0.setAlpha(1);
        
        l2 = getLayer(2);
        l2.add(World.board.all);
        addLayer(l3 = new ParticleLayer());        
        
        stars = new StarItem[3];
        for(int i = 0; i < stars.length; i++)
            stars[i] = new StarItem();
        l4 = getLayer(4);
        l4.add(stars);
        
        reset();
        update();        
    }
    
    // --------------------------------------------------
    private void position()
    {        
        // calc position of tiles and so on:
        final int w = World.sw;
        final int h = World.sh;
        
        sel_stripe = Math.min(World.tile3_size, w / 10);
        sel_size = sel_stripe * 8 / 10;
        sel_x0 = sel_size / 2;        
        
        big_stripe = World.tile1_size;
        big_size = big_stripe * 9 / 10;
        
        // we divide the unused space into 3 to get the gap between items:
        final int hgap = (int)(l0.getY() - sel_stripe - 2 * big_stripe - big_size) / 3;
        
        big_x0 = (w - 2 * big_stripe - big_size) / 2;
        big_y0 = hgap;        
        sel_y0 = big_y0 + 3 * big_stripe + hgap;
        
        final int starsize = l0.getSize();
        final int star_y0 = l0.getY();
        final int star_x0 = (w - 3 * starsize) / 2;
        
        
        // set position the items
        back0.setSize(w, sel_stripe);
        back0.setPosition(0, sel_y0 - (sel_stripe - sel_size) / 2);        
        
        // free bricks
        for(int i = 0; i < COUNT; i++) {
            final BrickItem bi = World.board.all[i];
            bi.setPosition(i);
            if(bi.free()) {
                bi.setSize(big_size, big_size);
                bi.setPosition(0.3f, 
                          bi.x2 = big_x0 + (i % 3) * big_stripe,
                          bi.y2 = big_y0 + (i / 3) * big_stripe
                          );
            }
        }
        
        // in use bricks
        final int y1 = sel_y0;
        int x1 = (World.sw - (World.board.cnt - 1) * sel_stripe - sel_size) / 2; 
        for(int i = 0; i < World.board.cnt; i++) {
            final BrickItem bi = World.board.selected[i];            
            bi.setSize(sel_size, sel_size);
            bi.setPosition(0.6f, bi.x2 = x1, bi.y2 = y1);
            x1 += sel_stripe;
        }        
        
        
        // set stars
        for(int i = 0; i < stars.length; i++) {
            final StarItem si = stars[i];
            si.setSize(starsize, starsize);
            si.setPosition(star_x0 + i * starsize, star_y0);
        }
    }
    
    private void animate(boolean in_)
    {
        if(in_) {
            // remove active tweens, just in case
            TweenHelper.remove(World.board.all, BaseItem.ITEM_X, true);
            TweenHelper.remove(World.board.all, BaseItem.ITEM_Y, true);
            
            back0.pause(BaseItem.ITEM_A, 0, 0.3f).tail(1).configure(1f, null);
            TweenHelper.animate(World.board.all, BaseItem.ITEM_A,
                      0, 1, 0.2f, 0.5f, null);            
            
        } else {
            // fade out game elements
            back0.pause(BaseItem.ITEM_A, 1, 0.3f).tail(0).configure(0.4f, null);
            TweenHelper.animate(World.board.all, BaseItem.ITEM_A,
                      1, 0, 0.1f, 0.5f, null);
        }       
    }
    
    public void resize(int w, int h)
    {
    	super.resize(w, h);        
        l0.position(w, h);
        
        update();
    }
    
    public void reset()
    {
        seen.setIcon(-1, false);
        l0.setIcon(2, World.board.cnt == 0 ? ICONS_SHUFFLE : ICONS_DEL, false);
        l3.killAllParticles();
    }
    
    public void onShow()
    {       
    	reset();        
        update();
        l0.animate(true);
        animate(true);
    }
    
    public void onHide()
    {
    	// save level if changed
    	GameService.saveChangedLevels();
        
        update();
        l0.animate(false);
        animate(false);        
    }
    
    private void update()
    {
        position();        
        l0.setIcon(2, World.board.cnt == 0 ? ICONS_SHUFFLE : ICONS_DEL, true);
    }
    
    // ----------------------------------------------------
    
    private void anim_gained_stars(int num)
    {
        // particle speed is screen dependent
    	final int speed = Math.min(World.sw, World.sh) / 2;
        
        
        // now show the stars
        for(int i = 0; i < stars.length; i++) {
            final StarItem si = stars[i];
            si.show(i < num);
            
            // particle animation if star is taken
            for(int j = 0; j < 8 && i < num; j++) {
                final float t = RandomService.get(0.8f, 2.5f);
                final float d = RandomService.get(0.45f, 0.6f);
                final Particle p = l3.create(d, t);                
                p.configure(Assets.tex_icons, ICONS_STAR1, 0x30000000);
                p.attach(si);
                p.setAcceleration(0, - speed * 2, 0);
                p.setVelocity(RandomService.get(-1, +1) * speed,
                          RandomService.get(-1, +1) * speed,
                          RandomService.get(-1, +1) * 90);
            }            
        }        
    }
    
    private void anim_added()
    {
    	// speed depends on screen size
    	final float speed = Math.min(UIC.sw, UIC.sh) / 2;
        
        for(int i = 0; i < World.board.cnt; i++) {
            final BrickItem bi = World.board.selected[i];
            
            for(int j = 0; j < 3; j++ ) {
                final float t = RandomService.get(0.4f, 1.5f);
                final float d = RandomService.get(0.45f, 0.6f);
                
                final Particle p = l3.create(d, t);
                p.configure(Assets.tex_rect, 0, 0x30000000);
                p.attach(bi);
                
                p.setAcceleration(0, -speed * 2, 0);
                p.setVelocity(RandomService.get(-1, +1) * speed,
                          RandomService.get(-1, +1) * speed,
                          RandomService.get(-1, +1) * 90);
            }
        }
    }
    
    private void register_change()
    {
        update();
        
        final Level level = World.level_curr;
        final int old_stars = GameService.calcLevelStars(level);
        
        if( board_check() == WORD_NEW) {
            anim_added();
            
            final int new_stars = GameService.calcLevelStars(level);
            if(old_stars != new_stars)
                anim_gained_stars(new_stars);
        }        
    }
    
    
    // ADD
    private void char_add_brick(BrickItem bi)
    {
    	if(World.board.add(bi)) {
            l2.moveLast(bi);
            register_change();
        }
    }
    
    private void char_add_char(char c)
    {
    	BrickItem added = World.board.add(c);
    	if(added != null) {
            l2.moveLast(added);
            register_change();
    	}
    }
    
    // REMOVE
    private void char_del_this(BrickItem bi)
    {
    	if(World.board.remove(bi)) {
            l2.moveLast(bi);
            register_change();
        }
    }
    
    private void char_del_last()
    {
    	BrickItem removed = World.board.remove();
    	if(removed != null) {
            l2.moveLast( removed);
    	    register_change();
    	}
    }
    
    
    private void board_clear()
    {
    	World.board.clear();
        register_change();
    }
    
    //
    private void board_shuffle()
    {
      	World.board.shuffle();
        update();
    }
    
    private int board_check()
    {
        seen.setIcon(-1, false);
        
        if(World.board.cnt < WORD_MIN_SIZE)
            return WORD_SHORT;
        
        final int found = World.words.lookup(World.board.letters, World.board.cnt);
        if(found == FOUND_EXACT) {
            final String str = new String(World.board.letters, 0, World.board.cnt);
            boolean added = World.level_curr.add(str);
            if(added) {
                return WORD_NEW;
            } else {
                seen.setIcon(ICONS_SEEN, true);
                return WORD_SEEN;
            }
        }
        return WORD_INVALID;
    }
    
    private void go_back()
    {
        World.mgr.setScene(World.scene_stats);
    }
    
    private void button_press(int id)
    {
        switch(id) {
        case ICONS_BACK:
            go_back();
            break;
        case ICONS_SHUFFLE:
            board_shuffle();
            break;
        case ICONS_DEL:
            char_del_last();
            break;
        }
    }
    
    // ----------------------------------------------------
    
    public boolean type(int key, boolean down)
    {
        if(down && (key == Keys.BACK || key == Keys.ESCAPE)) {
            go_back();
            return false;
        }
        
        if(!down) {
            switch(key) {
            case Keys.DEL:
                char_del_last();
                return true;
            default:
                String s = Keys.toString(key);
                if(s != null && s.length() == 1) {
                    char c = Character.toLowerCase( s.charAt(0));
                    char_add_char(c);
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public boolean touch(int ptr, int x, int y, boolean down, boolean drag)
    {
        // handle buttons:
        final int but = l0.touch(x, y, down, drag);
        if(but != -1) {
            button_press(but);
            return true;
        }
        
        
        // handle bricks
        BrickItem hit = (BrickItem) l2.hit(x, y);
        if(down && !drag) {
            brick_sel = hit;
            if(brick_sel != null) {
                brick_sel.setScale(brick_sel.free() ? 0.9f : 1.3f);
            }
            
        } else if(!down) {
            if(brick_sel != null) {
                brick_sel.setScale(1.0f);
                if(hit == brick_sel) {
                    if(brick_sel.free() )
                        char_add_brick(brick_sel);
                    else
                        char_del_this(brick_sel);
                }
            }
        }
        
        return true;
    }
    
}
