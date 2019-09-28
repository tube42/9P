
package se.tube42.p9.logic;

import java.io.*;
import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;

import se.tube42.p9.data.*;
import static se.tube42.p9.data.Constants.*;

public final class GameService {
	public static void setLevel(Level l) {
		/* same level */
		if (World.level_curr == l) {
			return;
		}

		if (World.level_curr != null) {
			// TODO: make sure it is saved and so on
		}

		World.level_curr = l;
		World.board.setLevel(World.level_curr);
	}

	public static int calcLevelStars(Level l) {
		/*
		 * final int score_curr = l.calcScore(); final int score_max = l.calcScoreMax();
		 * return Math.min(3, (score_curr * 4) / score_max);
		 */
		return l.calcStars();
	}

	public static synchronized void saveChangedLevels() {
		try {

			for (int i = 0; i < World.levels.length; i++) {
				if (World.levels[i].dirty) {
					IOService.saveLevelProgress(World.words, World.levels[i]);
					World.levels[i].dirty = false;
				}
			}
		} catch (Exception exx) {
			System.err.println("Could not save levels...");
		}
	}

	public static synchronized void deleteSavedLevels() {
		for (int i = 0; i < World.levels.length; i++) {
			World.levels[i].reset();
			try {
				IOService.deleteProgress(World.words, World.levels[i]);
			} catch (Exception exx) {
				// ignored
			}
		}
	}

	public static final int PROGRESS_NONE = 0, PROGRESS_THREE = 1, PROGRESS_ALL = 2;

	public static int getGroupProgress(int offset) {
		boolean first3 = true;
		boolean all = true;
		for (int i = 0; i < COUNT; i++) {
			int index = i + offset;
			if (index < 0 || index >= World.levels.length)
				continue;
			boolean has9 = World.levels[index].found_cnt[9] != 0;
			if (i < 3)
				first3 &= has9;
			all &= has9;
		}

		return all ? PROGRESS_ALL : (first3 ? PROGRESS_THREE : PROGRESS_NONE);
	}

}
