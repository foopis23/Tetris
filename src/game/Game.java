package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class Game extends JPanel implements KeyListener
{
    private JFrame frame;
    private Dimension windowSize;
    private int yOffset;
    private int xOffset;
    private int tileSize;
    private int boardWidth;
    private int boardHeight;
    private int startPoint;

    private int[] speeds = new int[]{-1,15,13,11,9,7,5,4,3};
    private int speed;

    private int ticksSinceMove;
    private int ticksSinceKey;

    private int score;

    private boolean left;
    private boolean right;
    private boolean rotate;
    private boolean rotateReleased;
    private boolean speedUp;
    private boolean classicMode;

    private boolean gameOver;
    private boolean started;

    private Tetromino current;
    private int next;
    private int rowAnim[];
    private Block settled[];

    Game()
    {
        frame = new JFrame();
    }

    void fullScreenMode()
    {
        tileSize = (int)windowSize.getHeight()/20;
        yOffset = 0;
        xOffset = 0;
        boardHeight = 20;
        boardWidth = (int)windowSize.getWidth()/tileSize;
        startPoint = (int)boardWidth/2;
    }

    void classicMode()
    {
        if((windowSize.getHeight()/2)>windowSize.getWidth())
        {
            tileSize = (int)windowSize.getWidth()/10;
            xOffset = 0;
            yOffset = (int)(windowSize.getHeight()-(20*tileSize))/2;
        }else{
            tileSize = (int)windowSize.getHeight()/20;
            yOffset = 0;
            xOffset = (int)(windowSize.getWidth()-(10*tileSize))/2;
        }
        boardHeight = 20;
        boardWidth = 10;
        startPoint = (int)boardWidth/2;
    }

    void init()
    {
        Toolkit tk = Toolkit.getDefaultToolkit();
        windowSize = tk.getScreenSize();
        this.setPreferredSize(windowSize);
        this.addKeyListener(this);
        this.setOpaque(false);
        frame.setAlwaysOnTop(true);
        frame.setUndecorated(true);
        frame.setBackground(new Color(0,0,0,20));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(this);
        frame.pack();
    }

    void start()
    {
        frame.setVisible(true);
        this.requestFocus();
        classicMode();
        classicMode = true;
        reset();
        started=true;
    }

    void reset()
    {
        left = false;
        right = false;
        rotate = false;
        rotateReleased = true;
        speedUp=false;
        gameOver = false;
        settled = new Block[]{};
        speed = 1;
        ticksSinceMove = 0;
        ticksSinceKey = 0;
        Random random = new Random(System.currentTimeMillis());
        current = new Tetromino(random.nextInt(7),startPoint,-4,0);
        next = random.nextInt(7);
        score = 0;
        rowAnim = new int[]{};
    }

    void update()
    {
        this.requestFocus();
        boolean paused;
        if(frame.getState()==Frame.ICONIFIED)
        {
            paused=true;
        }else{
            paused=false;
        }

        if(!gameOver&&!paused)
        {
            if(ticksSinceKey>2)
            {
                if(left&&!right)
                {
                    if(!collisionLeft())
                    {
                        current.left();
                        ticksSinceKey=0;
                    }
                }

                if(right&&!left)
                {
                    if(!collisionRight())
                    {
                        current.right();
                        ticksSinceKey=0;
                    }
                }

                if(rotate&&rotateReleased)
                {
                    current.rotate();
                    rotateReleased = false;
                    if(collisionRotate())
                    {
                        rotateReleased = true;
                        current.reverseRotate();
                        ticksSinceKey=0;
                    }
                }
            }

            if(!speedUp)
            {
                if(ticksSinceMove>=speeds[speed])
                {
                    for(int i=0;i<rowAnim.length;i++)
                    {
                        Block temp[] = new Block[settled.length-boardWidth];
                        int index = 0;
                        int y = rowAnim[i];
                        for(int s=0;s<settled.length;s++)
                        {
                            if(settled[s].getY()<y)
                            {
                                settled[s].setY(settled[s].getY()+1);
                                temp[index] = settled[s];
                                index++;
                            }else if(settled[s].getY()>y)
                            {
                                temp[index] = settled[s];
                                index++;
                            }
                        }
                        settled = temp;
                    }

                    rowAnim = new int[]{};

                    if(!collisionDown())
                    {
                        current.down();
                    }else{
                        settleCurrent();
                        Random random = new Random(System.currentTimeMillis());
                        current = new Tetromino(next,startPoint,-4,0);
                        next = random.nextInt(7);
                    }
                    ticksSinceMove=0;
                }
            }else{
                if(ticksSinceMove>=2)
                {
                    for(int i=0;i<rowAnim.length;i++)
                    {
                        Block temp[] = new Block[settled.length-boardWidth];
                        int index = 0;
                        int y = rowAnim[i];
                        for(int s=0;s<settled.length;s++)
                        {
                            if(settled[s].getY()<y)
                            {
                                settled[s].setY(settled[s].getY()+1);
                                temp[index] = settled[s];
                                index++;
                            }else if(settled[s].getY()>y)
                            {
                                temp[index] = settled[s];
                                index++;
                            }
                        }
                        settled = temp;
                    }

                    rowAnim = new int[]{};

                    if(!collisionDown())
                    {
                        current.down();
                    }else{
                        settleCurrent();
                        Random random = new Random(System.currentTimeMillis());
                        current = new Tetromino(next,startPoint,-4,0);
                        next = random.nextInt(7);
                    }
                    ticksSinceMove=0;
                }
            }

            if(score>400)
            {
                speed=2;
            }

            if(score>1000)
            {
                speed=3;
            }

            if(score>1500)
            {
                speed=4;
            }

            if(score>1900)
            {
                speed=5;
            }

            if(score>2200)
            {
                speed=6;
            }

            if(score>3000)
            {
                speed =7;
            }

            if(score>3500)
            {
                speed = 8;
            }

            ticksSinceMove++;
            ticksSinceKey++;
        }
    }

    void render()
    {
        this.repaint();
    }

    @Override
    public void paint(Graphics g)
    {
        super.paint(g);

        if(started)
        {
            g.setColor(Color.BLACK);
            g.drawRect(xOffset-1,yOffset-1,boardWidth*tileSize+1,boardHeight*tileSize+1);

            for(int i=0;i<settled.length;i++)
            {
                settled[i].render(g,tileSize,xOffset,yOffset);
            }

            for(int i=0;i<rowAnim.length;i++)
            {
                for(int w=0;w<boardWidth;w++)
                {
                    g.setColor(Color.WHITE);
                    double x = w*tileSize;
                    double y = rowAnim[i]*tileSize;
                    x+=xOffset;
                    y+=yOffset;
                    g.fillRect((int)x,(int)y,tileSize,tileSize);
                    g.setColor(g.getColor().darker().darker());
                    g.drawRect((int)x,(int)y,tileSize,tileSize);
                }
            }

            current.render(g,tileSize,xOffset,yOffset);

            g.setFont(new Font("Tahoma",Font.PLAIN,tileSize/2));
            FontMetrics fontMetric = g.getFontMetrics();

            g.setColor(Tetromino.colors[next]);
            g.drawString("Restart [Delete]",(int)windowSize.getWidth()-fontMetric.stringWidth("Restart [Delete] "),(int)fontMetric.getHeight());
            g.drawString("Pause [P]",(int)windowSize.getWidth()-fontMetric.stringWidth("Pause [P] "),(int)(fontMetric.getHeight()*2)+((fontMetric.getHeight()/2)*1));
            g.drawString("Change Mode [=]",(int)windowSize.getWidth()-fontMetric.stringWidth("Change Mode [=] "),(int)(fontMetric.getHeight()*3)+((fontMetric.getHeight()/2)*2));
            g.drawString("Exit [Esc]",(int)windowSize.getWidth()-fontMetric.stringWidth("Exit [Esc] "),(int)(fontMetric.getHeight()*4)+((fontMetric.getHeight()/2)*3));

            if(xOffset>0)
            {
                g.setFont(new Font("Tahoma",Font.PLAIN,tileSize));
                fontMetric = g.getFontMetrics();
                g.setColor(Tetromino.colors[next]);
                int x = (int)(xOffset-fontMetric.stringWidth("Score: "+score))/2;
                int y = (int)(windowSize.getHeight()-fontMetric.getHeight())/2;
                g.drawString("Score: "+score,x,y);

                int highest = -1;
                int lowest = 5;
                int left = 5;
                int right = -1;
                for(int i=0;i<4;i++)
                {
                    Point p = Tetromino.tetromino[next][0][i];

                    if(p.getY()>highest)
                        highest = (int)p.getY();

                    if(p.getY()<lowest)
                        lowest = (int)p.getY();

                    if(p.getX()<left)
                        left = (int)p.getX();

                    if(p.getY()>right)
                        right = (int)p.getX();
                }

                int width = Math.abs(left-right)*tileSize;
                int height = Math.abs(highest-lowest)*tileSize;

                int startX = (int)((xOffset - width)/2)+xOffset+tileSize*boardWidth;
                int startY = (int)((windowSize.getHeight()-height)/2);

                for(int i=0;i<4;i++)
                {
                    Point p = Tetromino.tetromino[next][0][i];
                    g.setColor(Tetromino.colors[next]);
                    g.fillRect(startX+(int)(p.getX()*tileSize),startY+(int)(p.getY()*tileSize),tileSize,tileSize);
                    g.setColor(Tetromino.colors[next].darker().darker());
                    g.drawRect(startX+(int)(p.getX()*tileSize),startY+(int)(p.getY()*tileSize),tileSize,tileSize);
                }
            }else{
                g.setFont(new Font("Tahoma",Font.PLAIN,tileSize/2));
                fontMetric = g.getFontMetrics();
                g.setColor(Tetromino.colors[next]);
                int x = 0;
                int y = fontMetric.getHeight();
                g.drawString("Score: "+score,x,y);
            }

            if(gameOver)
            {
                g.setColor(Color.BLACK);
                g.drawString("Game Over",(int)((windowSize.getWidth()-fontMetric.stringWidth("Game Over"))/2)+5,(int)((windowSize.getHeight()-fontMetric.getHeight())/2)+5);
                g.setColor(Color.WHITE);
                g.drawString("Game Over",(int)((windowSize.getWidth()-fontMetric.stringWidth("Game Over"))/2),(int)((windowSize.getHeight()-fontMetric.getHeight())/2));
            }
        }
    }

    @Override
    public void keyTyped(KeyEvent e)
    {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {
        int k = e.getKeyCode();

        if(k==KeyEvent.VK_A||k==KeyEvent.VK_LEFT)
        {
            left = true;
        }

        if(k==KeyEvent.VK_D||k==KeyEvent.VK_RIGHT)
        {
            right = true;
        }

        if(k==KeyEvent.VK_W||k==KeyEvent.VK_UP)
        {
            rotate = true;
        }

        if(k==KeyEvent.VK_S||k==KeyEvent.VK_DOWN)
        {
            speedUp = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e)
    {
        int k = e.getKeyCode();

        if(k==KeyEvent.VK_A||k==KeyEvent.VK_LEFT)
        {
            left = false;
        }

        if(k==KeyEvent.VK_D||k==KeyEvent.VK_RIGHT)
        {
            right = false;
        }

        if(k==KeyEvent.VK_W||k==KeyEvent.VK_UP)
        {
            rotate = false;
            rotateReleased = true;
        }

        if(k==KeyEvent.VK_S||k==KeyEvent.VK_DOWN)
        {
            speedUp = false;
        }

        if(k==KeyEvent.VK_ESCAPE)
        {
            System.exit(0);
        }

        if(k==KeyEvent.VK_DELETE)
        {
            reset();
        }

        if(k==KeyEvent.VK_EQUALS)
        {
            if(classicMode)
            {
                fullScreenMode();
                reset();
            }else{
                classicMode();
                reset();
            }

            classicMode=!classicMode;
        }

        if(k==KeyEvent.VK_P)
        {
            frame.setState(Frame.ICONIFIED);
        }
    }

    int getTileSize()
    {
        return tileSize;
    }

    public void addRowAnim(int y)
    {
        int[] temp = new int[rowAnim.length+1];
        for(int i=0;i<rowAnim.length;i++)
        {
            temp[i] = rowAnim[i];
        }
        temp[temp.length-1]=y;
        rowAnim = temp;
    }

    boolean collisionDown()
    {
        int id = current.getId();
        int rotation = current.getRotation();
        int x = (int)current.getPos().getX();
        int y = (int)current.getPos().getY()+1;
        for(int i=0;i<4;i++)
        {
            Point point = new Point(x+(int)Tetromino.tetromino[id][rotation][i].getX(),y+(int)Tetromino.tetromino[id][rotation][i].getY());
            for(int s=0;s<settled.length;s++)
            {
                if(point.equals(settled[s].getPos()))
                {
                    return true;
                }
            }

            if(point.getY()>boardHeight-1)
            {
                return true;
            }
        }
        return false;
    }

    boolean collisionLeft()
    {
        int id = current.getId();
        int rotation = current.getRotation();
        int x = (int)current.getPos().getX()-1;
        int y = (int)current.getPos().getY();
        for(int i=0;i<4;i++)
        {
            Point point = new Point(x+(int)Tetromino.tetromino[id][rotation][i].getX(),y+(int)Tetromino.tetromino[id][rotation][i].getY());
            for(int s=0;s<settled.length;s++)
            {
                if(point.equals(settled[s].getPos()))
                {
                    return true;
                }
            }

            if(point.getX()<0)
            {
                return true;
            }
        }
        return false;
    }

    boolean collisionRight()
    {
        int id = current.getId();
        int rotation = current.getRotation();
        int x = (int)current.getPos().getX()+1;
        int y = (int)current.getPos().getY();
        for(int i=0;i<4;i++)
        {
            Point point = new Point(x+(int)Tetromino.tetromino[id][rotation][i].getX(),y+(int)Tetromino.tetromino[id][rotation][i].getY());
            for(int s=0;s<settled.length;s++)
            {
                if(point.equals(settled[s].getPos()))
                {
                    return true;
                }
            }

            if(point.getX()>boardWidth-1)
            {
                return true;
            }
        }
        return false;
    }

    boolean collisionRotate()
    {
        int id = current.getId();
        int rotation = current.getRotation();
        if(rotation>3)
        {
            rotation=0;
        }
        int x = (int)current.getPos().getX();
        int y = (int)current.getPos().getY();
        for(int i=0;i<4;i++)
        {
            Point point = new Point(x+(int)Tetromino.tetromino[id][rotation][i].getX(),y+(int)Tetromino.tetromino[id][rotation][i].getY());
            for(int s=0;s<settled.length;s++)
            {
                if(point.equals(settled[s].getPos()))
                {
                    return true;
                }
            }

            if(point.getX()<0)
            {
                return true;
            }

            if(point.getX()>boardWidth-1)
            {
                return true;
            }

            if(point.getY()>boardHeight-1)
            {
                return true;
            }
        }
        return false;
    }

    void settleCurrent()
    {
        int id = current.getId();
        int rotation = current.getRotation();
        Point pos = current.getPos();
        Color color = Tetromino.colors[id];
        Block[] temp = new Block[settled.length+4];
        int index = 0;
        for(int i=0;i<settled.length;i++)
        {
            temp[index] = settled[index];
            index++;
        }

        for(int i=0;i<4;i++)
        {
            double x = pos.getX()+Tetromino.tetromino[id][rotation][i].getX();
            double y = pos.getY()+Tetromino.tetromino[id][rotation][i].getY();
            temp[index] = new Block(new Point((int)x,(int)y),color);
            index++;
        }

        settled = temp;

        int rowsClear = 0;
        for(int y=0;y<boardHeight;y++)
        {
            for(int x=0;x<boardWidth;x++)
            {
                boolean found = false;
                for(int i=0;i<settled.length;i++)
                {
                    if(x==settled[i].getX()&&y==settled[i].getY())
                    {
                        found = true;
                        break;
                    }
                }

                if(!found)
                {
                    break;
                }

                if(x==boardWidth-1)
                {
                    addRowAnim(y);
                    rowsClear++;
                }
            }
        }

        if(rowsClear==1)
        {
            score+=100;
        }else if(rowsClear==2)
        {
            score+=200;
        }else if(rowsClear==3)
        {
            score+=400;
        }else if(rowsClear==4)
        {
            score+=800;
        }

        for(int i=0;i<settled.length;i++)
        {
            if(settled[i].getY()==0)
            {
                gameOver=true;
            }
        }
    }
}