package vrp;
import java.io.File;
import java.util.*;
import vrp.Pair;
import vrp.Avenue;
import tinshoes.geom.Point;
public class City {

	int totalPackages = 0;
	public int getPackages(){ return totalPackages; }
	public static int nStreets = 250, nAvenues = 50;
	private Pair[][] map;
	private int lisa, bart;
	public City() {
		map = new Pair[nStreets][nAvenues * 10];
		for (int i = 0; i < nStreets; i++) {
			for (int j = 0; j < nAvenues * 10; j++) {
				map[i][j] = new Pair(i + 1, (j/10) + 1, (char)((j % 10) + (int)'A'));
			}
		}
		// totalPackages = 0;
	}

	public void setupMap(File inputFile) throws Exception {
		System.out.println("Setting up Homerville...");
		Scanner sc = new Scanner(inputFile).useDelimiter(",|\r?\n\r?|\n?\r\n?");
		System.out.println("Cycle: " + sc.next() + " House Number: " + sc.next());
		while (sc.hasNext()) { //NOTE: Possible change to fixed house number counter?
			String streetStr = sc.next();
			if (streetStr.equals("Bart Complex")) {
				bart = sc.nextInt();
				sc.next(); //Lisa
				lisa = sc.nextInt();
				break;
			}
			// System.out.println(streetStr);
			int street = Integer.parseInt(streetStr.substring(0, streetStr.length() - 1));
			String aveStr = sc.next();
			int avenue = Integer.parseInt(aveStr.substring(0, aveStr.length() - 1));
			char letter = sc.next().charAt(0);
			map[street - 1][calcY(avenue, letter)].addDeliver();
		}
		// System.out.println(bart + " " + lisa);
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

	Queue<Pair> searchQueue = new LinkedList<Pair>();
	boolean[][] searchedBool = new boolean[nStreets][nAvenues * 10];

	public Path closestTo(Pair startPoint){
		for (int i = 0; i < searchedBool.length; i ++){
			for (int j = 0; j < searchedBool[i].length; j ++){
				searchedBool[i][j] = false;
			}
		}
		int street = startPoint.getStreet();
		int avenue = calcY(startPoint.getAvenue(), startPoint.getName());

		if (avenue % 2 == 0){
			searchQueue.add(startPoint);
		}else{
			if (search(street, avenue + 1)) return getPath(startPoint, map[street][avenue + 1]);
			if (search(street, avenue - 1)) return getPath(startPoint, map[street][avenue - 1]);
			searchedBool[street][avenue + 1] = true;
			searchedBool[street][avenue - 1] = true;
			searchQueue.add(map[street][avenue + 1]);
			searchQueue.add(map[street][avenue - 1]);
		}

		while (searchQueue.peek() != null){
			Pair currentPair = searchQueue.poll();
			int currAvenue = calcY(currentPair.getAvenue(), currentPair.getName());
			if (currAvenue % 2 == 0){
				for (int[] i : getNextTo(currentPair)){
					if (search(i[0], i[1])){
						System.out.println("Found Point X: " + i[0] + "\t" + i[1]);
						return getPath(startPoint, map[i[0]][i[1]]);
					}else{
						searchedBool[i[0]][i[1]] = true;
						searchQueue.add(map[i[0]][i[1]]);
					}
				}
			}
		}

		return null;

	}

	public boolean search(int x, int y){ return search(map[x][y]); }

	public boolean search(Pair searchPoint){
		if (searchPoint.getDeliver() > 0){
			totalPackages += searchPoint.getDeliver();
			searchPoint.setDeliver(0);
			return true;
		}
		return false;
	}

	public ArrayList<int[]> getNextTo(Pair pair){

		int street = pair.getStreet();
		int avenue = calcY(pair.getAvenue(), pair.getName());

		ArrayList<int[]> points = new ArrayList<int[]>();
		if (avenue != 498 && street != 250){ 
			// System.out.println("Street:  " + street);
			if (!searchedBool[street][avenue + 1]) points.add(new int[]{street, avenue + 1}); 
			if (!searchedBool[street][avenue + 2]) points.add(new int[]{street, avenue + 2});
		}else if (street != 250){
			if (!searchedBool[street][avenue + 1]) points.add(new int[]{street, avenue + 1}); 
		}
		if (avenue != 0 && street != 250){
			if (!searchedBool[street][avenue - 1]) points.add(new int[] {street, avenue - 1});
			if (!searchedBool[street][avenue - 2]) points.add(new int[] {street, avenue - 2});
		}
		if (avenue % 10 == 0){
			if (street != 0){
				if (!searchedBool[street - 1][avenue]) points.add(new int[] {street - 1, avenue});
			}
			if (street != 249 && street != 250){
				if (!searchedBool[street + 1][avenue]) points.add(new int[] {street + 1, avenue});
			}
		}
		return points;
	}

	public Path getPath(Pair startPair, Pair endPair){
		int distance = 0;
		
		int aveStart = calcY(startPair.getAvenue(), startPair.getName());
		int aveEnd = calcY(endPair.getAvenue(), endPair.getName());

		int streetDif = Math.abs(startPair.getStreet() - endPair.getStreet());
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
		return new Path(startPair, endPair, totalDist);
	}

	public void finish(){

	}

	public int calcY(int ave, char name) {
		return ((ave - 1) * 10) + (name - 'A');
	}


//David's Function


// public int getPath(Pair startPair, Pair endPair){
// 		int distance = 0;
		
// 		int aveStart = calcY(startPair.getAvenue(), startPair.getName());
// 		int aveEnd = calcY(endPair.getAvenue(), endPair.getName());

// 		int streetDif = Math.abs(startPair.getStreet() - endPair.getStreet());
// 		int aveDif = Math.abs(aveStart - aveEnd);
//9 and 4
// 		Integer totalDist = null;
// 		if (aveDif < 10 && streetDif > 0){
// 			boolean thisTop = (startPair.getName() - 'A' >= 5 && startPair.getAvenue() != 50), thatTop = (endPair.getName() - 'A' >= 5 && endPair.getAvenue() != 50);
// 			if (thisTop && thatTop) {
// 				totalDist = (9 - (startPair.getName() - 'A')) + (9 - (endPair.getName() - 'A'));
// 			} else {
// 				System.out.println(startPair.getName() + " " + endPair.getName() + " " + endPair);
// 				totalDist = (startPair.getName() - 'A') + (endPair.getName() - 'A');
// 			}
// 			totalDist *= 100;
// 		} else {
// 			totalDist = aveDif * 100;
// 		}
// 		totalDist += streetDif * 200;
// 		return new Path(startPair, endPair, totalDist);;
// 	}




//Work of my own

	// public Path getPath(Pair startPair, Pair endPair){
	// 	int distance = 0;
		
	// 	int aveStart = calcY(startPair.getAvenue(), startPair.getName());
	// 	int aveEnd = calcY(endPair.getAvenue(), endPair.getName());

	// 	int streetDif = Math.abs(startPair.getStreet() - endPair.getStreet());
	// 	int aveDif = Math.abs(aveStart - aveEnd);

	// 	if (aveDif < 10 && streetDif > 0){
	// 		System.out.println("LOL");
	// 		int start = aveStart % 10 < aveEnd % 10 ? aveStart % 10 : aveEnd % 10;
	// 		int end = aveStart % 10 >= aveEnd % 10 ? aveStart % 10 : aveEnd % 10;
	// 		// System.out.println("start: " + start + " \t end : " + end);
	// 		aveDif = end + start < (10 - end) + (10 - start) ? end + start : (10 - end) + (10 - start);
	// 		if (aveStart / 10 == 49){
	// 			aveDif = end + start;
	// 		}
	// 	} 
	// 	// System.out.println("StreetDif: " + streetDif + " \t AveDif : " + aveDif);
	// 	int totalDist = (streetDif * 200) + (aveDif * 100);
	// 	return new Path(startPair, endPair, totalDist);
	// }











	// public Path nearestTo(Pair startPoint){
	// 	int street = startPoint.getStreet();
	// 	int avenue = calcY(startPoint.getAvenue(), startPoint.getName());
	// 	if (avenue % 2 == 1){
	// 		if(addNextTo(map[street][avenue + 1]) != null){
	// 			System.out.println("test1");
	// 			return new Path(startPoint, map[street][avenue + 1], 1);
	// 		}else if (addNextTo(map[street][avenue - 1]) != null){
	// 			System.out.println("test2");
	// 			return new Path(startPoint, map[street][avenue - 1], 1);
	// 		}

	// 	}else{
	// 		addNextTo(startPoint);
	// 	}


		

	// 	searched[street][avenue] = true;

	// 	Pair currSearch = new Pair();

	// 	while(searchQueue.peek() != null){
	// 		currSearch = (Pair) searchQueue.poll();
	// 		searched[currSearch.getStreet()][calcY(currSearch.getAvenue(), currSearch.getName())] = true;
	// 		Pair finalPair = addNextTo(currSearch);
	// 		if (finalPair != null){
	// 			// System.out.println("A Pair was found");
	// 			int dist = findDistance(finalPair);
	// 			return new Path(startPoint, finalPair, dist);
	// 		}
	// 	}
	// 	return null;
	// }

	// public Pair addNextTo(Pair point){

	// 	///SUCH A FUCKING IDIOT - alden - to be fixed
	// 	// nextFoundPoint(point);
	// 	int x = point.getStreet();
	// 	int y = calcY(point.getAvenue(), point.getName());
	// 	if(y % 2 == 0){
	// 		ArrayList<int[]> points = new ArrayList<int[]>();
	// 		if (y != 498){ 
	// 			points.add(new int[]{x, y + 1}); 
	// 			points.add(new int[]{x, y + 2});
	// 		}
	// 		if (y != 0){
	// 			points.add(new int[] {x, y - 1});
	// 			points.add(new int[] {x, y - 2});
	// 		}

	// 		if (y % 10 == 0){
	// 			if (x != 0){
	// 				points.add(new int[] {x - 1, x});
	// 			}
	// 			if (x != 250){
	// 				points.add(new int[] {x + 1, y});
	// 			}
	// 		}

	// 		for (int[] i : points){
	// 			if (!searched[i[0]][i[1]]){
	// 				map[i[0]][i[1]].setPair(point);
	// 				if(nextFoundPoint(map[i[0]][i[1]])){
	// 					System.out.println("Point found : " + i[0] + "\t" + i[1]);
	// 					return map[i[0]][i[1]];
	// 				}else{
	// 					searchQueue.add(map[i[0]][i[1]]);
	// 				}
	// 			}
	// 		}
	// 	}else{
	// 		if(nextFoundPoint(point)){
	// 			System.out.println("FOUND FAILEEEDDFEEDFEF");
	// 			return point;
	// 			// System.out.println("Point found : " + i[0] + "\t" + i[1]);
	// 			// return map[i[0]][i[1]];
	// 		}
	// 	}
	// 	return null;

	// }

	// public boolean nextFoundPoint(Pair nextPair){

	// 	//I think the error has to do with this line being here but idk
	// 	//EIther way we should move it to the correct spot.

	// 	searched[nextPair.getStreet()][calcY(nextPair.getAvenue(), nextPair.getName())] = true;

	// 	if(nextPair.getDeliver() > 0){
	// 		totalPackages += nextPair.getDeliver();
	// 		nextPair.setDeliver(0);

	// 		// System.out.println(nextPair.getStreet() + "\t" + nextPair.getAvenue());
	// 		while(searchQueue.peek() != null){
	// 			try{
	// 				searchQueue.remove();	
	// 			}catch(Exception e){
	// 				System.out.println("Removal failed");
	// 				return true;
	// 			}
	// 		}
	// 		// System.out.println("Finished clearing");
	// 		for (int i = 0; i < searched.length; i++){
	// 			for (int j = 0; j < searched[0].length; j++){
	// 				searched[i][j] = false;
	// 			}
	// 		}
	// 		return true;
	// 	}
	// 	return false;
	// }
}