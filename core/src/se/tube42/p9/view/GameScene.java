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
import static se.tube42.p9.data.WordList.*;

public class GameScene extends Scene
{
    private BarLayer lui;
    private Layer lplaced, ltiles, lstars;
    private ParticleLayer lparticles;
    private SpriteItem back0;
    private StarItem []stars;
    private IconItem seen;

    private int big_size, big_stripe, big_x0, big_y0;
    private int sel_size, sel_stripe, sel_x0, sel_y0;
	private int sel_dx, sel_dy;

    private BrickItem brick_sel;

    public GameScene()
    {
        super("game");

        World.board = new Board();

        addLayer( lui = new BarLayer(true, 3));
        lui.setPosition(0, 0);
        lui.setIcon(0, ICONS_BACK, false);

        lui.setPosition(1, 1);
        lui.setIcon(1, -1, false);
        lui.getButton(1).button = false;
        lui.setPosition(2, 2);
        lui.setIcon(2, ICONS_SHUFFLE, false);
        seen = lui.getButton(1);

        lplaced = getLayer(1);
        lplaced.add( back0 = new SpriteItem(Assets.tex_rect, 0));
        back0.setColor(ColorHelper.lighter(COLOR_1));
		back0.setAlpha(1);


		ltiles = getLayer(2);
        ltiles.add(World.board.all);
        addLayer(lparticles = new ParticleLayer());

        stars = new StarItem[3];
        for(int i = 0; i < stars.length; i++)
            stars[i] = new StarItem();
        lstars = getLayer(4);
        lstars.add(stars);

        reset();
        update();
    }


    public void resize(int w, int h)
    {
		super.resize(w, h);
		lui.position(w, h);

		// compute board placement
        sel_stripe = Math.min(World.tile3_size, w / 10);
        sel_size = sel_stripe * 8 / 10;
        sel_x0 = sel_size / 2;
        big_stripe = World.tile1_size;
        big_size = big_stripe * 9 / 10;

        // we divide the unused space into 3 to get the gap between items:
        final int hgap = (int)(lui.getY() - sel_stripe - 2 * big_stripe - big_size) / 3;
        big_x0 = (w - 2 * big_stripe - big_size) / 2;
        big_y0 = hgap;
        sel_y0 = big_y0 + 3 * big_stripe + hgap;

		// set position the items
		back0.setSize(w, sel_stripe);
		back0.setPosition(0, sel_y0 - (sel_stripe - sel_size) / 2);

		// position stars stars
        final int starsize = lui.getSize();
        final int star_y0 = lui.getY();
		final int star_x0 = (w - 3 * starsize) / 2;
        for(int i = 0; i < stars.length; i++) {
            final StarItem si = stars[i];
            si.setSize(starsize, starsize);
            si.setPosition(star_x0 + i * starsize, star_y0);
		}

		World.board.configure(big_size, sel_size);

        update();
    }

    // --------------------------------------------------
    private void position()
    {
		// place bricks on the board
        for(int i = 0; i < COUNT; i++) {
            final BrickItem bi = World.board.all[i];
            bi.setPosition(i);
            if(bi.free()) {
                bi.setPosition(SPEED_REMOVE,
                          big_x0 + (i % 3) * big_stripe,
						  big_y0 + (i / 3) * big_stripe);
			}
		}

		// place select bricks
		final int y1 = sel_y0;
        int x1 = (World.sw - (World.board.cnt - 1) * sel_stripe - sel_size) / 2;
		for(int i = 0; i < World.board.cnt; i++) {
			final BrickItem bi = World.board.selected[i];
			bi.setPosition(SPEED_ADD, x1, y1);
			x1 += sel_stripe;
        }
    }

    private void animate(boolean in_)
    {
        if(in_) {
            // remove active tweens, just in case
            TweenHelper.remove(World.board.all, BaseItem.ITEM_X, true);
            TweenHelper.remove(World.board.all, BaseItem.ITEM_Y, true);

            back0.pause(BaseItem.ITEM_A, 0, 0.3f).tail(1).configure(1f, null);
            TweenHelper.animate(World.board.all, BaseItem.ITEM_A,
                      0, 1, 0.2f, 0.5f, null);

        } else {
            // fade out game elements
            back0.pause(BaseItem.ITEM_A, 1, 0.3f).tail(0).configure(0.4f, null);
            TweenHelper.animate(World.board.all, BaseItem.ITEM_A,
                      1, 0, 0.1f, 0.5f, null);
        }
    }

    public void reset()
    {
        seen.setIcon(-1, false);
        lui.setIcon(2, World.board.cnt == 0 ? ICONS_SHUFFLE : ICONS_DEL, false);
        lparticles.killAllParticles();
    }

    public void onShow()
    {
    	reset();
        update();
        lui.animate(true);
		animate(true);

		// if we are doing size animatin, this is not the time to do them:
		for(BrickItem bi : World.board.all)
			bi.removeTween(BaseItem.ITEM_V, true);

        World.scene_bg.onHide(); // disable bg for now
    }

    public void onHide()
    {
    	// save level if changed
    	GameService.saveChangedLevels();

        update();
        lui.animate(false);
        animate(false);

        World.scene_bg.onShow(); // bg is back...
    }

    private void update()
    {
        position();
        lui.setIcon(2, World.board.cnt == 0 ? ICONS_SHUFFLE : ICONS_DEL, true);
    }

    // ----------------------------------------------------

