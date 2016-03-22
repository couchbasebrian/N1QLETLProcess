# N1QLETLProcess
Example of how a basic ETL could be done using N1QL

When you run N1QLETLProcess it will connect to the cluster specified in the properties file, n1qletlprocess.properties, which looks like this:

    hostName=192.168.0.1
    portNumber=8091
    sourceBucketName=N1QLETLProcess-src
    destinationBucketName=N1QLETLProcess-dest
    userName=Administrator
    passWord=yourPassword

Currently this program uses only one cluster reference for both source and destination buckets.  But you could modify it to have two cluster references, one for source, and one for destination, if the buckets were in different clusters.

The program does the following in sequence.  After connecting to the cluster, it creates and populates the source bucket.  An index is created on the source bucket ( "CREATE PRIMARY INDEX ON...").  Then it queries the source bucket and keeps the results in memory as POJOs.  This is done in class CBQueryBucketTimer, using com.google.gson.Gson.

It then does an in-memory "transformation" of the results.  In the CBTransformArrayTimer class, the POJO's ageInMs field is populated.  

After that, the destination bucket connection is opened, and the in-memory results are upserted into the destination bucket ( Note this means that the destination bucket could be the same as the source bucket if you wish ).  Next an index is created on the destination bucket.

The destination bucket is then queried, the results are stored in memory again and displayed to the user.

Finally the program prints some reports and exits.


