package entity;

import control.GamePanel;
import general.Vector;
import general.Direction;
import item.Item;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

/**
 * Represents an Entity on the World, with its unique appearance and abilities
 */
public abstract class Entity {
    // general game stuff
    final GamePanel gamePanel;
    int attackTimer = 0;
    // general entity_2 qualities
    boolean alive;
    int health;
    int speed;
    final int sight;
    int attackRange;
    int interactionRange;
    int attackSpeed; //frames to wait for attack
    // entity state
    final List<Ability> abilities;
    final List<Item> items;
    Vector position;
    Direction direction;
    boolean moving;
    boolean isBeingAttacked;
    // image
    final BufferedImage[] img_front = new BufferedImage[3];
    final BufferedImage[] img_back = new BufferedImage[3];
    final BufferedImage[] img_left = new BufferedImage[3];
    final BufferedImage[] img_right = new BufferedImage[3];

    /**
     * Entity, part of the world.
     * @param gamePanel Reference to the Gamepanel
     * @param health Initial (and maximum) health points
     * @param speed Speed in Pixel / Frame
     * @param sight Sight range in Tiles
     * @param attackRange Attack range in Tiles
     * @param attackSpeed Seconds to wait between attacks
     * @param position Current Entity position
     */
    public Entity(GamePanel gamePanel, int health, int speed, int sight, double attackRange, int attackSpeed, Vector position) {
        this.gamePanel = gamePanel;
        this.alive = true;
        this.health = health;
        this.speed = speed;
        this.sight = sight;
        this.attackRange = (int) (attackRange * gamePanel.tileSize);
        this.attackSpeed = attackSpeed;
        this.interactionRange = gamePanel.tileSize;
        this.abilities = new LinkedList<>();
        this.items = new LinkedList<>();
        this.position = position;
        this.direction = Direction.DOWN;
        this.moving = false;
        this.isBeingAttacked = false;
        getImage();
    }

    /**
     * set all 12 standard images of creature
     */
    public abstract void getImage();

    /**
     * what should happen on death
     */
    public abstract void die();

    /**
     * update on frame
     */
    public void update() {
        moving = false;
        isBeingAttacked = false;
        attackTimer++;
        // update items
        for (Item item : items) {
            if (item != null)
                item.update();
        }
        update2();
    }
    /**
     * custom updates
     */
    public abstract void update2();

    /**
     * draw entity_2 in new frame
     * @param g2 2D-Graphic to draw in
     */
    public void draw(Graphics2D g2) {
        // Check if entity is visible
        // optional if (!gamePanel.getWorld().checkLineOfSight(gamePanel.getPlayer().getPosition(), this.position, 15))
        //    return;
        BufferedImage image = getDirectionalImage();
        g2.drawImage(image,
                gamePanel.getDrawingOffset(gamePanel.getPlayerDependantCoordinate(position).x()),
                gamePanel.getDrawingOffset(gamePanel.getPlayerDependantCoordinate(position).y()),
                gamePanel.tileSize,
                gamePanel.tileSize,
                null);
    }

    /**
     * Determines current image of entity depending on position
     * @return direction dependant image
     */
    BufferedImage getDirectionalImage() {
        BufferedImage image = null;
        switch (direction) {
            case UP -> {
                if (moving && (gamePanel.currentFrame() < gamePanel.FPS / 4 || (gamePanel.currentFrame() > gamePanel.FPS / 2 && gamePanel.currentFrame() < gamePanel.FPS - gamePanel.FPS / 4)))
                    image = img_back[1];
                else if (moving)
                    image = img_back[2];
                else
                    image = img_back[0];
            }
            case DOWN -> {
                if (moving && (gamePanel.currentFrame() < gamePanel.FPS / 4 || (gamePanel.currentFrame() > gamePanel.FPS / 2 && gamePanel.currentFrame() < gamePanel.FPS - gamePanel.FPS / 4)))
                    image = img_front[1];
                else if (moving)
                    image = img_front[2];
                else
                    image = img_front[0];
            }
            case LEFT, UPLE, DOLE -> {
                if (moving && (gamePanel.currentFrame() < gamePanel.FPS / 4 || (gamePanel.currentFrame() > gamePanel.FPS / 2 && gamePanel.currentFrame() < gamePanel.FPS - gamePanel.FPS / 4)))
                    image = img_left[1];
                else if (moving)
                    image = img_left[2];
                else
                    image = img_left[0];
            }
            case RIGHT, UPRI, DORI -> {
                if (moving && (gamePanel.currentFrame() < gamePanel.FPS / 4 || (gamePanel.currentFrame() > gamePanel.FPS / 2 && gamePanel.currentFrame() < gamePanel.FPS - gamePanel.FPS / 4)))
                    image = img_right[1];
                else if (moving)
                    image = img_right[2];
                else
                    image = img_right[0];
            }
        }
        return image;
    }

