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

public class IconItem
extends BaseItem
implements TweenListener
{
    /* package */ int pos;
    /* package */ boolean button;
    
    private int icon;
    
    public IconItem()
    {
        this.flags |= BaseItem.FLAG_TOUCHABLE;
        this.pos = -1;
        this.icon = -1;
        this.button = true;
    }
    
    public void press()
    {
        set(ITEM_S, 1.5f).configure(0.15f, null)
              .tail(1).configure(0.15f, null);
    }
    
    public void setIcon(int icon, boolean animate)
    {
        if(this.icon != icon) {
            if(animate) {
                final float t = 0.15f;
                set(ITEM_S, 0.5f).configure(t, null)
                      .tail(1f).configure(t, null);
                
                set(ITEM_A, 0).configure(t, null)
                      .finish(this, icon)
                      .tail(1).configure(t, null);
            } else {
                this.icon = icon;
            }
        }
    }
    
    public int getIcon()
    {
        return icon;
    }
    
    public void draw(SpriteBatch sb)
    {
        
        if(icon < 0)
            return;
        
        
        // draw bg:
        final float w = getW();
        final float h = getH();
        final float x0 = getX();
        final float y0 = getY();
        final float a = getAlpha();
        
        
        final float s = getScale();
        final float w2 = w / 2;
        final float h2 = h / 2;
        final float w4 = w / 4;
        final float h4 = h / 4;
        
        ColorHelper.set(sb, COLOR_FG, a);
        final TextureRegion tr = Assets.tex_icons[ icon];
        // sb.draw(tr, x0 + w4, y0 + w4, w4, h4, w2, w2, s, s, 0);
        sb.draw(tr, x0, y0, w2, h2, w, h, s, s, 0);
    }
    
    public void onFinish(Item item, int index, int msg)
    {
        this.icon = msg;
    }
}
