package vrp;
public class Pair {
	private char name;
	private int street;
	private int avenue;
	private int deliver;
	private Pair pairFrom;

	public Pair() {
		//Empty Pair
	}

	public Pair(int street, int avenue, char name) {
		this.name = name;
		this.street = street;
		this.avenue = avenue;
		this.deliver = 0;
		this.pairFrom = null;
	}

	public int distanceTo(Pair nextPair){
		int xdist = Math.abs(nextPair.getStreet() - this.street) * 200;
		int ydist = Math.abs(calcY(nextPair.getAvenue(), nextPair.getName()) - calcY(this.avenue, this.name));
		if (ydist < 10){
			int first = calcY(nextPair.getAvenue(), nextPair.getName()) % 10;
			int second = this.avenue % 10;
			ydist = ((first + second) < ((10 - first) + (10 - second))) ? (first + second) : ((10 - first) + (10 - second));
		}
		ydist *= 100;
		return ydist + xdist;
	}
	
	public void setPair(Pair newPair){ this.pairFrom = newPair; }

	public Pair getPair(){ return this.pairFrom; }

	public void setName(char name) { this.name = name; }

	public char getName() {	return this.name; }

	public void setStreet(int street) {	this.street = street; }

	public int getStreet() { return this.street; }

	public void setAvenue(int avenue) {	this.avenue = avenue; }

	public int getAvenue() { return this.avenue; }

	public void setDeliver(int deliver) { this.deliver = deliver; }

	public int getDeliver() { return this.deliver; }

	public void addDeliver() { this.deliver++; }

	public int calcY(int ave, char name) {
		return ((ave - 1) * 10) + (name - 'A');
	}
}
