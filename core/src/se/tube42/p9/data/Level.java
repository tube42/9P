
package se.tube42.p9.data;

import java.util.*;

public class Level
{
    public int id;
    public String board;
    public int []solutions, found_cnt;
    public HashSet<String> found_set;
    public boolean dirty;
    
    
    public Level(int id)
    {
        this.id = id;
        this.solutions = new int[10];
        this.found_cnt = new int[10];
        this.found_set = null;
        this.dirty = false;
        
        for(int i = 0; i < solutions.length; i++) {
            solutions[i] = 0;
            found_cnt[i] = 0;
        }
        
        reset();
    }
    
    public void reset()
    {
        for(int i = 0; i < solutions.length; i++)
            found_cnt[i] = 0;
        
        if(found_set != null) {
            found_set.clear();
        }
    }
    
    public boolean seen(String word)
    {
        if(found_set == null) {
            found_set = new HashSet<String>();
        }
        
        return found_set.contains(word);
    }
    
    public boolean add(String word)
    {
        if(!seen(word)) {
            found_set.add(word);
            found_cnt[word.length()] ++;
            dirty = true;
            return true;
        }
        return false;
    }
    
    // --------------------------------------------------
    
    public int calcWords(int min, int max)
    {
        int ret = 0;
        for(int i = min; i <= max; i++)
            ret += solutions[i];
        return ret;
    }
    
    public int calcWordsFound(int min, int max)
    {
        int ret = 0;
        for(int i = min; i <= max; i++)
            ret += found_cnt[i];
        return ret;
    }
    public int calcStars()
    {
        if(found_cnt[9] > 0) return 3;
        if(found_cnt[8] > 0) return 2;
        if(found_cnt[7] > 0) return 1;
        return 0;
    }
    
    // --------------------------------------------------
    
    public String getProgress()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(' ');
        sb.append(board);
        
        for(int i = 0; i < found_cnt.length; i++) {
            sb.append(' ');
            sb.append(found_cnt[i]);
        }
        
        
        if(found_set != null) {
            for(String s : found_set) {
                sb.append(' ');
                sb.append(s);
            }
        }
        return sb.toString();
    }
    
    public boolean setProgress(String str)
    {
        String [] parts = str.split(" ");
        int offset = 2 + found_cnt.length;
        
        if(parts.length < offset) {
            System.err.println("ERROR: saved level data is too short!");
            return false;
        }
        
        if(id != Integer.parseInt( parts[0]) || !board.equals(parts[1])) {
            System.err.println("ERROR: board or ID missmatch!");
            return false;
        }
        
        reset();
        
        for(int i = offset; i < parts.length; i++) {
            if(!add(parts[i])) {
                System.err.println("ERROR: could not add " + parts[i]);
                return false;
            }
        }
        
        dirty = false;
        return true;
    }
}
