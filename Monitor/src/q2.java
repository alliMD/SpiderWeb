import java.util.ArrayList;

public class q2 {
	
	public abstract class Monitor {
		public abstract void enter();
		public abstract void exit();
		public abstract void await() throws InterruptedException;
		public abstract void signal();
	}

	class MonitorSC extends Monitor{
		ArrayList<DummyThread> waiters = new ArrayList<DummyThread>();
		DummyThread currentThread;
		boolean locked = false;
		
		public synchronized void enter() {
			// Acquired the lock for this monitor
			
			// check condition
			while(!currentThread.condition || locked) {
				// wait for condition to be true
				try {
					await();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			waiters.remove(currentThread);
			locked = true;
			// critical section
		}
		
		public synchronized void exit() {
			locked = false;
			// notify
			signal();
		}
		
		public synchronized void await() throws InterruptedException{
			waiters.add(currentThread);
			wait();
		}
		
		public synchronized void signal() {
			notify();
		}
	}
	
	class MonitorSW extends Monitor{
		
		public void enter() {
			
		}
		
		public void exit() {
			
		}
		
		public void await() throws InterruptedException{
			
		}
		
		public void signal() {
			
		}
	}
	
	class MonitorSUW extends Monitor{
		
		public void enter() {
			
		}
		
		public void exit() {
			
		}
		
		public void await() throws InterruptedException{
			
		}
		
		public void signal() {
			
		}
	}
	
	class DummyThread {
		boolean condition;
	}
}
