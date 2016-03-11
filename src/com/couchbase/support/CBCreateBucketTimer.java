package com.couchbase.support;

import com.couchbase.client.java.CouchbaseCluster;
import com.couchbase.client.java.cluster.BucketSettings;
import com.couchbase.client.java.cluster.ClusterManager;
import com.couchbase.client.java.cluster.DefaultBucketSettings;

public class CBCreateBucketTimer extends TimingClass {

	// This is for bucket creation only.
	
	CouchbaseCluster myCluster;
	String bucketName;
	String u;
	String p;
	
	int bucketQuota = 100; // 100 megabytes, the minimum possible size
	
	// Given a cluster and a bucket name, open the bucket
	public CBCreateBucketTimer(CouchbaseCluster c, String bName, String un, String pw) {
		myCluster  = c;
		bucketName = bName;
		u = un;
		p = pw;
	}
	
	public void doTheWork() throws Exception {
		DefaultBucketSettings.Builder bb = DefaultBucketSettings.builder();
		bb.name(bucketName);
		bb.quota(bucketQuota);
		BucketSettings bs = bb.build();
		
		ClusterManager cm = myCluster.clusterManager(u,p);
		cm.insertBucket(bs);
		
	}
	
} // Create a bucket
