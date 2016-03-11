package com.couchbase.support;

import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.CouchbaseCluster;

public class CBOpenBucketTimer extends TimingClass {
	
	CouchbaseCluster myCluster;
	Bucket bucket;
	String bucketName;
	
	// Given a cluster and a bucket name, open the bucket
	public CBOpenBucketTimer(CouchbaseCluster c, String bName) {
		myCluster  = c;
		bucketName = bName;
	}
	
	public Bucket getBucket() { return bucket; };
	
	public void doTheWork() throws Exception {
		bucket = myCluster.openBucket(bucketName);	
	}
	
} // Open a bucket
