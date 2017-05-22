package vrp;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import vrp.Pair;
import vrp.City;
public class Main {

	static PrintWriter writer;


	public static void main(String[] args) {


		int[] trucks = new int[11];
		double[] time = new double[11];
		double[] distance = new double[11];
		int[] packages = new int[11];
		ArrayList<ArrayList<Integer>> distPerClust = new ArrayList<ArrayList<Integer>>();



///OUT OF 12
		for (int i = 1; i < 11; i++){
			returnStatement r = vrp("cycle" + i + ".txt");
			trucks[i-1] = r.getTrucks();
			time[i-1] = r.getTime();
			distance[i-1] = r.getDistance();
			packages[i-1] = r.getPackages();
			distPerClust.add(r.getClusterDistance());
			System.out.println("Calculating: \t" + i);
		}

		int[] tempTrucks = trucks;
		Arrays.sort(tempTrucks);
		int median = tempTrucks[tempTrucks.length/2];

		int[] truckDist = new int[median];

		int totalCost = 0;

		for (int i = 0; i < 11; i++){

			int cost = 0;

			for (int dist = truckDist.length; dist > 0; dist--){
				int miles = truckDist[dist-1]/5000;
				if (miles > 100){
					truckDist[dist-1] -= 500000;
					cost += 1000;
				}
				ArrayList<Integer> distPerCluster = distPerClust.get(i-1);
				truckDist[dist] += distPerCluster.get(dist);
				cost += miles * 5;
			}
			if (trucks[i] > median){
				cost += 15000 * (trucks[i] - median);
			}
			totalCost += cost;
			System.out.println("Cycle: " + (i + 1) + "\tTrucks: " + trucks[i] + "\tTime: " + time[i] + "\tDistance: " + distance[i] + "\tPackages: " + packages[i] + "\tCost: " + cost);
		}






	}


	public static returnStatement vrp(String fileName){
		City hv = null;
		try {
			writer = new PrintWriter("verify.txt", "UTF-8");
			hv = new City();
			hv.setupMap(new File(fileName));
		} catch (Exception e) {
			// System.out.println("There was an error while setting up Homerville: " + e.getMessage());
			e.printStackTrace();
			return new returnStatement("There was an error while setting up Homerville: " + e.getMessage());
		}
		Pair[][] map = hv.getMap();
		Pair startPoint = hv.getMap()[126][220];

		int nTrucks = 0;
		double gTime = -1;
		int totalDistance = 0;
		int gPackages = 0;
		ArrayList<Integer> distClust;
		do {
			hv.resetDeliveries();
			nTrucks++;
			totalDistance = 0;
			gPackages = 0;
			int[][] kMeanPoints = hv.kMeans(startPoint, nTrucks);
			distClust = new ArrayList<Integer>();
			boolean start = true;

			ArrayList<ArrayList<Path>> routes = new ArrayList<ArrayList<Path>>(); //Not using arrays because generic arrays are not supported...
			for (int i = 0; i < nTrucks; i++) {
				int distance = 0;
				int packages = 0;
				routes.add(hv.greedyRoute(startPoint, i));

				for (Path p : routes.get(i)) {
					totalDistance += p.getDistance();
					distance += p.getDistance();
					distClust.add(p.getDistance());
					packages += p.getEnd().getDeliver();
				}
				double time = (packages * 60) + ((distance/100) * 3);
				if (gTime == -1 || time < gTime) {
					gTime = time;
					gPackages += packages;
					totalDistance = distance;
				}
			}
		} while (gTime/3600 > 24); // seconds / 3600 = hours
		writer.close();


		// System.out.println("The total cost is:\tto be determined");


		returnStatement r = new returnStatement(nTrucks, hv.totalPackages, round(gTime/3600, 4), totalDistance, distClust);
		return r;
	}

	public static double round(double value, int places) {
    	if (places < 0) throw new IllegalArgumentException();

    	long factor = (long) Math.pow(10, places);
    	value = value * factor;
    	long tmp = Math.round(value);
    	return (double) tmp / factor;
	}

	public static void clear(){

		writer.close();
		try{
			writer = new PrintWriter("verify.txt", "UTF-8");
		}catch(Exception e){
			System.out.print("Fail to clear");
		}
	}

	public static void write(String output){
		writer.print(output);
	}

}

class returnStatement{

	private int trucks, packages, distance;
	private double time;
	private ArrayList<Integer> clusterDistance;

	public returnStatement(int trucks, int packages, double time, int distance, ArrayList<Integer> clusterDistance){
		this.trucks = trucks;
		this.packages = packages;
		this.time = time;
		this.distance = distance;
		this.clusterDistance = clusterDistance;
	}

	public returnStatement(String error){ System.out.println(error); }

	public int getTrucks(){ return this.trucks; }
	public int getPackages(){ return this.packages; }
	public double getTime(){ return this.time; }
	public int getDistance(){ return this.distance; }
	public ArrayList<Integer> getClusterDistance(){ return this.clusterDistance; }
}
