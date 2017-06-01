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
	private ArrayList<Pair> deliveriesPairs;
	private int lisa, bart;

	public ArrayList<Pair> getdeliverArr(){ return this.deliveriesPairs; }

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

		deliveriesPairs = new ArrayList<Pair>();

		// System.out.println("Setting up Homerville...");
		Scanner sc = new Scanner(inputFile).useDelimiter(",|\r?\n\r?|\n?\r\n?");
		String lol = sc.next();
		String placeHolder = sc.next();
		// System.out.println("Cycle: " + sc.next() + " House Number: " + sc.next());
		while (sc.hasNext()) { //NOTE: Possible change to fixed house number counter?
			String streetStr = sc.next();
			if (streetStr.equals("Bart Complex")) {
				bart = sc.nextInt();
				map[2][36].setDeliver(bart);
				sc.next(); //Lisa
				lisa = sc.nextInt();
				map[149][336].setDeliver(lisa);
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

		for (Pair[] m : map){
			for (Pair l : m){
				if (l.getDeliver() > 0){
					deliveriesPairs.add(l);
				}
			}
		}

		System.out.println(deliveriesPairs.size());



	}

	public void resetDeliveries() {
		for (int i = 0; i < map.length; i++) {
			for (int j = 0; j < map[0].length; j++) {
				map[i][j].setDelivered(false);
				map[i][j].setCluster(-1);
			}
		}
		map[2][36].setDeliver(bart);
		map[149][336].setDeliver(lisa);
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



	//TO TEST
 	public ArrayList<Path> greedyRoute(Pair startPoint, int cluster) {
 		ArrayList<Path> route = new ArrayList<Path>();
 		Pair currentPair = startPoint;
 		while(currentPair != null){
 			// System.out.println("run!");
 			Path nextPath = nearestToFast(currentPair, cluster);
 			if (nextPath != null) {
 				// System.out.println("yes!");
 				route.add(nextPath);
 				currentPair = nextPath.getEnd();
			}else{
				route.add(currentPair.pathTo(startPoint));
				// System.out.println("ugh");
 				break;
 			}
 		}
 		return route;
 	}


 	public Path nearestToFast(Pair t, int cluster){
 		Pair closestPair = null;
 		Pair bartLisa = null;
 		int closestDist = Integer.MAX_VALUE;

 		for (Pair thisPair : deliveriesPairs){
 			if(thisPair.getCluster() == cluster && t.distanceTo(thisPair) < closestDist && !thisPair.delivered() && thisPair != t && thisPair != bartLisa){
 				closestPair = thisPair;
 				closestDist = t.distanceTo(thisPair);
 				


 				/*
				if (p != t && p.getDeliver() > 0 && !p.delivered() && p.getCluster() == cluster) {

					if (p.getDeliver() > 100){
						totalPackages += 100;
						p.setDeliver(p.getDeliver() - 100);
						return t.pathTo(map[p.getStreet()][calcY(p)]);
					}else{
						totalPackages += map[p.getStreet()][calcY(p)].getDeliver();
						map[p.getStreet()][calcY(p)].deliver();
						return t.pathTo(map[p.getStreet()][calcY(p)]);
					}
				}
 				*/



 			}
 		}

 		if(closestPair == null){
 			return null;
 		}


 		if(closestPair.getDeliver() > 100){
 			totalPackages += 100;
 			closestPair.setDeliver(closestPair.getDeliver() - 100);
 			bartLisa = closestPair;
 			return t.pathTo(closestPair);
 		}else{
 			bartLisa = null;
 			totalPackages += closestPair.getDeliver();
 			closestPair.deliver();
		}



 		// System.out.println("This is a try");
 		return new Path(t, closestPair, closestDist);

 	}





 	//TO TEST
 	public Path nearestTo(Pair t, int cluster) {
 		// System.out.println(cluster;)
		Pair start = null;
		if (calcY(t) % 2 != 0) {
			Pair l = checkUpDown(t, cluster);
			// if (l != null) System.out.println(l.getCluster());
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
		// boolean isStart = true;
		// boolean debug = false;//totalPackages > 776;
		while (true) {
			if (ring.isEmpty()) return null;
			ArrayList<Pair> newRing = new ArrayList<Pair>();
			// if (debug) System.out.println(Arrays.toString(ring.toArray()));
			for (Pair p : ring) {
				// if (p.getDeliver() > 0) System.out.println(p.getCluster());
				if (p != t && p.getDeliver() > 0 && !p.delivered() && p.getCluster() == cluster) {

					if (p.getDeliver() > 100){
						totalPackages += 100;
						p.setDeliver(p.getDeliver() - 100);
						return t.pathTo(map[p.getStreet()][calcY(p)]);
					}else{
						totalPackages += map[p.getStreet()][calcY(p)].getDeliver();
						map[p.getStreet()][calcY(p)].deliver();
						return t.pathTo(map[p.getStreet()][calcY(p)]);
					}
				}
				// isStart = false;
				Pair upDown = checkUpDown(p, cluster);
				// if (upDown != null) System.out.println(upDown.getCluster());
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
	public Pair checkUpDown(Pair center, int cluster) {
		//CENTER Y MUST BE EVEN
		// System.out.println(center);
		if (calcY(center) % 2 != 0) {
			// System.out.println("CENTER NOT EVEN");
//			new Scanner(System.in).next();
		}
		int y = calcY(center), x = center.getStreet();
		// System.out.println(x + " " + y);
		if (y != (nAvenues * 10) - 1 && !searched[x][y + 1] && map[x][y + 1].getDeliver() > 0 && map[x][y + 1].getCluster() == cluster && !map[x][y + 1].delivered()) {
			searched[x][y + 1] = true;
			return map[x][y + 1];
		}
		if (y != 0 && !searched[x][y - 1] && map[x][y - 1].getDeliver() > 0 && map[x][y - 1].getCluster() == cluster && !map[x][y - 1].delivered()) {
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

	public int[][] kMeansStartPoints(Pair startPoint, int trucks){

		int[][] res = new int[trucks][2];
		double baseDeg = ((double)360)/trucks;
		
		int w = map.length, h = map[0].length;
		// System.out.println(startPoint.getAvenue() + ", " + startPoint.getStreet())
		//x is stretched by 2 to make the map a square
		// double radius = Math.sqrt(Math.pow(xSize / 2, 2) + Math.pow(ySize / 2, 2));
		for (int i = 0; i < trucks; i++) {
			double currDeg = i * baseDeg;
			int x = 0, y = 0;
			if (currDeg <= 180) {
				y = h - calcY(startPoint);
				x = currDeg <= 90 ? w - startPoint.getStreet() : startPoint.getStreet(); 
			} else {
				y = calcY(startPoint);
				x = currDeg >= 270 ? w - startPoint.getStreet() : startPoint.getStreet();
			}
			double currRadius = Math.min(Math.abs(x/Math.cos(Math.toRadians(currDeg))), Math.abs(y/Math.sin(Math.toRadians(currDeg))));
			// System.out.println(currRadius);
			double currPointDist = Math.abs(currRadius/2);
			// System.out.println(currPointDist);
			double currX = currPointDist * Math.cos(Math.toRadians(currDeg)), currY = currPointDist * Math.sin(Math.toRadians(currDeg));
			currX += startPoint.getStreet();
			currY += calcY(startPoint);
			// double x = (radius * Math.sin(Math.toRadians(currDeg)))/2, y = (radius * Math.cos(Math.toRadians(currDeg)))/2;
			// x /= 2;
			// x += map.length / 2; y += map[0].length / 2;
			// System.out.println(currX + "," + currY + " - " + currDeg);
			res[i][0] = (int)currX; res[i][1] = (int)currY;
		}
		return res;
	}

	public void test(){
		// for(Pair[] i : map){
			// for (Pair j : i){
				// if (j.getCluster() == -1 && j.getDeliver() > 0){
					// System.out.println("Pair without a cluster");
				// }
			// }
		// }
	}

	public int[][] kMeans(Pair startPoint, int trucks) {
// System.out.println("wtf");
		totalPackages = 0;

		if(trucks == 1){
			// System.out.println("break");
			for (Pair[] i : map){
				for (Pair j : i){
					j.setCluster(0);
				}	
			}
			return new int[][]{{map.length / 2, map[0].length / 2}};
		}

		for (Pair[] i : map){
			for (Pair j : i){
				j.setCluster(-1);
			}	
		}

		// System.out.println("break 1");

		int[][] kMeansPoints = kMeansStartPoints(startPoint, trucks);

		// System.out.println("break 2");

		while (true) {

			int[][] averages = new int[trucks][2];
			int[] aveNums = new int[trucks];

			// System.out.println("break 3");

			for (Pair[] i : map){
				for (Pair j : i){
					if(j.getDeliver() > 0){
						
						// System.out.println("Street: " + j.getStreet() + "\t Avenue: " + j.getCalcAvenue());

						int index = 0;
						int greatest = Integer.MAX_VALUE;

						for(int ind = 0; ind < trucks; ind++){
							if(j.distanceTo(map[kMeansPoints[ind][0]][kMeansPoints[ind][1]]) < greatest){
								greatest = j.distanceTo(map[kMeansPoints[ind][0]][kMeansPoints[ind][1]]);
								index = ind;
							}
						}
						// System.out.println(index);
						j.setCluster(index);

						for (int lol = 0; lol < j.getDeliver(); lol++){
							averages[index][0] += j.getStreet();
							averages[index][1] += j.getCalcAvenue();
							aveNums[index] += 1;
						}
					}else{
						j.setCluster(-1);
					}
				}	
			}
			// System.out.println("break 4");
			boolean works = true;

			for (int i = 0; i < trucks; i ++){
				if (aveNums[i] == 0) {
					continue;
				}
				averages[i][0] /= aveNums[i];
				averages[i][1] /= aveNums[i]; 
				if (averages[i][0] != kMeansPoints[i][0] || averages[i][1] != kMeansPoints[i][1]){
					works = false;
					kMeansPoints[i][0] = averages[i][0];
					kMeansPoints[i][1] = averages[i][1];
				}
			}
			if (works) {
				test();
				return kMeansPoints;
			}
		}
		
	}
	public static int calcY(int ave, char name) {
		// System.out.println(ave);
		return (ave * 10) + (name - 'A');
	}
	public static int calcY(Pair p) {
		return calcY(p.getAvenue(), p.getName());
	}
}
