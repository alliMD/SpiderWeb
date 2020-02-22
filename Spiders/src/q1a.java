import java.util.ArrayList;

public class q1a {
	// Constants
	public static final float Boundary = 1000.0f;
	
	public static void main(String[] args) {
		int n, t, k;
		
		n = Integer.parseInt(args[0]); //number of points
		t = Integer.parseInt(args[1]); //number of threads
		k = Integer.parseInt(args[2]); //failure tolerance
		
		System.out.println("n= "+n+" t= "+t+" k="+k);

	}
	
	public class SpiderWeb {
		ArrayList<Point> region;
		int totalPoints, totalThreads, failureTolerance;
		
		SpiderWeb(int n, int t, int k){
			this.totalPoints = n;
			this.totalThreads = t;
			this.failureTolerance = k;
			
			region = new ArrayList<Point>();
		}
		
		public void CreatePoints() {
			// create corner points
			Point topLeft = new Point(0.0f, Boundary);
			Point bottomLeft = new Point(0.0f, 0.0f);
			Point topRight = new Point(Boundary, Boundary);
			Point bottomRight = new Point(Boundary, 0.0f);
			region.add(topLeft);
			region.add(bottomLeft);
			region.add(topRight);
			region.add(bottomRight);
			
			// Randomly create the (n-4) remaining points
		}
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
