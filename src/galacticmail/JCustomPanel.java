package tankgame;

import java.util.concurrent.CopyOnWriteArrayList;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Graphics;
import javax.swing.JPanel;
import javax.swing.Timer;

import tankgame.gameobject.GameObject;
import tankgame.gameobject.GameMovableObject;

public class JCustomPanel extends JPanel implements ActionListener {
    public Timer timer;

    private CopyOnWriteArrayList<GameObject> gameObjects;
    private CopyOnWriteArrayList<GameMovableObject> gameMovableObjects;

    public JCustomPanel() {
        super();
        // set timer
        this.timer = new Timer(100, this);
        // initialize gameObjects & gameMovableObjects
        this.gameObjects = new CopyOnWriteArrayList<>();
        this.gameMovableObjects = new CopyOnWriteArrayList<>();
        // start timer
        this.timer.start();
    }

    public CopyOnWriteArrayList getGameObjects() {
        return this.gameObjects;
    }

    public void putGameObject(GameObject gameObject) {
        this.gameObjects.add(gameObject);
    }

    public void removeGameObject(GameObject gameObject) {
        this.gameObjects.remove(gameObject);
    }

    public CopyOnWriteArrayList getGameMovableObjects() {
        return this.gameMovableObjects;
    }

    public void putGameMovableObject(GameMovableObject gameMovableObject) {
        this.gameMovableObjects.add(gameMovableObject);
    }

    public void removeGameMovableObject(GameMovableObject gameMovableObject) {
        this.gameMovableObjects.remove(gameMovableObject);
    }

    private boolean checkCollision(final GameObject gameObject_1, final GameObject gameObject_2) {
        boolean x, y;
        x = (gameObject_1.getX() >= gameObject_2.getX()
        && gameObject_1.getX() <= gameObject_2.getX() + gameObject_2.getWidth())
        || (gameObject_1.getX() + gameObject_1.getWidth() >= gameObject_2.getX()
        && gameObject_1.getX() + gameObject_1.getWidth() <= gameObject_2.getX() + gameObject_2.getWidth());
        y = (gameObject_1.getY() >= gameObject_2.getY()
        && gameObject_1.getY() <= gameObject_2.getY() + gameObject_2.getHeight())
        || (gameObject_1.getY() + gameObject_1.getHeight() >= gameObject_2.getY()
        && gameObject_1.getY() + gameObject_1.getHeight() <= gameObject_2.getY() + gameObject_2.getHeight());
        return (x && y);
    }

    private CopyOnWriteArrayList<GameObject> checkCollision(final GameMovableObject gameMovableObject) {
        CopyOnWriteArrayList<GameObject> collidedGameObjects = new CopyOnWriteArrayList<>();
        // check collision on gameObjects
        this.gameObjects.forEach((n) -> {
            if (checkCollision(gameMovableObject, n)) {
                collidedGameObjects.add(n);
            }
        });
        // check collision on gameMovableObjects
        this.gameMovableObjects.forEach((n) -> {
            if (!gameMovableObject.equals(n) && checkCollision(gameMovableObject, n)) {
                collidedGameObjects.add(n);
            }
        });
        return collidedGameObjects;
    }

    private void removeGameObjectGameMovableObject(GameObject gameObject) {
        switch (gameObject.getClass().getSuperclass().getSimpleName()) {
            case "GameObject":
                this.gameObjects.remove(gameObject);
                break;
            case "GameMovableObject":
                this.gameMovableObjects.remove(gameObject);
                break;
        }
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(this.getClass().getSimpleName() + " - actionPerformed()");
        this.gameMovableObjects.forEach((n) -> {
        GameObject gameObject;
            // update objects on gameMovableObjects
            n.update();
            // check collision on gameMovableObjects
            for (GameObject m : checkCollision(n)) {
                //System.out.println("x,y=" + m.getX() + "," + m.getY());
                //System.out.println("org x,y,x',y'=" + n.getX() + "," + n.getY() + "," + (n.getX() + n.getWidth()) + "," + (n.getY() + n.getHeight()));
                if (m.onCollision(n)) {
                    removeGameObjectGameMovableObject(m);
                }
                if (n.onCollision(m)) {
                    removeGameObjectGameMovableObject(n);
                }
            }
        });
        // repaint automatically
        repaint();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        // draw objects on gameObjects & gameMovableObjects
        this.gameObjects.forEach((n) -> n.draw(g));
        this.gameMovableObjects.forEach((n) -> n.draw(g));
    }
}
