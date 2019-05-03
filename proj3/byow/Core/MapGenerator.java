package byow.Core;
import byow.Delaunay.WeightedGraph;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import byow.Delaunay.MST;


import java.util.LinkedList;
import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;
//import java.util.HashMap;
import java.util.Iterator;
import byow.TileEngine.TERenderer;
import java.util.Arrays;




public class MapGenerator {

    private TETile[][] wworld;
    private TETile[][] original;
    private int width;
    private int height;
    private int ROOMS = 150;
    private int RADIUS = 7;
    private LinkedList[][] myCellArray;
    private Random rand;
    //private static final Random RANDOM = new Random();
    private ArrayList<Room> rms;
    private ArrayList<Position> centers;
    private ArrayList<Room> actualRooms;
    private ArrayList<Room> smallRooms;
    private boolean leftLightOff;
    private boolean rightLightOff;




    private Room[] rooms;



    public MapGenerator(long seed, int width, int height, TETile[][] world) {

        for (int i = 0; i < world.length; i++) {
            Arrays.fill(world[i], Tileset.NOTHING);
        }
        this.wworld = world;
        rand = new Random(seed);
        rms = new ArrayList<Room>();
        actualRooms = new ArrayList<>();
        smallRooms = new ArrayList<>();
        //this.world = world;
        myCellArray = new LinkedList[width][height];
        this.width = width;
        this.height = height;
        rms = generateRooms(ROOMS);
        leftLightOff = false;
        rightLightOff = false;

    }

    public void drawActualRooms(TETile[][] worldd) {
        for (Room r : actualRooms) {
            drawRoom(worldd, r);
        }


    }

    public void drawTheHallways(TETile[][] worldd) {
        HashSet<WeightedEdge> hallways = edgesForHallways(actualRooms);


        for (WeightedEdge hallway : hallways) {
            System.out.println(hallway.from().position + " to " + hallway.to().position);
            drawHallway(hallway, worldd);
        }

        verify(worldd);

    }

    public boolean isInLeftLight(int ii, int jj) {
        for (int i = 0; i < wworld.length / 3; i++) {
            for (int j = wworld[0].length - wworld[0].length / 3; j < wworld[0].length; j++) {
                if (ii == i && jj == j) {
                    return true;
                }
            }
        }

        int length = wworld.length / 3;
        for (int i = wworld.length / 3; i >= 0; i--) {
            int mdpoint = (length / 2);
            //int gg = (world.length - world.length / 4);
            int ll = Math.abs(i - mdpoint);
            int lll = (length / 2) + 2 - ll;
            for (int j = wworld[0].length - wworld[0].length / 4;
                 j > wworld[0].length - (wworld[0].length / 4) - lll; j--) {
                if (ii == i && jj == j) {
                    return true;
                }
            }
        }
        length = wworld[0].length / 4;
        for (int i = wworld[0].length - 1; i > wworld[0].length - wworld[0].length / 4; i--) {
            int mdpoint =  (wworld[0].length - wworld[0].length / 4) + (length / 2);
            int ll = Math.abs(i - mdpoint);
            int lll = (length / 2) + 2 - ll;
            for (int j =  wworld.length / 3; j < wworld.length / 3 + lll; j++) {
                if (ii == j && jj == i) {
                    return true;
                }
            }
        }
        return false;

    }

    public boolean isInRightLight(int ii, int jj) {

        for (int i = wworld.length - wworld.length / 3; i < wworld.length; i++) {
            for (int j = wworld[0].length - wworld[0].length / 3; j < wworld[0].length; j++) {
                if (ii == i && jj == j) {
                    return true;
                }
            }
        }

        int length = wworld.length / 3;
        for (int i = wworld.length - 1; i > wworld.length - wworld.length / 3; i--) {
            int mdpoint =  (wworld.length - wworld.length / 3) + (length / 2);
            //int gg = (world.length - world.length / 4);
            int ll = Math.abs(i - mdpoint);
            int lll = (length / 2) + 2 - ll;
            for (int j = wworld[0].length - wworld[0].length / 4;
                 j > wworld[0].length - (wworld[0].length / 4) - lll; j--) {
                if (ii == i && jj == j) {
                    return true;
                }
            }
        }

        length = wworld[0].length / 4;
        for (int i = wworld[0].length - 1; i > wworld[0].length - wworld[0].length / 4; i--) {
            int mdpoint =  (wworld[0].length - wworld[0].length / 4) + (length / 2);
            int ll = Math.abs(i - mdpoint);
            int lll = (length / 2) + 2 - ll;
            for (int j = wworld.length - wworld.length / 3;
                 j > wworld.length - (wworld.length / 3) - lll; j--) {
                if (ii == j && jj == i) {
                    return true;
                }

            }
        }
        return false;

    }

