package se.tube42.p9.view;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.Input.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;
import se.tube42.lib.util.*;

import se.tube42.p9.data.*;
import se.tube42.p9.logic.*;

import static se.tube42.p9.data.Constants.*;

public class StatsBarItem extends BaseItem
{
    private BitmapFont font;
    private TextureRegion tr;
    
    private String text;
    private int curr, max;
    
    public StatsBarItem(String text)
    {
        this.tr = Assets.tex_rect[0];
        this.font = Assets.fonts2[0];
        this.text = text;
        set(50, 100);
    }
    
    public void set(int total, int found)
    {
        this.curr = found;
        this.max = total;
    }
    
    public void draw(SpriteBatch sb)
    {
        final float a = getAlpha();
        final float s = getScale();
        final float x = 0.5f + getX();
        final float y = 0.5f + getY();
        final BitmapFont.TextBounds tb = font.getBounds(text);
        
        final int GAP = 1;
        final float h0 = (int)Math.max(2 * GAP, (0.5f + 1.2f * tb.height));
        final float h1 = (int)Math.max(2 * GAP, (0.5f + h - h0));
        
        ColorHelper.set(sb, COLOR_1, a);
        sb.draw(tr, x, y + h0, w, h1);
        
        if(max > 0) {
            final float h2 = (h1 - 2 * GAP) * curr / max;
            ColorHelper.set(sb, COLOR_2, a);
            sb.draw(tr, x + GAP, y + h0 + GAP, w - 2 * GAP, h2 - 2 * GAP);
        }
        
        ColorHelper.set(font, COLOR_FG, getAlpha());
        font.draw(sb, text, x + (w - tb.width) / 2, y);
    }
}
