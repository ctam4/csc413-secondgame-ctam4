package tankgame;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class SplashKeyListener implements KeyListener {
    private Launcher app;

    public SplashKeyListener(Launcher app) {
        // set app reference
        this.app = app;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_ENTER:
                // press enter key to start
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_ENTER");
                this.app.start();
                break;
            case KeyEvent.VK_ESCAPE:
                // press escape key to quit
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_ESCAPE");
                this.app.quit();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) { }
}
