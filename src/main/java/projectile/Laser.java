package projectile;

import control.GamePanel;
import entity.Entity;
import general.Vector;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class Laser extends Projectile{

    public Laser(GamePanel gamePanel, Vector position, Vector direction, Entity owner) {
        super(gamePanel, 3, 30, 10, position, direction, owner);
    }

    @Override
    void setImage() {
        try {
            img_up[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Projectiles/Faser/Faser-Laser-Particle.png")));
            img_up[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Projectiles/Faser/Faser-Laser-Particle-2.png")));
            img_down[0] = img_up[0];
            img_down[1] = img_up[1];
            img_left[0] = img_up[0];
            img_left[1] = img_up[1];
            img_right[0] = img_up[0];
            img_right[1] = img_up[1];
            img_impact[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Projectiles/Faser/Faser-Laser-Particle-Impact.png")));
            img_impact[1] = img_impact[0];
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }
}
