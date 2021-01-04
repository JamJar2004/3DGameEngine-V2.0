package engine.core;


import engine.audio.AudioEngine;
import engine.rendering.RenderingEngine;
import games.entities.Camera;

public class CoreEngine
{
    private boolean         isRunning;
    private Game            game;
    private double          frameTime;
    private RenderingEngine renderingEngine;
    private AudioEngine     audioEngine;

    public CoreEngine(Game game, double frameCap)
    {
        this.isRunning       = false;
        this.game            = game;
        this.frameTime       = 1.0 / frameCap;
    }

    public void createWindow(int width, int height, String title)
    {
        Window.create(width, height, title);
        this.renderingEngine = new RenderingEngine();
        this.audioEngine     = new AudioEngine();
        this.game.setCamera(new Camera());
    }

    public void start()
    {
        if(!isRunning)
            run();
    }

    public void stop()
    {
        if(isRunning)
            isRunning = false;
    }

    private void run()
    {
        isRunning = true;

        int    frames       = 0;
        double frameCounter = 0;

        System.out.println(RenderingEngine.getOpenGLVersion());
        game.setRenderingEngine(renderingEngine);
        game.setAudioEngine(audioEngine);
        game.init();

        double lastTime        = Time.getTime();
        double unprocessedTime = 0;

        while(isRunning)
        {
            boolean render = false;

            double startTime  = Time.getTime();
            double passedTime = startTime - lastTime;

            lastTime        = startTime;
            unprocessedTime += passedTime;
            frameCounter    += passedTime;

            while(unprocessedTime > frameTime)
            {
                render = true;
                unprocessedTime -= frameTime;

                if(Window.isCloseRequested())
                    stop();

                game.input((float)frameTime);
                Input.update();
                game.update((float)frameTime);
                game.gameLoop((float)frameTime);

                if(frameCounter >= 1.0)
                {
                    System.out.println(frames);
                    frames       = 0;
                    frameCounter = 0;
                }
            }

            if(render)
            {
                render();
                frames++;
            }
            else
            {
                try
                {
                    Thread.sleep(1);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
            }
        }
        cleanUp();
    }

    private void render()
    {
        game.render();
        Window.update();
    }

    private void cleanUp()
    {
        Window.cleanUp();
        audioEngine.cleanUp();
    }
}
