package se.tube42.p9.view;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.Input.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.ks.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;
import se.tube42.lib.util.*;

import se.tube42.p9.data.*;
import se.tube42.p9.logic.*;
import static se.tube42.p9.data.Constants.*;

public class BarLayer extends Layer
{
    private IconItem [] buttons;
    private int size, y0;
    private int [] x;
    private boolean top;
    
    public BarLayer(boolean top, int count)
    {
        this.top = top;
        this.x = new int[3];
        this.buttons = new IconItem[count];
        for(int i = 0; i < count; i++) {
            this.buttons[i] = new IconItem();
            setPosition(i, i);
            setIcon(i, -1, false);
        }
        
        add(buttons);
        update();
        resize(World.sw, World.sh);
    }
    
    public int getCount()
    {
        return buttons.length;
    }
    
    public IconItem getButton(int i)
    {
        return buttons[i];
    }
    
    public int getSize()
    {
        return size;
    }
    public int getY()
    {
        return y0;
    }
    
    public int getX(int n)
    {
        return x[n];
    }
    
    public void setPosition(int i, int pos)
    {
        buttons[i].pos = pos;
        update(i);
    }
    
    public void setIcon(int i, int icon)
    {
        setIcon(i, icon, true);
    }
    
    public void setIcon(int i, int icon, boolean animate)
    {
        buttons[i].setIcon(icon, animate);
    }
    
    public void resize(int w, int h)
    {
        position(w, h);
    }
    
    private int hit_index(int x, int y)
    {
        for(int i = 0; i < buttons.length; i++)
            if(buttons[i].button && buttons[i].hit(x, y))
                return i;
        return -1;
    }
    
    public int touch(int x, int y, boolean down, boolean drag)
    {
        if(down && !drag) {
            final int hit = hit_index(x, y);
            if(hit != -1) {
                buttons[hit].press();
                return buttons[hit].getIcon();
            }
        }
        
        return -1;
    }
    
    // ----------------------------------------------
    
    public void update(int i)
    {
        final IconItem b = buttons[i];
        b.setSize(size, size);
        b.setPosition(0.6f, b.x2 = x[b.pos], b.y2 = y0);
    }
    
    private void update()
    {
        for(int i = 0; i < buttons.length; i++)
            update(i);
    }
    
    void position(int w, int h)
    {
        // only calc the 3 positions, assigning them to an icon is done in update
        size = World.tile2_size / 2;
        final int gap = size / 4;
        x[0] = gap;
        x[1] = (w - size) / 2;
        x[2] = w - gap - size;
        y0 = top ? h - gap - size : gap;
        update();
    }
    
    void animate(boolean in_)
    {       
        final int count = buttons.length;
        for(int i = 0; i < count; i++) {
            final IconItem bi = buttons[i];
            bi.setPosition(bi.x2, bi.y2);
            
            if(bi.pos == 0)
                bi.set(BaseItem.ITEM_X, in_, - 2 * size, bi.x2, 0.3f, null);
            else if(bi.pos == 1)
                bi.set(BaseItem.ITEM_Y, in_, top ? World.sh + 2 * size : - 2 * size, 
                       bi.y2, 0.3f, null);
            else if(bi.pos == 2)
                bi.set(BaseItem.ITEM_X, in_, World.sw +  2 * size, bi.x2, 0.3f, null);
        }
    }
}
