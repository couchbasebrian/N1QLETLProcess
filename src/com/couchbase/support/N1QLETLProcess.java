package com.couchbase.support;

// Brian Williams
// Started:  March 10, 2016
// A program that simulates an ETL process on a Couchbase bucket using N1QL
// Uses Couchbase Java SDK 2.2.5 ( http://packages.couchbase.com/clients/java/2.2.5/Couchbase-Java-Client-2.2.5.zip )

// GSON
// http://search.maven.org/remotecontent?filepath=com/google/code/gson/gson/2.6.2/gson-2.6.2.jar

// Reference links
// http://developer.couchbase.com/documentation/server/4.1/sdks/java-2.2/download-links.html

public class N1QLETLProcess {

	public static void main(String[] args) {
		N1QLETLProcess proc = new N1QLETLProcess();
		proc.run();
	}

	public N1QLETLProcess() {
		System.out.println("N1QLETLProcess created.");
	}
	
	public void run() {

		long t1 = System.currentTimeMillis();
		
		System.out.println("Welcome to N1QLETLProcess");

		String hostName              = "192.168.0.1";
		int    portNumber            = 8091;
		String sourceBucketName      = "N1QLETLProcess-src";
		String destinationBucketName = "N1QLETLProcess-dest";
		String userName              = "Administrator";
		String passWord              = "password";
		
		int numDocs = 100;	// You may set this to any number
		
		// Get the version of CB
		String couchbaseVersion = SupportUtils.getCouchbaseVersion(hostName, portNumber);
		System.out.println("Couchbase Version: " + couchbaseVersion);
		
		// First connect to Couchbase cluster
		CBConnectTimer connect = new CBConnectTimer(hostName);
		SupportUtils.runATimingClass(connect);		
		
		// Then Create a bucket ( TODO, see if it exists first )
		CBCreateBucketTimer createBucket = new CBCreateBucketTimer(connect.getCluster(), sourceBucketName, userName, passWord);
		SupportUtils.runATimingClass(createBucket);
		
		// Open the bucket
		CBOpenBucketTimer openBucket = new CBOpenBucketTimer(connect.getCluster(), sourceBucketName);
		SupportUtils.runATimingClass(openBucket);
		
		// Load some data into the bucket
		CBPopulateBucketTimer populateBucket = new CBPopulateBucketTimer(openBucket.getBucket(), numDocs);
		SupportUtils.runATimingClass(populateBucket);
		
		// Create an Index on the bucket
		CBCreatePrimaryIndexTimer primaryIndex = new CBCreatePrimaryIndexTimer(openBucket.getBucket(), sourceBucketName);
		SupportUtils.runATimingClass(primaryIndex);
		
		// Query the bucket for some data, store in memory
		CBQueryBucketTimer queryBucket = new CBQueryBucketTimer(openBucket.getBucket(), sourceBucketName);
		SupportUtils.runATimingClass(queryBucket);
		
		// Display the original data pre-transformation
		DocumentPOJOUtils.printArray(queryBucket.getResults());
		
		// Do something with the data in-memory
		CBTransformArrayTimer transformArray = new CBTransformArrayTimer(queryBucket.getResults());
		SupportUtils.runATimingClass(transformArray);

		// Display the data after transformation
		DocumentPOJOUtils.printArray(transformArray.getWorkingList());

		// Create destination bucket
		CBCreateBucketTimer createDestinationBucket = new CBCreateBucketTimer(connect.getCluster(), destinationBucketName, userName, passWord);
		SupportUtils.runATimingClass(createDestinationBucket);

		// Open the destination bucket
		CBOpenBucketTimer destinationBucket = new CBOpenBucketTimer(connect.getCluster(), destinationBucketName);
		SupportUtils.runATimingClass(destinationBucket);

		// Upsert the transformed data into the destination bucket
		CBUpsertPojoTimer upsertPojo = new CBUpsertPojoTimer(transformArray.getWorkingList(), destinationBucket.getBucket());
		SupportUtils.runATimingClass(upsertPojo);
		
		// Query the destination bucket 
		CBQueryBucketTimer queryDestinationBucket = new CBQueryBucketTimer(destinationBucket.getBucket(), destinationBucketName);
		SupportUtils.runATimingClass(queryDestinationBucket);
		
		// Display the data from destination bucket
		DocumentPOJOUtils.printArray(queryDestinationBucket.getResults());
		
		// Clean up objects
		openBucket.getBucket().close();
		connect.getCluster().disconnect();
		
		long t2 = System.currentTimeMillis();

		System.out.println("Total run time was " + ( t2 - t1 ) + " ms. Goodbye.");
	}
		
}

// EOF