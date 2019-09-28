
package se.tube42.p9.logic;

import java.io.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.ks.*;
import se.tube42.lib.tweeny.*;
import se.tube42.p9.data.World;
import se.tube42.lib.service.*;

public final class ServiceProvider {

	public static void init() {
		StorageService.init("9P-0");
	}

	public static void service(long dt) {
		JobService.service(dt);
		TweenManager.service(dt);
	}

	public static void exit() {
		finish();
		Gdx.app.exit();
	}

	public static void finish() {
		StorageService.flush();
	}

	public static void saveSettings() {
		IOService.saveSettings();
	}

	public static void loadSettings() {
		IOService.loadSettings();
	}

	public static void saveAll() {
		GameService.saveChangedLevels();
		StorageService.flush();
	}

	public static void deleteSavedLevels() {
		GameService.deleteSavedLevels();
		StorageService.flush();
	}

	public static void loadCommonAssets() {
		AssetLoaderService.loadCommonAssets(World.ui_scale);
	}

	public static void setLanguage(int index) {
		LanguageService.setLanguage(index);
		AssetLoaderService.loadLanguageAssets(World.words, World.ui_scale);
	}

	public static String translate(String expr) {
		return LanguageService.translate(expr);
	}

}
