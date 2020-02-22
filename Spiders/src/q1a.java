import java.util.ArrayList;

public class q1a {
	// Constants
	public static final float Boundary = 1000.0f;
	// Global Variables
	ArrayList<Point> region = new ArrayList<Point>();

	public static void main(String[] args) {
		int n, t, k;
		
		n = Integer.parseInt(args[0]); //number of points
		t = Integer.parseInt(args[1]); //number of threads
		k = Integer.parseInt(args[2]); //failure tolerance
		
		System.out.println("n= "+n+" t= "+t+" k="+k);

	}
	
	public static void CreatePoints() {
		
	}
	
	public class Point{
		float x, y;
		ArrayList<Point> adjacentPoints; 
		
		Point(float x, float y){
			//coordinates
			this.x = x;
			this.y = y;
			this.adjacentPoints = new ArrayList<Point>();
		}
		
		// Monitor to access the adjacent point list of this object
		public synchronized void accessAdjacentPoints() {
			
		}
	}

}
