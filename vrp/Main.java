package vrp;
import java.io.File;
import java.util.Scanner;
import vrp.Pair;
import vrp.City;
public class Main {


	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		try {
			City hv = new City();
			hv.setupMap(new File(sc.nextLine()));
		} catch (Exception e) {
			System.out.println("There was an error while setting up Homerville.");
		}
		
	}
}
