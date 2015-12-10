
package se.tube42.p9.data;

public final class WordList
{
    
    public static final int
          FOUND_NONE = 0,
          FOUND_EXACT = 1,
          FOUND_PREFIX = 2
          ;
    
    private String name;
    private byte [] data;
    
    public WordList(String name, byte [] data)
    {
        this.name = name;
        this.data = data;
    }
    
    public String getName()
    {
        return name;
    }
    
    
    // ---------------------------------------------------------------------------
    
    public int lookup(final byte [] t, final int tlen)
    {
        final byte [] words = data;
        final int wlen = data.length;
        
        int mid, low = 1, high = wlen -1;
        int tmp;
        boolean partial = false;
        
        while(low < high) {
            tmp = mid = (low + high) / 2;
            while(tmp > 0 && words[tmp] != 0)
                tmp--;
            tmp++;
            
            int k = strcmp(words, tmp, t, tlen);
            if( k == 0 && words[tmp+tlen] != 0) {
                partial = true;
                k = +1;
            }
            
            if(k == 0) return FOUND_EXACT;
            else if(k < 0) low = mid + 1;
            else high = mid;
        }
        return partial ? FOUND_PREFIX : FOUND_NONE;
    }
    
    private static final int strcmp(byte [] list, int offset, byte []t, int tlen)
    {
        for(int i = 0; i < tlen; i++) {
            byte b1 = t[i];
            byte b2 = list[offset++];
            if(b1 < b2) return +1;
            if(b2 < b1) return -1;
        }
        
        return 0;
    }
    
}

