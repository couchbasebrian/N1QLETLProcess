package com.couchbase.support;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;

public class CBPopulateBucketTimer extends TimingClass {
	
	Bucket bucket;
	int numDocumentsToInsert;
	
	public CBPopulateBucketTimer(Bucket b, int numDocs) {
		bucket = b;
		numDocumentsToInsert = numDocs;
	}
	
	
	public void doTheWork() throws Exception {
	
		String DOCUMENTNAMEPREFIX = "testDocument";
		String jsonDocumentString = "";
		String documentKey        = "";
		JsonObject jsonObject     = null;
		
		// TODO: Consider putting this inside the loop or not.
		// When outside the loop, all docs will have the same creationDate
		// On the other hand the total time to populate the bucket
		// can be about 500-600 ms.
		
		//long timeNow = System.currentTimeMillis();
		
		for (int i = 0; i < numDocumentsToInsert; i++) {

			// test
			long timeNow = System.currentTimeMillis();
	
			// create a document
			documentKey = DOCUMENTNAMEPREFIX + i;
			jsonDocumentString = "{ \"name\" : \"testDocument\", \"serialNumber\" : " + i + ", \"creationDate\" : " + timeNow + " }";
			jsonObject = JsonObject.fromJson(jsonDocumentString);
			JsonDocument jsonDocument = JsonDocument.create(documentKey, jsonObject);

			// insert the document
			bucket.insert(jsonDocument);			
			
		} // for each document
		
	} // doTheWork
	
} // populate a bucket
