# hazelcastformarathon
There is sperate code to be written to support hazelcast in Marathon/Mesos.
In pom.xml you have to below entries-
<code><dependency>
			<groupId>org.apache.curator</groupId>
			<artifactId>curator-x-discovery</artifactId>
			<version>2.9.0</version>
		</dependency>
	<dependency>
			<groupId>com.hazelcast</groupId>
			<artifactId>hazelcast</artifactId>
			<version>3.6.1</version>
		</dependency>
		<dependency>
			<groupId>com.hazelcast</groupId>
			<artifactId>hazelcast-zookeeper</artifactId>
			<version>3.6.1</version>
		</dependency>
    </code>
    
    
    In docker file we have to export one more port for hazelcast
    
    <code>
    
  EXPOSE 8080
  EXPOSE 7000
    </code>
    
    Here 8080 is application port and 7000 is for hazel cast.
    
    Marthon entry
    
    <code>
    "portMappings": [
      {
        "containerPort": 8080,
        "hostPort": 0,
        "servicePort": 0,
        "protocol": "tcp",
        "labels": {}
      },
      {
        "containerPort": 7000,
        "hostPort": 0,
        "servicePort": 0,
        "protocol": "tcp",
        "labels": {}
      }
    </code>
    
    
    Changes for hazelcast initiazing, please see the HazelcastManager class 
