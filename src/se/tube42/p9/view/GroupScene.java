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

public class GroupScene extends ListBaseScene
{
    public GroupScene()
    {
        super("group");
    }

    // ----------------------------------------------------

    protected ListBaseItem [] createItems()
    {
        ListBaseItem [] items = new GroupItem[COUNT];
        for(int i = 0; i < COUNT; i++) {
            items[i] = new GroupItem(i);
        }

        return items;
    }

    protected void updateItems()
    {
        boolean enabled = true;
    	for(int i = 0; i < items.length; i++) {
            GroupItem gi = (GroupItem) items[i];
            gi.setEnabled(enabled);

            // all should be solved in order to enable the next group
            if(enabled) {
		int prog = GameService.getGroupProgress(i * COUNT);
		if(prog != GameService.PROGRESS_ALL)
		    enabled = false;
	    }
        }
    }

    protected void goForward(ListBaseItem b)
    {
        GroupItem l = (GroupItem) b;
        if(l.isEnabled()) {
            World.level_group = l.getGroup();
            World.mgr.setScene(World.scene_level, 1000);
        }
    }

    protected void goBack()
    {
        World.mgr.setScene(World.scene_menu);
    }


}