    public void turnLightOff(TETile[][] world, int index) {


        //right
        if (index == 0) {

            for (int i = world.length - world.length / 3; i < world.length; i++) {
                for (int j = world[0].length - world[0].length / 3; j < world[0].length; j++) {
                    world[i][j] = Tileset.NOTHING;
                }
            }

            int length = world.length / 3;
            for (int i = world.length - 1; i > world.length - world.length / 3; i--) {
                int mdpoint =  (world.length - world.length / 3) + (length / 2);
                //int gg = (world.length - world.length / 4);
                int ll = Math.abs(i - mdpoint);
                int lll = (length / 2) + 2 - ll;
                for (int j = world[0].length - world[0].length / 4;
                     j > world[0].length - (world[0].length / 4) - lll; j--) {
                    world[i][j] = Tileset.NOTHING;
                }
            }

            length = world[0].length / 4;
            for (int i = world[0].length - 1; i > world[0].length - world[0].length / 4; i--) {
                int mdpoint =  (world[0].length - world[0].length / 4) + (length / 2);
                int ll = Math.abs(i - mdpoint);
                int lll = (length / 2) + 2 - ll;
                for (int j = world.length - world.length / 3;
                     j > world.length - (world.length / 3) - lll; j--) {
                    world[j][i] = Tileset.NOTHING;
                }
            }

            world[world.length  - 1][world[0].length - 1] = Tileset.SAND;
            rightLightOff = true;


        }

        //left
        if (index == 1) {
            for (int i = 0; i < world.length / 3; i++) {
                for (int j = world[0].length - world[0].length / 3; j < world[0].length; j++) {
                    world[i][j] = Tileset.NOTHING;
                }
            }

            int length = world.length / 3;
            for (int i = world.length / 3; i >= 0; i--) {
                int mdpoint = (length / 2);
                //int gg = (world.length - world.length / 4);
                int ll = Math.abs(i - mdpoint);
                int lll = (length / 2) + 2 - ll;
                for (int j = world[0].length - world[0].length / 4;
                     j > world[0].length - (world[0].length / 4) - lll; j--) {
                    world[i][j] = Tileset.NOTHING;
                }
            }

            length = world[0].length / 4;
            for (int i = world[0].length - 1; i > world[0].length - world[0].length / 4; i--) {
                int mdpoint =  (world[0].length - world[0].length / 4) + (length / 2);
                int ll = Math.abs(i - mdpoint);
                int lll = (length / 2) + 2 - ll;
                for (int j =  world.length / 3; j < world.length / 3 + lll; j++) {
                    world[j][i] = Tileset.NOTHING;
                }
            }

            world[0][world[0].length - 1] = Tileset.SAND;
            leftLightOff = true;

        }
    }

