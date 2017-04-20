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
				bart = sc.nextInt();
				sc.next(); //Lisa
				lisa = sc.nextInt();
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
	boolean[][] searched = new boolean[250][500];

	public Pair floodFill(Pair startPoint){
		int street = startPoint.getStreet();
		int avenue = startPoint.getAvenue();
		addNextTo(startPoint);

		searched[street][avenue] = true;

		Pair fillerPair = new Pair();

		while(searchQueue.peek() != null){
			Pair currSearch = (Pair) searchQueue.poll();
			Pair finalPair = addNextTo(currSearch);
			if (finalPair != null){
				System.out.println("A Pair was found");
				return finalPair;
			}
		}
		//just for error stuf
		return fillerPair;
	}

	public Pair addNextTo(Pair point){
		nextFoundPoint(point);
		int x = point.getStreet();
		int y = point.getAvenue();


		ArrayList<int[]> points = new ArrayList<int[]>();
		int[] ex = new int[2];
		ex[0] = x;
		ex[1] = y + 1;
		points.add(ex);
		if (y != 498){ 
			points.add(ex); 
			ex[1] ++;
		}
		if (y != 0){
			ex[1] = y - 1;
			points.add(ex);
			ex[1]--;
			points.add(ex);
		}

		if (y % 10 == 0){
			if (x != 0){
				ex[0] = x - 1;
				ex[1] = y;
				points.add(ex);
			}
			if (x != 250){
				ex[0] = x + 1;
				points.add(ex);
			}
		}
		int[][] arr = new int[points.size()][2];
		arr = points.toArray(arr);

		for (int[] i : arr){
			if (searched[i[0]][i[1]] == false){
				map[i[0]][i[1]].setPair(point);
				if(nextFoundPoint(map[i[0]][i[1]])){
					return map[i[0]][i[1]];
				}else{
					searchQueue.add(map[i[0]][i[1]]);
				}
			}
		}
		return null;

	}

	public boolean nextFoundPoint(Pair nextPair){
		if(nextPair.getDeliver() > 0){
			System.out.println(nextPair.getStreet() + "\t" + nextPair.getAvenue());
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

	public int[][] findAllNextTo(int x, int y){
		ArrayList<int[]> points = new ArrayList<int[]>();
		int[] ex = new int[2];
		ex[0] = x;
		ex[1] = y + 1;
		points.add(ex);
		if (y != 498){ 
			points.add(ex); 
			ex[1] ++;
		}
		if (y != 0){
			ex[1] = y - 1;
			points.add(ex);
			ex[1]--;
			points.add(ex);
		}

		if (y % 10 == 0){
			if (x != 0){
				ex[0] = x - 1;
				ex[1] = y;
				points.add(ex);
			}
			if (x != 250){
				ex[0] = x + 1;
				points.add(ex);
			}
		}
		int[][] arr = new int[points.size()][2];
		arr = points.toArray(arr);
		return arr;
	}

}