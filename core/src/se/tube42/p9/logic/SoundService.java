
package se.tube42.p9.logic;

import java.io.*;
import java.util.*;

import com.badlogic.gdx.*;
import com.badlogic.gdx.files.*;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;


import se.tube42.p9.data.*;
import static se.tube42.p9.data.Constants.*;

public final class SoundService
{
	public static Sound[] loadSound(String name)
	{
		ArrayList<Sound> a = new ArrayList<Sound>();

        try {
            for(int i = 0; ; i++) {
				String filename = "sound/" + name + i + ".ogg";
				System.out.println(" " + filename);
                Sound s = Gdx.audio.newSound(
                          Gdx.files.internal(filename));
                a.add(s);
            }
        } catch(Exception e)  {
            // ignored
        }

        if(a.size() == 0) {
			System.err.println("nothing loaded :(");
			Gdx.app.exit();
		}
		Sound [] s = new Sound[a.size()];
		return a.toArray(s);
	}

	public static Music loadMusic(String name)
	{
		String filename = "sound/" + name + ".ogg";
		Music m = Gdx.audio.newMusic( Gdx.files.internal(filename));
		m.setLooping(true);
		return m;
	}


	public static void playMusic(boolean play) {
		Music m = Assets.music;
		if(Settings.sound_on && play) {
			if(!m.isPlaying())
				m.play();
		} else {
			m.stop();
		}
	}

	private static int new_idx = 0;
	private static long new_time = 0;
	public static void playNew(int n, int max) {
		if(!Settings.sound_on)
			return;

		// make sure we dont play too close to last one
		long now = System.currentTimeMillis();
		if(now - new_time < 250) {
			for(int i = 0; i < Assets.sound_new.length; i++) {
				Assets.sound_new[i].stop();
			}
		}
		new_time = now;


		int i = n - max + Assets.sound_new.length - 1;
		if(i < 0)
			i = 0;
		float amp = 0.2f + 0.5f * (n / max);
		Assets.sound_new[i].play(amp);
	}
}
