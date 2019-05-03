package byow.lab12;
import byow.Core.Position;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;
import org.junit.Test;
import static org.junit.Assert.assertEquals;


public class TestRandomWorldDemo {

	@Test
	public void testHexRowWidth() {
		assertEquals(3, RandomWorldDemo.hexRowWidth(3, 5));
		assertEquals(5, RandomWorldDemo.hexRowWidth(3, 4));
		assertEquals(7, RandomWorldDemo.hexRowWidth(3, 3));
		assertEquals(7, RandomWorldDemo.hexRowWidth(3, 2));
		assertEquals(5, RandomWorldDemo.hexRowWidth(3, 1));
		assertEquals(3, RandomWorldDemo.hexRowWidth(3, 0));
		assertEquals(2, RandomWorldDemo.hexRowWidth(2, 0));
		assertEquals(4, RandomWorldDemo.hexRowWidth(2, 1));
		assertEquals(4, RandomWorldDemo.hexRowWidth(2, 2));
		assertEquals(2, RandomWorldDemo.hexRowWidth(2, 3));
	}

	@Test
	public void testTopRight() {
		TERenderer ter = new TERenderer();
		ter.initialize(50, 50);

		TETile[][] randomTiles = new TETile[50][50];
		//fillWithRandomTiles(randomTiles);
		RandomWorldDemo.addHexagon(randomTiles, new Position(0,0), 3, Tileset.FLOWER);

	}



}
