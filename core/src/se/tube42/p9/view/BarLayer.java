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

public class BarLayer extends Layer {
	private IconItem[] buttons;
	private int size, y0;
	private int[] x;
	private boolean top;

	public BarLayer(boolean top, int... icons) {
		this.top = top;
		this.x = new int[3];
		this.buttons = new IconItem[3];

		for (int i = 0; i < icons.length && i < buttons.length; i++) {
			if (icons[i] >= 0) {
				buttons[i] = new IconItem();
				setIcon(i, icons[i], false);
				add(buttons[i]);
			}
		}

		update();
		resize(World.sw, World.sh);
	}

	public IconItem get(int i) {
		return buttons[i];
	}

	public int getSize() {
		return size;
	}

	public int getY() {
		return y0;
	}

	public int getX(int n) {
		return x[n];
	}

	public void setIcon(int i, int icon, boolean animate) {
		if (buttons[i] != null)
			buttons[i].setIcon(icon, animate);
	}

	public void resize(int w, int h) {
		// only calc the 3 positions, assigning them to an icon is done in update
		size = World.tile2_size / 2;
		final int gap = size / 4;
		x[0] = gap;
		x[1] = (w - size) / 2;
		x[2] = w - gap - size;
		y0 = top ? h - gap - size : gap;
		update();
	}

	private int hit_index(int x, int y) {
		for (int i = 0; i < buttons.length; i++)
			if (buttons[i] != null && buttons[i].button && buttons[i].hit(x, y))
				return i;
		return -1;
	}

	public int touch(int x, int y, boolean down, boolean drag) {
		if (down && !drag) {
			final int hit = hit_index(x, y);
			if (hit != -1) {
				buttons[hit].press();
				return buttons[hit].getIcon();
			}
		}

		return -1;
	}

	// ----------------------------------------------
	private void update() {
		for (int i = 0; i < buttons.length; i++) {
			final IconItem b = buttons[i];
			if (b != null) {
				b.setSize(size, size);
				b.setPosition(0.6f, x[i], y0);
			}
		}
	}

	void animate(boolean in_) {
		for (int i = 0; i < buttons.length; i++) {
			final IconItem b = buttons[i];
			if (b != null) {
				if (i == 0)
					b.set(BaseItem.ITEM_U, in_, b.getX() - 2 * size, 0, 0.3f, null);
				else if (i == 1)
					b.set(BaseItem.ITEM_V, in_, top ? 2 * size : -2 * size, 0, 0.3f, null);
				else if (i == 2)
					b.set(BaseItem.ITEM_U, in_, b.getX() + 2 * size, 0, 0.3f, null);
			}
		}
	}
}
