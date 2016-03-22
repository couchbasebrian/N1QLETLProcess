package com.couchbase.support;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;

public class CBCreatePrimaryIndexTimer extends TimingClass {

	String bucketName;
	Bucket bucket;
	
	public CBCreatePrimaryIndexTimer(Bucket b, String bn) {
		bucketName = b.name(); // Maybe don't need bn
		bucket = b;
	}
	
	public void doTheWork() throws Exception {
		String query1 = "CREATE PRIMARY INDEX ON `" + bucketName + "`;";

		System.out.println("About to execute: " + query1);

		N1qlQueryResult qr = SupportUtils.runAQuery(bucket, query1); 
				
		for (N1qlQueryRow row : qr.allRows()) { 
			System.out.println(row.value().toString()); 
		}
		
	}
	
}
