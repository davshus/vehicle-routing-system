package vrp;
import java.io.File;
import java.util.Scanner;
import vrp.Pair;
import vrp.Avenue;
public class City {
	Avenue[][] map;
	public City() {
	}
	public void setupMap(File inputFile) throws Exception {
		Scanner sc = new Scanner(inputFile).useDelimiter(",|\r?\n\r?|\n?\r\n?");
		while (sc.hasNext()) {
			System.out.println(sc.next());
		}


	}
	public Avenue[][] getMap() {
		return this.map;
	}
}
