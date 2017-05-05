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

		hv.kMeans(4);

		int[][] kMeanPoints = hv.getKMeans();

		for (int i = 249; i >= 0; i--){
			for (int j = 499; j >= 0; j--){

				for (int[] k : kMeanPoints){
					if (k[0] == i && k[1] == j){
						write("*");
					}
				}

				write((hv.getMap()[i][j].getCluster() == 0) ? " " : Integer.toString(hv.getMap()[i][j].getCluster()));
			}
			write("\n");
		}


		int numOfTrucks = 1;


		while(true){
			
			
			numOfTrucks ++;
		}


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

	public static void write(String output){
		writer.print(output);
	}


}
