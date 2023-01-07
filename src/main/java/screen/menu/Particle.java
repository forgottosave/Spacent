package screen.menu;

import general.Vector;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

public class Particle {
    BufferedImage particle;
    final int scale;
    final int speed;
    final Vector position;

    public Particle(Vector position, int scale) {
        this.scale = scale / 2;
        speed = (int) (Math.random() * 5 + 5);
        this.position = position;
        try {
            particle = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/MainMenu/Particle.png")));
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }

    public void draw(Graphics2D g2) {
        g2.drawImage(particle,
                position.x(),
                position.y(),
                particle.getWidth() * scale,
                particle.getHeight() * scale,
                null);
        position.shiftX(-speed);
    }

    public Vector getPosition() {
        return position;
    }
}
