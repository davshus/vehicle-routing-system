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
		for (int i = 0; i < nStreets; i++) {
			for (int j = 0; j < nAvenues * 10; j++) {
				map[i][j] = new Pair(i + 1, (j/10) + 1, (char)((j % 10) + (int)'A'));
			}
		}
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
		System.out.println(bart + " " + lisa);
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
	boolean[][] searched = new boolean[250][500];

	public Path nearestTo(Pair startPoint){
		int street = startPoint.getStreet();
		int avenue = calcY(startPoint.getAvenue(), startPoint.getName());
		if (avenue % 2 == 1){
			addNextTo(map[street][avenue + 1]);
			addNextTo(map[street][avenue - 1]);
		}else{
			addNextTo(startPoint);	
		}
		

		searched[street][avenue] = true;

		Pair currSearch = new Pair();

		while(searchQueue.peek() != null){
			System.out.print("\n");
			currSearch = (Pair) searchQueue.poll();
			searched[currSearch.getStreet()][calcY(currSearch.getAvenue(), currSearch.getName())] = true;
			Pair finalPair = addNextTo(currSearch);
			if (finalPair != null){
				System.out.println("A Pair was found");
				int dist = findDistance(finalPair);
				return new Path(startPoint, finalPair, dist);
			}
		}
		return null;
	}

	public Pair addNextTo(Pair point){
		nextFoundPoint(point);
		int x = point.getStreet();
		int y = calcY(point.getAvenue(), point.getName());
		if(y % 2 == 0){
			ArrayList<int[]> points = new ArrayList<int[]>();
			if (y != 498){ 
				points.add(new int[]{x, y + 1}); 
				points.add(new int[]{x, y + 2});
			}
			if (y != 0){
				points.add(new int[] {x, y - 1});
				points.add(new int[] {x, y - 2});
			}

			if (y % 10 == 0){
				if (x != 0){
					points.add(new int[] {x - 1, x});
				}
				if (x != 250){
					points.add(new int[] {x + 1, y});
				}
			}

			System.out.println("X: " + x);
			System.out.println("Y: " + y);

			for (int[] i : points){
				// System.out.println("X: " + i[0]);
				// System.out.println("Y: " + i[1]);

				if (!searched[i[0]][i[1]]){
					map[i[0]][i[1]].setPair(point);
					if(nextFoundPoint(map[i[0]][i[1]])){
						return map[i[0]][i[1]];
					}else{
						if (!searched[i[0]][i[1]]){
							searchQueue.add(map[i[0]][i[1]]);
						}
					}
				}
			}
		}
		return null;

	}

	public boolean nextFoundPoint(Pair nextPair){

		searched[nextPair.getStreet()][calcY(nextPair.getAvenue(), nextPair.getName())] = true;

		if(nextPair.getDeliver() > 0){
			nextPair.setDeliver(0);

			// System.out.println(nextPair.getStreet() + "\t" + nextPair.getAvenue());
			while(searchQueue.peek() != null){
				try{
					searchQueue.remove();	
				}catch(Exception e){
					System.out.println("Removal failed");
					return true;
				}
			}
			return true;
		}
		return false;
	}

	public int calcY(int ave, char name) {
		return ((ave - 1) * 10) + (name - 'A');
	}
}