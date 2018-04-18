package game;

import java.awt.*;

public class Block
{
    Point pos;
    Color c;

    public Block(Point pos, Color c)
    {
        this.pos = pos;
        this.c = c;
    }

    public Color getColor()
    {
        return c;
    }

    public int getX()
    {
        return (int)pos.getX();
    }

    public int getY()
    {
        return (int)pos.getY();
    }

    public void setY(int y)
    {
        pos = new Point((int)pos.getX(),y);
    }

    public void setX(int x)
    {
        pos = new Point(x,(int)pos.getY());
    }

    public Point getPos()
    {
        return pos;
    }

    public void setPos(Point pos)
    {
        this.pos = pos;
    }

    public void render(Graphics g, int tileSize, int xOffset, int yOffset)
    {
        g.setColor(c);
        g.fillRect((int)(pos.getX()*tileSize)+xOffset,(int)(pos.getY()*tileSize)+yOffset,tileSize,tileSize);
        g.setColor(c.darker().darker());
        g.drawRect((int)(pos.getX()*tileSize)+xOffset,(int)(pos.getY()*tileSize)+yOffset,tileSize,tileSize);
    }
}
