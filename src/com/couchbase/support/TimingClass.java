package com.couchbase.support;

public class TimingClass {
	
	long      startTime, endTime;
	boolean   exceptionOccurred;
	Exception caughtException;
	
	public TimingClass() {
		startTime         = 0;
		endTime           = 0;
		caughtException   = null;
		exceptionOccurred = false;
	}
	
	public void startTiming() {		startTime = System.currentTimeMillis();    }
	public void stopTiming()  {		endTime   = System.currentTimeMillis();    }

	public long getElapsedTime() { 
		return (endTime - startTime);
	}
	
	public boolean didExceptionOccur() { return exceptionOccurred; }
	
	public Exception getException() { return caughtException; }
	
	// override in subclass
	public void doTheWork() throws Exception {
		SupportUtils.printCenteredBanner("This is where you do something");
	}
	
	public void performTest() {

		SupportUtils.printCenteredBanner(this.getClass().getName());

		// call the method that can be overridden
		startTiming();
		try {
			doTheWork();
		} catch (Exception e) {
			caughtException = e;
			exceptionOccurred = true;
		}
		stopTiming();
	}
	
} // generic TimingClass, each specific operation below is a subclass of it and implements the doTheWork() method
