package vrp;
import java.io.File;
import java.util.Scanner;
import vrp.Pair;
// import vrp.Avenue;
public class City {
	public static int nStreets = 250, nAvenues = 50;
	Pair[][] map;
	public City() {
		map = new Pair[nStreets][nAvenues * 10];
	}
	public void setupMap(File inputFile) throws Exception {
		//Bart Complex
		Scanner sc = new Scanner(inputFile).useDelimiter(",|\r?\n\r?|\n?\r\n?");
		while (sc.hasNext()) {
			System.out.println(sc.next());
		}

	}
	public Pair[][] getMap() {
		return this.map;
	}
}