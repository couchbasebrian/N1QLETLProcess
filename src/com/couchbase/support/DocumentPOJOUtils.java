package com.couchbase.support;

import java.util.ArrayList;

public class DocumentPOJOUtils {

	public static void printArray(ArrayList<DocumentPOJO> pojoList) {
		
		System.out.println("Name             Serial Number    Creation Date    Age in ms");
		System.out.println("---------------  ---------------  ---------------  ----------");
		
		for (DocumentPOJO eachPojo : pojoList) {
			System.out.printf("%15s  %15s  %15s  %10d\n", eachPojo.name, eachPojo.serialNumber, eachPojo.creationDate, eachPojo.ageInMs);
		}
		
	}
	
}
