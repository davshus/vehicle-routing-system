package vrp;
public class Pair {
	private char name;
	private int street;
	private int avenue;
	private int deliver;
	private Pair pairFrom;

	public Pair(int street, int avenue, char name) {
		this.name = name;
		this.street = street;
		this.avenue = avenue;
		this.deliver = 0;
		this.pairFrom = null;
	}

	public int distanceTo(Pair endPair){
		int distance = 0;
		
		int aveStart = calcY(this.getAvenue(), this.getName());
		int aveEnd = calcY(endPair.getAvenue(), endPair.getName());

		int streetDif = Math.abs(this.getStreet() - endPair.getStreet());
		int aveDif = Math.abs(aveStart - aveEnd);

		if (aveDif < 10 && streetDif > 0){
			int start = aveStart % 10 < aveEnd % 10 ? aveStart % 10 : aveEnd % 10;
			int end = aveStart % 10 >= aveEnd % 10 ? aveStart % 10 : aveEnd % 10;
			// System.out.println("start: " + start + " \t end : " + end);
			aveDif = end + start < (10 - end) + (10 - start) ? end + start : (10 - end) + (10 - start);
			if (aveStart / 10 == 49){
				aveDif = end + start;
			}
		} 
		// System.out.println("StreetDif: " + streetDif + " \t AveDif : " + aveDif);
		int totalDist = (streetDif * 200) + (aveDif * 100);
		return totalDist;
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
