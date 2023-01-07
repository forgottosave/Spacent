package screen.world;

import screen.world.tile.Void;
import control.GamePanel;
import entity.Ability;
import entity.Entity;
import entity.Infected;
import entity.NPC;
import general.Vector;
import general.Direction;
import projectile.Projectile;
import screen.world.tile.*;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;

public class World {
    // general info
    final GamePanel gamePanel;
    String worldFile;
    Tile[][] world;
    Vector transporterPos;
    // world stats
    boolean isDark;
    ArrayList<Entity> entities;
    ArrayList<Projectile> projectiles;
    Tile tileOnScreen; // stores tile that overlays screen with action
    final Dimension dimension = new Dimension(50,40);

    public World(GamePanel gamePanel, String worldPath) {
        this.gamePanel = gamePanel;
        this.worldFile = worldPath;
        world = new Tile[dimension.width][dimension.height];
        isDark = false;
        entities = new ArrayList<>();
        projectiles = new ArrayList<>();
        tileOnScreen = null;
        loadWorld(worldPath);
    }

    //// Entity related methods
    /**
     * Checks for the nearest entity in sight-range from a given List of entities
     * @param requester Entity to check from
     * @return Direction to the nearest entity
     */
    public Entity checkForNearestEntityFromList(Entity requester, List<Entity> entityList) {
        double range = requester.getSight() * gamePanel.tileSize;
        Entity entity = null;
        for (Entity e : entityList) {
            double rangeToEntity = entityDistance(requester,e);
            //System.out.println("Range "+range+" to entity "+rangeToEntity);
            if (rangeToEntity < range && e != requester) {
                range = rangeToEntity;
                entity = e;
            }
        }
        return entity;
    }
    /**
     * Get a list of entities which are in sight
     * @param requester Entity to check from
     * @return ArrayList of Entities
     */
    public ArrayList<Entity> getAllEntitiesInSight(Entity requester) {
        double range = requester.getSight() * gamePanel.tileSize;
        ArrayList<Entity> nearEntities = new ArrayList<>();
        for (Entity e : entities) {
            if (entityDistance(requester,e) < range) {
                nearEntities.add(e);
            }
        }
        return nearEntities;
    }
    /**
     * Calculates distance between entities
     * @param e1 Entity 1
     * @param e2 Entity 2
     * @return distance
     */
    public double entityDistance(Entity e1, Entity e2) {
        int xToEntity = e2.getPosition().x() - e1.getPosition().x();
        int yToEntity = e2.getPosition().y() - e1.getPosition().y();
        return Math.sqrt(xToEntity * xToEntity + yToEntity * yToEntity);
    }
    /**
     * Adds Entity to entities-list
     * @param e Entity to spawn
     */
    public void spawnEntity(Entity e) {
        entities.add(e);
    }

    //// Projectile related methods
    /**
     * Spawns projectile
     * @param p Projectile
     */
    public void spawnProjectile(Projectile p) {
        projectiles.add(p);
    }

    //// World building methods

