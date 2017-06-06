
package se.tube42.p9.data;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;


public class Text
{
    private GlyphLayout layout;
    private BitmapFont font;
    private String text;

    public Text()
    {
        this("", null);
    }
    public Text(String text, BitmapFont font)
    {
        this.layout = new GlyphLayout();
        this.font = font;
        this.text = text;
    }
    public BitmapFont getFont()
    {
        return font;
    }
    public void setFont(BitmapFont font)
    {
        if(this.font != font) {
            this.font = font;
            updateBounds();
        }
    }
    public String getText()
    {
        return text;
    }
    public void setText(String text)
    {
        if(text == null || !text.equals(this.text)) {
            this.text = text;
            updateBounds();
        }
    }

    public float getWidth()
    {
        return layout.width;
    }
    public float getHeight()
    {
        return layout.height;
    }
    private void updateBounds()
    {
        if(font != null && text != null)
            layout.setText(font, text);
        else
            layout.width = layout.height = 1;
    }

}

