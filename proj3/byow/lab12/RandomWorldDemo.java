package byow.lab12;

import byow.Core.Position;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world that contains RANDOM tiles.
 */
public class RandomWorldDemo {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;

    private static final long SEED = 2873123;
    private static final Random RANDOM = new Random(SEED);


    /**
     * @source: Prof Josh Hug
     * @param world
     * @param p
     * @param s
     * @param t
     */
    public static void addHexagon(TETile[][] world, Position p, int s, TETile t) {

        if (s < 2) {
            throw new IllegalArgumentException("Hexagon must be at least size 2.");
        }

        // hexagons have 2*s rows. this code iterates up from the bottom row,
        // which we call row 0.
        for (int yi = 0; yi < 2 * s; yi += 1) {
            int thisRowY = p.getY() + yi;

            int xRowStart = p.getX() + hexRowOffset(s, yi);
            Position rowStartP = new Position(xRowStart, thisRowY);

            int rowWidth = hexRowWidth(s, yi);

            addRow(world, rowStartP, rowWidth, t);

        }
    }


    /**
     * @source: Prof Josh Hug
     * @param i
     * @param s
     */
    public static int hexRowWidth(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - effectiveI;
        }

        return s + 2 * effectiveI;
    }

    /**
     * @source: Prof Josh Hug
     * @param i
     * @param s
     */
    public static int hexRowOffset(int s, int i) {
        int effectiveI = i;
        if (i >= s) {
            effectiveI = 2 * s - 1 - effectiveI;
        }
        return -effectiveI;
    }

    /**
     * Fills the given 2D array of tiles with RANDOM tiles.
     * @param tiles
     */
    public static void fillWithRandomTiles(TETile[][] tiles) {
        int height = tiles[0].length;
        int width = tiles.length;
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                tiles[x][y] = randomTile();
            }
        }
    }

    /**
     * @Source: Prof Josh Hug
     * @param world
     * @param p
     * @param width
     * @param t
     */
    public static void addRow(TETile[][] world, Position p, int width, TETile t) {
        for (int xi = 0; xi < width; xi += 1) {
            int xCoord = p.getX() + xi;
            int yCoord = p.getY();
            if (xCoord < 0) {
                break;
            }
            if (yCoord < 0) {
                break;
            }
            world[xCoord][yCoord] = TETile.colorVariant(t, 32, 32, 32, RANDOM);
        }
    }



    /** Picks a RANDOM tile with a 33% change of being
     *  a wall, 33% chance of being a flower, and 33%
     *  chance of being empty space.
     */
    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(3);
        switch (tileNum) {
            case 0: return Tileset.WALL;
            case 1: return Tileset.FLOWER;
            case 2: return Tileset.NOTHING;
            default: return Tileset.NOTHING;
        }
    }


    public Position bottomLeftNeighbor(Position p, int s) {
        int currX = p.getX();
        int currY = p.getY();
        int midRow = s - 1;
        int newX = (currX - 1) + (-1 * hexRowOffset(s, midRow)) - s;
        int newY = currY - s;
        Position pRight = new Position(newX, newY);
        return pRight;

    }

    public Position bottomRightNeighbor(Position p, int s) {
        int currX = p.getX();
        int currY = p.getY();
        int midRow = s - 1;
        int newX = (currX + s) + (-1 * hexRowOffset(s, midRow));
        int newY = currY - s;
        Position pLeft = new Position(newX, newY);
        return pLeft;

    }

    public static void drawRandomVerticalHexes(int N, TETile[][] world, Position p, int s) {
        TETile[] types = new TETile[11];
        types[0] = Tileset.TREE;
        types[1] = Tileset.AVATAR;
        types[2] = Tileset.FLOWER;
        types[3] = Tileset.FLOOR;
        types[4] = Tileset.WALL;
        types[5] = Tileset.LOCKED_DOOR;
        types[6] = Tileset.MOUNTAIN;
        types[7] = Tileset.SAND;
        types[8] = Tileset.WATER;
        types[9] = Tileset.UNLOCKED_DOOR;
        types[10] = Tileset.GRASS;

        Position tempP = new Position(p.getX(), p.getY());

        addHexagon(world, p, s, types[RANDOM.nextInt(10)]);

        for (int i = 1; i < N; i+= 1) {
            p = new Position(p.getX(), p.getY() - (s * 2));
            addHexagon(world, p, s, types[RANDOM.nextInt(10)]);
        }

        //return tempP;
    }


    public static void main(String[] args) {
        RandomWorldDemo rd = new RandomWorldDemo();
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] randomTiles = new TETile[WIDTH][HEIGHT];
        //fillWithRandomTiles(randomTiles);
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                randomTiles[x][y] = Tileset.NOTHING;
            }
        }
        int sizeOfHexagon = 3;
        int startingColumns = HEIGHT/ (sizeOfHexagon * 2);

        int startingX = (WIDTH / 2) - (sizeOfHexagon / 2);
        int startingY = HEIGHT - (sizeOfHexagon * 2);
        Position startingPoint = new Position(startingX, startingY);
        drawRandomVerticalHexes(startingColumns, randomTiles, startingPoint, sizeOfHexagon);

        int  widthOfHex = hexRowWidth(sizeOfHexagon, sizeOfHexagon);
        int numberOfLeft = (WIDTH - startingX) / widthOfHex;
        int numberOfRight = (WIDTH - ((WIDTH / 2) + (sizeOfHexagon / 2))) / widthOfHex;

        Position startingLeft = startingPoint;
        int tempL = startingColumns;

        for (int i = 0; i < numberOfLeft; i++) {
            startingLeft = rd.bottomLeftNeighbor(startingLeft, sizeOfHexagon);
            tempL = tempL - 1;
            drawRandomVerticalHexes(tempL, randomTiles, startingLeft, sizeOfHexagon);
        }

        Position startingRight = startingPoint;
        int tempR = startingColumns;

        for (int i = 0; i < numberOfRight; i++) {
            startingRight = rd.bottomRightNeighbor(startingRight, sizeOfHexagon);
            tempR = tempR - 1;
            drawRandomVerticalHexes(tempR, randomTiles, startingRight, sizeOfHexagon);
        }


        ter.renderFrame(randomTiles);
    }




}
