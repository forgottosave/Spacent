package screen.world.tile;

import control.GamePanel;
import entity.Ability;
import general.Vector;

public class ReactorCore extends Tile{
    public static final String TILE_NAME = "Reactor Core";

    public ReactorCore(GamePanel gamePanel, Vector position) {
        super(gamePanel, TILE_NAME, position, true);
        tileSize = new Vector(5,6);
        neededEnterAbilities.add(Ability.MORPH);
        neededEnterAbilities.add(Ability.FLY);
    }

    @Override
    public void loadImagePaths() {
        pths_tile[0] = "/Tiles/ReactorChamber/Tile_ReactorCore.png";
        pths_tile[1] = "/Tiles/ReactorChamber/Tile_ReactorCore.png";
        pths_tile[2] = "/Tiles/ReactorChamber/Tile_ReactorCore.png";
        pths_tile[3] = "/Tiles/ReactorChamber/Tile_ReactorCore.png";
        pths_tile[4] = "/Tiles/ReactorChamber/Tile_ReactorCore.png";
        pths_tile[5] = "/Tiles/ReactorChamber/Tile_ReactorCore.png";
        pths_tile[6] = "/Tiles/ReactorChamber/Tile_ReactorCore.png";
        pths_tile[7] = "/Tiles/ReactorChamber/Tile_ReactorCore.png";
        pths_tile[8] = "/Tiles/ReactorChamber/Tile_ReactorCore.png";
    }
}
