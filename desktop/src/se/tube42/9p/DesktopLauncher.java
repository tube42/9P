
package se.tube42.p9;

import com.badlogic.gdx.*;
import com.badlogic.gdx.backends.lwjgl.*;

public class DesktopLauncher
{
    public static void main(String[] args )
    {
        P9 app = new P9();
        new LwjglApplication( app, "9P", 480, 720);
    }
}
