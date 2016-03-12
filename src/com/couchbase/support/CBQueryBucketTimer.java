package com.couchbase.support;

import java.util.ArrayList;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.google.gson.Gson;

public class CBQueryBucketTimer extends TimingClass {

	Bucket bucket;
	String bucketName;
	
	ArrayList<DocumentPOJO> pojoResults;
	
	public CBQueryBucketTimer(Bucket b, String bn) {
		bucket = b;
		bucketName = bn;
		pojoResults = new ArrayList<DocumentPOJO>();
	}
	
	public ArrayList<DocumentPOJO> getResults() {
		return pojoResults;
	}
	
	public int getResultCount() {
		int rc = 0;
		if (pojoResults != null) {
			rc = pojoResults.size();
		}
		return rc;	
	}
	
	public void doTheWork() throws Exception {
		
		String query1 = "SELECT * FROM `" + bucketName + "`;";

		System.out.println("About to execute: " + query1);

		N1qlQueryResult qr = SupportUtils.runAQuery(bucket, query1); 

		JsonObject   eachValue;
		JsonObject	 innerObject;
		String       eachValueString;
		DocumentPOJO eachDocPojo;
		
		Gson gson = SupportUtils.getGson();
		
		// TODO count the process exceptions
		
		for (N1qlQueryRow row : qr.allRows()) { 
			eachValue = row.value();
			if (eachValue != null) {
				innerObject = (JsonObject) eachValue.get(bucketName);
				if (innerObject != null) {
					eachValueString = innerObject.toString();
					if (eachValueString != null) {
						System.out.println(eachValueString);
						eachDocPojo = gson.fromJson(eachValueString, DocumentPOJO.class);
						pojoResults.add(eachDocPojo);
					}
				}
			}
		} // for each row in the result set

	} // doTheWork()

} // CBQueryBucketTimer
