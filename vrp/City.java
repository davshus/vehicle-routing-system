package vrp;
import java.io.File;
import java.util.*;
import vrp.Pair;
import vrp.Avenue;
import tinshoes.geom.Point;
public class City {
	private Pair[][] map;
	private int bart, lisa;
	public City() {
	}
	public void setupMap(File inputFile) throws Exception {
		Scanner sc = new Scanner(inputFile).useDelimiter(",|\r?\n\r?|\n?\r\n?");
		while (sc.hasNext()) {
			String curr = sc.next();
			if (curr.equals("Bart Complex")) {
				bart = curr.nextInt();
				curr.next(); //Lisa
				lisa = curr.nextInt();
			}
		}


	}
	public Pair[][] getMap() {
		return this.map;
	}
	public int getBart() {
		return this.bart;
	}
	public int getLisa() {
		return this.lisa;
	}
	Queue searchQueue = new LinkedList();
	boolean[][] searched = boolean[map.count][map[0].count];

	public Pair floodFill(Pair startPoint){
		int street = startPoint.getStreet();
		int avenue = startPoint.getAvenue();
		addNextTo(startPoint);

		searched[street][avenue] = true;

	}

	public void addNextTo(Pait point){
		nextFoundPoint(point);
		int street = point.getStreet();
		int avenue = point.getAvenue();
		if(avenue % 2 == 0){
			
		}

	}


	public void nextFoundPoint(Pair nextPair){
		if(nextPair.getDeliver()){
			System.out.println(nexPair.getStreet() + "\t" + nextPair.getAvenue());
			while(searchQueue.peek() != null){
				try{
					searchQueue.remove();	
				}catch(Exception e){
				}
			}
			
		}
	}	

}