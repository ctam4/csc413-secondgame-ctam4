package tankgame;

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
            case KeyEvent.VK_W:
                // player 1 up
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_W");
                this.app.getGameworld().setPlayerTankControl(1, "toggleUp", true);
                break;
            case KeyEvent.VK_A:
                // player 1 left
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_A");
                this.app.getGameworld().setPlayerTankControl(1, "toggleLeft", true);
                break;
            case KeyEvent.VK_S:
                // player 1 down
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_S");
                this.app.getGameworld().setPlayerTankControl(1, "toggleDown", true);
                break;
            case KeyEvent.VK_D:
                // player 1 right
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_D");
                this.app.getGameworld().setPlayerTankControl(1, "toggleRight", true);
                break;
            case KeyEvent.VK_Z:
                // player 1 shoot
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_Z");
                this.app.getGameworld().setPlayerTankControl(1, "toggleFire", true);
                break;
            case KeyEvent.VK_I:
                // player 2 up
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_I");
                this.app.getGameworld().setPlayerTankControl(2, "toggleUp", true);
                break;
            case KeyEvent.VK_J:
                // player 2 left
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_J");
                this.app.getGameworld().setPlayerTankControl(2, "toggleLeft", true);
                break;
            case KeyEvent.VK_K:
                // player 2 down
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_K");
                this.app.getGameworld().setPlayerTankControl(2, "toggleDown", true);
                break;
            case KeyEvent.VK_L:
                // player 2 right
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_L");
                this.app.getGameworld().setPlayerTankControl(2, "toggleRight", true);
                break;
            case KeyEvent.VK_M:
                // player 2 fire
                System.out.println(this.getClass().getSimpleName() + " - keyPressed() - VK_M");
                this.app.getGameworld().setPlayerTankControl(2, "toggleFire", true);
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
            case KeyEvent.VK_W:
                // player 1 up
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_W");
                this.app.getGameworld().setPlayerTankControl(1, "toggleUp", false);
                break;
            case KeyEvent.VK_A:
                // player 1 left
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_A");
                this.app.getGameworld().setPlayerTankControl(1, "toggleLeft", false);
                break;
            case KeyEvent.VK_S:
                // player 1 down
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_S");
                this.app.getGameworld().setPlayerTankControl(1, "toggleDown", false);
                break;
            case KeyEvent.VK_D:
                // player 1 right
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_D");
                this.app.getGameworld().setPlayerTankControl(1, "toggleRight", false);
                break;
            case KeyEvent.VK_Z:
                // player 1 fire
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_Z");
                this.app.getGameworld().setPlayerTankControl(1, "toggleFire", false);
                break;
            case KeyEvent.VK_I:
                // player 2 up
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_I");
                this.app.getGameworld().setPlayerTankControl(2, "toggleUp", false);
                break;
            case KeyEvent.VK_J:
                // player 2 left
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_J");
                this.app.getGameworld().setPlayerTankControl(2, "toggleLeft", false);
                break;
            case KeyEvent.VK_K:
                // player 2 down
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_K");
                this.app.getGameworld().setPlayerTankControl(2, "toggleDown", false);
                break;
            case KeyEvent.VK_L:
                // player 2 right
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_L");
                this.app.getGameworld().setPlayerTankControl(2, "toggleRight", false);
                break;
            case KeyEvent.VK_M:
                // player 2 fire
                System.out.println(this.getClass().getSimpleName() + " - keyReleased() - VK_M");
                this.app.getGameworld().setPlayerTankControl(2, "toggleFire", false);
                break;
        }
    }
}
