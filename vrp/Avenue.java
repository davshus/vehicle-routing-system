package vrp;
import vrp.Pair;
public class Avenue {
	Pair[] pairs;
	public Avenue(Pair[] pairs) {
		this.pairs = pairs;
	}
	public void setPairs(Pair[] pairs) {
		this.pairs = pairs;
	}
	public Pair[] getPairs() {
		return this.pairs;
	}
}
