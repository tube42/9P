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
        
        bi.select(true);
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
        
        bi.select(false);
        cnt--;
        return true;
    }
    
    public BrickItem remove()
    {
    	if(cnt <= 0)
            return null;
        
        cnt--;
        selected[cnt].select(false);
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
        for(int i = 0; i < COUNT; i++)
            all[i].setChar( l.board.charAt(i));
        
        clear();
    }
    
    public void clear()
    {
        for(int i = 0; i < COUNT; i++)
            all[i].select(false);
        
        this.cnt = 0;
    }
    
    
}