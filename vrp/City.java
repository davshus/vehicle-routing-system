package vrp;
import java.util.Scanner;
import vrp.Pair;
import vrp.Avenue;
public class City {
	Avenue[][] map;
	public City(Avenue[][] map) {
		this.map = map;
	}
	public void setupMap(File inputFile) throws Exception {
		Scanner sc = new Scanner(inputFile).useDelimiter(",|\r?\n\r?");
		while (sc.hasNext()) {
			String curr = sc.next();
		}


	}
	public Avenue[][] getMap() {
		return this.map;
	}
}