    /**
     * Generates Tile according to symbol
     * @param c Symbol of tile
     * @param position Position of tile
     * @return newly generated Tile
     */
    public Tile getTile(char c, Vector position) {
        switch (c) {
            // general tiles
            case ' ' -> {return new Void(gamePanel, position);}
            case 'X' -> {return new Wall(gamePanel, position);}
            case '_' -> {return new Floor(gamePanel, position);}
            case 'T' -> {return new Transporter(gamePanel, position);}
            case '+' -> {return new Storage_Unit(gamePanel, position);}
            case '*' -> {return new Glass(gamePanel, position);}
            // reactor chamber tiles
            case '@' -> {return new ReactorCore(gamePanel, position);}
        }
        return new Void(gamePanel, position);
    }
    /**
     * Loads world from a world file
     * @param path path to world file
     */
    public void loadWorld(String path) {
        gamePanel.log("Loading " + path + "...");
        try {
            InputStream is = Objects.requireNonNull(getClass().getResourceAsStream(path));
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            String line = br.readLine();
            interpretHeader(line);
            for (int y = 0; y < dimension.height; y++) {
                line = br.readLine();
                for (int x = 0; x < dimension.width; x++) {
                    if (line == null || line.length() <= x)
                        world[x][y] = new Void(gamePanel, new Vector(x,y));
                    else if (world[x][y] == null){
                        world[x][y] = getTile(line.charAt(x), new Vector(x, y));
                        // if tile is transporter set player spawn-point
                        if (world[x][y].name.equals(Transporter.TILE_NAME) && !entities.contains(gamePanel.getPlayer())) {
                            transporterPos = new Vector(
                                    x * gamePanel.tileSize,
                                    y * gamePanel.tileSize + gamePanel.tileSize);
                            spawnEntity(gamePanel.getPlayer());
                        }
                        // if tile bigger than one tile: add Overwrite-Tiles on overlapping tiles
                        if (world[x][y].getTileSize().x() > 1 || world[x][y].getTileSize().y() > 1) {
                            for (int y2 = y; y2 < y + world[x][y].getTileSize().y() && y2 < world.length; y2++) {
                                for (int x2 = x; x2 < x + Objects.requireNonNull(world[x][y]).getTileSize().x() && x2 < world[0].length; x2++) {
                                    if (!(x2 == x && y2 == y)) {
                                        world[x2][y2] = new OverwriteTile(gamePanel, world[x][y], new Vector(x2, y2));
                                    }
                                }
                            }
                        }
                    }
                }
            }
            // load tile images in world depending on surrounding tiles
            for (int y = 0; y < dimension.height; y++) {
                for (int x = 0; x < dimension.width; x++) {
                    Tile tileToFix = world[x][y];
                    Tile[] surroundingTiles = new Tile[4];
                    Arrays.fill(surroundingTiles, tileToFix);
                    if (y > 0)
                        surroundingTiles[0] = world[x][y - 1];
                    if (y < dimension.height - 1)
                        surroundingTiles[2] = world[x][y + 1];
                    if (x > 0)
                        surroundingTiles[3] = world[x - 1][y];
                    if (x < dimension.width - 1)
                        surroundingTiles[1] = world[x + 1][y];
                    tileToFix.loadTile(surroundingTiles);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to load world, invalid world format. Player put in void");
        }
    }
    private void interpretHeader(String header) {
        String[] commands = header.split(";");
        for (String s : commands) {
            if (s.charAt(0) == 'i') {
                String[] coordinate = s.substring(2).split("-");
                spawnEntity(new Infected(gamePanel,
                        new Vector(Integer.parseInt(coordinate[0]) * gamePanel.tileSize,
                                Integer.parseInt(coordinate[1]) * gamePanel.tileSize)));
            }
            else if (s.charAt(0) == 'n') {
                String[] coordinate = s.substring(2).split("-");
                spawnEntity(new NPC(gamePanel,
                        new Vector(
                                Integer.parseInt(coordinate[0]) * gamePanel.tileSize,
                                Integer.parseInt(coordinate[1]) * gamePanel.tileSize),
                        coordinate[2]));
            }
            else {
                System.err.println("Error while parsing header: unknown command: " + s);
            }
        }
    }

    /**
     * tries interaction with block in front of player
     */
    public void interact() {
        Vector tileInFrontOfPlayer = Vector.add(
                gamePanel.getPlayer().getPosition(),
                Vector.scalarMul(gamePanel.getPlayer().getDirection().getVector(), gamePanel.tileSize));
        world[tileInFrontOfPlayer.x() / gamePanel.tileSize][tileInFrontOfPlayer.y() / gamePanel.tileSize]
                .interact(gamePanel.getPlayer());
    }

    /**
     * checks whether entity can enter the next block
     * @param e entity
     * @param d direction
     * @return true = can enter; false = can't enter
     */
    public boolean checkEntryPermission(Entity e, Direction d) {
        int x = (e.getPosition().x() + d.getVector().x() * e.getSpeed()) / gamePanel.tileSize;
        int y = (e.getPosition().y() + d.getVector().y() * e.getSpeed()) / gamePanel.tileSize;
        if (x < 0 || x >= dimension.width || y < 0 || y >= dimension.height)
            return false;
        //System.out.println("Entering: "+ tile.get(world[x][y]).name+"|| Needed: "+tile.get(world[x][y]).neededEnterAbilities+" || Got: "+e.getAbilities());
        for(Ability a : world[x][y].neededEnterAbilities) {
            if (!e.getAbilities().contains(a))
                return false;
        }
        return true;
    }

    public boolean checkLineOfSight(Vector start, Vector end, int allowance) {
        Vector direction = Vector.scalarMul(Vector.norm(Vector.sub(end, start)), 2 * gamePanel.scale);
        while (!Vector.areClose(start,end, (int) (2 * gamePanel.scale))) {
            if (!world[start.x() / gamePanel.tileSize][start.y() / gamePanel.tileSize].isTransparent()) {
                if (allowance-- < 0)
                    return false;
            }
            start = Vector.add(start,direction);
        }
        return true;
    }

    public void switchLight(boolean isDark) {
        this.isDark = isDark;
    }
    public boolean isDark() {
        return isDark;
    }

    public void update() {
        if (gamePanel.currentFrame() == 0) {
            double r = Math.random();
            if (r < 0.2)
                switchLight(false);
            if (r < 0.05)
                switchLight(true);
        }
        // update world components
        entities.forEach(Entity::update);
        projectiles.forEach(Projectile::update);
        entities.removeIf(entity -> entity.getHealth() <= 0);
        projectiles.removeIf(projectile -> projectile.getRange() <= 0);
    }

    /**
     * Renders the world onto a Graphic
     * @param g2 Graphic to draw on
     */
    public void draw(Graphics2D g2) {
        for (int row = 0; row < dimension.height; row++) {
            for (int column = 0; column < dimension.width; column++) {
                Tile tl = world[column][row];
                // stop images outside of screen to render
                if (tl == null || Vector.length(Vector.sub(gamePanel.getPlayer().getPosition(), new Vector(column * gamePanel.tileSize, row * gamePanel.tileSize))) > gamePanel.screenSize.width / 1.5)
                    continue;
                //if (checkLineOfSight(gamePanel.getPlayer().getPosition(), new Vector(column * gamePanel.tileSize, row * gamePanel.tileSize), 0))
                tl.draw(g2);
            }
        }
        // render world objects
        entities.forEach(e -> e.draw(g2));
        projectiles.forEach(p -> p.draw(g2));
    }

    @Override
    public String toString() {
        return "World{" +
                "world:" + worldFile +
                ";isDark:" + isDark +
                ";entities:" + entities +
                ";projectiles:" + projectiles +
                ";dimension:" + dimension +
                '}';
    }
    public static World toWorld(GamePanel gamePanel, String s) {
        // parse values
        String[] worldValues = s.substring(7).split(";");
        for (int i = 0; i < worldValues.length; i++) {
            worldValues[i] = worldValues[i].split(":")[1];
        }
        // create world
        World world = new World(gamePanel,worldValues[0]);
        // reset values TODO
        world.entities = new ArrayList<>();
        world.projectiles = new ArrayList<>();
        // set values TODO
        world.isDark = Boolean.parseBoolean(worldValues[1]);
        String[] enty = worldValues[2].substring(1,worldValues[2].length() - 1).split(", E");
        for (String ent : enty) {
            System.out.println("ADD ENTITY TO WORLD("+worldValues[0]+"):  "+Entity.toEntity(gamePanel,ent));
            world.entities.add(Entity.toEntity(gamePanel,ent));
        }
        // TODO world.projectiles = worldValues[3];
        //world.dimension = worldValues[4];
        return world;
    }

    //// Getter
    public ArrayList<Entity> getEntities() {
        return entities;
    }
    public Vector getTransporterPos() {
        return transporterPos;
    }
}
