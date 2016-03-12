package com.couchbase.support;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.document.json.JsonObject;
import com.couchbase.client.java.query.N1qlQuery;
import com.couchbase.client.java.query.N1qlQueryResult;
import com.google.gson.Gson;

public class SupportUtils {

	static int SCREENCOLUMNS = 132;
	
	private static Gson gson;
	
	public static void main(String[] args) {
		System.out.println(getCouchbaseVersion("10.111.110.101", 8091));
	}

	public static Gson getGson() {
		
		if (gson == null) {
			gson = new Gson();
		}
		
		return gson;
		
	}
	
	public static void printDecoration(int c, String s) {
		for (int i = 0; i < c; i++) { System.out.print(s); }
	}

	public static void printCenteredBanner(String s) {
		int numDecorations = ((SCREENCOLUMNS - (s.length() + 2)) / 2);
		printDecoration(numDecorations,"=");
		System.out.print(" " + s + " ");
		printDecoration(numDecorations,"=");		
		System.out.println();
	}
	
	static void logMessage(String s) {
		System.out.println("=== " + s + " ===");
	}

	
	
	static N1qlQueryResult runAQuery(Bucket bucket, String queryString) {

		N1qlQueryResult qr = bucket.query(N1qlQuery.simple(queryString)); 

		int numberOfErrors         = 0;
		int numberOfResults        = qr.allRows().size(); 
		boolean finalSuccess       = qr.finalSuccess();
		List<JsonObject> errorList = qr.errors();

		if (errorList != null) { numberOfErrors = errorList.size(); }
		
		System.out.println("runAQuery() finalSuccess? " + finalSuccess + " numberOfResults: " + numberOfResults + " numberOfErrors: " + numberOfErrors);
		
		if (errorList != null) {
			for (JsonObject eachError : errorList ) {
				System.out.println("runAQuery() error: " + eachError);
			}
		}
		
		return qr;
	}
	
	
	static long runATimingClass(TimingClass tc) {

		// Currently we exit if an exception occurs.
		// Alternatively we could store the exception, and not exit
		// and allow the caller to query me to see what happened.

		long elapsedTime = 0;
		
		tc.performTest();
		
		if (tc.didExceptionOccur()) {
			printCenteredBanner(tc.getClass().getName() + ": An exception was thrown upwards");
			tc.getException().printStackTrace();
			System.exit(1);
		}
		else {
			printCenteredBanner(tc.getClass().getName() + ": No exception was thrown upwards");
		}

		elapsedTime = tc.getElapsedTime();
		
		printCenteredBanner(tc.getClass().getName() + ": Elapsed time: " + elapsedTime + " ms.  Exceptions while running: " + tc.getNumExceptions());		
		
		TimingClassResults.getInstance().addTimingClass(tc);
		
		return elapsedTime;
	}

	public static String getCouchbaseVersion(String host, int port) {
		
		// This method can take 5-6 seconds on 1 node virtual machine
		
		String cbVersion = "ERROR";

		boolean success = false;
		
		while (!success) {
			try {
				String restUrl = "http://" + host + ":" + port + "/pools";
				byte b[] = new byte[10000];
				URL u = new URL(restUrl);
				InputStream is = u.openStream();
				// int bytesRead = is.read(b);
				String result = new String(b);
				int impVerIndex = result.indexOf("implementationVersion");
				int firstCommaIndex = result.indexOf(",", impVerIndex);
				String versionTemp = result.substring(impVerIndex, firstCommaIndex);
				int colonIndex = versionTemp.indexOf(":", 0);
				String version = versionTemp.substring(colonIndex + 2, versionTemp.length() - 1);
				is.close();
				is = null;
				u = null;
				b = null;
				cbVersion = version;
				success = true;
			}
			catch (Exception e) {
				e.printStackTrace();
			}

			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}

		// In diag.log this generates a line like so
		// 10.111.110.1 - - [11/Mar/2016:23:30:45 +0000] "GET /pools HTTP/1.1" 200 769 - Java/1.8.0_25
		// 10.111.110.1 - - [11/Mar/2016:23:32:40 +0000] "GET /pools HTTP/1.1" 200 769 - Java/1.8.0_25
		
		return cbVersion;
	} // getCouchbaseVersion()

} // SupportUtils
