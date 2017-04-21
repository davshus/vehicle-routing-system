package vrp;
public class returnStatements{
	Pair startPair;
	Pair endPair;
	int distance;

	public returnStatements(Pair start, Pair end, int dist){
		this.startPair = start;
		this.endPair = end;
		this.distance = dist;
	}

	public Pair getStart(){ return startPair; }

	public Pair getEnd(){ return this.endPair; }

	public int getDistance(){ return this.distance; }
}