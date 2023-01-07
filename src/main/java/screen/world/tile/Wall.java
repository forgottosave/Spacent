package screen.world.tile;

import control.GamePanel;
import entity.Ability;
import general.Vector;

public class Wall extends Tile {
    public static final String TILE_NAME = "Wall";

    public Wall(GamePanel gamePanel, Vector position) {
        super(gamePanel, TILE_NAME, position, false);
        neededEnterAbilities.add(Ability.MORPH);
        ignoredTilesAtFix.add(Transporter.TILE_NAME);
    }

    @Override
    public void loadImagePaths() {
        pths_tile[0] = "/Tiles/Wall/Wall_Inside.png";
        pths_tile[1] = "/Tiles/Wall/Wall_Down.png";
        pths_tile[2] = "/Tiles/Wall/Wall_Left.png";
        pths_tile[3] = "/Tiles/Wall/Wall_Left.png";
        pths_tile[4] = "/Tiles/Wall/Wall_Up.png";
        if (position.x() % 5 * 16 == 0) {
            pths_tile[5] = "/Tiles/Wall/Wall_Up_Pillar.png";
        }
        else {
            pths_tile[5] = "/Tiles/Wall/Wall_Up.png";
        }
        pths_tile[6] = "/Tiles/Wall/Wall_Up.png";
        pths_tile[7] = "/Tiles/Wall/Wall_Right.png";
        pths_tile[8] = "/Tiles/Wall/Wall_Right.png";
    }
}
