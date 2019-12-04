package tankgame;

import javax.swing.JFrame;

public abstract class JContainer {
    protected Launcher app;
    protected JFrame frame;

    protected JContainer(Launcher app) {
        // set app reference
        this.app = app;
    }

    protected void show() {
        this.frame.setVisible(true);
        this.frame.setState(JFrame.NORMAL);
        // focus keyboard to frame
        this.frame.requestFocus();
    }

    protected void hide() {
        this.frame.setState(JFrame.ICONIFIED);
        this.frame.setVisible(false);
        // remove focus keyboard to frame
        this.frame.transferFocus();
    }

    protected void close() {
        this.frame.setVisible(false);
        this.frame.dispose();
    }
}
