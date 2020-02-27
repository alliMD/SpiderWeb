import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class q1b {
	
	// global variables
	q1a.SpiderWeb web;
	Random rnd = new Random();
	ArrayList<SpiderThread> spiders = new ArrayList<SpiderThread>();

	public static void main(String[] args) {
		int n, t, k, m;
		
		n = Integer.parseInt(args[0]); //number of points
		t = Integer.parseInt(args[1]); //number of threads
		k = Integer.parseInt(args[2]); //failure tolerance
		m = Integer.parseInt(args[3]); //seconds to run
		
		System.out.println("---- q1b parameters ----");
		System.out.println("n= "+n+" t= "+t+" k= "+k+" m= "+m);
		System.out.println("------------------------");
		
		q1b q1b = new q1b();
		q1b.execute(n, t, k, m);
	}
	
	public void execute(int n, int t, int k, int m) {
		// Create web
		q1a q1a = new q1a();
		web = q1a.execute(n, t, k);
		
		// Create Threads
		for(int i = 0; i < t; i++) {
			SpiderThread spider = new SpiderThread(n);
			spiders.add(spider);
		}
		
		// schedule a timer 
		new FutureScheduler(m);
		
		//start threads
		for(SpiderThread spider: spiders) {
			spider.start();
		}
		
		// wait for threads to end to show the final number of moves 
		// each spider has made
		for(SpiderThread spider: spiders) {
			try {
				spider.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		int dummyIndex = 1;
		for(SpiderThread spider: spiders) {
			System.out.println("Spider "+dummyIndex+" has jumped "+spider.totalJumps+" times");
			dummyIndex++;
		}
		
		System.out.println("---- q1b finished ----\n");
	}
	
	class SpiderThread extends Thread{
		q1a.Point body;
		q1a.Point[] legs = new q1a.Point[3];
		int totalPoints= 0; // total points in web
		int totalJumps = 0; // total jumps executed by the spider thread
		
		boolean stop = false;
		
		public SpiderThread(int n) {
			totalPoints = n;
		}
		
		public void run(){
			boolean failedToJump = false;
			while(!stop) {
				// get random index of a point in the web to place the body of the spider
				int bodyIndex = rnd.nextInt(totalPoints);
				q1a.Point bodyPoint = web.pointSet.get(bodyIndex);
				
				// acquire the lock for this point and check if room for legs
				synchronized(bodyPoint) {
					if(bodyPoint.occupied) {
						failedToJump = true;
					}
					else if(bodyPoint.adjacentPoints.size() < 3) {
						//not enough adjacent points
						failedToJump = true;
					}else {
						q1a.Point leg0 = bodyPoint.adjacentPoints.get(0);
						q1a.Point leg1 = bodyPoint.adjacentPoints.get(1);
						q1a.Point leg2 = bodyPoint.adjacentPoints.get(2);
						
						// acquire locks for the 3 other leg points
						synchronized(leg0) {
							if(leg0.occupied) {
								failedToJump = true;
							}else {
								// acquire locks for the 2 other leg points
								synchronized(leg1) {
									if(leg1.occupied) {
										failedToJump = true;
									}else {
										// acquire locks for the last leg point
										synchronized(leg2) {
											if(leg2.occupied) {
												failedToJump = true;
											}else {
												// Spider can make the jump!
												// note: we are holding 4 locks here
												leg0.occupied = true;
												leg1.occupied = true;
												leg2.occupied = true;
												bodyPoint.occupied = true;
												
												// release current occupied points
												freeOldPoints();
												
												// replace with new destination point
												this.legs[0] = leg0;
												this.legs[1] = leg1;
												this.legs[2] = leg2;
												this.body = bodyPoint;
												
												// increase jump counter
												this.totalJumps++;
											}
										} // release lock of leg2
									}
								} // release lock of leg1
							}
						} // release lock of leg0
					}
				} // release lock of body
				
				// all lock released
				if(failedToJump) {
					// sleep
					// toggle sleep time
					int selectedTime = rnd.nextInt(2);
					long sleepTime = selectedTime == 0 ? 40:50;
					
					try {
						Thread.sleep(sleepTime);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
			// Thread no longer tries to jump
		}
		
		public void freeOldPoints() {
			if(body != null) {
				synchronized(body) {
					synchronized(legs[0]) {
						synchronized(legs[1]) {
							synchronized(legs[2]){
								body.occupied = false;
								legs[0].occupied = false;
								legs[1].occupied = false;
								legs[2].occupied = false;
							}
						}
					}
				}
			}
			// else, the spider was not occupying any points
		}
		
		public void stopJumping() {
			stop = true;
		}
	}
	
	class FutureScheduler{
		Timer timer;
		
		FutureScheduler(int seconds){
			 timer = new Timer();
		     timer.schedule(new ScheduledTask(), seconds*1000);
		}
		
		class ScheduledTask extends TimerTask {
	        public void run() {
	            for(SpiderThread spider: spiders) {
	            	spider.stopJumping();
	            }
	            timer.cancel(); //Terminate the timer thread
	        }
	    }
	}

}
