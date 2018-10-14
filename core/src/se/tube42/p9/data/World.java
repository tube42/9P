
package se.tube42.p9.data;

import java.util.*;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.audio.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;

import se.tube42.lib.scene.*;
import se.tube42.lib.tweeny.*;
import se.tube42.lib.util.*;

import se.tube42.p9.view.*;
import se.tube42.p9.logic.*;

public final class World extends UIC
{
	// UI
    public static int ui_scale, ui_gap;
    public static int tile1_size;
    public static int tile2_size;
    public static int tile3_size;

    // game data
    public static WordList words;

    public static int level_group;
    public static Level []levels;
    public static Level level_curr;

    public static Board board;

    // scenes
    public static BackgroundScene scene_bg;
    public static MenuScene scene_menu;
    public static SettingsScene scene_settings;
    public static AboutScene scene_about;
    public static LevelScene scene_level;
    public static GroupScene scene_group;
    public static StatsScene scene_stats;
    public static GameScene scene_game;

    // app stuff
    public static SceneManager mgr;
    public static Item bgc; // background color

}
