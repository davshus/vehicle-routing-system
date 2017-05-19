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
		for (int i = 1; i < 12; i++){
			returnStatement r = vrp("cycle" + i + ".txt");
			System.out.println("Cycle: " + i + "\tTrucks: " + r.getTrucks() + "\tTime: " + r.getTime() + "\tDistance: " + r.getDistance() + "\tPackages: " + r.getPackages());
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
		do {
			hv.resetDeliveries();
			nTrucks++;
			totalDistance = 0;
			gPackages = 0;
			int[][] kMeanPoints = hv.kMeans(startPoint, nTrucks);

			boolean start = true;

			ArrayList<ArrayList<Path>> routes = new ArrayList<ArrayList<Path>>(); //Not using arrays because generic arrays are not supported...
			for (int i = 0; i < nTrucks; i++) {
				int distance = 0;
				int packages = 0;
				routes.add(hv.greedyRoute(startPoint, i));

				for (Path p : routes.get(i)) {
					totalDistance += p.getDistance();
					distance += p.getDistance();
					packages += p.getEnd().getDeliver();
				}
				double time = (packages * 60) + ((distance/100) * 3);
				if (gTime == -1 || time < gTime) {
					gTime = time;
					gPackages += packages;
					totalDistance = distance;
				}
			}



			// clear();
			// for (ArrayList<Path> a : routes){
			// 	for (Path f : a){
			// 		write(f.getEnd().toString() + "\n");
			// 	}
			// 	write("\n\n");
			// }

			// for (Pair[] a : hv.getMap()){
			// 	for(Pair f : a){
			// 		if (f.getDeliver() > 0){
			// 			write(f.toString() + "\t" + f.getCluster() + "\t" + (f.delivered() ? "DELIVERED" : "NOT DELIVERED") + "\t" + (City.calcY(f) % 2 == 0 ? "EVEN" : "ODD") + "\t" + f.getDeliver() + "\n");

			// 		}
			// 	}
			// }

			// for(int z = 249; z >= 0; z--){
			// 		for (int j = 449; j >= 0; j--){
			// 			int f = 0;
			// 			for (int[] k : kMeanPoints){
			// 				if (k[0] == z && k[1] == j){
			// 					write((f < 10 ? f : Character.toString((char)((f - 10) + (int)'a'))) + "*");
			// 				}
			// 				f ++;
			// 			}
			// 			write((hv.getMap()[z][j].getCluster() == -1) ? " " : (hv.getMap()[z][j].getCluster() < 10 ? Integer.toString(hv.getMap()[z][j].getCluster()) : Character.toString((char)((hv.getMap()[z][j].getCluster() - 10) + (int)'a'))));
			// 		}
			// 		write("\n");
			// 	}

			// System.out.println(gTime/3600);
		} while (gTime/3600 > 24); // seconds / 3600 = hours
		writer.close();

		
		// System.out.println("The total cost is:\tto be determined");


		returnStatement r = new returnStatement(nTrucks, hv.totalPackages, round(gTime/3600, 4), totalDistance);
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

	private int trucks, packages;
	private double time, distance;

	public returnStatement(int trucks, int packages, double time, double distance){
		this.trucks = trucks;
		this.packages = packages;
		this.time = time;
		this.distance = distance;
	}

	public returnStatement(String error){ System.out.println(error); }

	public int getTrucks(){ return this.trucks; }
	public int getPackages(){ return this.packages; }
	public double getTime(){ return this.time; }
	public double getDistance(){ return this.distance; }
}