    /**
     * Moves Entity with its speed into a given direction
     * @param dir Movement-Direction
     */
    void moveEntity(Direction dir) {
        direction = dir;
        if (!gamePanel.allowedToEnterArea(this, dir))
            return;
        moving = true;
        switch (dir) {
            case UP -> position.shiftY(-speed);
            case DOWN -> position.shiftY(speed);
            case LEFT -> position.shiftX(-speed);
            case RIGHT -> position.shiftX(speed);
            case UPRI -> {
                position.shiftX((int) (speed * 0.9));
                position.shiftY((int) (-speed * 0.9));
            }
            case DORI -> {
                position.shiftX((int) (speed * 0.9));
                position.shiftY((int) (speed * 0.9));
            }
            case UPLE -> {
                position.shiftX((int) (-speed * 0.9));
                position.shiftY((int) (-speed * 0.9));
            }
            case DOLE -> {
                position.shiftX((int) (-speed * 0.9));
                position.shiftY((int) (speed * 0.9));
            }
        }
    }

    public void attack() {
        if (attackTimer < attackSpeed) {
            //System.out.println("Can't attack yet, you need to wait "+(attackSpeed-attackTimer)+" frames");
            return;
        }
        attackTimer = 0;
        List<Entity> attackableEntities = gamePanel.getWorld()
                .getAllEntitiesInSight(this)
                .stream()
                .filter(entity -> entity != this && Vector.length(Vector.vectorBetweenEntities(this, entity)) < getCurrentAttackRange())
                .toList();
        Entity closestEntity = gamePanel.getWorld().checkForNearestEntityFromList(this, attackableEntities);
        if (closestEntity != null) {
            closestEntity.changeHealth(-getDealingDamage());
        }
    }

    public int getCurrentAttackRange() {
        return attackRange;
    }
    public int getDealingDamage() {
        return 1;
    }

    // Change Values
    public void changeHealth(int value) {
        health += value;
        if (health <= 0) {
            alive = false;
            die();
        }
    }
    // Set Values
    public void setPosition(Vector position) {
        this.position = position;
    }
    public void setBeingAttacked(boolean beingAttacked) {
        isBeingAttacked = beingAttacked;
    }

    // Get Values
    public int getHealth() {
        return health;
    }
    public int getSpeed() {
        return speed;
    }
    public Vector getPosition() {
        return position;
    }
    public List<Ability> getAbilities() {
        return abilities;
    }
    public List<Item> getItems() {
        return items;
    }
    public int getSight() {
        return sight;
    }
    public boolean isBeingAttacked() {
        return isBeingAttacked;
    }
    public GamePanel getGamePanel() {
        return gamePanel;
    }
    public Direction getDirection() {
        return direction;
    }

    @Override
    public String toString() {
        return "Entity{" +
                "type=" + getClass().getName() +
                "-attackTimer=" + attackTimer +
                "-health=" + health +
                "-speed=" + speed +
                "-sight=" + sight +
                "-attackRange=" + attackRange +
                "-interactionRange=" + interactionRange +
                "-attackSpeed=" + attackSpeed +
                "-abilities=" + abilities +
                "-items=" + items +
                "-position=" + position +
                "-direction=" + direction +
                '}';
    }
    public static Entity toEntity(GamePanel gamePanel, String s) {
        // parse values
        String[] entityValues = s.substring(7).split("-");
        for (int i = 0; i < entityValues.length; i++) {
            entityValues[i] = entityValues[i].split("=")[1];
        }
        // create entity
        Entity entity;
        switch (entityValues[0]) {
            case "entity.Player" -> entity = new Player(gamePanel,gamePanel.getKeyHandler(),Vector.toVector(entityValues[10]));
            case "entity.Infected" -> entity = new Infected(gamePanel, Vector.toVector(entityValues[10]));
            case "entity.NPC" -> entity = new NPC(gamePanel,Vector.toVector(entityValues[10]), entityValues[12]);
            default -> {
                System.err.println("Encountered wrong entity type while loading game.");
                return null;
            }
        }
        // reset values TODO
        //entity.abilities = new ArrayList<>();
        //entity.items = new ArrayList<>();
        // set values TODO
        entity.attackTimer = Integer.parseInt(entityValues[1]);
        entity.health = Integer.parseInt(entityValues[2]);
        entity.speed = Integer.parseInt(entityValues[3]);
        entity.attackRange = Integer.parseInt(entityValues[4]);
        entity.interactionRange = Integer.parseInt(entityValues[5]);
        entity.attackSpeed = Integer.parseInt(entityValues[6]);
        //TODO entity.abilities = Integer.parseInt(entityValues[7]);
        //entity.items = Integer.parseInt(entityValues[8]);
        return entity;
    }
}
