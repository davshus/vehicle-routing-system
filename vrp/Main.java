package vrp;
import java.util.*;
import java.io.File;
import java.io.PrintWriter;
import java.util.Scanner;
import vrp.Pair;
import vrp.City;
public class Main {


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		City hv = null;
		try {
			hv = new City();
			hv.setupMap(new File(sc.nextLine()));
		} catch (Exception e) {
			System.out.println("There was an error while setting up Homerville: " + e.getMessage());
			e.printStackTrace();
			return;
		}
		Pair[][] map = hv.getMap();

		ArrayList<Path> route = new ArrayList<Path>();

		// while (true) {
		// 	int street = sc.nextInt(), avenue = sc.nextInt(), letter = sc.next().charAt(0);
		// 	if (street == -1) break;
		// 	Pair curr = map[street - 1][(((avenue - 1) * 10) + (letter - 'A'))];
		// 	System.out.println(curr.getStreet() + " " + curr.getAvenue() + " " + curr.getName() + " " + curr.getDeliver());
		// }

		int totalDistance = 0;
		int totalPackages = 0;

		Pair startPoint = hv.getMap()[126][220];
		Pair currentPair = startPoint;

		while(currentPair != null){
			Path nextPath = hv.nearestTo(currentPair);
			if (nextPath != null){
				totalDistance += nextPath.getDistance();
				currentPair = nextPath.getEnd();
				totalPackages += currentPair.getDeliver();
			}else{
				break;
			}
		}
		totalDistance += currentPair.distanceTo(startPoint);

		System.out.println("The total time (in seconds) is: " + (totalPackages * 60 + totalDistance * 3));

	}
}
