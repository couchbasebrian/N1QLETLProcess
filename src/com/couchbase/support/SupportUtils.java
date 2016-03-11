package com.couchbase.support;

import java.io.InputStream;
import java.net.URL;

import com.google.gson.Gson;

public class SupportUtils {

	static int SCREENCOLUMNS = 132;
	
	private static Gson gson;
	
	public static void main(String[] args) {
		// TODO
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

	// Currently we exit if an exception occurs.
	// Alternatively we could store the exception, and not exit
	// and allow the caller to query me to see what happened.
	
	static long runATimingClass(TimingClass tc) {
		
		long elapsedTime = 0;
		
		tc.performTest();
		
		if (tc.didExceptionOccur()) {
			printCenteredBanner(tc.getClass().getName() + ": An exception did occur");
			tc.getException().printStackTrace();
			System.exit(1);
		}
		else {
			printCenteredBanner(tc.getClass().getName() + ": No exception occurred");
		}
			
		elapsedTime = tc.getElapsedTime();
		
		printCenteredBanner(tc.getClass().getName() + ": Elapsed time: " + elapsedTime + " ms.");		
		
		return elapsedTime;
	}

	public static String getCouchbaseVersion(String host, int port) {
		
		String cbVersion = "ERROR";

		try {
			String restUrl = "http://" + host + ":" + port + "/pools";
			byte b[] = new byte[10000];
			URL u = new URL(restUrl);
			InputStream is = u.openStream();
			int bytesRead = is.read(b);
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
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return cbVersion;
	} // getCouchbaseVersion()

} // SupportUtils
