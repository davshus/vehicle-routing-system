package vrp;
import java.io.File;
import java.util.*;
import vrp.Pair;
import vrp.Avenue;
import tinshoes.geom.Point;
public class City {

	public int totalPackages;
	public int getDeliver(){ return totalPackages; }
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
		totalPackages = 0;
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
			// if (map[street - 1][calcY(avenue - 1, letter)].getDeliver() > 0) System.out.println(map[street - 1][calcY(avenue - 1, letter)].getDeliver());
			map[street - 1][calcY(avenue - 1, letter)].addDeliver();
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

	public int findDistance(Pair startPair){
		int distance = 0;
		Pair currentToCheck = startPair;
		while(currentToCheck != null && currentToCheck.getPair() != null) {

			// System.out.println(currentToCheck.getPair().getStreet());
			int distX = Math.abs(currentToCheck.getStreet() - currentToCheck.getPair().getStreet());
			int distY = Math.abs(calcY(currentToCheck.getAvenue(), currentToCheck.getName()) - calcY(currentToCheck.getPair().getAvenue(), currentToCheck.getPair().getName()));
			distance += distX * 200;
			distance += distX * 100;
			currentToCheck = currentToCheck.getPair();
		}
		return distance;

	}

	Queue<Pair> searchQueue = new LinkedList<Pair>();
	boolean[][] searched;
	Point[] kMeanPoints;


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
	public Path nearestTo(Pair t) {
		Pair start = null;
		if (calcY(t) % 2 != 0) {
			Pair l = checkUpDown(t);
			if (l != null) {
				totalPackages += map[l.getStreet()][calcY(l)].getDeliver();
				map[l.getStreet()][calcY(l)].deliver();
				return t.pathTo(map[l.getStreet()][calcY(l)]);
			}
			start = map[t.getStreet()][calcY(t) + (calcY(t) != (nAvenues * 10) - 1 ? 1 : -1)];
		} else {
			start = t;
		}
		//Implementation of modified flood fill
		searched = new boolean[nStreets][nAvenues * 10];
		// Arrays.fill(searched, Arrays.fill(new boolean[250], false));
		for (int i = 0; i < searched.length; i++)
			for (int j = 0; j < searched[0].length; j++)
				searched[i][j] = false;
		searched[start.getStreet()][calcY(start)] = true;
		ArrayList<Pair> ring = new ArrayList<Pair>();
		ring.add(start);
		// boolean debug = false;//totalPackages > 776;
		while (true) {
			if (ring.isEmpty()) return null;
			ArrayList<Pair> newRing = new ArrayList<Pair>();
			// if (debug) System.out.println(Arrays.toString(ring.toArray()));
			for (Pair p : ring) {
				if (p.getDeliver() > 0) {
					totalPackages += map[p.getStreet()][calcY(p)].getDeliver();
					map[p.getStreet()][calcY(p)].deliver();
					return t.pathTo(map[p.getStreet()][calcY(p)]);
				}
				Pair upDown = checkUpDown(p);
				if (upDown != null) {
					// System.out.println(p.getStreet() + " " + calcY(p));
					// System.out.println(upDown.getStreet() + " " + calcY(upDown));
					totalPackages += map[upDown.getStreet()][calcY(upDown)].getDeliver();
					map[upDown.getStreet()][calcY(upDown)].deliver();
					return t.pathTo(map[upDown.getStreet()][calcY(upDown)]);
				}
				newRing.addAll(calcAround(p));
				// if (debug) System.out.println(calcY(p));
			}
			// if (debug) System.out.println(Arrays.toString(ring.toArray()));
			ring = newRing;
			// if (debug) System.out.println(Arrays.toString(ring.toArray()));
			// if (debug) new Scanner(System.in).next();
		}

	}
	public Pair checkUpDown(Pair center) {
		//CENTER Y MUST BE EVEN
		// System.out.println(center);
		if (calcY(center) % 2 != 0) {
			// System.out.println("CENTER NOT EVEN");
//			new Scanner(System.in).next();
		}
		int y = calcY(center), x = center.getStreet();
		// System.out.println(x + " " + y);
		if (y != (nAvenues * 10) - 1 && !searched[x][y + 1] && map[x][y + 1].getDeliver() > 0) {
			searched[x][y + 1] = true;
			return map[x][y + 1];
		}
		if (y != 0 && !searched[x][y - 1] && map[x][y - 1].getDeliver() > 0) {
			searched[x][y - 1] = true;
			return map[x][y - 1];
		}
		if (y != (nAvenues * 10) - 1) searched[x][y + 1] = true;
		if (y != 0) searched[x][y - 1] = true;
		return null;
	}
	public ArrayList<Pair> calcAround(Pair center) {
		//CENTER Y MUST BE EVEN
		if (calcY(center) % 2 != 0) {
			// System.out.println("CENTER NOT EVEN");
			// new Scanner(System.in).next();
		}
                // System.out.println(center);
		ArrayList<Pair> ans = new ArrayList<Pair>();
		int x = center.getStreet(), y = calcY(center);
		if (y % 10 == 0) {
			if (x != 0 && !searched[x - 1][y]) {
				ans.add(map[x - 1][y]);
				searched[x - 1][y] = true;
			}
			if (x != nStreets - 1 && !searched[x + 1][y]) {
				ans.add(map[x + 1][y]);
				searched[x + 1][y] = true;
			}
		}
		if (y != 0 && !searched[x][y - 2]) {
			ans.add(map[x][y - 2]);
			searched[x][y - 2] = true;
		}
		if (y != (nAvenues * 10) - 2 && !searched[x][y + 2]) {
			ans.add(map[x][y + 2]);
			searched[x][y + 2] = true;
		}
		return ans;
	}
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
	// 		// System.out.println("X: " + x);
	// 		// System.out.println("Y: " + y);

	// 		for (int[] i : points){
	// 			// System.out.println("X: " + i[0]);
	// 			// System.out.println("Y: " + i[1]);

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
	public void kMean(int trucks) {
		kMeanPoints = new Point[trucks];
		double deg = ((double)360)/trucks;
		while (true) {
			/* There is a problem.
			 * The map is not square
			 * Solution: dilate x?
			 */
		}
	}
	public int calcY(int ave, char name) {
		// System.out.println(ave);
		return (ave * 10) + (name - 'A');
	}
	public int calcY(Pair p) {
		return calcY(p.getAvenue(), p.getName());
	}
}