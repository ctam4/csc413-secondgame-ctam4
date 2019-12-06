package galacticmail;

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

import galacticmail.gameobject.*;

public class Gameworld extends JContainer implements ActionListener {
    private Timer timer;
    private JCustomPanel panel;

    private int level = 1;
    private Rocket rocket;
    private JLabel playerHealth, playerPay;

    public Gameworld(Launcher app) {
        super(app);
        // set timer
        this.timer = new Timer(100, this);
        // import resources using path
        this.app.putResource("Gameworld/moon", "/resources/Moon.gif");
        this.app.putResource("Gameworld/rocket_landed", "/resources/Landed.gif");
        this.app.putResource("Gameworld/rocket_flying", "/resources/Flying.gif");
        this.app.putResource("Gameworld/asteroid", "/resources/Asteroid.gif");
        // import strings
        this.app.putString("Gameworld/rules", "Rules");
        this.app.putString("Gameworld/player", "PLAYER");
        this.app.putString("Gameworld/rule_1", "You must earn $10000 to win the game.");
        this.app.putString("Gameworld/rule_2", "You have 3 lives, and when it hits 0, you lose.");
        this.app.putString("Gameworld/rule_3", "Player - Turn Left/Turn Right/Ignite: ←/→/SPACEBAR");
        this.app.putString("Gameworld/ok", "Okay");
        this.app.putString("Gameworld/congrats", "Congrats");
        this.app.putString("Gameworld/sorry", "Sorry");
        this.app.putString("Gameworld/is_overall_winner", "You are the all-time winner");
        this.app.putString("Gameworld/is_level_winner", "You are the level winner");
        this.app.putString("Gameworld/is_loser", "You are the loser");
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
        // create JCustomPanel object
        this.panel = new JCustomPanel();
        // set panel layout
        this.panel.setLayout(new BorderLayout());
        // set panel size
        this.panel.setSize(new Dimension(this.frame.getContentPane().getWidth(), this.frame.getContentPane().getHeight()));
        // set panel background color
        this.panel.setBackground(Color.BLACK);
        // set panel visible
        this.panel.setVisible(true);
        // set GameObjects
        addMoons(this.app.getLevel() * 10);
        addLandedRocket(null);
        addAsteroids(this.app.getLevel() * 20);
        // set player info bar
        playerInfoBar();
        // add panel to frame
        this.frame.add(this.panel);
        // bind GameworldKeyListener to frame
        this.frame.addKeyListener(new GameworldKeyListener(this.app));
        // start timer
        this.timer.start();
    }

    private void showRules() {
        JOptionPane.showMessageDialog(this.frame, "<html><body>" + this.app.getString("Gameworld/rule_1") + "<br>" + this.app.getString("Gameworld/rule_2") + "<br>" + this.app.getString("Gameworld/rule_3") + "</body></html>", this.app.getString("Gameworld/rules"), JOptionPane.INFORMATION_MESSAGE);
    }

    private void addMoons(int num) {
        if (num < 1) {
            throw new IllegalArgumentException();
        }
        int x, y, angle;
        // add moons to panel
        for (int i = 0; i < num; i++) {
            // generate x, y, angle
            x = (int) Math.round(Math.random() * this.panel.getWidth());
            y = (int) Math.round(Math.random() * this.panel.getHeight());
            angle = (int) Math.round(Math.random() * 360);
            this.panel.putGameMovableObject(new Moon(this.app, 1.0, this.app.getResource("Gameworld/moon"), x, y, 0, 0, angle, this.panel.getWidth(), this.panel.getHeight(), 1000, 5));
        }
    }

    public void addLandedRocket(GameObject gameObject) {
        GameMovableObject gameMovableObject = (GameMovableObject) gameObject;
        // remove landed rocker from panel
        if (this.rocket != null) {
            this.panel.removeGameMovableObject(this.rocket);
        }
        // set rocket to panel
        int x, y, angle, maxX, maxY, health, pay;
        // if gameObject is NULL
        if (this.rocket == null) {
            // find first moon
            /*gameMovableObject = this.panel.getGameMovableObjects().stream().filter(n -> n.getClass().getSimpleName().equals("Moon")).findFirst().get();
            if (gameMovableObject == null) {
                return;// TODO
            }*/
            x = 0;
            y = 0;
            angle = 180;
            maxX = this.panel.getWidth();
            maxY = this.panel.getHeight();
            health = 300;
            pay = 0;
        } else {
            /*int vx = 0, vy = 0;
            do {
                vx += (int) Math.round((gameMovableObject.getWidth() + 1) * Math.cos(Math.toRadians(gameMovableObject.getAngle())));
                vy += (int) Math.round((gameMovableObject.getHeight() + 1) * Math.sin(Math.toRadians(gameMovableObject.getAngle())));
            } while ((vx != 0 && Math.abs(vx) < gameMovableObject.getWidth() + 1) || (vy != 0 && Math.abs(vy) < gameMovableObject.getHeight() + 1));
            if (vx == 0) {
                if (gameMovableObject.getAngle() == 0 || gameMovableObject.getAngle() == 270) {
                    vy -= gameMovableObject.getHeight() + 1;
                } else {
                    vy += gameMovableObject.getHeight() + 1;
                }
            } else if (vy == 0) {
                if (gameMovableObject.getAngle() == 90 || gameMovableObject.getAngle() == 180) {
                    vx -= gameMovableObject.getWidth() + 1;
                } else {
                    vx += gameMovableObject.getWidth() + 1;
                }
            }
            x = gameMovableObject.getX() + vx;
            y = gameMovableObject.getY() + vy;*/
            x = gameMovableObject.getX();
            y = gameMovableObject.getY();
            angle = gameMovableObject.getAngle();
            maxX = this.rocket.getMaxX();
            maxY = this.rocket.getMaxY();
            health = this.rocket.getHealth();
            pay = this.rocket.getPay();
        }
        this.rocket = new Rocket(this.app, 1.0, this.app.getResource("Gameworld/rocket_landed"), x, y, 0, 0, angle, maxX, maxY, false, health, pay);
        this.panel.putGameMovableObject(this.rocket);
    }