    public void turnLightOn(TETile[][] world, int index) {
        if (index == 0) {


            for (int i = world.length - world.length / 3; i < world.length; i++) {
                for (int j = world[0].length - world[0].length / 3; j < world[0].length; j++) {
                    world[i][j] = this.original[i][j];
                }
            }

            int length = world.length / 3;
            for (int i = world.length - 1; i > world.length - world.length / 3; i--) {
                int mdpoint =  (world.length - world.length / 3) + (length / 2);
                //int gg = (world.length - world.length / 4);
                int ll = Math.abs(i - mdpoint);
                int lll = (length / 2) + 2 - ll;
                for (int j = world[0].length - world[0].length / 4;
                     j > world[0].length - (world[0].length / 4) - lll; j--) {
                    world[i][j] = this.original[i][j];
                }
            }

            length = world[0].length / 4;
            for (int i = world[0].length - 1; i > world[0].length - world[0].length / 4; i--) {
                int mdpoint =  (world[0].length - world[0].length / 4) + (length / 2);
                int ll = Math.abs(i - mdpoint);
                int lll = (length / 2) + 2 - ll;
                for (int j = world.length - world.length / 3;
                     j > world.length - (world.length / 3) - lll; j--) {
                    world[j][i] = this.original[j][i];
                }
            }

            world[world.length  - 1][world[0].length - 1] = Tileset.TREE;
            rightLightOff = false;

        }

        if (index == 1) {
            for (int i = 0; i < world.length / 3; i++) {
                for (int j = world[0].length - world[0].length / 3; j < world[0].length; j++) {
                    world[i][j] = this.original[i][j];
                }
            }

            int length = world.length / 3;
            for (int i = world.length / 3; i >= 0; i--) {
                int mdpoint = (length / 2);
                //int gg = (world.length - world.length / 4);
                int ll = Math.abs(i - mdpoint);
                int lll = (length / 2) + 2 - ll;
                for (int j = world[0].length - world[0].length / 4;
                     j > world[0].length - (world[0].length / 4) - lll; j--) {
                    world[i][j] = this.original[i][j];
                }
            }

            length = world[0].length / 4;
            for (int i = world[0].length - 1; i > world[0].length - world[0].length / 4; i--) {
                int mdpoint =  (world[0].length - world[0].length / 4) + (length / 2);
                int ll = Math.abs(i - mdpoint);
                int lll = (length / 2) + 2 - ll;
                for (int j =  world.length / 3; j < world.length / 3 + lll; j++) {
                    world[j][i] = this.original[j][i];
                }
            }

            world[0][world[0].length - 1] = Tileset.TREE;
            leftLightOff = false;

        }

    }

    public boolean isRightLightOff() {
        return rightLightOff;
    }

    public boolean isLeftLightOff() {
        return leftLightOff;
    }


    private void putIntoCellArray(Room room) {
        for (int i = (int) room.position.getX(); i < room.position.getX() + room.width; i++) {
            for (int j = (int) room.position.getY(); i < room.position.getY() + room.height; j++) {
                if (myCellArray[i][j] == null) {
                    LinkedList<Room> roomList = new LinkedList<>();
                    roomList.add(room);
                    myCellArray[i][j] = roomList;
                } else {
                    myCellArray[i][j].add(room);
                }
            }
        }
    }

    public ArrayList<Room> generateRooms(int numberOfRooms) {
        //Room[] rooms = new Room[numberOfRooms];
        for (int i = 0; i < numberOfRooms; i++) {
            int roomWidth = rand.nextInt(RADIUS - 4) + 4;
            int roomHeight = rand.nextInt(RADIUS - 4) + 4;

            int xPos = rand.nextInt(width - roomWidth);
            int yPos = rand.nextInt(height - roomHeight);

            Room room = new Room(roomWidth, roomHeight, xPos, yPos, i);
            if (rms.size() > 0) {
                /*
                KDTree kd = new KDTree(rms);
                int counter = 0;
                while (room.doesOverlap(kd.nearest(room)) && counter < 100) {
                    room.position.setX(rand.nextInt(width - roomWidth));
                    room.position.setY(rand.nextInt(height - roomHeight));
                    counter += 1;
                    if (counter > 10) {
                        if (room.width > 3) {
                            room.width -= 1;

                        }
                        if (room.height > 3) {
                            room.height -= 1;

                        }
                        break;
                    }
                }

                if (room.doesOverlap(kd.nearest(room))) {
                    break;
                }
                */
                int counter2 = 0;
                while (room.neighbors(rms).size() > 0 && counter2 < 100) {
                    room.position.setX(rand.nextInt(width - roomWidth));
                    room.position.setY(rand.nextInt(height - roomHeight));
                    counter2 += 1;
                    if (counter2 > 10) {
                        if (room.width > 3) {
                            room.width -= 1;

                        }
                        if (room.height > 3) {
                            room.height -= 1;

                        }
                    }
                }
                if (room.neighbors(rms).size() > 0) {
                    break;
                }
                if (outOfWorld(room)) {
                    break;
                }
                //rooms[i] = room;
                rms.add(room);
                if (room.width >= 5 && room.height >= 5) {
                    actualRooms.add(room);
                } else {
                    smallRooms.add(room);
                }

            } else {
                if (outOfWorld(room)) {
                    break;
                }
                //rooms[i] = room;
                rms.add(room);
                if (room.width >= 5 && room.height >= 5) {
                    actualRooms.add(room);
                } else {
                    smallRooms.add(room);
                }
            }
        }
        return rms;
    }


