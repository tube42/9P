
package se.tube42.p9.logic;

import java.io.*;
import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;

import se.tube42.p9.data.*;
import static se.tube42.p9.data.Constants.*;

public final class GameService
{
    
    public static void setDict(String dict)
    {
        try {
            World.words = IOService.loadWordList(dict);
            World.levels = IOService.loadLevels(World.words);
        } catch(Exception e) {
            System.err.println(e.toString());
            Gdx.app.exit();
        }
    }
    
    public static void setLevel(Level l)
    {
    	/* same level */
    	if(World.level_curr == l) {
            return;
    	}
        
        if(World.level_curr != null) {
            // TODO: make sure it is saved and so on
        }
        
        World.level_curr = l;
        World.board.setLevel(World.level_curr);
    }
    
    public static int calcLevelStars(Level l)
    {
        /*
           final int score_curr = l.calcScore();
           final int score_max = l.calcScoreMax();
           return Math.min(3, (score_curr * 4) / score_max);
         */
        return l.calcStars();
    }
    
    public static synchronized void saveChangedLevels()
    {
    	try {
            
            for(int i = 0; i < World.levels.length; i++) {
                if(World.levels[i].dirty) {
                    IOService.saveLevelProgress(World.words, World.levels[i]);
                    World.levels[i].dirty = false;
                }
            }
        } catch(Exception exx) {
            System.err.println("Could not save levels...");
        }
    }
}
