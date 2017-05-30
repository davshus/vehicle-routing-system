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

		// int totalCost = 0;
		int[] trucks = new int[11];
		double[] time = new double[11];
		double[] distance = new double[11];
		int[] packages = new int[11];
		ArrayList<ArrayList<Integer>> distPerClust = new ArrayList<ArrayList<Integer>>();
		ArrayList<ArrayList<Double>> times = new ArrayList<ArrayList<Double>>();



///OUT OF 12
		for (int i = 1; i < 11; i++){
			returnStatement r = vrp("cycle" + i + ".txt");
			trucks[i-1] = r.getTrucks();
			time[i-1] = r.getTime();
			distance[i-1] = r.getDistance();
			packages[i-1] = r.getPackages();
			// System.out.println("\n" + r.getClusterDistance());
			distPerClust.add(r.getClusterDistance());
			// System.out.println("\n" + r.getTimes());
			times.add(r.getTimes());
			System.out.println("Calculating: \t" + i);// + "\t" + r.getCluster() + "\t" + r.getTrucks() + "\t" + r.getClusterDistance() + "\n" + r.getTimes());
		}

		int[] tempTrucks = Arrays.copyOf(trucks, trucks.length - 1);
		Arrays.sort(tempTrucks);
		int median = tempTrucks[tempTrucks.length/2];
		System.out.println(median);
		int[] truckDist = new int[median];

		int totalCost = 0;

		totalCost += median * 100000;

		for (int f : trucks){
			System.out.println(f + "\tyeee");
		}

//OUT OF 11
		for (int i = 0; i < 10; i++){

			int cost = 0;

			ArrayList<Integer> distancesInThisCluster = distPerClust.get(i);

			for (int j = 0; j < distancesInThisCluster.size()-1; j++){
				if (j < truckDist.length){
					truckDist[j] += distancesInThisCluster.get(j);
					int miles = truckDist[j]/5000;
					cost += 1000*(miles/100);
				}
			}

			ArrayList<Double> thisTimes = times.get(i);
			for (int j = 0; j < thisTimes.size() - 1; j++){
				int hours = (int)((thisTimes.get(j)/60)/60);
				if (hours > 8){
					cost += 240 * 2;
					double lolTime = hours - 8;
					// lolTime -= 8;
					cost += lolTime * 45 * 2;
				}else{
					cost += hours * 30 * 2;
				}
			}

			cost += (distance[i]/5000) * 5;

						// for (int dist = truckDist.length; dist > 0; dist--){
				// int miles = truckDist[dist-1]/5000;
				// if (miles > 100){
					// truckDist[dist-1] -= 500000;
					// cost += 1000;
				// }
				// ArrayList<Integer> distPerCluster = distPerClust.get(i);

				// System.out.println(distPerCluster.get(dist));

				// if (distPerCluster.get(dist) != null){
					// System.out.println("lol");
						// truckDist[dist] += distPerCluster.get(dist);
						// System.out.println("lol2");
				// }

			if (trucks[i] > median){
				cost += 15000 * (trucks[i] - median);
			}
			totalCost += cost;
			System.out.println("Cycle: " + (i + 1) + "\tTrucks: " + trucks[i] + "\tTime: " + time[i] + "\tDistance: " + distance[i] + "\tPackages: " + packages[i] + "\tCost: " + cost);
		}
		System.out.println("Final cost: " + totalCost);





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
		ArrayList<Double> timeCluster;
		do {
			hv.resetDeliveries();
			nTrucks++;
			totalDistance = 0;
			gTime = -1;
			gPackages = 0;
			int[][] kMeanPoints = hv.kMeans(startPoint, nTrucks);
			distClust = new ArrayList<Integer>();
			timeCluster = new ArrayList<Double>();
			boolean start = true;
			int distance = 0;

			ArrayList<ArrayList<Path>> routes = new ArrayList<ArrayList<Path>>(); //Not using arrays because generic arrays are not supported...
			for (int i = 0; i < nTrucks; i++) {
				distance = 0;
				int packages = 0;
				routes.add(hv.greedyRoute(startPoint, i));

				for (Path p : routes.get(i)) {
					totalDistance += p.getDistance();
					distance += p.getDistance();
					
					packages += p.getEnd().getDeliver();
				}
				double time = (packages * 60 / 2) + ((distance/100) * 3);
				if (gTime == -1 || time > gTime) {
					gTime = time;
					gPackages += packages;
					totalDistance = distance;
				}
				distClust.add(distance);
				timeCluster.add(time);
			}
		} while (gTime/3600 > 24); // seconds / 3600 = hours
		writer.close();


		// System.out.println("The total cost is:\tto be determined");


		returnStatement r = new returnStatement(nTrucks, hv.totalPackages, round(gTime/3600, 4), totalDistance, nTrucks, timeCluster, distClust);
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

	private int trucks, packages, distance, cluster;
	private double time;
	private ArrayList<Integer> clusterDistance;
	private ArrayList<Double> times;

	public returnStatement(int trucks, int packages, double time, int distance, int cluster, ArrayList<Double> times, ArrayList<Integer> clusterDistance){
		this.trucks = trucks;
		this.packages = packages;
		this.time = time;
		this.distance = distance;
		this.cluster = cluster;
		this.times = times;
		this.clusterDistance = clusterDistance;
	}

	public returnStatement(String error){ System.out.println(error); }

	public int getTrucks(){ return this.trucks; }
	public int getPackages(){ return this.packages; }
	public double getTime(){ return this.time; }
	public int getDistance(){ return this.distance; }
	public int getCluster(){ return this.cluster; }
	public ArrayList<Double> getTimes(){ return this.times; }
	public ArrayList<Integer> getClusterDistance(){ return this.clusterDistance; }
}
