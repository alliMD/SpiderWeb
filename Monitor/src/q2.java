import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class q2 {
	
	public abstract class Monitor {
		public abstract void enter(DummyThread t);
		public abstract void exit(DummyThread t);
		public abstract void await() throws InterruptedException;
		public abstract void signal(DummyThread t);
	}

	class MonitorSC extends Monitor{
		Queue<Boolean> conditions = new LinkedList<Boolean>();
		ArrayList<DummyThread> waiters = new ArrayList<DummyThread>();
		boolean locked = false;
		
		public synchronized void enter(DummyThread t) {
			// Acquired the lock for this monitor
			
			// check condition
			while(!t.condition || locked) {
				// wait for condition to be true and acquire lock
				try {
					conditions.add(t.condition);
					waiters.add(t);
					await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			waiters.remove(t);
			locked = true;
			// critical section
		}
		
		public synchronized void exit(DummyThread t) {
			locked = false;
			// notify
			signal(t);
		}
		
		public synchronized void await() throws InterruptedException{
			wait();
		}
		
		public synchronized void signal(DummyThread t) {
			notify();
		}
	}
	
	class MonitorSW extends Monitor{
		Queue<Boolean> conditions = new LinkedList<Boolean>();
		ArrayList<DummyThread> waiters = new ArrayList<DummyThread>();
		boolean locked = false;
		
		public synchronized void enter(DummyThread t) {
			// Acquired the lock for this monitor
			
			// check condition
			while(!t.condition || locked) {
				// wait for condition to be true and acquire lock
				try {
					conditions.add(t.condition);
					waiters.add(t);
					await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			waiters.remove(t);
			locked = true;
			// critical section
		}
		
		public synchronized void exit(DummyThread t) {
			locked = false;
			// notify
			signal(t);
		}
		
		public synchronized void await() throws InterruptedException{
			wait();
		}
		
		public synchronized void signal(DummyThread t) {
			try {
				// notify and go to sleep
				conditions.add(t.condition);
				waiters.add(t);
				notify();
				await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	class MonitorSUW extends Monitor{
		Queue<Boolean> conditions = new LinkedList<Boolean>();
		ArrayList<DummyThread> waiters = new ArrayList<DummyThread>();
		Queue<DummyThread> wakeNext = new LinkedList<DummyThread>();
		boolean locked = false;
		
		public synchronized void enter(DummyThread t) {
			// Acquired the lock for this monitor
			
			// check condition
			while(!t.condition || locked) {
				// wait for condition to be true and acquire lock
				try {
					conditions.add(t.condition);
					waiters.add(t);
					await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			waiters.remove(t);
			locked = true;
			// critical section
		}
		
		public synchronized void exit(DummyThread t) {
			locked = false;
			// notify
			signal(t);
		}
		
		public synchronized void await() throws InterruptedException{
			wait();
		}
		
		public synchronized void signal(DummyThread t) {
			try {
				// notify and go to sleep
				conditions.add(t.condition);
				waiters.add(t);
				notifySpecific(wakeNext.remove());
				wakeNext.add(t);
				await();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		public synchronized void notifySpecific(DummyThread t) {
			// check if a thread needs to be awaken next
			if(t == null) {
				notify();
			}else {
				while(!waiters.contains(t)) {
					try {
						wait();
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				waiters.remove(t);
				notifyAll();
			}
		}
	}
	
	class DummyThread extends Thread{
		boolean condition = false;
		
		public void run() {
			
		}
	}
}
