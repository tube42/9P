package se.tube42.lib.item;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;

import se.tube42.lib.util.*;

public class Particle
{
    private static final int
          MAX_VELOCITY = 30000
          ;
    
    public int w, h;
    public float angle, x, y;
    public float acceleration_r, acceleration_y, acceleration_x;
    public float velocity_r, velocity_x, velocity_y;
    public float lifeSpan;
    private float cr, cg, cb, alpha;
    
    public float delay;
    private float sx, sy, tx, ty;
    private TextureRegion [] tex;
    private int tex_index;
    private BaseItem attached;
    public Particle next;
    
    
    // -----------------------------------------------
    
    public Particle()
    {
        this.tex = null;
        this.tex_index = 0;
        
        this.w = 1;
        this.h = 1;
        this.next = null;
        reset();
    }
    
    public void attach(BaseItem bi)
    {
    	this.attached = bi;
        
    	if(bi != null) {
            this.cr = bi.cr;
            this.cg = bi.cg;
            this.cb = bi.cb;
            this.w = (int) bi.getW();
            this.h = (int) bi.getH();
        }
    }
    
    public void configure(TextureRegion [] tex, int index, int color)
    {
        this.tex = tex;
        this.tex_index = index;
        this.alpha = (0xFF & (color >> 24)) / 255f;
        this.cr = (0xFF & (color >> 16)) / 255f;
        this.cg = (0xFF & (color >>  8)) / 255f;
        this.cb = (0xFF & (color >>  0)) / 255f;
    }
    
    public void setSize(int w, int h)
    {
        this.w = w;
        this.h = h;
    }
    
    public void setPosition(float x, float y)
    {
        this.x = x;
        this.y = y;
    }
    
    public void setAlignment(float sx, float sy, float tx, float ty)
    {
        this.sx = sx;
        this.sy = sy;
        this.tx = tx;
        this.ty = ty;    
    }
    
    public void setAcceleration(float ax, float ay, float ar)
    {
        acceleration_x = ax;
        acceleration_y = ay;
        acceleration_r = ar;
    }
    
    public void setVelocity(float vx, float vy, float vr)
    {
        velocity_x = vx;
        velocity_y = vy;
        velocity_r = vr;
    }
    
    public float getX() { return x + UIC.sw * sx + w * tx; }
    public float getY() { return y + UIC.sh * sy + h * ty; }
    
    
    public boolean isDead()
    {
        return lifeSpan < 0;
    }
    
    // -----------------------------------------------
    
    public void update(float dt)
    {
    	if(delay > 0) {
            delay -= dt;
            return;
    	}
        
        // motion:
        velocity_x = Math.max( -MAX_VELOCITY, Math.min( +MAX_VELOCITY, velocity_x + (acceleration_x ) * dt));
        velocity_y = Math.max( -MAX_VELOCITY, Math.min( +MAX_VELOCITY, velocity_y + (acceleration_y ) * dt));
        x += velocity_x * dt;
        y += velocity_y * dt;
        
        // rotation
        velocity_r += acceleration_r * dt;
        angle += velocity_r * dt;
        
        // life
        lifeSpan -= dt;
    }
    
    public void reset()
    {
        acceleration_y = acceleration_x = 0;
        velocity_x = velocity_y = 0;
        velocity_r = acceleration_r = 0;
        angle = 0;
        alpha = 0;
        setAlignment(0, 0, 0, 0);
        attached = null;
        delay = 0;
        lifeSpan= 10;
        x = y = 0;
        w = h = 1;
    }
    
    public void draw(SpriteBatch sb)
    {
    	if(delay > 0)
            return;
        
        if(alpha > 0.001f && tex != null) {
            float x0 = getX();
            float y0 = getY();
            
            
            if(attached != null) {
            	x0 += attached.getX();
            	y0 += attached.getY();
            	w = (int) attached.getW();
            	h = (int) attached.getH();
            }
            final TextureRegion tr = tex[tex_index];
            final float w2 = w * 0.5f;
            final float h2 = h * 0.5f;
            
            sb.setColor(cr, cg, cb, alpha);
            sb.draw(tr, x0,  y0,
                    w2, h2, w, h,
                    1, 1, angle);
        }
    }
}