    public void drawVerticalCorri(Position p1, Position p2, TETile[][] map) {
        if (!p1.equals(p2)) {
            int h = Math.abs(p1.getY() - p2.getY());
            //figure out the lower room
            if (p1.getY() < p2.getY()) {
                Room room = new Room(2, h, p1.getX(), p1.getY(), 0);
                drawHallways(map, room, true);
                return;
            } else {
                Room room = new Room(2, h, p2.getX(), p2.getY(), 0);
                drawHallways(map, room, true);
                return;
            }
        }
    }

    public void drawHorizontalCorri(Position p1, Position p2, TETile[][] map) {
        if (!p1.equals(p2)) {
            int w = Math.abs(p1.getX() - p2.getX());
            //figure out the leftmost position
            if (p1.getX() < p2.getX()) {
                Room room = new Room(w, 2, p1.getX(), p1.getY(), 0);
                drawHallways(map, room, false);
                return;
            } else {
                Room room = new Room(w, 2, p2.getX(), p2.getY(), 0);
                drawHallways(map, room, false);
                return;
            }
        }
    }

    public void drawHallways(TETile[][] map, Room r, boolean dir) {
        for (int i = r.position.getX(); i < r.position.getX() + r.width; i++) {
            for (int j = r.position.getY(); j < r.position.getY() + r.height; j++) {
                if (i < 0 || i >= map.length || j < 0 || j >= map[0].length) {
                    break;
                }
                map[i][j] = Tileset.FLOOR;


                //if dir is true, vertical hallway
                //world[i][j] = Tileset.SAND;


                /*

                else if (dir) {
                    if (i == r.position.getX() || i == r.position.getX() + r.width - 1) {
                        if (world[i][j] == Tileset.FLOOR) {
                            world[i][j] = Tileset.FLOOR;
                        }
                        world[i][j] = Tileset.WALL;
                    } else {
                        world[i][j] = Tileset.FLOOR;

                    } //if dir false horizontal
                } else {
                    if (j == r.position.getY() || j == r.position.getY() + r.height - 1) {
                        if (world[i][j] == Tileset.FLOOR) {
                            world[i][j] = Tileset.FLOOR;
                        }
                        world[i][j] = Tileset.WALL;
                    } else {
                        world[i][j] = Tileset.FLOOR;
                    }
                }
                */

            }

        }

    }


    public void drawRoom(TETile[][] map, Room r) {
        for (int i = r.position.getX(); i < r.position.getX() + r.width; i++) {
            for (int j = r.position.getY(); j < r.position.getY() + r.height; j++) {
                if (i < 0 || i >= map.length || j < 0 || j >= map[0].length) {
                    break;
                }
                map[i][j] = Tileset.FLOOR;

            }

        }

    }

    public HashSet<WeightedEdge> edgesForHallways(ArrayList<Room> rooms1) {

        WeightedGraph wdg = new WeightedGraph(rooms1);
        HashSet<WeightedEdge> allEdges = wdg.allEdges();
        MST mstEdges = new MST(rooms1, wdg);
        HashSet<WeightedEdge> mst = mstEdges.getMST();

        HashSet<WeightedEdge> someEdges = new HashSet<>();
        int sizeOfSome = (int) 0.15 * allEdges.size();


        for (int i = 0; i < sizeOfSome; i++) {
            int index = rand.nextInt(allEdges.size());
            Iterator<WeightedEdge> iter = allEdges.iterator();
            for (int j = 0; i < index; i++) {
                iter.next();
            }
            WeightedEdge w = iter.next();
            someEdges.add(w);
            allEdges.remove(w);
        }

        //mst.addAll(someEdges);
        return mst;
    }