    private void anim_gained_stars(int num)
    {
        // particle speed is screen dependent
    	final int speed = Math.min(World.sw, World.sh) / 2;


        // now show the stars
        for(int i = 0; i < stars.length; i++) {
            final StarItem si = stars[i];
            si.show(i < num);

            // particle animation if star is taken
            for(int j = 0; j < 8 && i < num; j++) {
                final float t = RandomService.get(0.8f, 2.5f);
                final float d = RandomService.get(0.45f, 0.6f);
                final Particle p = lparticles.create(d, t);
                p.configure(Assets.tex_icons, ICONS_STAR1, 0x30000000);
                p.attach(si);
                p.setAcceleration(0, - speed * 2, 0);
                p.setVelocity(RandomService.get(-1, +1) * speed,
                          RandomService.get(-1, +1) * speed,
                          RandomService.get(-1, +1) * 90);
            }
        }
    }

    private void anim_added()
    {
    	// speed depends on screen size
    	final float speed = Math.min(UIC.sw, UIC.sh) / 2;

        for(int i = 0; i < World.board.cnt; i++) {
            final BrickItem bi = World.board.selected[i];

            for(int j = 0; j < 3; j++ ) {
                final float t = RandomService.get(0.4f, 1.5f);
                final float d = RandomService.get(0.45f, 0.6f);

                final Particle p = lparticles.create(d, t);
                p.configure(Assets.tex_rect, 0, 0x30000000);
                p.attach(bi);

                p.setAcceleration(0, -speed * 2, 0);
                p.setVelocity(RandomService.get(-1, +1) * speed,
                          RandomService.get(-1, +1) * speed,
                          RandomService.get(-1, +1) * 90);
            }
        }
    }

    private void register_change()
    {
        update();

        final Level level = World.level_curr;
        final int old_stars = GameService.calcLevelStars(level);

        if( board_check() == WORD_NEW) {
            anim_added();

            final int new_stars = GameService.calcLevelStars(level);
            if(old_stars != new_stars) {
				anim_gained_stars(new_stars);

				// also save progress in case player kills app after this
				ServiceProvider.saveAll();
			}
        }
    }


    // ADD
    private void char_add_brick(BrickItem bi)
    {
    	if(World.board.add(bi)) {
			moved(bi, true);;
        }
    }

    private void char_add_char(char c)
    {
    	BrickItem added = World.board.add(c);
    	if(added != null) {
			moved(added, true);;
    	}
    }

    // REMOVE
    private void char_del_this(BrickItem bi)
    {
    	if(World.board.remove(bi)) {
			moved(bi, false);
        }
    }

    private void char_del_last()
    {
    	BrickItem removed = World.board.remove();
    	if(removed != null) {
			moved(removed, false);
    	}
	}

	private void moved(BrickItem bi, boolean sel)
	{
		ltiles.moveLast(bi);
		register_change();

	}

    private void board_clear()
    {
    	World.board.clear();
        register_change();
    }

    //
    private void board_shuffle()
    {
      	World.board.shuffle();
        update();
    }

    private int board_check()
    {
        seen.setIcon(-1, false);

        if(World.board.cnt < WORD_MIN_SIZE)
            return WORD_SHORT;

        final int found = World.words.lookup(World.board.letters, World.board.cnt);
        if(found == FOUND_EXACT) {
            final String str = new String(World.board.letters, 0, World.board.cnt);
            boolean added = World.level_curr.add(str);
            if(added) {
                return WORD_NEW;
            } else {
                seen.setIcon(ICONS_SEEN, true);
                return WORD_SEEN;
            }
        }
        return WORD_INVALID;
    }

    private void go_back()
    {
		World.mgr.setScene(World.scene_stats);
		ServiceProvider.saveAll();
    }

    private void button_press(int id)
    {
        switch(id) {
        case ICONS_BACK:
            go_back();
            break;
        case ICONS_SHUFFLE:
            board_shuffle();
            break;
        case ICONS_DEL:
            char_del_last();
            break;
        }
    }

    // ----------------------------------------------------

    public boolean type(int key, boolean down)
    {
        if(down && (key == Keys.BACK || key == Keys.ESCAPE)) {
            go_back();
            return false;
        }

        if(!down) {
            switch(key) {
            case Keys.DEL:
                char_del_last();
                return true;
            default:
                String s = Keys.toString(key);
                if(s != null && s.length() == 1) {
                    char c = Character.toLowerCase( s.charAt(0));
                    char_add_char(c);
                    return true;
                }
            }
        }

        return false;
    }

    public boolean touch(int ptr, int x, int y, boolean down, boolean drag)
    {
		if(ptr != 0)
			return false;

		// handle buttons:
		if(brick_sel == null) {
			final int but = lui.touch(x, y, down, drag);
			if(but != -1) {
				button_press(but);
				return true;
			}
		}

        // handle bricks
		if(down) {
			if(!drag) {
				BrickItem hit = (BrickItem) ltiles.hit(x, y);
				brick_sel = hit;
				if(brick_sel != null) {
					brick_sel.setScale(brick_sel.free() ? 0.9f : 1.3f);
					sel_dx = (int)(x - brick_sel.getX());
					sel_dy = (int)(y - brick_sel.getY());
				}

			} else {
				if(brick_sel != null ) {
					brick_sel.setPosition(x - sel_dx, y - sel_dy);
				}
			}
        } else if(!down) {
            if(brick_sel != null) {
				brick_sel.setScale(1.0f);
                    if(brick_sel.free() )
                        char_add_brick(brick_sel);
                    else
                        char_del_this(brick_sel);
            }
		}

		// always end round with dropping this
		if(!down) {
			brick_sel = null;
		}

        return true;
    }

}
