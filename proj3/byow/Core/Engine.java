package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;

import java.awt.Color;

import java.io.FileReader;
import java.io.File;
import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Arrays;


import byow.TileEngine.Tileset;
import edu.princeton.cs.introcs.StdDraw;


public class Engine {
    TERenderer ter;
    boolean newWorld = false;
    TETile[][] map;
    //TETile[][] copy;

    Random ran;
    MapGenerator mg;
    Queue<Character> inp;
    boolean running = false;
    Charac cha;
    Long seed;
    String prev = ""; //stores the previous game as text file
    char avatype = 'e';
    TETile savedAvatile = Tileset.AVATAR2;


    /* Feel free to change the width and height. */
    int width;
    int height;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */
    public void interactWithKeyboard() {
        ter.initialize(width, height);
        mainMenu();
        TETile[][] world = new TETile[width][height];
        Charac ava = new Charac();
        fillMap(world);
        TETile charTile = Tileset.AVATAR2;
        boolean ifSaved = false;
        String taken = "";
        running = true;
        int loc = 0;
        while (running) {
            while (running) {
                if (StdDraw.hasNextKeyTyped()) {
                    char c = StdDraw.nextKeyTyped();
                    Character.toLowerCase(c);
                    loc += 1;
                    if (c == 'n') {
                        taken += 'n';
                        pressN();
                        StdDraw.pause(100);
                        String given = inputString();
                        taken += given;
                        TETile tt = changeAvatarMenu();
                        taken += avatype;
                        if (tt != null) {
                            charTile = tt;
                        }
                        break;
                    } else if (c == 'l') {
                        ifSaved = true;
                        System.out.println("loading " + loading());
                        String temp = loading();
                        System.out.println("temp " + temp);
                        if (temp.endsWith("q")) {
                            temp = temp.substring(0, temp.length() - 2);
                        }
                        taken += temp;
                        world = interactWithInputString(temp);

                        charTile = savedAvatile;
                        System.out.println("First taken in keyboard" + taken);

                        //call method for loading
                        break;
                    } else if (c == 'q') {

                        StdDraw.clear(Color.black);
                        StdDraw.show();

                        return;

                    }
                }
            }
            System.out.println("Taken before the seed is found " + taken);

            seed = findSeed(taken);
            System.out.println(seed);
            while (running) {
                StdDraw.clear(Color.BLACK);
                StdDraw.show();
                if (!ifSaved) {
                    mg = new MapGenerator(seed, width, height, world);
                    mg.drawActualRooms(world);
                    mg.drawTheHallways(world);
                }
                TETile [][] copy = new TETile[world.length][];
                for (int i = 0; i < world.length; i++) {
                    TETile[] t = world[i];
                    int aLength = t.length;
                    copy[i] = new TETile[aLength];
                    System.arraycopy(t, 0, copy[i], 0, aLength);
                }
                ter.renderFrame(world);

                if (!ifSaved) {
                    ava = new Charac(world, copy, mg, charTile);
                } else {
                    ava = cha;
                }
                ava.visualize();
                ter.renderFrame(world);
                TETile current = Tileset.NOTHING;
                String desc = current.description();
                while (true) { //interface for the HUD
                    if (StdDraw.mouseX() < world.length && StdDraw.mouseX() >= 0
                            && StdDraw.mouseY() < world[0].length && StdDraw.mouseY() >= 0) {
                        TETile hover = world[(int) StdDraw.mouseX()][(int) StdDraw.mouseY()];
                        if (!hover.equals(current)) {
                            current = hover;
                            desc = hover.description();
                        }
                        StdDraw.textLeft(0, height - 1, desc);
                        StdDraw.show();
                        ter.renderFrame(world);
                    }
                    if (StdDraw.hasNextKeyTyped()) {
                        char c = StdDraw.nextKeyTyped();
                        c = Character.toLowerCase(c);
                        taken += c;
                        if (c == 'p') {
                            if (mg.isRightLightOff()) {
                                mg.turnLightOn(world, 0);
                                ava.visualize();
                            } else {
                                mg.turnLightOff(world, 0);
                            }
                        }
                        if (c == 'o') {
                            if (mg.isLeftLightOff()) {
                                mg.turnLightOn(world, 1);
                                ava.visualize();
                            } else {
                                mg.turnLightOff(world, 1);
                            }
                        }
                        if (c == 'q' && taken.charAt(taken.length() - 2) == ':') {
                            map = world;
                            ifSaved = true;
                            this.seed = seed;
                            this.mg = mg;
                            cha = ava;
                            System.out.println("Second taken in keyboard" + taken);
                            saving(taken, true);
                            running = false;
                            prev = taken;
                            ter.renderFrame(world);
                            break;
                        }
                        ava.movement(c);
                        ter.renderFrame(world);
                    }
                }
            }
        }
        StdDraw.clear(Color.black);
        StdDraw.show();
        return;
    }

