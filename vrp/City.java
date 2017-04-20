package vrp;
import java.io.File;
import java.util.*;
import vrp.Pair;
import vrp.Avenue;
import tinshoes.geom.Point;
public class City {
	public static int nStreets = 250, nAvenues = 50;
	private Pair[][] map;
	private int lisa, bart;
	public City() {
		map = new Pair[nStreets][nAvenues * 10];
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

	public Pair floodFill(Pair startPoint){
	// 	int street = startPoint.getStreet();
	// 	int avenue = startPoint.getAvenue();
	// 	if (street % 2 == 0){
	// 		Pair down = map[street][avenue - 1];
	// 			down.setUp(false);
	// 			down.setDistance(100);
	// 			searchQueue.add(down);

	// 		if (street != 49){
	// 			Pair up = map[street][avenue + 1];
	// 			up.setUp(true);
	// 			up.setDistance(100);
	// 			searchQueue.add(up);
	// 		}
	// 	}else{
	// 		if (avenue == 0){

	// 		}else if (avenue == 50){

	// 		}else{
	// 			addNextTo(startPoint);
	// 		}
	// 	}



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