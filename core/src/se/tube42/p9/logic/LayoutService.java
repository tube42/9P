
package se.tube42.p9.logic;

import se.tube42.p9.data.*;
import static se.tube42.p9.data.Constants.*;
import static se.tube42.p9.data.World.*;

public final class LayoutService
{
    
    public static void resize(int w, int h)
    {
        ui_scale = Math.max(1, Math.min(w / 320, h / 480));
        ui_gap = Math.min(20, 4 * ui_scale);
        
        tile1_size = (~3) & Math.min((w - 2 * ui_gap) / 3, (h - 2 * ui_gap) / 5);
        tile2_size = (~3) & Math.min((w - 2 * ui_gap) / 4, (h - 2 * ui_gap) / 6);
        tile3_size = (~3) & Math.min((w - 2 * ui_gap) / 6, (h - 2 * ui_gap) / 9);
    }
}
