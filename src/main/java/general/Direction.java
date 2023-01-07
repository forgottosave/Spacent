package general;

public enum Direction {
    NO      (0,0),
    UP      (0,-1),
    UPRI    (1,-1),
    RIGHT   (1,0),
    DORI    (1,1),
    DOWN    (0,1),
    DOLE    (-1,1),
    LEFT    (-1,0),
    UPLE    (-1,-1);

    private final Vector vector;
    Direction(int x, int y) {
        vector = new Vector(x,y);
    }

    public Vector getVector() {
        return vector;
    }

    public static Direction getOppositeDirection(Direction dir) {
        Direction opposite = Direction.NO;
        switch (dir) {
            case UP -> opposite = DOWN;
            case DOWN -> opposite = UP;
            case RIGHT -> opposite = LEFT;
            case LEFT -> opposite = RIGHT;
            case UPRI -> opposite = DOLE;
            case UPLE -> opposite = DORI;
            case DORI -> opposite = UPLE;
            case DOLE -> opposite = UPRI;
        }
        return opposite;
    }

    public static Direction getCombinedDirection(Direction d1, Direction d2) {
        if (d1 == d2)
            return d1;
        if (d1 == Direction.NO)
            return d2;
        if (d2 == Direction.NO)
            return d1;
        if (d1 == Direction.UPRI || d1 == Direction.DORI || d1 == Direction.UPLE || d1 == Direction.DOLE)
            return d1;
        if (d2 == Direction.UPRI || d2 == Direction.DORI || d2 == Direction.UPLE || d2 == Direction.DOLE)
            return d2;
        switch (d1) {
            case UP -> {switch (d2) {
                case DOWN -> {
                    return Direction.NO;
                }
                case LEFT -> {
                    return Direction.UPLE;
                }
                case RIGHT -> {
                    return Direction.UPRI;
                }
            }}
            case DOWN -> {switch (d2) {
                case UP -> {
                    return Direction.NO;
                }
                case LEFT -> {
                    return Direction.DOLE;
                }
                case RIGHT -> {
                    return Direction.DORI;
                }
            }}
            case LEFT -> {switch (d2) {
                case UP -> {
                    return Direction.UPLE;
                }
                case DOWN -> {
                    return Direction.DOLE;
                }
                case RIGHT -> {
                    return Direction.NO;
                }
            }}
            case RIGHT -> {switch (d2) {
                case UP -> {
                    return Direction.UPRI;
                }
                case DOWN -> {
                    return Direction.DORI;
                }
                case LEFT -> {
                    return Direction.NO;
                }
            }}
        }
        return Direction.NO;
    }

    public static Direction getDirectionFromVector(Vector vector) {
        Direction d1 = Direction.NO;
        Direction d2 = Direction.NO;
        int absX = Math.abs(vector.x());
        int absY = Math.abs(vector.y());
        // check left-right margin
        if (vector.x() >= 0.5 * absY)
            d1 = Direction.RIGHT;
        else if (vector.x() <= -0.5 * absY)
            d1 = Direction.LEFT;
        // check up-down margin
        if (vector.y() >= 0.5 * absX)
            d2 = Direction.DOWN;
        else if (vector.y() <= -0.5 * absX)
            d2 = Direction.UP;
        // combine
        //System.out.println("Vector " + vector + " =>" + d1 + "+" + d2 + " => " + getCombinedDirection(d1,d2));
        return getCombinedDirection(d1,d2);
    }
}
