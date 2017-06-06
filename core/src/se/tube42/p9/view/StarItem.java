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
import se.tube42.lib.service.*;

import se.tube42.p9.data.*;
import se.tube42.p9.logic.*;

import static se.tube42.p9.data.Constants.*;

public class StarItem extends SpriteItem implements TweenListener
{
    public StarItem()
    {
        super(Assets.tex_icons, 0);     
        setVisible(false);
        setColor(Constants.COLOR_FG);
    }
    
    // ----------------------------------------
    public void setVisible(boolean vis)
    {
        if(vis)
            flags |= BaseItem.FLAG_VISIBLE;
        else
            flags &= ~BaseItem.FLAG_VISIBLE;
    }
    
    public void show(boolean got)
    {
        setVisible(true);
        setIndex(got ? ICONS_STAR1 : ICONS_STAR0);        
        
        if(got) {
            pause(ITEM_S, 1, 0.8f)
                  .tail(1.1f).configure(0.1f, null)
                  .tail(1.0f).configure(0.1f, null)
                  .tail(1.1f).configure(0.1f, null)
                  .tail(1.0f).configure(0.1f, null)
                  .tail(1.1f).configure(0.1f, null)
                  .tail(1.0f).configure(0.3f, null);
        }
        
        set(ITEM_A, 0, 1).configure(0.5f, null)
              .pause(3)
              .tail(0).configure(0.7f, null)
              .finish(this, 0);
        
        set(ITEM_A, 0, 1).configure(0.5f, null)
              .pause(3)
              .tail(0).configure(0.7f, null)
              .finish(this, 0);
        
    }
    
    public void onFinish(Item item, int index, int msg)
    {
        setVisible(false);
    }
    
    
}
