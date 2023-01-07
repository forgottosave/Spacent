package screen.world.tile;

import control.GamePanel;
import entity.Ability;
import entity.Player;
import general.Vector;
import item.Item;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class Tile {
    final GamePanel gamePanel;
    // generals
    final Vector position;
    final public String name;
    Vector tileSize; // standard (1,1)
    // image related
    int image;
    final public String[] pths_tile; // path for all image files: one for each side plus a middle one
    public BufferedImage img_tile; // image of tile
    // world building
    final List<String> ignoredTilesAtFix;
    // attributes
    public List<Ability> neededEnterAbilities;
    final boolean isTransparent;
    final boolean isInteractedWith;
    // Item storing abilities
    boolean canHoldItem;
    Item itemOnTile;
    final Vector itemPositionOnTile;

    /**
     * One (gamepanel.tilesize)² sized Tile in a world.
     * Amongst other attributes, when construction a Tile needed entering abilities for entities can be added.
     * @param gamePanel Reference to the Gamepanel
     * @param name Identifying name of the Tile
     * @param position Position in world
     * @param isTransparent Can be seen through by entities
     */
    public Tile(GamePanel gamePanel, String name, Vector position, boolean isTransparent) {
        this.gamePanel = gamePanel;
        this.position = position;
        this.name = name;
        this.image = 0;
        this.pths_tile = new String[9];
        this.ignoredTilesAtFix = new ArrayList<>();
        ignoredTilesAtFix.add(name);
        ignoredTilesAtFix.add(Void.TILE_NAME);
        this.neededEnterAbilities = new ArrayList<>();
        this.isTransparent = isTransparent;
        this.isInteractedWith = false;
        this.tileSize = new Vector(1,1);
        this.canHoldItem = false;
        this.itemOnTile = null;
        this.itemPositionOnTile = new Vector(Math.random() * gamePanel.tileSize / 2, Math.random() * gamePanel.tileSize / 2);
        loadImagePaths();
    }

    /**
     * Loads all 9 images of the tile:
     * 0 = standard,
     * 1 = up,
     * 2 = corner up right,
     * 3 = right,
     * 4 = corner down right,
     * 5 = down,
     * 6 = corner down left,
     * 7 = left,
     * 8 = corner up left
     */
    public abstract void loadImagePaths();

    /**
     * Replaces image to use with the according one.
     * @param surroundingTiles Array of surrounding tiles (size 4)
     */
    public void loadTile(Tile[] surroundingTiles) {
        // determine image according to surrounding tiles
        if (!ignoredTilesAtFix.contains(surroundingTiles[0].name)) {
            if (!ignoredTilesAtFix.contains(surroundingTiles[1].name)) {
                image = 2;
            }
            else if (!ignoredTilesAtFix.contains(surroundingTiles[3].name)) {
                image = 8;
            }
            else {
                image = 1;
            }
        }
        else if (!ignoredTilesAtFix.contains(surroundingTiles[1].name)) {
            if (!ignoredTilesAtFix.contains(surroundingTiles[2].name)) {
                image = 4;
            }
            else {
                image = 3;
            }
        }
        else if (!ignoredTilesAtFix.contains(surroundingTiles[2].name)) {
            if (!ignoredTilesAtFix.contains(surroundingTiles[3].name)) {
                image = 6;
            }
            else {
                image = 5;
            }
        }
        else if (!ignoredTilesAtFix.contains(surroundingTiles[3].name)) {
            image = 7;
        }
        else {
            image = 0;
        }
        // load image
        try {
            if (pths_tile[image] != null)
                img_tile = ImageIO.read(Objects.requireNonNull(getClass().getResourceAsStream(pths_tile[image])));
        } catch (Exception ignored) {
            System.err.println("Failed to load image");
        }
    }

    /**
     * Starts interaction with tile.
     * Overwritten by child class if interaction is possible.
     * @param player Player to interact with
     */
    public void interact(Player player) {
    }

    public boolean isTransparent() {
        return isTransparent;
    }

    /**
     * Draws the Tile onto the graphic
     * @param g2 Graphic
     */
    public void draw(Graphics2D g2) {
        Vector c = gamePanel.getPlayerDependantCoordinate(position.x() * gamePanel.tileSize, position.y() * gamePanel.tileSize);
        // draw tile
        if (pths_tile[image] != null)
            g2.drawImage(img_tile,
                    c.x(),
                    c.y(),
                    gamePanel.tileSize * tileSize.x(),
                    gamePanel.tileSize * tileSize.y(),
                    null);
        // draw item if tile holds one
        if (itemOnTile != null)
            g2.drawImage(itemOnTile.getGraphic(),
                    c.x() + itemPositionOnTile.x(),
                    c.y() + itemPositionOnTile.y(),
                    gamePanel.tileSize / 2,
                    gamePanel.tileSize / 2,
                    null);
    }

    public Vector getTileSize() {
        return tileSize;
    }
}
