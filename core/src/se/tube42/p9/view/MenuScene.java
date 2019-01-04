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

public class MenuScene extends Scene {
	private Layer l0;
	private ButtonItem[] buttons;

	public MenuScene() {
		super("menu");

		l0 = getLayer(0);
		l0.flags |= Layer.FLAG_TOUCHABLE;

		buttons = new ButtonItem[3];
		buttons[0] = new ButtonItem(ServiceProvider.translate("About"), Assets.tex_icons, ICONS_ABOUT);
		buttons[1] = new ButtonItem(ServiceProvider.translate("Settings"), Assets.tex_icons, ICONS_SETTINGS);
		buttons[2] = new ButtonItem(ServiceProvider.translate("Play"), Assets.tex_icons, ICONS_PLAY);
		for (int i = 0; i < buttons.length; i++) {
			buttons[i].setColor(Constants.COLOR_FG);
			buttons[i].setTextPosition(ButtonItem.TEXTPOS_BELOW);
		}
		l0.add(buttons);
	}

	// --------------------------------------------------

	private void position() {
		final int s0 = (~15) & World.tile3_size; // 6 * 9
		final int x0 = (World.sw - s0 * 6) / 2;
		final int y0 = (World.sh - s0 * 9) / 2;

		// play
		final ButtonItem p = buttons[2];
		p.setSize(s0 * 2, s0 * 2);
		p.setPosition(p.x2 = x0 + s0 * 2, p.y2 = y0 + s0 * 5);

		// about
		final ButtonItem a = buttons[0];
		a.setSize(s0, s0);
		a.setPosition(a.x2 = x0 + s0 * 1, a.y2 = y0 + s0 * 1);

		// settings
		final ButtonItem s = buttons[1];
		s.setSize(s0, s0);
		s.setPosition(s.x2 = x0 + s0 * 4, s.y2 = y0 + s0 * 1);
	}

	private void animate(boolean in_) {
		for (int i = 0; i < buttons.length; i++) {
			final BaseItem bi = buttons[i];
			final float t = 0.3f + i * 0.1f;

			bi.set(BaseItem.ITEM_A, in_, 0, 1, t, null);
			bi.set(BaseItem.ITEM_Y, in_, bi.y2 - World.sh, bi.y2, t, TweenEquation.QUAD_OUT);
		}
	}

	public void resize(int w, int h) {
		super.resize(w, h);
		position();
	}

	public void onShow() {
		position();
		animate(true);
		SoundService.playMusic(true);
	}

	public void onHide() {
		position();
		animate(false);
	}

	// ----------------------------------------------------

	private void handle_button(BaseItem bi) {
		if (buttons[2] == bi) {
			World.mgr.setScene(World.scene_group);
		} else if (bi == buttons[1]) {
			World.mgr.setScene(World.scene_settings);
		} else if (bi == buttons[0]) {
			World.mgr.setScene(World.scene_about);
		}
	}
	// ----------------------------------------------------

	public boolean type(int key, boolean down) {
		if (down && (key == Keys.BACK || key == Keys.ESCAPE)) {
			ServiceProvider.exit();
		}

		return false;
	}

	public boolean touch(int ptr, int x, int y, boolean down, boolean drag) {

		if (down && !drag) {
			ButtonItem hit = (ButtonItem) l0.hit(x, y);
			if (hit != null) {
				hit.press();
				handle_button(hit);
			}
		}
		return true;
	}

}
