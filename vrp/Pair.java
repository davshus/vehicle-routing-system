package vrp;
public class Pair {
	private char name;
	private int street;
	private int avenue;
	private boolean deliver;
	private int distance;

	public Pair(char name, int street, int avenue, boolean deliver) {
		this.name = name;
		this.street = street;
		this.avenue = avenue;
		this.deliver = deliver;
		this.distance = 0;
	}

	public void addDistance(int distance){
		this.distance += distance;
	}

	public int getDistance(){
		return distance;
	}

	public void setName(char name) {
		this.name = name;
	}
	public char getName() {
		return this.name;
	}
	public void setStreet(int street) {
		this.street = street;
	}
	public int getStreet() {
		return this.street;
	}
	public void setAvenue(int avenue) {
		this.avenue = avenue;
	}
	public int getAvenue() {
		return this.avenue;
	}
	public void setDeliver(boolean deliver) {
		this.deliver = deliver;
	}
	public boolean getDeliver() {
		return this.deliver;
	}
}
