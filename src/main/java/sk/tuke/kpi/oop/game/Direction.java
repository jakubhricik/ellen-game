package sk.tuke.kpi.oop.game;

/**
 * enum of directions for player and for movement
 */
public enum Direction {
    NORTH(0, 1),
    NORTHEAST(1,1),
    EAST(1,0),
    SOUTHEAST(1,-1),
    SOUTH(0,-1),
    SOUTHWEST(-1,-1),
    WEST(-1,0),
    NORTHWEST(-1,1),
    NONE(0,0);

    private final int dx;
    private final int dy;

    Direction(int dx, int dy){
        this.dx = dx;
        this.dy = dy;
    }

    /**
    * returning float angle of direction
     */
    public float getAngle(){
        switch(this){
            case NORTHWEST: return 45f;
            case WEST: return 90f;
            case SOUTHWEST: return 135f;
            case SOUTH: return 180f;
            case SOUTHEAST: return 225f;
            case EAST: return 270f;
            case NORTHEAST: return 315f;
            default: return 0;
        }
    }

    public static Direction fromAngle(float angle){
        switch((int) angle){
            case 0  : return NORTH;
            case 45 : return NORTHWEST;
            case 90 : return WEST;
            case 135: return SOUTHWEST;
            case 180: return SOUTH;
            case 225: return SOUTHEAST;
            case 270: return EAST;
            case 315: return NORTHEAST;
            default: return null;
        }
    }

    /**
     * Combine this and other direction to the result
     *
     * it combines 2 directions and returning the combination of them
     * it combines by x and y
     *
     * @param other direction from Directions enum
     * @return direction combination by this and
     */
    public Direction combine(Direction other){
        if(this == other) return this;

        int dirX = this.getDx() + other.getDx();
        int dirY = this.getDy() + other.getDy();

        if(dirX < -1) dirX = -1;
        if(dirY < -1) dirY = -1;
        if(dirX > 1) dirX = 1;
        if(dirY > 1) dirY = 1;

        for( Direction direction : Direction.values()){
            if(dirX == direction.getDx() && dirY == direction.getDy())
                return direction;
        }
        return NONE;
    }

    //getters :
    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
