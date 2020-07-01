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

public class ButtonItem extends SpriteItem {
	public static final int TEXTPOS_CENTER = 0, TEXTPOS_BELOW = 1;

	private BitmapFont font;
	private String text;
	private int textpos, textcolor;
	private GlyphLayout layout = new GlyphLayout();

	public ButtonItem(String text) {
		this(text, Assets.fonts2[0], Assets.tex_rect, 0);
	}

	public ButtonItem(String text, BitmapFont font, TextureRegion[] tex, int index) {
		super(tex, index);

		this.font = font;
		this.flags |= BaseItem.FLAG_TOUCHABLE;
		this.textpos = TEXTPOS_CENTER;
		this.text = text;
		setColor(Constants.COLOR_1);
		setTextColor(COLOR_FG);
	}

	public void setTextColor(int c) {
		this.textcolor = c;
	}

	public void setTextPosition(int textpos) {
		this.textpos = textpos;
	}

	public void setText(String text) {
		this.text = text;
	}

	public void press() {
		set(BaseItem.ITEM_S, 1.1f).configure(0.1f, null).tail(1).configure(0.1f, null);
	}

	public void draw(SpriteBatch sb) {
		super.draw(sb);

		layout.setText(font, text);
		final int x = (int) (getX() + (w - layout.width) / 2);
		int y = (int) getY();

		if (textpos == TEXTPOS_CENTER)
			y += (h + layout.height) / 2;
		else if (textpos == TEXTPOS_BELOW)
			y -= layout.height / 4;

		ColorHelper.set(font, textcolor, getAlpha());
		font.draw(sb, text, x, y);

	}
}
