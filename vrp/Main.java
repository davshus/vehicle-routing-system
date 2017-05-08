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
		Scanner sc = new Scanner(System.in);
		City hv = null;
		try {
			writer = new PrintWriter("verify.txt", "UTF-8");
			hv = new City();
			hv.setupMap(new File(sc.nextLine()));
			// hv.setupMap(new File("cycle1PRACTICE.txt"));
			// write("Start With Point: 126 22 A")
		} catch (Exception e) {
			System.out.println("There was an error while setting up Homerville: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		Pair[][] map = hv.getMap();

		Pair startPoint = hv.getMap()[126][220];

		// hv.kMeans(startPoint, 4);

		// int[][] kMeanPoints = hv.getKMeans();

		// for (int i = 249; i >= 0; i--){
		// 	for (int j = 499; j >= 0; j--){

		// 		for (int[] k : kMeanPoints){
		// 			if (k[0] == i && k[1] == j){
		// 				write("*");
		// 			}
		// 		}

		// 		write((hv.getMap()[i][j].getCluster() == 0) ? " " : (hv.getMap()[i][j].getCluster() < 10 ? Integer.toString(hv.getMap()[i][j].getCluster()) : Character.toString((char)((hv.getMap()[i][j].getCluster() - 10) + (int)'a'))));
		// 	}
		// 	write("\n");
		// }


		int nTrucks = 0;
		double gTime = -1;
		int totalDistance;
		do {
			hv.resetDeliveries();
			nTrucks++;
			totalDistance = 0;
			System.out.println(nTrucks);
			int[][] kMeanPoints = hv.kMeans(startPoint, nTrucks);
			clear();
			for(int i = 249; i >= 0; i--){
				for (int j = 449; j >= 0; j--){
					int f = 0;
					for (int[] k : kMeanPoints){
						if (k[0] == i && k[1] == j){
							write((f < 10 ? f : Character.toString((char)((f - 10) + (int)'a'))) + "*");
						}
						f ++;
					}
					write((hv.getMap()[i][j].getCluster() == -1) ? " " : (hv.getMap()[i][j].getCluster() < 10 ? Integer.toString(hv.getMap()[i][j].getCluster()) : Character.toString((char)((hv.getMap()[i][j].getCluster() - 10) + (int)'a'))));
				}
				write("\n");
			}

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
				if (gTime == -1 || time > gTime) {
					gTime = time;
				}
				System.out.println(time + "s = " + time/3600 + "h");
			}
		} while (gTime/3600 > 24 && nTrucks < 2); // seconds / 3600 = hours
		System.out.println("With " + nTrucks + " trucks, Homerville was delivered to in " + gTime + " seconds, delivering " + hv.totalPackages + " packages and traveling " + totalDistance + " feet.");


		// ArrayList<Path> route = new ArrayList<Path>();

		// long totalDistance = 0;
		// int totalPackages = 0;

		// Pair startPoint = hv.getMap()[126][220];
		// Pair currentPair = startPoint;
		// while(currentPair != null){
		// 	Path nextPath = hv.nearestTo(currentPair);
		// 	if (nextPath != null) {
		// 		route.add(nextPath);
		// 		totalDistance += nextPath.getDistance();
		// 		currentPair = nextPath.getEnd();
		// 		totalPackages = hv.totalPackages;
		// 		write(currentPair.getStreet() + " " + currentPair.getAvenue() + " " + currentPair.getName() + " " + nextPath.getDistance());
		// 	}else{
		// 		route.add(currentPair.pathTo(startPoint));
		// 		System.out.println(currentPair.getName() + "   " + nextPath);
		// 		System.out.println("End");
		// 		writer.close();
		// 		break;
		// 	}
		// }
		// totalDistance += currentPair.distanceTo(startPoint);
		// System.out.println("hv.totalPackages = " + hv.totalPackages);
		// System.out.println("route.size() = " + route.size());
		// System.out.println("The total time (in seconds) is: " + (totalPackages * 60 + (totalDistance/100) * 3));

	}

	public static void clear(){
		try{
			PrintWriter w = new PrintWriter(new File("verify.txt"));
			w.write("");
			w.close();
		}catch(Exception e){
			System.out.print("Fail to clear");
		}
	}

	public static void write(String output){
		writer.print(output);
	}


}