    public void addFlyingRocket() {
        // remove flying rocker from panel
        if (this.rocket != null) {
            this.panel.removeGameMovableObject(this.rocket);
        }
        // set flying rocket to panel
        this.rocket = new Rocket(this.app, 1.0, this.app.getResource("Gameworld/rocket_flying"), this.rocket.getX(), this.rocket.getY(), this.rocket.getVx(), this.rocket.getVy(), this.rocket.getAngle(), this.rocket.getMaxX(), this.rocket.getMaxY(), true, this.rocket.getHealth(), this.rocket.getPay());
        this.panel.putGameMovableObject(this.rocket);
    }

    private void addAsteroids(int num) {
        if (num < 1) {
            throw new IllegalArgumentException();
        }
        int x, y, angle;
        // add moons to panel
        for (int i = 0; i < num; i++) {
            // generate x, y, vx, vy
            x = (int) Math.round(Math.random() * this.panel.getWidth());
            y = (int) Math.round(Math.random() * this.panel.getHeight());
            angle = (int) Math.round(Math.random() * 360);
            this.panel.putGameMovableObject(new Asteroid(this.app, 1.0, this.app.getResource("Gameworld/asteroid"), x, y, 0, 0, angle, this.panel.getWidth(), this.panel.getHeight(), 50));
        }
    }

    private void playerInfoBar() {
        Font font;
        // font for player
        font = new Font(Font.MONOSPACED, Font.BOLD, (int) Math.round(30 * this.app.getScale()));
        // set player text to panel
        JLabel player = new JLabel();
        player.setLayout(new BorderLayout());
        player.setText("< LEVEL " + this.app.getLevel() + "   " + this.app.getString("Gameworld/player") + " >");
        player.setFont(font);
        player.setForeground(Color.WHITE);
        player.setHorizontalAlignment(JLabel.CENTER);
        player.setVerticalAlignment(JLabel.BOTTOM);
        player.setOpaque(false);
        player.setVisible(true);
        this.panel.add(player);
        // font for playerHealth, playerPay
        font = new Font(Font.MONOSPACED, Font.PLAIN, (int) Math.round(30 * this.app.getScale()));
        // set playerHealth to panel
        this.playerHealth = new JLabel();
        this.playerHealth.setFont(font);
        this.playerHealth.setMaximumSize(new Dimension(player.getWidth() / 2, player.getHeight()));
        this.playerHealth.setForeground(Color.WHITE);
        this.playerHealth.setHorizontalAlignment(JLabel.LEFT);
        this.playerHealth.setVerticalAlignment(JLabel.BOTTOM);
        this.playerHealth.setOpaque(false);
        this.playerHealth.setVisible(true);
        player.add(this.playerHealth);
        // set playerPay to panel
        this.playerPay = new JLabel();
        this.playerPay.setFont(font);
        this.playerPay.setMaximumSize(new Dimension(player.getWidth() / 2, player.getHeight()));
        this.playerPay.setForeground(Color.WHITE);
        this.playerPay.setHorizontalAlignment(JLabel.RIGHT);
        this.playerPay.setVerticalAlignment(JLabel.BOTTOM);
        this.playerPay.setOpaque(false);
        this.playerPay.setVisible(true);
        player.add(this.playerPay);
    }

    private void addPlayerInfo() {
        this.playerHealth.setText("❤ " + ((this.rocket.getHealth() % 100.0 == 0) ? (this.rocket.getHealth() / 100 - 1) : (this.rocket.getHealth() / 100)));
        this.playerPay.setText(this.rocket.getPay() + " $");
    }

    public void setPlayerRocketControl(String action, boolean value) {
        try {
            if (!this.rocket.getIsFlying()) {
                this.rocket.getClass().getDeclaredMethod(action, boolean.class).invoke(this.rocket, value);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println(this.getClass().getSimpleName() + " - setPlayerRocketControl() - " + e);
        }
    }

    private void checkWinnerLoser() {
        // winner
        if (this.rocket.getPay() >= 10000) {
            // overall winner
            if (this.app.getLevel() == 5) {
                JOptionPane.showMessageDialog(this.frame, "<html><body>" + this.app.getString("Gameworld/is_winner") + "!!!</body></html>", this.app.getString("Gameworld/congrats"), JOptionPane.INFORMATION_MESSAGE);
                // reset level
                this.app.setLevel(1);
            }
            // level winner
            else {
                JOptionPane.showMessageDialog(this.frame, "<html><body>" + this.app.getString("Gameworld/is_winner") + "!!!</body></html>", this.app.getString("Gameworld/congrats"), JOptionPane.INFORMATION_MESSAGE);
                // level up
                this.app.setLevel(this.app.getLevel() + 1);
            }
            this.app.reset();
        }
        // loser
        else if (this.rocket.getHealth() == 0) {
            JOptionPane.showMessageDialog(this.frame, "<html><body>" + this.app.getString("Gameworld/is_loser") + "!!!</body></html>", this.app.getString("Gameworld/sorry"), JOptionPane.INFORMATION_MESSAGE);
            // reset level
            this.app.setLevel(1);
            this.app.reset();
        }
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(this.getClass().getSimpleName() + " - actionPerformed()");
        // add player info
        addPlayerInfo();
        // check winner/loser
        checkWinnerLoser();
    }

    @Override
    protected void close() {
        this.panel.timer.stop();
        this.timer.stop();
        super.close();
    }
}
