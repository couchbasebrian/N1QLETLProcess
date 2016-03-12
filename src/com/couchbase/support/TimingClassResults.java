package com.couchbase.support;

import java.util.ArrayList;

public class TimingClassResults {

	private ArrayList<TimingClass> completedTimingClasses;

	private static TimingClassResults _instance;

	static TimingClassResults getInstance() {
		if (_instance == null) {
			_instance = new TimingClassResults();
		}
		return _instance;
	}

	public TimingClassResults() {
		completedTimingClasses = new ArrayList<TimingClass>();
	}

	public void addTimingClass(TimingClass tc) {
		completedTimingClasses.add(tc);
	}

	public void displayReport() {
		// Report on all Timing Classes, in sequential order

		System.out.println("Class Name                                          startTime        endTime          exceptionOccurred	caughtException                 numExceptions");
		System.out.println("--------------------------------------------------  ---------------  ---------------  -----------------  ------------------------------  -------------");

		String exceptionName;
		
		for (TimingClass eachTC : completedTimingClasses) {

			if (eachTC.caughtException != null) {
				exceptionName = eachTC.caughtException.getClass().getName();
			} else {
				exceptionName = "(none)";
			}
			
			System.out.printf("%50s  %15d  %15d  %17s  %30s  %13d\n", eachTC.getClass().getName(),
					eachTC.startTime,
					eachTC.endTime,
					eachTC.exceptionOccurred,
					exceptionName,
					eachTC.numExceptions
					);

		}

	}


}
