package se.tube42.p9.view;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.Input.*;
import com.badlogic.gdx.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.item.*;
import se.tube42.lib.util.*;
import se.tube42.lib.service.*;

import se.tube42.p9.data.*;
import se.tube42.p9.logic.*;

import static se.tube42.p9.data.Constants.*;

public class BackgroundScene extends Scene implements TweenListener
{
    private static final int COUNT = 6;

    private BaseText [] words;
    private SpriteItem [] rects;

    public BackgroundScene()
    {
        super("bg");

        rects = new SpriteItem[COUNT];
        for(int i = 0; i < rects.length; i++) {
            rects[i] = new SpriteItem(Assets.tex_rect, 0);
            rects[i].setColor(Constants.COLOR_4);
        }
        getLayer(0).add(rects);

        words = new BaseText[COUNT];
        for(int i = 0; i < words.length; i++) {
            words[i] = new BaseText(Assets.fonts2[0]);
            words[i].setColor(Constants.COLOR_1);
            words[i].setAlignment(-0.5f, 0.5f);
        }

        getLayer(1).add(words);

        /* force show, since background scenes dont see these */
        onShow();
    }


    public void onShow()
    {
        for(int i = 0; i < words.length; i++) {
            final float t = 0.6f * i+ RandomService.get(2.5f, 4.0f);
            words[i].setText("");
            words[i].set(BaseItem.ITEM_A, 0, 0.1f)
                .configure(t, null)
                .finish(this, i);

            rects[i].setImmediate(BaseItem.ITEM_A, 0);
        }
    }
    public void onHide()
    {
        for(int i = 0; i < words.length; i++) {
            words[i].set(BaseItem.ITEM_A, 0)
                .configure(0.1f + 0.1f * i, null);
        }
    }

    public void resize(int w, int h)
    {
        System.out.println("onresize " + w + "x" + h);
        super.resize(w, h);

        final int h0 = h / 16;;
        final int h1 = ( h - h0 * 2) / (words.length - 1);
        for(int i = 0; i < words.length; i++) {
            final int h2 = (int)(0.5f + words[i].getH());
            words[i].setPosition(w / 2, h0 + h1 * i);
            rects[i].setSize(w, h2 * 2);
            rects[i].setPosition(0, h0 + i * h1 - 1.0f * h2);
        }
    }



    public void onFinish(Item item, int index, int msg)
    {
        final float t0 = RandomService.get(0.5f, 0.7f);
        final float t1 = RandomService.get(6.0f, 12.0f);
        final float t2 = RandomService.get(0.5f, 0.7f);

        final float x0 = UIC.sw * RandomService.get(0.2f, 0.8f);
        final float x1 = UIC.sw * RandomService.get(0.2f, 0.8f);
        final float x2 = UIC.sw * RandomService.get(0.2f, 0.8f);

        final int i = msg;

        words[i].setText(World.words.random());
        words[i].set(BaseItem.ITEM_X, x0, x1).configure(t0, null)
            .pause(t1)
            .tail(x2);

        words[i].set(BaseItem.ITEM_A, 0, 0.25f).configure(t0, null)
            .pause(t1)
            .tail(0).finish(this, i);

        rects[i].set(BaseItem.ITEM_A, 0, 0.15f).configure(t0 / 2, null)
            .tail(0.0f).configure(t0 * 2, null);
    }

}
