package Modele.Plateau;

import Utils.Position;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class PlateauTest {
	Plateau p;
	@Before
	public void setUp() {
		p = new Plateau(3);
	}

	@Test
	public void initTab() {
		String expected = "{3, [" +
				"[[(0,0), false], [(0,1), false], [(0,2), true]]," +
				"[[(1,0), false], [(1,1), false], [(1,2), false]]," +
				"[[(2,0), false], [(2,1), false], [(2,2), true]]," +
				"}";
		Assert.assertEquals(expected, p.toString());
	}
}
