package galacticmail;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;

public class GameworldKeyListener implements KeyListener {
    private Launcher app;

    public GameworldKeyListener(Launcher app) {
        // set app reference
        this.app = app;
    }

    @Override
    public void keyTyped(KeyEvent e) { }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                // player left
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_LEFT");
                this.app.getGameworld().setPlayerRocketControl("toggleLeft", true);
                break;
            case KeyEvent.VK_RIGHT:
                // player right
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_RIGHT");
                this.app.getGameworld().setPlayerRocketControl("toggleRight", true);
                break;
            case KeyEvent.VK_SPACE:
                // player ignite
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_SPACE");
                this.app.getGameworld().setPlayerRocketControl("toggleIgnite", true);
                break;
            case KeyEvent.VK_ESCAPE:
                // press escape key to reset
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_ESCAPE");
                this.app.reset();
                break;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                // player left
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_LEFT");
                this.app.getGameworld().setPlayerRocketControl("toggleLeft", false);
                break;
            case KeyEvent.VK_RIGHT:
                // player right
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_RIGHT");
                this.app.getGameworld().setPlayerRocketControl("toggleRight", false);
                break;
            case KeyEvent.VK_SPACE:
                // player ignite
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_SPACE");
                this.app.getGameworld().setPlayerRocketControl("toggleIgnite", false);
                break;
        }
    }
}
