package game;

import java.awt.*;

public class Tetromino
{
    public static final Point[][][] tetromino =
        {
            {//i 0
                {new Point(0,0),new Point(0,1),new Point(0,2),new Point(0,3),new Point(0,2)},
                {new Point(0,0),new Point(1,0),new Point(2,0),new Point(3,0),new Point(1,0)},
                {new Point(0,0),new Point(0,1),new Point(0,2),new Point(0,3),new Point(0,2)},
                {new Point(0,0),new Point(1,0),new Point(2,0),new Point(3,0),new Point(1,0)}
            },
            {//o 1
                {new Point(0,0),new Point(0,1),new Point(1,0),new Point(1,1),new Point(0,0)},
                {new Point(0,0),new Point(0,1),new Point(1,0),new Point(1,1),new Point(0,0)},
                {new Point(0,0),new Point(0,1),new Point(1,0),new Point(1,1),new Point(0,0)},
                {new Point(0,0),new Point(0,1),new Point(1,0),new Point(1,1),new Point(0,0)}
            },
            {//s 2
                {new Point(1,0),new Point(2,0),new Point(0,1),new Point(1,1),new Point(1,0)},
                {new Point(0,0),new Point(0,1),new Point(1,1),new Point(1,2),new Point(0,1)},
                {new Point(1,0),new Point(2,0),new Point(0,1),new Point(1,1),new Point(1,0)},
                {new Point(0,0),new Point(0,1),new Point(1,1),new Point(1,2),new Point(0,1)}
            },
            {//j 3
                {new Point(1,0),new Point(1,1),new Point(0,2),new Point(1,2),new Point(1,1)},
                {new Point(0,0),new Point(0,1),new Point(1,1),new Point(2,1),new Point(1,1)},
                {new Point(0,0),new Point(1,0),new Point(0,1),new Point(0,2),new Point(0,1)},
                {new Point(0,0),new Point(1,0),new Point(2,0),new Point(2,1),new Point(1,0)}
            },
            {//z 4
                {new Point(0,0),new Point(1,0),new Point(1,1),new Point(2,1),new Point(1,0)},
                {new Point(1,0),new Point(0,1),new Point(1,1),new Point(0,2),new Point(1,1)},
                {new Point(0,0),new Point(1,0),new Point(1,1),new Point(2,1),new Point(1,0)},
                {new Point(1,0),new Point(0,1),new Point(1,1),new Point(0,2),new Point(1,1)}
            },
            {//l 5
                {new Point(0,0),new Point(0,1),new Point(0,2),new Point(1,2),new Point(0,1)},
                {new Point(0,0),new Point(1,0),new Point(2,0),new Point(0,1),new Point(1,0)},
                {new Point(0,0),new Point(1,0),new Point(1,1),new Point(1,2),new Point(1,1)},
                {new Point(2,0),new Point(0,1),new Point(1,1),new Point(2,1),new Point(1,1)}
            },
            {//t 6
                {new Point(1,0),new Point(0,1),new Point(1,1),new Point(2,1),new Point(1,1)},
                {new Point(0,0),new Point(0,1),new Point(1,1),new Point(0,2),new Point(0,1)},
                {new Point(0,0),new Point(1,0),new Point(2,0),new Point(1,1),new Point(1,0)},
                {new Point(1,0),new Point(0,1),new Point(1,1),new Point(1,2),new Point(1,1)},
            }
        };
    public static final Color[] colors = new Color[]{Color.CYAN,Color.ORANGE,Color.GREEN,Color.BLUE,Color.RED,Color.MAGENTA,Color.YELLOW};
    private Point pos;
    private int rotation;
    private int id;

    public Tetromino(int id, int x, int y, int rotation)
    {
        this.id = id;
        this.pos = new Point(x,y);
        this.rotation = rotation;
    }

    public int getRotation()
    {
        return rotation;
    }

    public int getId()
    {
        return id;
    }

    public Point getPos()
    {
        return pos;
    }

    public void setPos(Point pos)
    {
        this.pos = pos;
    }

    public void rotate()
    {
        Point oldPivot = tetromino[id][rotation][4];
        rotation++;
        if(rotation>3)
        {
            rotation = 0;
        }
        Point newPivot = tetromino[id][rotation][4];
        pos.translate((int)(oldPivot.getX()-newPivot.getX()),(int)(oldPivot.getY()-newPivot.getY()));
    }

    public void reverseRotate()
    {
        Point oldPivot = tetromino[id][rotation][4];
        rotation--;
        if(rotation<0)
        {
            rotation = 3;
        }
        Point newPivot = tetromino[id][rotation][4];
        pos.translate((int)(oldPivot.getX()-newPivot.getX()),(int)(oldPivot.getY()-newPivot.getY()));
    }

    public void left()
    {
        pos.translate(-1,0);
    }

    public void right()
    {
        pos.translate(1,0);
    }

    public void down()
    {
        pos.translate(0,1);
    }

    public void render(Graphics g,int tileSize,int xOffset,int yOffset)
    {
        for(int i=0;i<4;i++)
        {
            Point p = tetromino[id][rotation][i];
            double x = (p.getX()+pos.getX())*tileSize;
            double y = (p.getY()+pos.getY())*tileSize;
            x+=xOffset;
            y+=yOffset;
            g.setColor(colors[id]);
            g.fillRect((int)x,(int)y,tileSize,tileSize);
            g.setColor(colors[id].darker().darker());
            g.drawRect((int)x,(int)y,tileSize,tileSize);
        }
    }
}