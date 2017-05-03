package vrp;
public class Pair {
	private char name;
	private int street;
	private int avenue;
	private int deliver;
	private Pair pairFrom;
	private int cluster;

	public Pair() {
		//Empty Pair
	}

	public Pair(int street, int avenue, char name) {
		this.name = name;
		this.street = street - 1;
		this.avenue = avenue - 1;
		this.deliver = 0;
		this.pairFrom = null;
		this.cluster = 0;
	}

	public int distanceTo(Pair endPair){
		int distance = 0;
		
		int aveStart = calcY(this.getAvenue(), this.getName());
		int aveEnd = calcY(endPair.getAvenue(), endPair.getName());

		int streetDif = Math.abs(this.getStreet() - endPair.getStreet());
		int aveDif = Math.abs(aveStart - aveEnd);
		Integer totalDist = null;
		if (aveDif < 10 && streetDif > 0){
			boolean thisTop = this.getName() - 'A' >= 5, thatTop = endPair.getName() - 'A' >= 5;
			if (thisTop && thatTop) {
				totalDist = (9 - (this.getName() - 'A')) + (9 - (endPair.getName() - 'A'));
			} else if (!thisTop && !thatTop) {
			   totalDist = (this.getName() - 'A') + (endPair.getName() - 'A');
			} else {
			   totalDist = Math.min((this.getName() - 'A') + (9 - (endPair.getName() - 'A')), (9 - (this.getName() - 'A')) + (endPair.getName() - 'A'));
			}
			totalDist *= 100;
		} else {
			// System.out.println("StreetDif: " + streetDif + " \t AveDif : " + aveDif);
			totalDist = aveDif * 100;
		}
		// System.out.println(totalDist/100 + " " + endPair + " " + streetDif);
		totalDist += streetDif * 200;
		return totalDist;
	}
	
	public Path pathTo(Pair dest) {
		return new Path(this, dest, distanceTo(dest));
	}

	public void deliver() {
		setDeliver(0);
	}

	public void setCluster(int cluster){this.cluster = cluster;}

	public int getCluster(){return cluster;}

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
	public String toString() { return "(" + street + ", " + avenue + ", " + name + ")"; }
}
