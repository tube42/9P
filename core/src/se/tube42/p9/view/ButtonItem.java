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
    public static final int
	TEXTPOS_CENTER = 0,
	TEXTPOS_BELOW = 1
	;

    private Text text;
    private int textpos;

    public ButtonItem(String text)
    {
	    this(text, Assets.tex_rect, 0);
    }

    public ButtonItem(String text, TextureRegion [] tex, int index)
    {
        super(tex, index);

        this.text = new Text();
        this.text.setFont( Assets.fonts2[0] );
        this.text.setText(text);

        this.flags |= BaseItem.FLAG_TOUCHABLE;
        this.textpos = TEXTPOS_CENTER;
        setColor(Constants.COLOR_1);
    }

    public void setTextPosition(int textpos)
    {
	this.textpos = textpos;
    }

    public void setText(String text)
    {
        this.text.setText(text);
    }
    public void press()
    {
        set(BaseItem.ITEM_S, 1.1f).configure(0.1f, null)
            .tail(1).configure(0.1f, null);
    }
    public void draw(SpriteBatch sb)
    {
        super.draw(sb);

        final BitmapFont font = text.getFont();

	    final int x = (int)(getX() + (w - text.getWidth()) / 2);
        int y = (int) getY();

        if(textpos == TEXTPOS_CENTER)
            y += (h + text.getHeight()) / 2;
        else if(textpos == TEXTPOS_BELOW)
            y -= text.getHeight() / 4;

        ColorHelper.set(font, COLOR_FG, getAlpha() );
        font.draw(sb, text.getText(), x, y);

    }
}
