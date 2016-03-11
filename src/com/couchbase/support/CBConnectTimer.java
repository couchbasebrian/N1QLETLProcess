package com.couchbase.support;

import com.couchbase.client.java.CouchbaseCluster;

public class CBConnectTimer extends TimingClass {

	CouchbaseCluster sourceCluster;
	String hostName;
	
	public CBConnectTimer(String s) {
		hostName = s;
	}
	
	public CouchbaseCluster getCluster() { return sourceCluster; };
	
	public void doTheWork() throws Exception {
		sourceCluster = CouchbaseCluster.create(hostName);
	}

} // CBConnectTimer
