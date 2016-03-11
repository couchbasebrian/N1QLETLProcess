package com.couchbase.support;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;

public class CBCreatePrimaryIndexTimer extends TimingClass {

	String bucketName;
	Bucket bucket;
	
	public CBCreatePrimaryIndexTimer(Bucket b, String bn) {
		bucketName = bn;
		bucket = b;
	}
	
	public void doTheWork() throws Exception {
		String query1 = "CREATE PRIMARY INDEX ON `" + bucketName + "`;";

		System.out.println("About to execute: " + query1);

		N1qlQueryResult qr = bucket.query(N1qlQuery.simple(query1)); 
		System.out.println("# RESULTS: " + qr.allRows().size());

		for (N1qlQueryRow row : qr.allRows()) { 
			System.out.println(row.value().toString()); 
		}
		
	}
	
}
