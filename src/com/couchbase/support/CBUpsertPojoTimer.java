package com.couchbase.support;

import java.util.ArrayList;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.couchbase.client.java.query.N1qlQueryRow;
import com.google.gson.Gson;

public class CBUpsertPojoTimer extends TimingClass {

	ArrayList<DocumentPOJO> dataList;
	Bucket bucket;
	
	public CBUpsertPojoTimer(ArrayList<DocumentPOJO> listOfData, Bucket b) {
		dataList = listOfData;
		bucket = b;
	}
	
	public void doTheWork() throws Exception {
		
		Gson gson = SupportUtils.getGson();
		
		int i = 0;
		
		for (DocumentPOJO eachPojo : dataList) {
			// Upsert using N1QL query
			
			// UPSERT INTO product (KEY, VALUE) VALUES ("odwalla-juice1", 
			//  { 
			//    "productId": "odwalla-juice1", 
		    //    "unitPrice": 5.40, 
			//    "type": "product", "color":"red" 
			//  } 
			// )
		    
			String pojoJsonString = gson.toJson(eachPojo);
			
			String documentKey = "upsertedDocument" + i++;
			
			String query1 = "UPSERT INTO `" + bucket.name() + "` (KEY,VALUE) VALUES ( \"" + documentKey + "\", " + pojoJsonString + " );";

			System.out.println("About to upsert: " + query1);

			N1qlQueryResult qr = SupportUtils.runAQuery(bucket, query1); 

			for (N1qlQueryRow row : qr.allRows()) { 
				System.out.println(row.value().toString()); 
			}

		}
	}
	
} // CBUpsertPojoTimer
