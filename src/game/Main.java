package game;

public class Main
{
    Game game;
    
    Main()
    {
        game = new Game();
    }
    
    private void init()
    {
        game.init();
    }
    
    private void start()
    {
        game.start();
    }
    
    private void loop()
    {
        long lastLoopTime = System.nanoTime();
        final int TARGET_FPS = 30;
        final long OPTIMAL_TIME = 1000000000 / TARGET_FPS;
        long lastFpsTime = 0;
        while(true){            
            long now = System.nanoTime();
            long updateLength = now - lastLoopTime;
            double delta = updateLength / ((double)OPTIMAL_TIME);

            if(delta>=1)
            {
                game.update();
                game.render();
                lastLoopTime = now;
            }
        }
    }
    
    public static void main(String[] args)
    {
        Main main = new Main();
        main.init();
        main.start();
        main.loop();
    }
}