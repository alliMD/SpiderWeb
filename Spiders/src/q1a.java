import java.util.ArrayList;
import java.util.Random;
import java.lang.Math;

public class q1a {
	// Constants
	public static final float Boundary = 1000.0f;
	public static final float Margin = 0.2f; // threshold for comparing float values
	
	// Global Variables
	SpiderWeb web;
	Random rnd = new Random();
	
	public static void main(String[] args) {
		int n, t, k;
		
		n = Integer.parseInt(args[0]); //number of points
		t = Integer.parseInt(args[1]); //number of threads
		k = Integer.parseInt(args[2]); //failure tolerance
		
		System.out.println("n= "+n+" t= "+t+" k="+k);
		q1a q1a = new q1a();
		q1a.execute(n, t, k);
	}
	
	public void execute(int n, int t, int k) {
		web = new SpiderWeb(n, t, k);
		web.CreatePoints();
		TriangulationThread aThread = new TriangulationThread(10);
		aThread.run();
	}
	
	public class SpiderWeb {
		ArrayList<Point> pointSet;
		int totalPoints, totalThreads, failureTolerance;
		
		SpiderWeb(int n, int t, int k){
			this.totalPoints = n;
			this.totalThreads = t;
			this.failureTolerance = k;
			
			pointSet = new ArrayList<Point>();
		}
		
		public void CreatePoints() {
			// create corner points
			Point topLeft = new Point(0.0f, Boundary);
			Point bottomLeft = new Point(0.0f, 0.0f);
			Point topRight = new Point(Boundary, Boundary);
			Point bottomRight = new Point(Boundary, 0.0f);
			pointSet.add(topLeft);
			pointSet.add(bottomLeft);
			pointSet.add(topRight);
			pointSet.add(bottomRight);
			
			// Randomly create the (n-4) remaining points
			int remaining = totalPoints - 4;
			while(remaining > 0) {
				float randX = rnd.nextFloat() * Boundary;
				float randY = rnd.nextFloat() * Boundary;
				boolean overlap = false;
				
				//check if overlaps with another point
				for(Point point: pointSet) {
					if(Math.abs(randX-point.x) < Margin && Math.abs(randY-point.y) < Margin) {
						overlap = true;
						break;
					}
				}
				
				if(!overlap) {
					Point pointCreated = new Point(randX, randY);
					pointSet.add(pointCreated);
					remaining--;
				}
			}
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
		
		public String toString() {
			return "("+x+", "+y+")";
		}
	}
	
	public class TriangulationThread implements Runnable{
		int edgesToDraw = 0; 
		
		TriangulationThread(int k){
			edgesToDraw = k;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			while(edgesToDraw > 0) {
				// pick random indexes
				int n = web.pointSet.size();
				int index1 = rnd.nextInt(web.pointSet.size());
				int index2 = rnd.nextInt(web.pointSet.size());
				index2 = index2 == index1 ? (index1+1) % (n-1) : index2;
				System.out.println("index 1 = "+index1+"   index2 = "+index2);
				System.out.println(web.pointSet.get(0).toString());
				edgesToDraw--;
			}
			
		}
		
	}

}
