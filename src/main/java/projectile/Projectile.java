package projectile;

import control.GamePanel;
import entity.Entity;
import general.Direction;
import general.Vector;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Optional;

public abstract class Projectile {
    // general game stuff
    final GamePanel gamePanel;
    // general entity_2 qualities
    final public Entity owner;
    final int damage;
    int range;
    Vector position;
    final Vector normedDirection;
    // image
    final BufferedImage[] img_up = new BufferedImage[2];
    final BufferedImage[] img_down = new BufferedImage[2];
    final BufferedImage[] img_left = new BufferedImage[2];
    final BufferedImage[] img_right = new BufferedImage[2];
    final BufferedImage[] img_impact = new BufferedImage[2];

    public Projectile(GamePanel gamePanel, int damage, int range, int speed, Vector position, Vector direction, Entity owner) {
        this.gamePanel = gamePanel;
        this.damage = damage;
        this.range = (int) (range * gamePanel.scale);
        this.position = position;
        this.normedDirection = Vector.scalarMul(Vector.norm(direction), speed);
        this.owner = owner;
        setImage();
    }

    abstract void setImage();

    public void update() {
        range--;
        moveProjectile();
        Entity hitEntity = hit(owner);
        if (hitEntity != null && hitEntity != owner) {
            System.out.println("HIT " + hitEntity.getClass().getName());
            hitEntity.changeHealth(-damage);
            range = -1;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage image = null;
        switch (Direction.getDirectionFromVector(normedDirection)) {
            case UP -> {
                if ((gamePanel.currentFrame() < gamePanel.FPS / 4 || (gamePanel.currentFrame() > gamePanel.FPS / 2 && gamePanel.currentFrame() < gamePanel.FPS - gamePanel.FPS / 4)))
                    image = img_up[1];
                else
                    image = img_up[0];
            }
            case DOWN -> {
                if ((gamePanel.currentFrame() < gamePanel.FPS / 4 || (gamePanel.currentFrame() > gamePanel.FPS / 2 && gamePanel.currentFrame() < gamePanel.FPS - gamePanel.FPS / 4)))
                    image = img_down[1];
                else
                    image = img_down[0];
            }
            case LEFT, UPLE, DOLE -> {
                if ((gamePanel.currentFrame() < gamePanel.FPS / 4 || (gamePanel.currentFrame() > gamePanel.FPS / 2 && gamePanel.currentFrame() < gamePanel.FPS - gamePanel.FPS / 4)))
                    image = img_left[1];
                else
                    image = img_left[0];
            }
            case RIGHT, UPRI, DORI -> {
                if ((gamePanel.currentFrame() < gamePanel.FPS / 4 || (gamePanel.currentFrame() > gamePanel.FPS / 2 && gamePanel.currentFrame() < gamePanel.FPS - gamePanel.FPS / 4)))
                    image = img_right[1];
                else
                    image = img_right[0];
            }
        }
        g2.drawImage(image,
                gamePanel.getDrawingOffset(gamePanel.getPlayerDependantCoordinate(position).x()),
                gamePanel.getDrawingOffset(gamePanel.getPlayerDependantCoordinate(position).y()),
                gamePanel.tileSize,gamePanel.tileSize, null);
    }

    public Entity hit(Entity exception) {
        Optional<Entity> entity = gamePanel.getWorld().getEntities()
                .stream()
                .filter(e -> e != exception && Vector.areClose(e.getPosition(), this.position, (int) (5 * gamePanel.scale)))
                .findFirst();
        return entity.orElse(null);
    }

    public void moveProjectile() {
        position = Vector.add(position,normedDirection);
        // TODO stop on wall impact
    }

    public int getRange() {
        return range;
    }
}
