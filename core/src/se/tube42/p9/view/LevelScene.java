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

public class LevelScene extends ListBaseScene
{
    private static final int COUNT = 9;

    public LevelScene()
    {
        super("level");
    }

    // ----------------------------------------------------

    protected ListBaseItem [] createItems()
    {
        ListBaseItem [] items = new LevelItem[COUNT];
        for(int i = 0; i < COUNT; i++) {
            items[i] = new LevelItem();
        }

        return items;
    }

    protected void updateItems()
    {
        final int offset = World.level_group * 9;
	final int prog = GameService.getGroupProgress(offset);

    	for(int i = 0; i < COUNT; i++) {
            final int index = offset + i;
            final LevelItem l = (LevelItem) items[i];

            if(index < 0 && index >= World.levels.length)  {
                l.setLevel(null);
		l.setEnabled(false);
            } else {
                l.setLevel(World.levels[index]);
		l.setEnabled( i < 3 || prog != GameService.PROGRESS_NONE);
            }
        }
    }

    protected void goForward(ListBaseItem l_)
    {
        LevelItem l = (LevelItem) l_;
	if(l != null && l.isEnabled()) {
	    GameService.setLevel( l.getLevel());
	    World.mgr.setScene(World.scene_game, 1000);
	}
    }

    protected void goBack()
    {
        World.mgr.setScene(World.scene_group);
    }
}