    public TETile changeAvatarMenu() {
        TETile t;
        StdDraw.clear(Color.BLACK);
        StdDraw.show();
        StdDraw.setPenColor(Color.PINK);
        StdDraw.text(width / 2, height * 0.90, "Choose Your Character!");
        StdDraw.show();
        StdDraw.pause(500);
        StdDraw.setPenColor(Color.green);
        StdDraw.text(width * 0.33, height * 0.33, "Green Girl: Press G");
        StdDraw.circle(width * 0.33, (height * 0.33) - 3, 2);

        StdDraw.setPenColor(new Color(255, 204, 51));
        StdDraw.text(width * 0.66, height * 0.33, "Golden Boy: Press O");
        StdDraw.circle(width * 0.66, (height * 0.33) - 3, 2);

        StdDraw.setPenColor(new Color(51, 153, 255));
        StdDraw.text(width * 0.66, height * 0.66, "Blue Bird: Press B");
        StdDraw.circle(width * 0.66, (height * 0.66) - 3, 2);


        StdDraw.setPenColor(Color.YELLOW);
        StdDraw.text(width * 0.33, height * 0.66, "Sunny Sun: Press S");
        StdDraw.circle(width * 0.33, (height * 0.66) - 3, 2);

        StdDraw.setPenColor(Color.LIGHT_GRAY);
        StdDraw.text(width * 0.5, height * 0.1, "I don't care "
                + "(will play with default): Press E");
        StdDraw.show();

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                Character.toLowerCase(c);
                if (c == 's') {
                    t = Tileset.SUN;
                    avatype = 's';
                    break;
                }
                if (c == 'g') {
                    t = Tileset.TREE;
                    avatype = 'g';

                    break;
                }
                if (c == 'o') {
                    t = Tileset.SAND;
                    avatype = 'o';
                    break;
                }
                if (c == 'b') {
                    t = Tileset.WATER;
                    avatype = 'b';
                    break;
                }
                if (c == 'e') {
                    t = null;
                    break;
                }
            }

        }
        StdDraw.clear(Color.BLACK);
        return t;
    }

    private void saving(String str, boolean exists) {
        File file = new File("./save_data.txt");
        try {
            if (!file.exists() || (exists && file.delete())) {
                file.createNewFile();
            }

            FileWriter filew = new FileWriter(file, true);
            filew.write(str);
            filew.close();
        } catch (IOException io) {
            io.printStackTrace();
        }

    }

    private String loading() {
        File file = new File("./save_data.txt");
        String str = "";
        try {
            FileReader fileReader = new FileReader(file);
            BufferedReader buffread = new BufferedReader(fileReader);
            str = buffread.readLine();
        } catch (IOException io) {
            io.printStackTrace();
        }
        return str;
    }


    public void mainMenu() {

        StdDraw.clear(Color.BLACK);
        StdDraw.show();
        StdDraw.setPenColor(Color.PINK);
        StdDraw.text(width / 2, height * 0.70, "The Lost Islander");
        StdDraw.text(width / 2, height * 0.4, "by Rajita & Mitali, CS61B w/ Prof Hug");

        StdDraw.show();
        StdDraw.pause(2000);
        StdDraw.clear(Color.BLACK);
        StdDraw.show();
        StdDraw.setPenColor(Color.green);
        StdDraw.text(width / 2, height * 0.666, "New Game : N");
        StdDraw.text(width / 2, height * 0.333, "Load Game : L");
        StdDraw.text(width / 2, height * 0.2, "Quit Game : Q");
        StdDraw.show();
        StdDraw.pause(1000);
    }

    public void pressN() { //switches to the page with the page which takes a seed
        newWorld = true;
        StdDraw.clear(Color.BLACK);
        StdDraw.show();
        StdDraw.text(width / 2, height / 2, "PlEaSe EnTeR a SeEd");
        StdDraw.show();
        StdDraw.pause(2000);
    }

    public Long findSeed(String s) {
        String toturn = "";
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (Character.isDigit(c)) {
                toturn += c;
            }
        }
        Long toreturn = Long.parseLong(toturn);
        return toreturn;
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        //ter.initialize(WIDTH, HEIGHT);
        boolean savedGame = false;
        String seedStr = "";
        TETile avatile = Tileset.AVATAR2;
        String taken = "";
        int index = 0;
        System.out.println(input);
        for (int i = 0; i < input.length(); i++) {
            index = i;
            char curr = input.charAt(i);
            curr = Character.toLowerCase(curr);
            taken += curr;
            if (curr == 'n' || curr == 'l') {
                newWorld = true;
            }
            if (curr == 's') {
                avatile = Tileset.SUN;
                savedAvatile = Tileset.SUN;
                avatype = 's';
            }
            if (curr == 'g') {
                avatile = Tileset.TREE;
                savedAvatile = Tileset.TREE;
                avatype = 'g';
            }
            if (curr == 'o') {
                avatile = Tileset.SAND;
                savedAvatile = Tileset.SAND;
                avatype = 'o';
            }
            if (curr == 'b') {
                avatile = Tileset.WATER;
                savedAvatile = Tileset.WATER;

                avatype = 'b';
            }
            if (curr == 's') {
                seedStr += 's';
                break;
            }
            /*
            if (curr == 'l') {
                String newOrders = "";
                taken += loading();
                int p = 0;
                for (p = 0; p < taken.length(); p++) {
                    char b = taken.charAt(p);
                    if (Character.isDigit(b)) {
                        seedStr = seedStr + Character.toString(b);
                    }
                    if (b == 's') {
                        break;
                    }
                }
                for (int pp = i + 1; pp < input.length(); pp++) {
                    if (Character.isLetter(input.charAt(pp))) {
                        newOrders += input.charAt(pp);
                    }
                }
                index = p;
                input = loading() + newOrders;
                break;
            }
            if (newWorld && (curr >= '0' && curr <= '9')) {
                seedStr = seedStr + Character.toString(curr);
            }
            */
        }

        //will error if fail is seedString is empty String
        long seedd = findSeed(taken);
        System.out.println(seedd);
        System.out.println(taken);

        TETile[][] world = new TETile[width][height];
        System.out.println(seedd);
        System.out.println(width);
        System.out.println(height);
        MapGenerator mgg = new MapGenerator(seedd, width, height, world);
        mgg.drawActualRooms(world);
        mgg.drawTheHallways(world);

        TETile [][] copy = new TETile[world.length][];
        for (int i = 0; i < world.length; i++) {
            TETile[] t = world[i];
            int aLength = t.length;
            copy[i] = new TETile[aLength];
            System.arraycopy(t, 0, copy[i], 0, aLength);
        }

        Charac ava = new Charac(world, copy, mgg, avatile);

        for (int k = index; k < input.length(); k++) {
            char curr = input.charAt(k);
            curr = Character.toLowerCase(curr);
            if (Character.isLetter(curr) && curr != 'q') {
                seedStr += curr;
            }
            if (curr == 'p') {
                if (mgg.isRightLightOff()) {
                    mgg.turnLightOn(world, 0);
                } else {
                    mgg.turnLightOff(world, 0);
                }
            }
            if (curr == 'o') {
                if (mgg.isLeftLightOff()) {
                    mgg.turnLightOn(world, 1);
                } else {
                    mgg.turnLightOff(world, 1);
                }
            }
            ava.movement(curr);
        }
        cha = ava;
        cha.world = world;
        mg = mgg;

        String ending = input.substring(Math.max(0, input.length() - 2));
        if (ending.equals(":q")) {
            //map = world;
            //this.seed = seed;
            //this.mg = mg;
            saving(seedStr, true);
            //map = world;
        }
        return world;
    }

    public Engine() {
        this.width = 80;
        this.height = 30;
        ter = new TERenderer();
        map = new TETile[width][height];
        fillMap(map);
        //copy = new TETile[width][height];
        //fillMap(copy);

        inp = new PriorityQueue<>();
        ran = new Random(100);
        cha = new Charac();
        seed = new Long(0);

    }

    public void drawframe(String s) {
        StdDraw.clear(Color.BLACK);
        StdDraw.show();
        ///StdDraw.setFont(myFont);
        //StdDraw.setPenColor(Color.PINK);
        StdDraw.text(width / 2, height / 2, s);
        StdDraw.show();
    }

    public void fillMap(TETile[][] mapp) {
        for (int i = 0; i < mapp.length; i++) {
            Arrays.fill(mapp[i], Tileset.NOTHING);
        }

    }

    public String inputString() {
        String str = "";
        StdDraw.pause(100);
        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char c = StdDraw.nextKeyTyped();
                if (c == 's') {
                    break;
                }
                str = str + Character.toString(c);
                drawframe(str);
                StdDraw.pause(100);
            }

        }
        return str;
    }
    public static void main(String[] args) {

        Engine e = new Engine();

        e.interactWithKeyboard();

        //testSameInputString();

    }

    private class Charac {
        int xPos;
        int yPos;
        TETile t;
        TETile[][] world;
        TETile[][] copy;
        MapGenerator mg;

        TETile currPos;
        ArrayList<Position> track;

        Charac() {
            this.world = null;
        }

        Charac(TETile[][] world, TETile[][] copy, MapGenerator mg, TETile t) {
            track = new ArrayList<>();
            this.world = world;
            this.mg = mg;
            this.copy = copy;
            this.t = t;
            this.xPos = ran.nextInt(width);
            this.yPos = ran.nextInt(height);
            currPos = copy[xPos][yPos];
            while (!copy[xPos][yPos].equals(Tileset.FLOOR)) {
                this.xPos = ran.nextInt(width);
                this.yPos = ran.nextInt(height);
                currPos = world[xPos][yPos];
            }
            visualize();
        }

        public void visualize() {
            world[xPos][yPos] = t;

            System.out.println("Initialized");

        }

        public void movement(char c) { //only works if the right buttons are pressed
            track.add(new Position(xPos, yPos));
            if (c == 'a') { //move left
                Position p2 = new Position(xPos - 1, yPos);
                if (copy[p2.getX()][p2.getY()].equals(Tileset.FLOOR)) {
                    if ((!mg.isInLeftLight(p2.getX(), p2.getY()) || !mg.isLeftLightOff())
                            && (!mg.isInRightLight(p2.getX(), p2.getY())
                            || !mg.isRightLightOff())) {
                        world[xPos - 1][yPos] = t;
                        world[xPos][yPos] = Tileset.FLOOR;
                    } else {
                        world[xPos - 1][yPos] = Tileset.NOTHING;
                        if (world[xPos][yPos].equals(Tileset.NOTHING)) {
                            world[xPos][yPos] = Tileset.NOTHING;
                        } else {
                            world[xPos][yPos] = Tileset.FLOOR;
                        }
                    }
                    track.add(p2);
                    xPos = xPos - 1;
                    currPos = world[xPos][yPos];
                    System.out.println("move left");
                }
            } else if (c == 'd') { //move right
                Position p2 = new Position(xPos + 1, yPos);
                if (copy[p2.getX()][p2.getY()].equals(Tileset.FLOOR)) {
                    if ((!mg.isInLeftLight(p2.getX(), p2.getY()) || !mg.isLeftLightOff())
                            && (!mg.isInRightLight(p2.getX(), p2.getY())
                            || !mg.isRightLightOff())) {
                        world[xPos][yPos] = Tileset.FLOOR;

                        world[xPos + 1][yPos] = t;
                    } else {
                        world[xPos + 1][yPos] = Tileset.NOTHING;
                        if (world[xPos][yPos].equals(Tileset.NOTHING)) {
                            world[xPos][yPos] = Tileset.NOTHING;

                        } else {
                            world[xPos][yPos] = Tileset.FLOOR;
                        }
                    }
                    track.add(p2);
                    xPos = xPos + 1;
                    currPos = world[xPos][yPos];
                    System.out.println("move right");
                }
            } else if (c == 's') { //move down
                Position p2 = new Position(xPos, yPos - 1);
                if (copy[p2.getX()][p2.getY()].equals(Tileset.FLOOR)) {
                    if ((!mg.isInLeftLight(p2.getX(), p2.getY()) || !mg.isLeftLightOff())
                            && (!mg.isInRightLight(p2.getX(), p2.getY())
                            || !mg.isRightLightOff())) {
                        world[xPos][yPos] = Tileset.FLOOR;
                        world[xPos][yPos - 1] = t;
                    } else {
                        world[xPos][yPos - 1] = Tileset.NOTHING;
                        if (world[xPos][yPos].equals(Tileset.NOTHING)) {
                            world[xPos][yPos] = Tileset.NOTHING;

                        } else {
                            world[xPos][yPos] = Tileset.FLOOR;
                        }
                    }
                    track.add(p2);
                    yPos = yPos - 1;
                    currPos = world[xPos][yPos];
                    System.out.println("move down");
                }
            } else if (c == 'w') { //move up
                Position p2 = new Position(xPos, yPos + 1);
                if (copy[p2.getX()][p2.getY()].equals(Tileset.FLOOR)) {
                    if ((!mg.isInLeftLight(p2.getX(), p2.getY()) || !mg.isLeftLightOff())
                            && (!mg.isInRightLight(p2.getX(), p2.getY())
                            || !mg.isRightLightOff())) {
                        world[xPos][yPos] = Tileset.FLOOR;
                        world[xPos][yPos + 1] = t;
                    } else {
                        world[xPos][yPos + 1] = Tileset.NOTHING;
                        if (world[xPos][yPos].equals(Tileset.NOTHING)) {
                            world[xPos][yPos] = Tileset.NOTHING;
                        } else {
                            world[xPos][yPos] = Tileset.FLOOR;
                        }
                    }
                    track.add(p2);
                    yPos = yPos + 1;
                    currPos = world[xPos][yPos];
                    System.out.println("move up");
                }
            }
        }
    }

}

