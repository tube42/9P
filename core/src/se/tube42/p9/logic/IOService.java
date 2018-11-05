
package se.tube42.p9.logic;

import java.io.*;
import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;

import se.tube42.p9.data.*;
import static se.tube42.p9.data.Constants.*;

public final class IOService
{
    private static final String BASE = "save";

    // -----------------------------------------------------------------
    // wordlist
    // -----------------------------------------------------------------

    /* package */ static WordList loadWordList(String name)
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

    /* package */ static Level [] loadLevels(WordList wl)
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

    /* package */ static Level loadLevel(int id, String data)
    {
        final Level ret = new Level(id);
        final String [] ls = data.split(" ");
        ret.board = ls[0];

        for(int i = 1; i < ls.length; i++)
            ret.solutions[i - 1 + 4] = Integer.parseInt(ls[i]);

        ret.reset();
        return ret;
    }

	private static String getSaveFile(WordList wl, Level l)
	{
		return BASE + "/levelprog_" + wl.getName() + "_" + l.board;
	}
    /* package */ static void loadLevelProgress(WordList wl, Level l)
          throws IOException
    {
        final String filename = getSaveFile(wl, l);
    	FileHandle file = Gdx.files.local(filename);
    	if(file != null && file.exists()) {
            l.setProgress( file.readString() );
            // file.close();
    	}
    }

    /* package */ static void saveLevelProgress(WordList wl, Level l)
          throws IOException
    {
        final String filename = getSaveFile(wl, l);
        FileHandle file = Gdx.files.local(filename);
        if(file == null) {
            System.err.println("ERROR: could not write to " + filename);
            return;
        }
        file.writeString( l.getProgress(), false);
    }

   /* package */ static void deleteProgress(WordList wl, Level l)
		throws IOException
   {
		final String filename = getSaveFile(wl, l);
		FileHandle file = Gdx.files.local(filename);
		if(file != null)
		   file.delete();
   }

    // --------------------------------------------------------------

    public static void saveSettings()
    {
        try {
            Writer wr = Gdx.files.local( BASE + "/settings.txt").writer(false);
            wr.write(Settings.sound_on ? "sound_on" : "sound_off");
            wr.close();
        } catch(Exception e) {
            System.err.println("ERROR: " + e);
        }
    }

    public static void loadSettings()
    {
        try {
            Reader rd = Gdx.files.local(BASE + "/settings.txt").reader();
            BufferedReader r = new BufferedReader(rd);
            for(;;) {
                String line = r.readLine();
                if(line == null) break;
                if("sound_on".equals(line)) Settings.sound_on = true;
                else if("sound_off".equals(line)) Settings.sound_on = false;
                else System.err.println("Unknown setting: " + line);
            }
            rd.close();
        } catch(Exception e) {
            System.err.println("ERROR: " + e);
        }
    }

	// --------------------------------------------------------------
	public static HashMap<String, String> loadTranslation(String lang)
	{
		HashMap<String, String> ret = new HashMap<String, String> ();
		try {
			Reader r = Gdx.files.internal("translation.txt").reader();
			BufferedReader br = new BufferedReader(r);
			String id = "<?>";
			for(;;) {
				String line = br.readLine();
				if(line == null)
					break;
				line = line.trim();
				if(line.length() == 0 || line.charAt(0) == '#')
					continue;

				int n = line.indexOf(':');
				if(n != -1) {
					String key = line.substring(0, n).trim();
					String value = line.substring(n + 1).trim();
					if(key.length() == 0) {
						id = value;
					} else if(key.equals(lang)) {
						value = value.replace("\\n", "\n");
						value = value.replace("\\t", "\t");
						String old = ret.get(id);
						if(old != null) {
							value = old + value;
						}

						ret.put(id, value);
					}
				}
			}

			r.close();
		} catch(Exception e) {
            System.err.println("ERROR: " + e);
        }
		return ret;
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
