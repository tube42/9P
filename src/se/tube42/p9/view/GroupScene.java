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
    private static final int COUNT = 9;
    
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
            
            // see how many in this group are enabled
            if(enabled) {
                int cnt = 0;
                final int offset = i * COUNT;
                for(int j = 0; j < COUNT; j++) {
                    final Level l = World.levels[offset + j];
                    if(l != null && l.calcStars() > 0)
                        cnt ++;               
                }
                
                // next group is enabled?
                enabled = cnt >= (COUNT * 3 / 4);
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
