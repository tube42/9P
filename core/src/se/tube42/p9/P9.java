package se.tube42.p9;

import com.badlogic.gdx.*;

import se.tube42.lib.tweeny.*;
import se.tube42.lib.scene.*;
import se.tube42.lib.util.*;

import se.tube42.p9.data.*;
import se.tube42.p9.view.*;
import se.tube42.p9.logic.*;

public class P9 extends BaseApp {

	public P9() {
	}

	public void onCreate(SceneManager mgr, Item bgc) {
		// force one first resize!
		onResize(World.sw, World.sh);

		ServiceProvider.init();
		ServiceProvider.loadSettings();
		ServiceProvider.loadCommonAssets();

		// another one to update the compo right away
		onResize(World.sw, World.sh);

		World.mgr = mgr;

		// background color
		World.bgc = bgc;
		World.bgc.set(0, 0);
		World.bgc.set(1, 0);
		World.bgc.set(2, 0);

		// first screen
		World.mgr.setScene(new SplashScene());

		// TEMP until we fix the code handling back:
		Gdx.input.setCatchBackKey(false);
	}

	public void onUpdate(float dt, long dtl) {
		ServiceProvider.service(dtl); // this will update job manager, tween manager and so on
	}

	public void onResize(int w, int h) {
		LayoutService.resize(w, h);
		System.out.println("RESIZE: " + w + "x" + h);
	}
}
