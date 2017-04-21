package vrp;
public class Path{
	Pair startPair;
	Pair endPair;
	int distance;

	public Path(Pair start, Pair end, int dist){
		this.startPair = start;
		this.endPair = end;
		this.distance = dist;
	}

	public Pair getStart(){ return startPair; }

	public Pair getEnd(){ return this.endPair; }

	public int getDistance(){ return this.distance; }
}