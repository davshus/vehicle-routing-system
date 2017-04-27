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


		while (true) {


 			int street = sc.nextInt();
 			if (street == -1) break;
 			int avenue = sc.nextInt(), letter = sc.next().charAt(0);
 			int street2 = sc.nextInt(), avenue2 = sc.nextInt(), letter2 = sc.next().charAt(0);
 			Pair curr = map[street - 1][(((avenue - 1) * 10) + (letter - 'A'))];
 			Pair curr2 = map[street2 - 1][(((avenue2 - 1) * 10) + (letter2 - 'A'))];
 			System.out.println(hv.getPath(curr, curr2).getDistance());
  		}

		ArrayList<Path> route = new ArrayList<Path>();

		long totalDistance = 0;
		int totalPackages = 0;

		Pair startPoint = hv.getMap()[126][220];
		Pair currentPair = startPoint;

		while(currentPair != null){
			Path nextPath = hv.closestTo(currentPair);
			if (nextPath != null){
				totalDistance += nextPath.getDistance();
				currentPair = nextPath.getEnd();
				write(currentPair.getStreet() + " " + currentPair.getAvenue() + " " + currentPair.getName() + " " + nextPath.getDistance());
			}else{
				totalPackages = hv.totalPackages;
				System.out.println("End");
				writer.close();
				break;
			}
		}

		totalDistance += currentPair.distanceTo(startPoint);
		System.out.println("The total time (in seconds) is: " + (totalPackages * 60 + (totalDistance/100) * 3));

	}

	public static void write(String output){
		writer.println(output);
	}


}
