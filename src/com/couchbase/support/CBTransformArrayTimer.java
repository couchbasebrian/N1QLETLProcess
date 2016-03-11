package com.couchbase.support;

import java.util.ArrayList;

public class CBTransformArrayTimer extends TimingClass {

	ArrayList<DocumentPOJO> workingList;
	
	public CBTransformArrayTimer(ArrayList<DocumentPOJO> dpList) {
		workingList = dpList;
	}
	
	public ArrayList<DocumentPOJO> getWorkingList() {
		return workingList;
	}
	
	public void doTheWork() throws Exception {
		// Calculate and set the age, in place
		for (DocumentPOJO eachPojo : workingList) {
			long creationDate = Long.parseLong(eachPojo.creationDate);
			eachPojo.ageInMs = ( System.currentTimeMillis() - creationDate );
		}
		
	}
}
