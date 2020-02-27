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
	ArrayList<TriangulationThread> threads = new ArrayList<TriangulationThread>();
	int totalEdges = 0;
	
	public static void main(String[] args) {
		int n, t, k;
		
		n = Integer.parseInt(args[0]); //number of points
		t = Integer.parseInt(args[1]); //number of threads
		k = Integer.parseInt(args[2]); //failure tolerance
		
		System.out.println("n= "+n+" t= "+t+" k="+k+"\n");
		q1a q1a = new q1a();
		q1a.execute(n, t, k);
	}
	
	public SpiderWeb execute(int n, int t, int k) {
		web = new SpiderWeb(n, t, k);
		web.CreatePoints();
		
		for(int i = 0; i < t; i++) {
			//create t threads
			threads.add(new TriangulationThread(k));
		}
		
		for(TriangulationThread thread: threads) {
			thread.start();
		}
		
		for(TriangulationThread thread: threads) {
			try {
				// waits for this thread to die
				thread.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("\ntotal edges added = "+totalEdges);
		System.out.println("---- q1a finished ----\n");
		
		return web;
	}
	
	public class SpiderWeb {
		ArrayList<Point> pointSet;
		int totalPoints, totalThreads, failureTolerance;
		boolean stopAddingEdges = false;
		
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
		boolean occupied;
		
		Point(float x, float y){
			//coordinates
			this.x = x;
			this.y = y;
			this.adjacentPoints = new ArrayList<Point>();
			occupied = false; //default
		}
		
		// Monitor to access the adjacent point list of this object
		// returns true if end point of edge was successfully added to the list
		public synchronized boolean addEdge(Point endPoint) {
			// check whether the point has already been added
			if(adjacentPoints.contains(endPoint)) {
				//System.out.println("Failed to add: point already in list");
				return false;
			}else {
				adjacentPoints.add(endPoint);
			}
			
			return true;
		}
		
		public String toString() {
			return "("+x+", "+y+")";
		}
	}
	
	public class TriangulationThread extends Thread{
		int k = 0;
		boolean exit = false;
		
		TriangulationThread(int k){
			this.k = k;
		}

		@Override
		public void run() {
			int failedToAdd=0;
			int counter = 0;
			
			while(failedToAdd < k && !exit) {
				// pick random indexes
				int n = web.pointSet.size();
				int index1 = rnd.nextInt(web.pointSet.size());
				int index2 = rnd.nextInt(web.pointSet.size());
				
				// ensure that index1 and index 2 are not the same
				index2 = index2 == index1 ? (index1+1) % (n-1) : index2;
				
				Point point1 = web.pointSet.get(index1);
				Point point2 = web.pointSet.get(index2);
				
				// try adding reference from point 1 to point2
				boolean point2_wasAdded = point1.addEdge(point2);
				
				if(point2_wasAdded) {
					// add reference from point2 to point1
					point2.addEdge(point1);
					counter++;
					
				}else {
					failedToAdd++;
				}
				
				synchronized(web){
					exit = web.stopAddingEdges;
				}
			}
			
			// failed to add k numbers, set the stop all thread flag to true
			synchronized(web){
				if(!web.stopAddingEdges) {
					web.stopAddingEdges = true;
				}
				totalEdges = totalEdges + counter;
				System.out.println("thread "+ this.getId()+" added "+counter+" edges");
			}
		}
		
	}

}
