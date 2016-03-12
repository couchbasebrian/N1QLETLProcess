package com.couchbase.support;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.JsonDocument;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.error.DocumentAlreadyExistsException;

public class CBPopulateBucketTimer extends TimingClass {
	
	Bucket bucket;
	int numDocumentsToInsert;
	
	public CBPopulateBucketTimer(Bucket b, int numDocs) {
		bucket = b;
		numDocumentsToInsert = numDocs;
		numExceptions = 0;
	}
	
	public void doTheWork() throws Exception {
	
		String DOCUMENTNAMEPREFIX = "testDocument";
		String jsonDocumentString = "";
		String documentKey        = "";
		JsonObject jsonObject     = null;
				
		for (int i = 0; i < numDocumentsToInsert; i++) {

			long timeNow = System.currentTimeMillis();
	
			// create a document
			documentKey = DOCUMENTNAMEPREFIX + i;
			jsonDocumentString = "{ \"name\" : \"testDocument\", \"serialNumber\" : " + i + ", \"creationDate\" : " + timeNow + " }";
			jsonObject = JsonObject.fromJson(jsonDocumentString);
			JsonDocument jsonDocument = JsonDocument.create(documentKey, jsonObject);

			// insert the document
			try {
				bucket.insert(jsonDocument);			// Note this does not use N1QL
			}
			catch (DocumentAlreadyExistsException dae) {
				numExceptions++;
			}
				
		} // for each document
		
	} // doTheWork
	
} // populate a bucket
