
package se.tube42.p9.logic;

import java.io.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.ks.*;
import se.tube42.lib.tweeny.*;
import se.tube42.lib.service.*;

public final class ServiceProvider
{
    
    public static void init()
    {
        StorageService.init("9P-0");
    }
    
    public static void service(long dt)
    {
        JobService.service(dt);
        TweenManager.service( dt);
    }
    
    public static void finish()
    {
        StorageService.flush();
    }
    
    public static void saveAll()
    {
    	GameService.saveChangedLevels();
    	StorageService.flush();
    }
    
    // ------------------------------------------------
    // IOService
    public static InputStream readFile(String name)
    {
        FileHandle fh = Gdx.files.internal(name);
        return fh == null ? null : fh.read();
    }
}
