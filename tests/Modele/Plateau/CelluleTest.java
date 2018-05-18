package Modele.Plateau;

import Utils.Position;
import org.junit.Assert;
import org.junit.Test;

import java.io.*;
import java.util.Random;

public class CelluleTest {
	@Test
	public void getFish() {
		Random r  = new Random();
		int nb_fish;
		Cellule c;

		for (int i = 0; i < 100; i++) {
			nb_fish = r.nextInt(3) + 1;
			c = new Cellule(new Position(0,0), nb_fish);
			Assert.assertEquals(nb_fish, c.getFish());
		}
	}

	@Test
	public void getPosition() {
		Random r = new Random();
		Position p;
		Cellule c;
		for (int i = 0; i < 100; i++) {
			p = new Position(r.nextInt(),r.nextInt());
			c = new Cellule(p,r.nextInt());
			Assert.assertEquals(p,c.getPosition());
		}
	}

	@Test
	public void pingouin() {
		Random r = new Random();
		Pingouin pingouin;
		Position position;
		Cellule c;
		for (int i = 0; i < 100; i++) {
			position = new Position(r.nextInt(), r.nextInt());
			pingouin = new Pingouin(r.nextInt(), position);
			c = new Cellule(position, r.nextBoolean(), r.nextInt(), pingouin);
			Assert.assertEquals(pingouin, c.pingouin());
		}
	}

	@Test
	public void setDestroyed() {
		Cellule c = new Cellule();
		Assert.assertFalse(c.isDestroyed());
		c.setDestroyed(true);
		Assert.assertTrue(c.isDestroyed());
	}

	@Test
	public void setFish() {
		int new_fish = 0,
			old_fish = 0;
		Random r = new Random();
		Cellule c = new Cellule(new Position(0,0), old_fish);

		for (int i = 0; i < 100; i++) {
			old_fish = new_fish;
			new_fish = r.nextInt();
			Assert.assertNotEquals(new_fish, c.getFish());
			Assert.assertEquals(old_fish, c.getFish());
			c.setFish(new_fish);
			Assert.assertNotEquals(old_fish, c.getFish());
			Assert.assertEquals(new_fish,c.getFish());

		}
	}

	@Test
	public void destroy() {
		Cellule c = new Cellule();
		Assert.assertFalse(c.isDestroyed());
		c.destroy();
		Assert.assertTrue(c.isDestroyed());
	}


	@Test
	public void isDestroyed() {
		Cellule c = new Cellule();

		Assert.assertFalse(c.isDestroyed());
		c.setDestroyed(true);
		Assert.assertTrue(c.isDestroyed());
	}

	@Test
	public void aPingouin() {
		Cellule c = new Cellule(new Position(0,0), false, 1, null);
		Assert.assertFalse(c.aPingouin());
		c = new Cellule(new Position(0,0), false,1, new Pingouin(1, new Position(0,0)));
		Assert.assertTrue(c.aPingouin());
	}

	@Test
	public void isObstacle() {
		Cellule c1 = new Cellule(new Position(0,0), false, 1, null);
		Cellule c2 = new Cellule(new Position(0,0), true, 1, null);
		Cellule c3 = new Cellule(new Position(0,0), false, 1, new Pingouin(1, new Position(0,0)));
		Cellule c4 = new Cellule(new Position(0,0), true, 1, new Pingouin(1, new Position(0,0)));

		Assert.assertFalse(c1.isObstacle());
		Assert.assertTrue(c2.isObstacle());
		Assert.assertTrue(c3.isObstacle());
		Assert.assertTrue(c4.isObstacle());
	}

	@Test
	public void setPenguin() {
		Pingouin p = new Pingouin(1, new Position(0,0));
		Cellule c = new Cellule(new Position(0,0), false, 1, null);

		Assert.assertEquals(c.pingouin(), null);
		c.setPenguin(p);
		Assert.assertEquals(c.pingouin(), p);
	}


	public void serial_test(Cellule c) {
		String filename = "tests/rsc/test_serial.bin";
		try {
			ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(filename));
			os.writeObject(c);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			Assert.fail();
		} catch (IOException e) {
			e.printStackTrace();
			Assert.fail();
		}

		try {
			ObjectInputStream is = new ObjectInputStream(new FileInputStream(filename));
			Cellule c_lecture = (Cellule) is.readObject();
			Assert.assertEquals(c, c_lecture);
		} catch (FileNotFoundException e) {
			System.err.println(e.getMessage());
			Assert.fail();
		} catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
			Assert.fail();
		}
	}
	@Test
	public void serial() {
		Cellule c = new Cellule(new Position(0,0), false, 2, null);
		serial_test(c);
		c =  new Cellule(new Position(0,0), false, 2, new Pingouin(0, new Position(0,0)));
		serial_test(c);
	}

	@Test
	public void clonetests() {
		Cellule c1 = new Cellule(new Position(0,0), false, 1, null);
		Assert.assertEquals(c1, c1.clone());
	}

	@Test
	public void equals() {
		Cellule c1 = new Cellule(new Position(0,0), false, 1, null);
		Cellule c2 = new Cellule(new Position(0,0), false, 1, null);
		Cellule c3 = new Cellule(new Position(0,1), false, 1, null);
		Cellule c4 = new Cellule(new Position(0,0), true, 1, null);
		Cellule c5 = new Cellule(new Position(0,0), false, 2, null);
		Cellule c6 = new Cellule(new Position(0,0), false, 1, new Pingouin(1));
		Assert.assertEquals(c1,c2);
		Assert.assertFalse(c1.equals(c3));
		Assert.assertFalse(c1.equals(c4));
		Assert.assertFalse(c1.equals(c5));
		Assert.assertFalse(c1.equals(c6));
	}
}
