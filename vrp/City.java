package vrp;
import java.io.File;
import java.util.*;
import vrp.Pair;
import vrp.Avenue;
import tinshoes.geom.Point;
public class City {
	Pair[][] map;
	public City() {
	}
	public void setupMap(File inputFile) throws Exception {
		Scanner sc = new Scanner(inputFile).useDelimiter(",|\r?\n\r?|\n?\r\n?");
		while (sc.hasNext()) {
			System.out.println(sc.next());
		}


	}
	public Pair[][] getMap() {
		return this.map;
	}

	Queue searchQueue = new LinkedList();
	HashMap<Pair, Pair> valueToParent = new HashMap<Pair, Pair>();

	public Pair floodFill(Pair startPoint){
		int street = startPoint.getStreet();
		int avenue = startPoint.getAvenue();
		if (street % 2 == 0){
			Pair down = map[street][avenue - 1];
				down.setUp(false);
				down.setDistance(100);
				searchQueue.add(down);

			if (street != 49){
				Pair up = map[street][avenue + 1];
				up.setUp(true);
				up.setDistance(100);
				searchQueue.add(up);
			}
		}else{
			if (avenue )
		}



	}

	public void addNextTo(Pait point){
		if (point.getUp() && point.getAvenue() != ){

		}else if (point.getDown())
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