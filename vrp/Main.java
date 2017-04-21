package vrp;
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
		while (true) {
			int street = sc.nextInt(), avenue = sc.nextInt(), letter = sc.next().charAt(0);
			Pair curr = map[street - 1][(((avenue - 1) * 10) + (letter - 'A'))];
			System.out.println(curr.getStreet() + " " + curr.getAvenue() + " " + curr.getName() + " " + curr.getDeliver());
		}
		
	}
}
