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
		int totalDistance = 0;
		int gPackages = 0;
		do {
			gTime = -1;
			hv.resetDeliveries();
			nTrucks++;
			totalDistance = 0;
			gPackages = 0;
			// System.out.println(nTrucks);
			int[][] kMeanPoints = hv.kMeans(startPoint, nTrucks);

			boolean start = true;

			ArrayList<ArrayList<Path>> routes = new ArrayList<ArrayList<Path>>(); //Not using arrays because generic arrays are not supported...
			for (int i = 0; i < nTrucks; i++) {
				int distance = 0;
				int packages = 0;
				routes.add(hv.greedyRoute(startPoint, i));
				// clear();

				for (Path p : routes.get(i)) {
					if (start){
						start = false;
						// write(p.getStart().toString() + "\n");
					}
					// write(p.getEnd().toString() + "\n");
					totalDistance += p.getDistance();
					distance += p.getDistance();
					packages += p.getEnd().getDeliver();
				}
				double time = (packages * 60) + ((distance/100) * 3);
				if (gTime == -1 || time > gTime) {
					gTime = time;
					gPackages += packages;
					totalDistance = distance;
				}
				// System.out.println(time + "s = " + time/3600 + "h");
			}

			// for (ArrayList<Path> a : routes){
			// 	for (Path f : a){
			// 		System.out.println(f.getEnd().toString());
			// 	}
			// 	System.out.print("\n\n");
			// }

			clear();

			for (Pair[] a : hv.getMap()){
				for(Pair f : a){
					if (f.getDeliver() > 0){
						write(f.toString() + "\t" + f.getCluster() + "\t" + (f.delivered() ? "DELIVERED" : "NOT DELIVERED") + "\t" + (City.calcY(f) % 2 == 0 ? "EVEN\n" : "ODD\n"));
					}
				}
			}

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
		System.out.println("With " + nTrucks + " trucks, Homerville was delivered to in " + gTime/3600 + " hours, delivering " + gPackages + " packages and traveling " + totalDistance + " feet.");
		System.out.println("The total cost is:\tto be determined");

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
		// System.out.print(output);
	}



}
