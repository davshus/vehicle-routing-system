package vrp;
import vrp.Pair;
import vrp.Avenue;
public class City {
	Avenue[][] map;
	public City(Avenue[][] map) {
		this.map = map;
	}
	public void setMap(Avenue[][] map) {
		this.map = map;
	}
	public Avenue[][] getMap() {
		return this.map;
	}
}
