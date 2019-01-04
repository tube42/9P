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

public abstract class ListBaseScene extends Scene
{
    protected BarLayer l0;
    protected Layer l1;
    protected ListBaseItem [] items;
    protected ListBaseItem hit;

    public ListBaseScene(String name)
    {
        super(name);

        addLayer( l0 = new BarLayer(true, ICONS_BACK));

        items = createItems();
        l1 = getLayer(1);
        l1.add(items);
        l1.flags |= Layer.FLAG_TOUCHABLE;
    }

    // --------------------------------------------------

    protected abstract ListBaseItem [] createItems();
    protected abstract void updateItems();

    protected abstract void goBack();
    protected abstract void goForward(ListBaseItem l);

    // --------------------------------------------------

    private void position(int w, int h)
    {
        // 3*rows list in the center of the screen
        final int rows = items.length / 3;

        final int s = World.tile2_size;
        final int g = s * 5 / 4;
        final int x0 = (w - 2 * g - s) / 2;
        final int y0 = (l0.getY() - (rows - 1) * g - s) / 2;

        for(int i = 0; i < items.length; i++) {
            final int x = i % 3;
            final int y = i / 3;

            final BaseItem bi = items[x + (rows - 1 - y) * 3];
            final float t = RandomService.get(0.2f, 0.3f);
            bi.setSize(s, s);
            bi.setPosition(t, bi.x2 = x0 + x * g, bi.y2 = y0 + y * g);
        }
    }

    private void animate(boolean in_)
    {
        l0.animate(in_);

        // size and fade animation
        if(in_) {
            TweenHelper.animate(items, BaseItem.ITEM_S, 0.1f, 1f, 0.1f, 0.3f,
                      TweenEquation.BACK_OUT);
            TweenHelper.animate(items, BaseItem.ITEM_A, 0.1f, 1f, 0.1f, 0.3f, null);
        } else {
            TweenHelper.animate(items, BaseItem.ITEM_S, 1f, 0.5f, 0.3f, 0.7f,
                      TweenEquation.BACK_OUT);
            TweenHelper.animate(items, BaseItem.ITEM_A, 1f, 0f, 0.3f, 0.7f, null);
        }

        // fly in from sides animation
        final int w = World.sw;
        final int s = (int)items[0].getW();
        for(int i = 0; i < items.length; i++) {
            final BaseItem bi = items[i];
            final float t = RandomService.get(0.4f, 0.6f);
            bi.set(BaseItem.ITEM_X, in_,  bi.x2 < w / 2 ? -s : w + 1, bi.x2,
                   t, TweenEquation.BACK_OUT);
            bi.setImmediate(BaseItem.ITEM_Y, bi.y2);
        }
    }

    public void resize(int w, int h)
    {
		super.resize(w, h);
		l0.resize(w, h);
        position(w, h);
    }

    public void onShow()
    {
        hit = null;
    	updateItems();
        animate(true);
    }

    public void onHide()
    {
        animate(false);
    }

    // ----------------------------------------------------

    public boolean type(int key, boolean down)
    {
        if(down && (key == Keys.BACK || key == Keys.ESCAPE)) {
            goBack();
        }

        return false;
    }

    public boolean touch(int ptr, int x, int y, boolean down, boolean drag)
    {
        switch(l0.touch(x, y, down, drag)) {
        case ICONS_BACK:
            goBack();
            return true;
        }

        if(down && !drag) {
            hit = (ListBaseItem) l1.hit(x, y);
            if(hit != null) {
                hit.press();
                goForward(hit);
            }
        }

        return true;
    }
}
