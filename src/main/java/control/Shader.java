package control;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Objects;

public class Shader {
    final GamePanel gamePanel;
    BufferedImage shade;
    BufferedImage shade2;

    public Shader(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
        try {
            shade = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Shaders/Shade1.png")));
            shade2 = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Shaders/Shade2.png")));
        } catch (Exception ignored) {
            System.err.println("Failed to load shade");
        }
    }

    public void draw(Graphics2D g2) {
        if (gamePanel.getWorld().isDark())
            g2.drawImage(shade2, 0, 0, gamePanel.screenSize.width, gamePanel.screenSize.height,null);
        else
            g2.drawImage(shade, 0, 0, gamePanel.screenSize.width, gamePanel.screenSize.height,null);
    }
}
