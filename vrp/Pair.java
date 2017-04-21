package vrp;
public class Pair {
	private char name;
	private int street;
	private int avenue;
	private int deliver;

	public Pair(int street, int avenue, char name) {
		this.name = name;
		this.street = street;
		this.avenue = avenue;
		this.deliver = 0;
	}
	
	public void setName(char name) { this.name = name; }

	public char getName() {	return this.name; }

	public void setStreet(int street) {	this.street = street; }

	public int getStreet() { return this.street; }

	public void setAvenue(int avenue) {	this.avenue = avenue; }

	public int getAvenue() { return this.avenue; }

	public void setDeliver(int deliver) { this.deliver = deliver; }

	public int getDeliver() { return this.deliver; }

	public void addDeliver() { this.deliver++; }
}
