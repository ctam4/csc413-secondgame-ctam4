package galacticmail;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.lang.Math;
import java.lang.IllegalArgumentException;
import java.lang.RuntimeException;
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
        this.app.putString("Gameworld/player", "GALACTIC MAIL CARRIER");
        this.app.putString("Gameworld/rule_1", "You are a Galactic Mail Carrier at Level " + this.app.getLevel() + ". You must earn $" + this.app.getLevel() * 5000 + " at this level to level up.");
        this.app.putString("Gameworld/rule_2", "You have 3 lives, and once it hits 0, you lose this level, and your level resets to 1. Lives resets at every level.");
        this.app.putString("Gameworld/rule_3", "Once you win at level 3, you are the overall winner - Super Galactic Mail Carrier.");
        this.app.putString("Gameworld/rule_4", "You can turn left/turn right/ignite by pressing ←/→/SPACEBAR. When rocket is in flight, you cannot steer until it approaches the moon.");
        this.app.putString("Gameworld/ok", "Okay");
        this.app.putString("Gameworld/congrats", "Congrats");
        this.app.putString("Gameworld/sorry", "Sorry");
        this.app.putString("Gameworld/is_overall_winner", "You are the all-time winner, and you get this title - Super Galactic Mail Carrier!");
        this.app.putString("Gameworld/is_level_winner", "You win this level, so you get promoted!!!");
        this.app.putString("Gameworld/is_loser", "You are the loser!!!");
        this.app.putString("Gameworld/name", "Now you are one of the Super Galactic Mail Carriers. What is your name, Captain?");
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
        addMoons((int) Math.round(this.app.getLevel() * 5 * 1.8));
        addLandedRocket(null);
        addAsteroids(this.app.getLevel() * 5);
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
        JOptionPane.showMessageDialog(this.frame, "<html><body>" + this.app.getString("Gameworld/rule_1") + "<br>" + this.app.getString("Gameworld/rule_2") + "<br>" + this.app.getString("Gameworld/rule_3") + "<br>" + this.app.getString("Gameworld/rule_4") + "</body></html>", this.app.getString("Gameworld/rules"), JOptionPane.INFORMATION_MESSAGE);
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
            angle = (int) Math.round(Math.random() * 72) * 5;
            this.panel.putGameMovableObject(new Moon(this.app, 1.0, this.app.getResource("Gameworld/moon"), x, y, 0, 0, angle, this.panel.getWidth(), this.panel.getHeight(), 1000, 2));
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
        // if gameMovableObject is NULL
        if (gameMovableObject == null) {
            // find first moon
            gameMovableObject = (GameMovableObject) this.panel.getGameMovableObjects().stream().filter(n -> n.getClass().getSimpleName().equals("Moon")).findFirst().orElse(null);
            // found no moon
            if (gameMovableObject == null) {
                throw new RuntimeException();
            }
            maxX = this.panel.getWidth();
            maxY = this.panel.getHeight();
            health = 300;
            pay = 0;
        }
        else {
            maxX = this.rocket.getMaxX();
            maxY = this.rocket.getMaxY();
            health = this.rocket.getHealth();
            pay = this.rocket.getPay();
        }
        x = gameMovableObject.getX();
        y = gameMovableObject.getY();
        angle = gameMovableObject.getAngle();
        this.rocket = new Rocket(this.app, 1.0, this.app.getResource("Gameworld/rocket_landed"), x, y, 0, 0, angle, maxX, maxY, true, false, health, pay);
        this.panel.putGameMovableObject(this.rocket);
    }

    public void addFlyingRocket() {
        // remove flying rocker from panel
        if (this.rocket != null) {
            this.panel.removeGameMovableObject(this.rocket);
        }
        // set flying rocket to panel
        this.rocket = new Rocket(this.app, 1.0, this.app.getResource("Gameworld/rocket_flying"), this.rocket.getX(), this.rocket.getY(), this.rocket.getVx(), this.rocket.getVy(), this.rocket.getAngle(), this.rocket.getMaxX(), this.rocket.getMaxY(), true, true, this.rocket.getHealth(), this.rocket.getPay());
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
            angle = (int) Math.round(Math.random() * 72) * 5;
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
        this.playerHealth.setLayout(new BorderLayout());
        this.playerHealth.setFont(font);
        this.playerHealth.setSize(new Dimension(player.getWidth(), player.getHeight()));
        this.playerHealth.setForeground(Color.WHITE);
        this.playerHealth.setHorizontalAlignment(JLabel.LEFT);
        this.playerHealth.setVerticalAlignment(JLabel.BOTTOM);
        this.playerHealth.setOpaque(false);
        this.playerHealth.setVisible(true);
        player.add(this.playerHealth);
        // set playerPay to panel
        this.playerPay = new JLabel();
        this.playerPay.setLayout(new BorderLayout());
        this.playerPay.setFont(font);
        this.playerPay.setSize(new Dimension(player.getWidth(), player.getHeight()));
        this.playerPay.setForeground(Color.WHITE);
        this.playerPay.setHorizontalAlignment(JLabel.RIGHT);
        this.playerPay.setVerticalAlignment(JLabel.BOTTOM);
        this.playerPay.setOpaque(false);
        this.playerPay.setVisible(true);
        this.playerHealth.add(this.playerPay);
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
        if (this.rocket.getPay() >= this.app.getLevel() * 5000) {
            // overall winner
            if (this.app.getLevel() == 3) {
                JOptionPane.showMessageDialog(this.frame, this.app.getString("Gameworld/is_overall_winner"), this.app.getString("Gameworld/congrats"), JOptionPane.INFORMATION_MESSAGE);
                // ask for name
                String name;
                do {
                    name = JOptionPane.showInputDialog(this.frame, this.app.getString("Gameworld/name"), this.app.getString("Gameworld/congrats"), JOptionPane.PLAIN_MESSAGE);
                } while (name.length() == 0 || this.app.getScoreboard().containsKey(name.toUpperCase()));
                // put name in scoreboard
                this.app.putScoreboard(name.toUpperCase(), this.rocket.getPay());
                // reset level
                this.app.setLevel(1);
                this.app.reset();
                this.app.getSplash().showScoreboard();
            }
            // level winner
            else {
                JOptionPane.showMessageDialog(this.frame, this.app.getString("Gameworld/is_level_winner"), this.app.getString("Gameworld/congrats"), JOptionPane.INFORMATION_MESSAGE);
                // level up
                this.app.setLevel(this.app.getLevel() + 1);
                this.app.restart();
            }
        }
        // loser
        else if (this.rocket.getHealth() == 0 || this.panel.getGameMovableObjects().stream().noneMatch(n -> n.getClass().getSimpleName().equals("Moon"))) {
            JOptionPane.showMessageDialog(this.frame, this.app.getString("Gameworld/is_loser"), this.app.getString("Gameworld/sorry"), JOptionPane.INFORMATION_MESSAGE);
            // reset level
            this.app.setLevel(1);
            this.app.reset();
        }
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(this.getClass().getSimpleName() + " - actionPerformed()");
        // add player info
        addPlayerInfo();
        // add random asteroids
        if (Math.random() < 0.01) {
            addAsteroids(this.app.getLevel() * 1);
        }
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
