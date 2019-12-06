package galacticmail;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.awt.Dimension;
import java.awt.Font;
import java.util.stream.Collectors;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.BorderFactory;

public class Splash extends JContainer {
    private JPanel panel;

    public Splash(Launcher app) {
        super(app);
        // import resources using path
        this.app.putResource("Splash/title", "/resources/Title.gif");
        // import strings
        this.app.putString("Splash/instruction_1", "[ENTER] to start/continue");
        this.app.putString("Splash/instruction_2", "[SPACEBAR] to show scoreboard");
        this.app.putString("Splash/instruction_3", "[ESC] to quit");
        this.app.putString("Splash/scoreboard", "Scoreboard");
        this.app.putString("Splash/empty_scoreboard", "No one received the Super Galactic Mail Carrier title yet.");
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
        this.frame.setSize(size);
        System.out.println(this.getClass().getSimpleName() + " - Splash() - Set frame size: " + this.frame.getWidth() + "x" + this.frame.getHeight());
        // create JPanel object
        this.panel = new JPanel();
        // set panel size
        this.panel.setSize(this.frame.getSize());
        // set panel padding
        int paddingWidth = (int) Math.round(this.panel.getWidth() * 0.2);
        int paddingHeight = (int) Math.round(this.panel.getHeight() * 0.15);
        this.panel.setBorder(BorderFactory.createEmptyBorder(paddingHeight, paddingWidth, paddingHeight, paddingWidth));
        // set panel visible
        this.panel.setVisible(true);
        // add panel to frame
        this.frame.setContentPane(panel);
        // set title to panel
        JLabel title = new JLabel(new ImageIcon(this.app.getResource("Splash/title")));
        title.setPreferredSize(new Dimension(this.panel.getWidth(), (int) Math.round(this.panel.getHeight() * 0.4)));
        title.setHorizontalAlignment(JLabel.CENTER);
        title.setVerticalAlignment(JLabel.TOP);
        this.panel.add(title);
        // set instruction to panel
        JLabel instruction = new JLabel();
        instruction.setText("<html><body style='text-align: center'>" + this.app.getString("Splash/instruction_1") + "<br>" + this.app.getString("Splash/instruction_2") + "<br>" + this.app.getString("Splash/instruction_3") + "</body></html>");
        instruction.setFont(new Font(Font.MONOSPACED, Font.PLAIN, (int) Math.round(30 * this.app.getScale())));
        instruction.setPreferredSize(new Dimension(this.panel.getWidth(), (int) Math.round(this.panel.getHeight() * 0.25)));
        instruction.setHorizontalAlignment(JLabel.CENTER);
        instruction.setVerticalAlignment(JLabel.BOTTOM);
        this.panel.add(instruction);
        // bind SplashKeyListener to frame
        this.frame.addKeyListener(new SplashKeyListener(this.app));
    }

    public void showScoreboard() {
        // sort scoreboard
        List<Map.Entry<String, Integer>> scoreboard = new LinkedList<Map.Entry<String, Integer>>(this.app.getScoreboard().entrySet());
        Collections.sort(scoreboard, (a, b) -> (a.getValue()).compareTo(b.getValue()));
        // display result
        String message;
        if (!scoreboard.isEmpty()) {
            message = "<html><body>" + scoreboard.stream().map(e -> e.getKey() + ": $" + e.getValue()).collect(Collectors.joining("<br>")) + "</body></html>";
        }
        else {
            message = this.app.getString("Splash/empty_scoreboard");
        }
        JOptionPane.showMessageDialog(this.frame, message, this.app.getString("Splash/scoreboard"), JOptionPane.INFORMATION_MESSAGE);
    }
}