    public ArrayList<TETile> neighbors(int x, int y, TETile[][] map) {
        ArrayList<TETile> neighbors = new ArrayList<>();

        if (x > 0 && map[x - 1][y] != null) {
            neighbors.add(map[x - 1][y]);
        }
        if (y < (map[0].length - 1) && map[x][y + 1] !=  null) {
            neighbors.add(map[x][y + 1]);
        }
        if (x < (map.length - 1) && map[x + 1][y] != null) {
            neighbors.add(map[x + 1][y]);
        }
        if (y > 0 && map[x][y - 1] !=  null) {
            neighbors.add(map[x][y - 1]);
        }

        return neighbors;
    }

    public ArrayList<TETile> cornerTile(int x, int y, TETile[][] map) {
        ArrayList<TETile> corners = new ArrayList<>();

        if (x < map.length - 1 && y < map[0].length - 1) {
            corners.add(map[x + 1][y + 1]);
        }
        if (x < map.length - 1 && y > 0) {
            corners.add(map[x + 1][y - 1]);
        }
        if (x > 0 && y < map[0].length - 1) {
            corners.add(map[x - 1][y + 1]);
        }
        if (x > 0 && y > 0) {
            corners.add(map[x - 1][y - 1]);

        }
        return corners;

    }


    public boolean upDown(int x, int y, TETile[][] map, TETile type) {

        if (y < map[0].length - 1 && map[x][y + 1] != type) {
            return false;
        }

        if (y > 0 && map[x][y - 1] != type) {
            return false;
        }


        return true;


    }

    public boolean rightLeft(int x, int y, TETile[][] map, TETile type) {

        if (x < map.length - 1 && map[x + 1][y] != type) {
            return false;
        }

        if (x > 0 && map[x - 1][y] != type) {
            return false;
        }

        return true;


    }


    public void verify(TETile[][] worldd) {
		for (int i = 0; i < worldd.length; i += 1) {
			for (int j = 0; j < worldd[0].length; j += 1) {
				if (worldd[i][j] == Tileset.NOTHING) {
					ArrayList<TETile> nbors = neighbors(i, j, worldd);
					if (nbors.contains(Tileset.FLOOR)) {
						worldd[i][j] = Tileset.SAND;
					}
					ArrayList<TETile> corners = cornerTile(i, j, worldd);

					if (corners.contains(Tileset.WALL) && nbors.contains(Tileset.WALL)) {
						worldd[i][j] = Tileset.SAND;
					}

					if (corners.contains(Tileset.FLOOR)) {
						worldd[i][j] = Tileset.SAND;
					}
				}

				if (worldd[i][j] == Tileset.WALL) {
					ArrayList<TETile> nbors = neighbors(i, j, worldd);
					ArrayList<TETile> corners = cornerTile(i, j, worldd);
					if (corners.contains(Tileset.WALL) && nbors.contains(Tileset.WALL)) {
						worldd[i][j] = Tileset.FLOOR;
					}

					if (rightLeft(i, j, worldd, Tileset.WALL) || upDown(i, j, worldd, Tileset.WALL)) {
						worldd[i][j] = Tileset.FLOOR;
					}


				}


			}
		}

		for (int i = 0; i < worldd.length; i += 1) {
			for (int j = 0; j < worldd[0].length; j += 1) {

				if (worldd[i][j] == Tileset.SAND) {
					ArrayList<TETile> nbors = neighbors(i, j, worldd);
					if (!nbors.contains(Tileset.NOTHING)) {
						worldd[i][j] = Tileset.FLOOR;
					}

				}


			}
		}


		for (int i = 0; i < worldd.length; i += 1) {
			for (int j = 0; j < worldd[0].length; j += 1) {

				if (worldd[i][j] == Tileset.NOTHING) {
					ArrayList<TETile> nbors = neighbors(i, j, worldd);

					ArrayList<TETile> corners = cornerTile(i, j, worldd);


					if (corners.contains(Tileset.FLOOR)) {
						worldd[i][j] = Tileset.SAND;
					}


				}

			}
		}

		for (int i = 0; i < worldd.length; i += 1) {
			for (int j = 0; j < worldd[0].length; j += 1) {


				if (worldd[i][j] == Tileset.SAND) {
					worldd[i][j] = Tileset.WALL;
				}


				if (worldd[i][j] == Tileset.FLOOR) {
					if (i == 0 || i == worldd.length - 1 || j == 0 || j == worldd[0].length - 1) {
						worldd[i][j] = Tileset.WALL;
					}
				}

			}
		}







		worldd[0][worldd[0].length - 1] = Tileset.TREE;
		worldd[worldd.length  - 1][worldd[0].length - 1] = Tileset.TREE;

		TETile [][] copy = new TETile[worldd.length][];
		for (int i = 0; i < worldd.length; i++) {
			TETile[] t = worldd[i];
			int aLength = t.length;
			copy[i] = new TETile[aLength];
			System.arraycopy(t, 0, copy[i], 0, aLength);
		}
		this.original = copy;
    }



