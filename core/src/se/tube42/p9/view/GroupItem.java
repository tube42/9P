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

public class GroupItem extends ListBaseItem
{
    private int group;

    public GroupItem(int group, String label)
    {
        this.group = group;
        this.text.setText(label);
        this.text.setAlignment(-0.5f, 0.5f);
    }

    public int getGroup()
    {
        return group;
    }

    public void draw(SpriteBatch sb)
    {
        super.draw(sb);

        final float x = getX();
        final float y = getY();
        final float w = getW();
        final float h = getH();
        final float a = getAlpha();

        if(enabled) {
            // level number
            ColorHelper.set(text.getFont(), COLOR_FG, a);
            text.draw(sb, getX() + w / 2, getY() + h / 2);
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
