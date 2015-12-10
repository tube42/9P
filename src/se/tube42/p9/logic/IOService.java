
package se.tube42.p9.logic;

import java.io.*;
import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;

import se.tube42.p9.data.*;
import static se.tube42.p9.data.Constants.*;

/* package */ final class IOService
{
    
    // -----------------------------------------------------------------
    // wordlist
    // -----------------------------------------------------------------
    
    public static WordList loadWordList(String name)
          throws IOException
    {
    	final String filename = "dict/" + name + ".bin";
        InputStream is = Gdx.files.internal(filename).read();
        int size = read_int32(is) - 4;
        final byte [] data = new byte[size];
        
        int got = 0;
        while(size > 0) {
            int n = is.read(data, got, size);
            if(n < 1)
                throw new IOException("ERROR: read returned " + n);
            got += n;
            size -= n;
        }
        
        return new WordList(name, data);
    }
    
    // -----------------------------------------------------------------
    // levels (all of them)
    // -----------------------------------------------------------------
    
    public static Level [] loadLevels(WordList wl)
          throws IOException
    {
    	final String filename = "levels/level." + wl.getName() + ".txt";
        Reader r0 = Gdx.files.internal(filename).reader();
        BufferedReader r = new BufferedReader(r0);
        ArrayList<String> list = new ArrayList<String>();
        
        for(;;) {
            String line = r.readLine();
            if(line == null) break;
            if(line.length() > 10)
                list.add(line);
        }
        
        r.close();
        r0.close();
        
        // parse those strings into a level
        final int cnt = list.size();
        Level []ret = new Level[cnt];
        for(int i = 0; i < cnt; i++) {
            ret[i] = loadLevel(i, list.get(i));
            loadLevelProgress(wl,  ret[i]);
        }
        return ret;
    }
    
    
    // -----------------------------------------------------------------
    // level ( a single one)
    // -----------------------------------------------------------------
    
    public static Level loadLevel(int id, String data)
    {
        final Level ret = new Level(id);
        final String [] ls = data.split(" ");
        ret.board = ls[0];
        
        for(int i = 1; i < ls.length; i++)
            ret.solutions[i - 1 + 4] = Integer.parseInt(ls[i]);
        
        ret.reset();
        return ret;
    }
    
    public static void loadLevelProgress(WordList wl, Level l)
          throws IOException
    {
    	final String filename = "progress/levelprog_" + wl.getName() + "_" + l.board;
    	FileHandle file = Gdx.files.local(filename);
    	if(file != null && file.exists()) {
            l.setProgress( file.readString() );
            // file.close();
    	}
    }
    
    public static void saveLevelProgress(WordList wl, Level l)
          throws IOException
    {
    	final String filename = "progress/levelprog_" + wl.getName() + "_" + l.board;
        FileHandle file = Gdx.files.local(filename);
        if(file == null) {
            System.err.println("ERROR: could not write to " + filename);
            return;
        }
        file.writeString( l.getProgress(), false);
    }
    
    
    // --------------------------------------------------------------
    // helper functions
    
    private static final int read_int32(InputStream is)
          throws IOException
    {
    	int ret = 0;
    	for(int i = 0; i < 4; i++) {
            int c = is.read() & 0xFF;
            ret = (ret << 8) | c;
    	}
        
    	return ret;
    }
    
}
