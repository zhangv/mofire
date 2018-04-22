package com.modofo.jmeutil;


public class Timer {
	boolean paused = true;
	boolean stopped = false;
	private TimerListener listener;
	private long updateFreq = 1; // notfiy frequency in seconds
	private long elapse = 0; // elapse time in seconds
	private Thread tickingThread;
	
	public void setListener(TimerListener listener) {
		this.listener = listener;
	}

	public Timer() {
	}

	// Stops the thread
	public synchronized void killThread() {
		stopped = true;
		tickingThread.interrupt();
	}

	// Determines whether or not the thread is running
	public synchronized boolean threadIsRunning() {
		return !stopped;
	}

	// Starts the stopwatch (or resumes after being paused)
	public synchronized void start() {
		paused = false;
		// Wake up the thread
		notifyAll();
		tickingThread = new Thread() {
			public void run() {
				while (threadIsRunning()) {
					try {
						if (!isPaused()) {
							Thread.sleep(updateFreq * 1000);
							elapse += updateFreq;
							listener.onUpdate(elapse);
						} else {
							synchronized (this) {
								wait();
							}
						}
					} catch (InterruptedException e) {
						
					}
				}
			}
		};
		tickingThread.start();
	}

	// Pauses the stopwatch
	public synchronized void pause() {
		paused = true;
		notifyAll();
	}

	public synchronized boolean isPaused() {
		return paused;
	}

}