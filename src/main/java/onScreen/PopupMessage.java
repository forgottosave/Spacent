package onScreen;

import control.GamePanel;

import java.awt.*;

public class PopupMessage {
    final GamePanel gamePanel;
    int timer;
    final String msg;
    final Color msg_color;

    public PopupMessage(GamePanel gamePanel, String msg, Color color, double seconds) {
        this.gamePanel = gamePanel;
        this.msg_color = color;
        this.timer = (int) seconds * 60;
        this.msg = msg;
    }

    public void draw(Graphics2D g2) {
        timer--;
        if (timer > 0) {
            g2.setColor(msg_color);
            g2.setFont(new Font("MONOSPACED", Font.PLAIN, 40));
            g2.drawString(msg,
                    (int) (gamePanel.screenSize.width / 2 - gamePanel.screenSize.width * 0.005 * msg.length()),
                    (int) (gamePanel.screenSize.height / 2 - 0.2 * gamePanel.tileSize));
        }
    }
}
