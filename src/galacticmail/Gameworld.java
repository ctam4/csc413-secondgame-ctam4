package tankgame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSplitPane;
import javax.swing.Timer;
import java.lang.Math;
import java.lang.IllegalArgumentException;
import java.lang.reflect.InvocationTargetException;

import tankgame.gameobject.*;

public class Gameworld extends JContainer implements ActionListener {
    private JFrame mini_frame;
    private Timer timer;
    private JCustomPanel panel_1, panel_2, mini_panel_1, mini_panel_2;
    private JLabel tank_1_health, tank_2_health;

    private Tank tank_1, tank_2, mini_tank_1, mini_tank_2;

    public Gameworld(Launcher app) {
        super(app);
        // set timer
        this.timer = new Timer(100, this);
        // import resources using path
        this.app.putResource("Gameworld/wall_1", "/resources/Wall1.gif");
        this.app.putResource("Gameworld/wall_2", "/resources/Wall2.gif");
        this.app.putResource("Gameworld/bullet", "/resources/Shell.gif");
        this.app.putResource("Gameworld/tank_1", "/resources/Tank1.gif");
        this.app.putResource("Gameworld/tank_2", "/resources/Tank2.gif");
        this.app.putResource("Gameworld/powerup", "/resources/Pickup.gif");
        // import strings
        this.app.putString("Gameworld/minimap", "Minimap");
        this.app.putString("Gameworld/rules", "Rules");
        this.app.putString("Gameworld/player_1", "PLAYER 1");
        this.app.putString("Gameworld/player_2", "PLAYER 2");
        this.app.putString("Gameworld/rule_1", "You must stay in your region. Your bullets can hit your enemy.");
        this.app.putString("Gameworld/rule_2", "You have 3 lives, and when it hits 0, you lose.");
        this.app.putString("Gameworld/rule_3", "Player # - Drive/Turn Left/Turn Right/Reverse/Fire: #1 - W/A/D/S/Z | #2 - I/J/L/K/M");
        this.app.putString("Gameworld/ok", "Okay");
        this.app.putString("Gameworld/congrats", "Congrats");
        this.app.putString("Gameworld/is_winner", "is the winner");
        // create JFrame object
        this.frame = new JFrame();
        // set frame title
        this.frame.setTitle(this.app.getString("name"));
        // set frame icon
        this.frame.setIconImage(this.app.getResource("icon"));
        // set frame close rules
        this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // set frame resizable
        this.frame.setResizable(false);
        // set frame size
        Dimension size;
        switch (this.app.getScale()) {
            case 2:
                size = new Dimension(1920, 1440);
                break;
            case 3:
                size = new Dimension(2160, 1620);
                break;
            default:
                size = new Dimension(1280, 960);
                break;
        }
        this.frame.getContentPane().setPreferredSize(size);
        this.frame.pack();
        System.out.println(this.getClass().getSimpleName() + " - Gameworld() - Set frame size: " + this.frame.getWidth() + "x" + this.frame.getHeight());
        // show game rules
        showRules();
        // create JCustomPanel objects
        this.panel_1 = new JCustomPanel();
        this.panel_2 = new JCustomPanel();
        // set panels layout
        this.panel_1.setLayout(new BorderLayout());
        this.panel_2.setLayout(new BorderLayout());
        // set panels size
        size = new Dimension((int) Math.round(this.frame.getContentPane().getWidth() * 0.5), this.frame.getContentPane().getHeight());
        this.panel_1.setSize(size);
        this.panel_2.setSize(size);
        // set panels background color
        this.panel_1.setBackground(Color.ORANGE);
        this.panel_2.setBackground(Color.PINK);
        // set panels visible
        this.panel_1.setVisible(true);
        this.panel_2.setVisible(true);
        // set GameObjects
        addWalls(this.panel_1, this.panel_2, 1.0);
        addPowerUps(this.panel_1, this.panel_2, 1.0);
        addTanks(this.panel_1, this.panel_2, this.tank_1, this.tank_2, 1.0);
        // set player info bar
        playerInfoBar();
        // create JSplitPane object
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.panel_1, this.panel_2);
        // remove splitPane divider
        splitPane.setDividerSize(0);
        // set splitPane equal width
        splitPane.setResizeWeight(0.5);
        // set splitPane visible
        splitPane.setVisible(true);
        // add splitPane to frame
        this.frame.add(splitPane);
        // bind GameworldKeyListener to frame
        this.frame.addKeyListener(new GameworldKeyListener(this.app));
        // start timer
        this.timer.start();
        // create JFrame object for mini
        this.mini_frame = new JFrame();
        // set frame title
        this.mini_frame.setTitle(this.app.getString("Gameworld/minimap"));
        // set frame icon
        this.mini_frame.setIconImage(this.app.getResource("icon"));
        // set frame close rules
        this.mini_frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        // set frame resizable
        this.mini_frame.setResizable(false);
        // set frame size
        this.mini_frame.getContentPane().setPreferredSize(new Dimension((int) Math.round(this.frame.getWidth() * 0.5), (int) Math.round(this.frame.getHeight() * 0.5)));
        this.mini_frame.pack();
        System.out.println(this.getClass().getSimpleName() + " - Gameworld() - Set mini frame size: " + this.mini_frame.getWidth() + "x" + this.mini_frame.getHeight());
        // create JCustomPanel objects for mini
        this.mini_panel_1 = new JCustomPanel();
        this.mini_panel_2 = new JCustomPanel();
        // set panels layout
        this.mini_panel_1.setLayout(new BorderLayout());
        this.mini_panel_2.setLayout(new BorderLayout());
        // set panels size
        size = new Dimension((int) Math.round(this.mini_frame.getContentPane().getWidth() * 0.5), this.mini_frame.getContentPane().getHeight());
        this.mini_panel_1.setSize(size);
        this.mini_panel_2.setSize(size);
        // set panels background color
        this.mini_panel_1.setBackground(Color.ORANGE);
        this.mini_panel_2.setBackground(Color.PINK);
        // set panels visible
        this.mini_panel_1.setVisible(true);
        this.mini_panel_2.setVisible(true);
        // set GameObjects
        addWalls(this.mini_panel_1, this.mini_panel_2, 0.5);
        addTanks(this.mini_panel_1, this.mini_panel_2, this.mini_tank_1, this.mini_tank_2, 0.5);
        // create JSplitPane object
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.mini_panel_1, this.mini_panel_2);
        // remove splitPane divider
        splitPane.setDividerSize(0);
        // set splitPane equal width
        splitPane.setResizeWeight(0.5);
        // set splitPane visible
        splitPane.setVisible(true);
        // add splitPane to frame
        this.mini_frame.add(splitPane);
        this.mini_frame.setVisible(true);
        this.mini_frame.setState(JFrame.NORMAL);
    }

    private void showRules() {
        JOptionPane.showMessageDialog(this.frame, "<html><body>" + this.app.getString("Gameworld/rule_1") + "<br>" + this.app.getString("Gameworld/rule_2") + "<br>" + this.app.getString("Gameworld/rule_3") + "</body></html>", this.app.getString("Gameworld/rules"), JOptionPane.INFORMATION_MESSAGE);
    }

    private void addWalls(JCustomPanel panel_1, JCustomPanel panel_2, double scale) {
        BufferedImage image;
        int unitX, unitY, maxUnitX, maxUnitY;
        // get unitX & unitY for unbreakable wall
        image = this.app.getResource("Gameworld/wall_1");
        unitX = (int) Math.round(this.app.getUnitSize() * this.app.getScale() * scale);
        unitY = (int) Math.round(this.app.getUnitSize() * this.app.getScale() * scale);
        // add unbreakable walls on border to panel_1
        maxUnitX = (int) Math.ceil(panel_1.getWidth() / unitX);
        maxUnitY = (int) Math.ceil(panel_1.getHeight() / unitY);
        for (int x = 0; x < maxUnitX; x++) {
            for (int y = 0; y < maxUnitY; y++) {
                if (x == 0 || y == 0 || y == maxUnitY - 1) {
                    panel_1.putGameObject(new Wall(this.app, scale, image, x * unitX, y * unitY, false));
                }
            }
        }
        // add unbreakable walls on border to panel_2
        maxUnitX = (int) Math.ceil(panel_2.getWidth() / unitX);
        maxUnitY = (int) Math.ceil(panel_2.getHeight() / unitY);
        for (int x = 0; x < maxUnitX; x++) {
            for (int y = 0; y < maxUnitY; y++) {
                if (x == maxUnitX - 1 || y == 0 || y == maxUnitY - 1) {
                    panel_2.putGameObject(new Wall(this.app, scale, image, x * unitX, y * unitY, false));
                }
            }
        }
        // get unitX & unitY for breakable wall
        image = this.app.getResource("Gameworld/wall_2");
        unitX = (int) Math.round(this.app.getUnitSize() * this.app.getScale() * scale);
        unitY = (int) Math.round(this.app.getUnitSize() * this.app.getScale() * scale);
        // add breakable walls on border to panel_1
        maxUnitX = (int) Math.ceil(panel_1.getWidth() / unitX);
        maxUnitY = (int) Math.ceil(panel_1.getHeight() / unitY);
        for (int x = 0; x < maxUnitX; x++) {
            for (int y = 0; y < maxUnitY; y++) {
                if (x == maxUnitX - 1 && !(y == 0 || y == maxUnitY - 1)) {
                    panel_1.putGameObject(new Wall(this.app, scale, image, x * unitX, y * unitY, true));
                }
            }
        }
        // add breakable walls on border to panel_2
        maxUnitX = (int) Math.ceil(panel_2.getWidth() / unitX);
        maxUnitY = (int) Math.ceil(panel_2.getHeight() / unitY);
        for (int x = 0; x < maxUnitX; x++) {
            for (int y = 0; y < maxUnitY; y++) {
                if (x == 0 && !(y == 0 || y == maxUnitY - 1)) {
                    panel_2.putGameObject(new Wall(this.app, scale, image, x * unitX, y * unitY, true));
                }
            }
        }
    }

    private void addPowerUps(JCustomPanel panel_1, JCustomPanel panel_2, double scale) {
        BufferedImage image = this.app.getResource("Gameworld/powerup");
        for (int i = 0; i < 3; i++) {
            // add powerups to panel_1
            panel_1.putGameObject(new PowerUp(this.app, scale, image, (int) Math.round(Math.random() * (panel_1.getWidth() * 0.8) + panel_1.getWidth() * 0.1), (int) Math.round(Math.random() * (panel_1.getHeight() * 0.8) + panel_1.getWidth() * 0.1), "health"));
            // add powerups to panel_2
            panel_2.putGameObject(new PowerUp(this.app, scale, image, (int) Math.round(Math.random() * (panel_2.getWidth() * 0.8) + panel_2.getWidth() * 0.1), (int) Math.round(Math.random() * (panel_2.getHeight() * 0.8) + panel_2.getWidth() * 0.1), "health"));
        }
    }

    private void addTanks(JCustomPanel panel_1, JCustomPanel panel_2, Tank tank_1, Tank tank_2, double scale) {
        Tank tank;
        // set tank_1 to panel_1
        tank = new Tank(this.app, scale, this.app.getResource("Gameworld/tank_1"), panel_1.getWidth() / 2, panel_1.getHeight() / 2, 0, 0, 0, panel_1.getWidth(), panel_1.getHeight());
        if (panel_1.equals(this.panel_1)) {
            this.tank_1 = tank;
            panel_1.putGameMovableObject(this.tank_1);
        } else if (panel_1.equals(this.mini_panel_1)) {
            this.mini_tank_1 = tank;
            panel_1.putGameMovableObject(this.mini_tank_1);
        }
        // set tank_2 to panel_2
        tank = new Tank(this.app, scale, this.app.getResource("Gameworld/tank_2"), panel_2.getWidth() / 2, panel_2.getHeight() / 2, 0, 0, 180, panel_2.getWidth(), panel_2.getHeight());
        if (panel_2.equals(this.panel_2)) {
            this.tank_2 = tank;
            panel_2.putGameMovableObject(this.tank_2);
        } else if (panel_2.equals(this.mini_panel_2)) {
            this.mini_tank_2 = tank;
            panel_2.putGameMovableObject(this.mini_tank_2);
        }
    }

    private void playerInfoBar() {
        Font font;
        // font for player_1 & player_2
        font = new Font(Font.MONOSPACED, Font.BOLD, (int) Math.round(30 * this.app.getScale()));
        // set player_1 text to panel_1
        JLabel player_1 = new JLabel();
        player_1.setLayout(new BorderLayout());
        player_1.setText("< " + this.app.getString("Gameworld/player_1") + " >");
        player_1.setFont(font);
        player_1.setForeground(Color.WHITE);
        player_1.setHorizontalAlignment(JLabel.CENTER);
        player_1.setVerticalAlignment(JLabel.BOTTOM);
        player_1.setOpaque(false);
        player_1.setVisible(true);
        this.panel_1.add(player_1);
        // set player_2 text to panel_2
        JLabel player_2 = new JLabel();
        player_2.setLayout(new BorderLayout());
        player_2.setText("< " + this.app.getString("Gameworld/player_2") + " >");
        player_2.setFont(font);
        player_2.setForeground(Color.WHITE);
        player_2.setHorizontalAlignment(JLabel.CENTER);
        player_2.setVerticalAlignment(JLabel.BOTTOM);
        player_2.setOpaque(false);
        player_2.setVisible(true);
        this.panel_2.add(player_2);
        font = new Font(Font.MONOSPACED, Font.PLAIN, (int) Math.round(30 * this.app.getScale()));
        // set tank_1_health to panel_1
        this.tank_1_health = new JLabel();
        this.tank_1_health.setFont(font);
        this.tank_1_health.setForeground(Color.WHITE);
        this.tank_1_health.setHorizontalAlignment(JLabel.LEFT);
        this.tank_1_health.setVerticalAlignment(JLabel.BOTTOM);
        this.tank_1_health.setOpaque(false);
        this.tank_1_health.setVisible(true);
        player_1.add(this.tank_1_health);
        // set tank_2_health to panel_2
        this.tank_2_health = new JLabel();
        this.tank_2_health.setFont(font);
        this.tank_2_health.setForeground(Color.WHITE);
        this.tank_2_health.setHorizontalAlignment(JLabel.RIGHT);
        this.tank_2_health.setVerticalAlignment(JLabel.BOTTOM);
        this.tank_2_health.setOpaque(false);
        this.tank_2_health.setVisible(true);
        player_2.add(this.tank_2_health);
    }

    public void addPlayerTankHealthInfo() {
        this.tank_1_health.setText("❤ " + ((this.tank_1.getHealth() % 100.0 == 0) ? (this.tank_1.getHealth() / 100 - 1) : (this.tank_1.getHealth() / 100)));
        this.tank_2_health.setText(((this.tank_2.getHealth() % 100.0 == 0) ? (this.tank_2.getHealth() / 100 - 1) : (this.tank_2.getHealth() / 100)) + " ❤");
    }

    public void setPlayerTankControl(int player, String action, boolean value) {
        switch (player) {
            case 1:
                try {
                    this.tank_1.getClass().getDeclaredMethod(action, boolean.class).invoke(this.tank_1, value);
                    this.mini_tank_1.getClass().getDeclaredMethod(action, boolean.class).invoke(this.mini_tank_1, value);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    System.out.println(this.getClass().getSimpleName() + " - setPlayerTankControl() - " + e);
                }
                break;
            case 2:
                try {
                    this.tank_2.getClass().getDeclaredMethod(action, boolean.class).invoke(this.tank_2, value);
                    this.mini_tank_2.getClass().getDeclaredMethod(action, boolean.class).invoke(this.mini_tank_2, value);
                } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                    System.out.println(this.getClass().getSimpleName() + " - setPlayerTankControl() - " + e);
                }
                break;
            default:
                throw new IllegalArgumentException();
        }
    }

    private void addPlayerBullet(JCustomPanel panel_1, JCustomPanel panel_2, Tank tank, double scale, int x, int y, int vx, int vy, int angle, int damage) {
        if (tank.equals(this.tank_1) || tank.equals(this.mini_tank_1)) {
            System.out.println(this.getClass().getSimpleName() + " - addPlayerBullet() - Player 1 - x,y,vx,vy,angle: " + x + "," + y + "," + vx + "," + vy + "," + angle);
            panel_1.putGameMovableObject(new Bullet(this.app, scale, this.app.getResource("Gameworld/bullet"), x, y, vx, vy, angle, panel_1.getWidth(), panel_1.getHeight(), damage));
        } else if (tank.equals(this.tank_2) || tank.equals(this.mini_tank_2)) {
            System.out.println(this.getClass().getSimpleName() + " - addPlayerBullet() - Player 2 - x,y,vx,vy,angle: " + x + "," + y + "," + vx + "," + vy + "," + angle);
            panel_2.putGameMovableObject(new Bullet(this.app, scale, this.app.getResource("Gameworld/bullet"), x, y, vx, vy, angle, panel_2.getWidth(), panel_2.getHeight(), damage));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void addPlayerBullet(Tank tank, int x, int y, int vx, int vy, int angle, int damage) {
        if (tank.equals(this.tank_1) || tank.equals(this.tank_2)) {
            addPlayerBullet(this.panel_1, this.panel_2, tank, 1.0, x, y, vx, vy, angle, damage);
        } else if (tank.equals(this.mini_tank_1) || tank.equals(this.mini_tank_2)) {
            addPlayerBullet(this.mini_panel_1, this.mini_panel_2, tank, 0.5, x, y, vx, vy, angle, damage);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void movePlayerBullet(JCustomPanel panel_1, JCustomPanel panel_2, Bullet bullet, double scale, int x, int y, int vx, int vy, int angle, int damage) {
        // move from panel_1 to panel_2
        if (panel_1.getGameMovableObjects().contains(bullet)) {
            System.out.println(this.getClass().getSimpleName() + " - movePlayerBullet() - Player 1 - x,y,vx,vy,angle: " + x + "," + y + "," + vx + "," + vy + "," + angle);
            // remove bullet from panel_1
            panel_1.removeGameMovableObject(bullet);
            panel_2.putGameMovableObject(new Bullet(this.app, scale, this.app.getResource("Gameworld/bullet"), x - panel_2.getWidth(), y, vx, vy, angle, panel_2.getWidth(), panel_1.getHeight(), damage));
        }
        // move from panel_2 to panel_1
        else if (panel_2.getGameMovableObjects().contains(bullet)) {
            System.out.println(this.getClass().getSimpleName() + " - movePlayerBullet() - Player 2 - x,y,vx,vy,angle: " + x + "," + y + "," + vx + "," + vy + "," + angle);
            // remove bullet from panel_2
            panel_2.removeGameMovableObject(bullet);
            panel_1.putGameMovableObject(new Bullet(this.app, scale, this.app.getResource("Gameworld/bullet"), x + panel_1.getWidth(), y, vx, vy, angle, panel_1.getWidth(), panel_1.getHeight(), damage));
        } else {
            throw new IllegalArgumentException();
        }
    }

    public void movePlayerBullet(Bullet bullet, int x, int y, int vx, int vy, int angle, int damage) {
        if (this.panel_1.getGameMovableObjects().contains(bullet) || this.panel_2.getGameMovableObjects().contains(bullet)) {
            movePlayerBullet(this.panel_1, this.panel_2, bullet, 1.0, x, y, vx, vy, angle, damage);
        } else if (this.mini_panel_1.getGameMovableObjects().contains(bullet) || this.mini_panel_2.getGameMovableObjects().contains(bullet)) {
            movePlayerBullet(this.mini_panel_1, this.mini_panel_2, bullet, 0.5, x, y, vx, vy, angle, damage);
        } else {
            throw new IllegalArgumentException();
        }
    }

    private void checkWinner() {
        int player = 0;
        if (this.tank_2.getHealth() == 0) {
            player = 1;
        } else if (this.tank_1.getHealth() == 0) {
            player = 2;
        }
        if (player != 0) {
            switch (player) {
                case 1:
                    JOptionPane.showMessageDialog(this.frame, "<html><body>" + this.app.getString("Gameworld/player_1") + " " + this.app.getString("Gameworld/is_winner") + "!!!</body></html>", this.app.getString("Gameworld/congrats"), JOptionPane.INFORMATION_MESSAGE);
                    break;
                case 2:
                    JOptionPane.showMessageDialog(this.frame, "<html><body>" + this.app.getString("Gameworld/player_2") + " " + this.app.getString("Gameworld/is_winner") + "!!!</body></html>", this.app.getString("Gameworld/congrats"), JOptionPane.INFORMATION_MESSAGE);
                    break;
            }
            this.app.reset();
        }
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(this.getClass().getSimpleName() + " - actionPerformed()");
        // add player tank health info
        addPlayerTankHealthInfo();
        // check winner
        checkWinner();
    }

    @Override
    protected void close() {
        this.panel_1.timer.stop();
        this.panel_2.timer.stop();
        this.mini_panel_1.timer.stop();
        this.mini_panel_2.timer.stop();
        this.mini_frame.setVisible(false);
        this.mini_frame.dispose();
        this.timer.stop();
        super.close();
    }
}
