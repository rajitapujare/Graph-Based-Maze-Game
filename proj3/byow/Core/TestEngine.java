package byow.Core;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
//import org.junit.Test;
public class TestEngine {

    public static void main(String[] args) {

        TERenderer ter = new TERenderer();
        TERenderer ter2 = new TERenderer();

        ter.initialize(80, 30);


        Engine e1 = new Engine();
        TETile[][] newfinalWorldFrame = e1.interactWithInputString("lwsd");
        ter.renderFrame(newfinalWorldFrame);



        //testSameInputString();
    }

    public static void testSameInputString() {
        TERenderer ter = new TERenderer();

        ter.initialize(80, 30);

        Engine e = new Engine();
        TETile[][] finalWorldFrame = e.interactWithInputString("n123sss");
        ter.renderFrame(finalWorldFrame);
        //ter.initialize(80,30);

        TETile[][] newfinalWorldFrame = e.interactWithInputString("n123sss");
        //ter.renderFrame(newfinalWorldFrame);


    }


}
