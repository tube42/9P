package se.tube42.p9.view;

import se.tube42.lib.service.*;

import se.tube42.p9.data.*;
import se.tube42.p9.logic.*;

import static se.tube42.p9.data.Constants.*;

public class Board
{
    public int cnt;
    public byte [] letters;
    public BrickItem [] all;
	public BrickItem [] selected;
	private int big_size, small_size;

    public Board()
    {
        this.cnt = 0;
        this.letters = new byte[COUNT];
        this.all = new BrickItem[COUNT];
        this.selected = new BrickItem[COUNT];

        for(int i = 0; i < COUNT; i++) {
            all[i] = new BrickItem(i);
            final float shade = RandomService.get(0.9f, 1);
            all[i].cr *= shade;
            all[i].cg *= shade;
            all[i].cb *= shade;
        }
    }

	public void configure(int big_size, int small_size)
	{
		this.big_size = big_size;
		this.small_size = small_size;

		// bricks:
		for(BrickItem b : all) {
			final int size = b.free() ? big_size : small_size;
			b.setSize(size, size);
		}
	}

	private void moved(BrickItem bi, boolean sel)
	{
		bi.select(sel, big_size, small_size);
	}

    // ----------------------------------------------------
    private int find(BrickItem bi)
    {
        for(int i = 0; i <cnt; i++) {
            if(selected[i] == bi)
                return i;
        }

        return -1;
    }

    public boolean add(BrickItem bi)
    {
        if(!bi.free())
            return false;

        bi.select(true, big_size, small_size);
        selected[cnt] = bi;
        letters[cnt] = (byte) bi.getChar();
        cnt++;

        return true;
    }

    public BrickItem add(char c)
    {
        for(int i = 0; i < COUNT; i++) {
            if(all[i].free() && all[i].getChar() == c) {
                if(add(all[i]))
                    return all[i];
            }
        }
        return null;
    }

    public boolean remove(BrickItem bi)
    {
        if(bi.free())
            return false;

        int n = find(bi);
        if(n == -1)
            return false;

        for(int i = n; i < cnt - 1; i++) {
            selected[i] = selected[i + 1];
            letters[i] = letters[i + 1];
        }

		moved(bi, false);
        cnt--;
        return true;
    }

    public BrickItem remove()
    {
    	if(cnt <= 0)
            return null;

		cnt--;
		moved(selected[cnt], false);
        return selected[cnt];
    }

    public void shuffle()
    {
        for(int j = 0; j < 2; j++) {
            for(int i = 0; i < COUNT; i++) {
                final int r = RandomService.getInt(COUNT);
                BrickItem tmp = all[i];
                all[i] = all[r];
                all[r] = tmp;
            }
        }
    }

    // ----------------------------------------------------

    public void setLevel(Level l)
    {
        for(int i = 0; i < COUNT; i++) {
			char ascii = l.board.charAt(i);
			char unicode = World.words.convert(ascii, true);
            all[i].setChar(ascii, unicode);
		}

        clear();
    }

    public void clear()
    {
		for(int i = 0; i < COUNT; i++)
			moved(all[i], false);

        this.cnt = 0;
    }


}