    public void drawHallway(WeightedEdge e, TETile[][] worldd) {
        Room source = e.from();
        Room dest = e.to();
        Position firstStart;
        Position firstEnd;
        Position secondStart;
        Position secondEnd;

        //If Source room is below dest room
        if (source.position.getY() < dest.position.getY()) {

            //if source room is left to dest room
            if (source.position.getX() < dest.position.getX()) {

                firstStart = new Position(source.position.getX() + source.width,
                        source.center.getY() - 1);
                firstEnd = new Position(dest.position.getX() + 1, source.center.getY() - 1);

                drawHorizontalCorri(firstStart, firstEnd, worldd);
                secondStart = new Position(dest.position.getX() + 1, source.center.getY() - 1);
                secondEnd = new Position(dest.position.getX() + 3, dest.position.getY());
                drawVerticalCorri(secondStart, secondEnd, worldd);

                //if source is right of dest room
            } else {
                firstStart = new Position(source.position.getX(),
                        source.position.getY());
                firstEnd = new Position((dest.position.getX() + dest.width) - 2,
                        source.position.getY());
                drawHorizontalCorri(firstStart, firstEnd, worldd);

                secondStart = new Position((dest.position.getX() + dest.width) - 2,
                        source.position.getY());
                secondEnd = new Position(dest.position.getX() + dest.width,
                        dest.position.getY());

                drawVerticalCorri(secondStart, secondEnd, worldd);

            } //if source room is above dest room
        } else {
            if (source.position.getX() < dest.position.getX()) {


                firstStart = new Position(source.position.getX(), source.position.getY());
                firstEnd = new Position(source.position.getX(), dest.position.getY() + 1);

                drawVerticalCorri(firstStart, firstEnd, worldd);

                secondStart = new Position(source.position.getX(), dest.position.getY());
                secondEnd = new Position(dest.position.getX() + 2, dest.position.getY());

                drawHorizontalCorri(secondStart, secondEnd, worldd);

            } else {

                firstStart = new Position(source.position.getX(), source.position.getY());
                firstEnd = new Position((dest.position.getX() + dest.width) - 2,
                        source.position.getY());
                drawHorizontalCorri(firstStart, firstEnd, worldd);
                secondStart = new Position((dest.position.getX() + dest.width) - 2,
                        dest.position.getY() + dest.height);
                secondEnd = new Position(dest.position.getX() + dest.width,
                        source.position.getY() + 2);
                drawVerticalCorri(secondStart, secondEnd, worldd);

            }
        }

    }



    public boolean outOfWorld(Room r) {
        if (r.position.getY() + r.height >= height || r.position.getY() < 0
            || r.position.getX() + r.width >= width || r.position.getX() < 0) {
            return true;
        }
        return false;
    }



    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(80, 30);

        TETile[][] randomTiles = new TETile[80][30];
        for (int i = 0; i < randomTiles.length; i++) {
            Arrays.fill(randomTiles[i], Tileset.NOTHING);
        }
        MapGenerator mg = new MapGenerator(12542, 80, 30, randomTiles);

        mg.drawActualRooms(randomTiles);
        mg.drawTheHallways(randomTiles);


        System.out.println(mg.actualRooms.size());

        ter.renderFrame(randomTiles);



    }













}
