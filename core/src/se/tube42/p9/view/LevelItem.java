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

public class LevelItem extends ListBaseItem
{
    private Level level;
    private int stars;

    public LevelItem()
    {
        this.level = null;
    }

    public Level getLevel()
    {
        return level;
    }

    public void setLevel(Level l)
    {
        this.level = l;
        if(l == null) {
            this.flags &= ~BaseItem.FLAG_VISIBLE;
            this.text.setText("");
            this.stars = 0;
        } else {
            this.flags |= BaseItem.FLAG_VISIBLE;
            this.text.setText( "" + (1 + l.id) );
            this.stars = GameService.calcLevelStars(l);
        }
    }


    public void draw(SpriteBatch sb)
    {
        super.draw(sb);

        final float x = getX();
        final float y = getY();
        final float a = getAlpha();

	if(enabled) {
	    // stars
	    final int w4 = (int)w / 4;
	    final float x0 = 0.5f + x + ((int)w - 3 * w4) / 2;
	    final float y0 = 0.5f + y + w4 / 4;

	    ColorHelper.set(sb, COLOR_FG, a);
	    for(int i = 0; i < 3; i++) {
		final TextureRegion tr = Assets.tex_icons[ stars > i ? ICONS_STAR1 : ICONS_STAR0];
		sb.draw(tr, x0 + w4 * i, y0, w4, w4);
	    }


	    // level number
	    final BitmapFont font = text.getFont();
	    ColorHelper.set(font, COLOR_FG, a);
	    font.draw(sb, text.getText(),
		      getX() + (w - text.getWidth()) / 2,
		      getY() + (h + text.getHeight()) / 2
		      );
	} else {
            final float h2 = h / 2;
            final float w2 = w / 2;
            final float h4 = h / 4;
            final float w4 = w / 4;

            ColorHelper.set(sb, COLOR_FG, a);
            sb.draw(Assets.tex_icons[ICONS_LOCK], x + w4, y + h4, w2, h2);
	}
    }
}
