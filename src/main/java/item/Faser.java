package item;

import control.GamePanel;
import entity.Entity;
import general.Vector;
import projectile.Laser;

import javax.imageio.ImageIO;
import java.io.IOException;
import java.util.Objects;

public class Faser extends Item{

    final int maxDurability;
    final int regeneration = 60 * 60;
    int regTimer;

    public Faser(GamePanel gamePanel) {
        super(gamePanel,"Faser", 5, 3, "Faser out of energy");
        maxDurability = 5;
    }

    @Override
    public void getImage() {
        try {
            img_itembar = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/Faser/Faser_Right.png")));
            img_broken = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/Faser/Faser_OnScreen.png")));
            img_item[0] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/Faser/Faser_OnScreen_1.png")));
            img_item[1] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/Faser/Faser_OnScreen_2.png")));
            img_item[2] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/Faser/Faser_OnScreen_3.png")));
            img_item[3] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/Faser/Faser_OnScreen_4.png")));
            img_item[4] = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream("/Items/Faser/Faser_OnScreen_5.png")));
        } catch (IOException ignored) {
            System.err.println("Error: Corrupted or missing image file");
        }
    }

    @Override
    public void update() {
        super.update();
        if (regTimer++ >= regeneration && durability < maxDurability) {
            regTimer = 0;
            durability++;
        }
    }

    @Override
    public void use2(Entity user) {
        gamePanel.getWorld().spawnProjectile(
                new Laser(gamePanel,
                        Vector.add(user.getPosition(),new Vector(0,-5 * gamePanel.scale)),
                        user.getDirection().getVector(),
                        user));
    }
}
