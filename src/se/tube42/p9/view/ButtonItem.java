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

public class ButtonItem extends SpriteItem
{
    private BitmapFont font;
    private String text;
    
    
    public ButtonItem(String text)
    {
        super(Assets.tex_rect, 0);
        
        this.font = Assets.fonts2[0];
        this.flags |= BaseItem.FLAG_TOUCHABLE;
        setColor(Constants.COLOR_1);
        setText(text);
    }
    
    public void setText(String text)
    {
        this.text = text;
    }
    public void press()
    {
        set(BaseItem.ITEM_S, 1.1f).configure(0.1f, null)
              .tail(1).configure(0.1f, null);
    }
    public void draw(SpriteBatch sb)
    {
        super.draw(sb);
        
        BitmapFont.TextBounds tb = font.getBounds(text);
        ColorHelper.set(font, COLOR_FG, getAlpha() );
        font.draw(sb, text,
                  getX() + (w - tb.width) / 2,
                  getY() + (h + tb.height) / 2
                  );
        
    }
}
