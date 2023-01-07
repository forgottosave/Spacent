package screen.world.tile;

import control.GamePanel;
import general.Vector;

public class Floor extends Tile {
    public static final String TILE_NAME = "Floor";

    public Floor(GamePanel gamePanel, Vector position) {
        super(gamePanel, TILE_NAME, position,true);
        canHoldItem = true;
    }

    @Override
    public void loadImagePaths() {
        pths_tile[0] = "/Tiles/Floor.png";
        pths_tile[1] = "/Tiles/Floor.png";
        pths_tile[2] = "/Tiles/Floor.png";
        pths_tile[3] = "/Tiles/Floor.png";
        pths_tile[4] = "/Tiles/Floor.png";
        pths_tile[5] = "/Tiles/Floor.png";
        pths_tile[6] = "/Tiles/Floor.png";
        pths_tile[7] = "/Tiles/Floor.png";
        pths_tile[8] = "/Tiles/Floor.png";
    }
}