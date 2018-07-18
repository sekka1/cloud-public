MongoDB
============

Helm chart: https://github.com/kubernetes/charts/tree/master/stable/mongodb-replicaset

Using helm version: 2.8.1

# Using with make file:
```
export KUBE_NAMESPACE=infrastructure
```

## Install:
```
make install
```

## Updating:
```
make upgrade
```

## Deleting:
```
make delete
```

## Listing helm charts:
```
make list
```

# Installing Manually:
```
export KUBE_NAMESPACE=demo-mongo
```

```
helm repo add stable https://kubernetes-charts.storage.googleapis.com/

helm install \
--version 3.4.0 \
--name ${KUBE_NAMESPACE}-mongo \
--namespace ${KUBE_NAMESPACE} \
--values ./kubernetes/pods/mongodb-helm/values.yaml \
--debug \
stable/mongodb-replicaset
```

# Creating a static service name to reference mongo
The Helm chart will create a service name that is based on the helm chart name.
The Helm chart name has to be globally unique in the cluster.  We will add a
name to the helm chart with the option `--name ${KUBE_NAMESPACE}-mongo`.  This
will make a name like `demo-mongo`.  This is good but then we still have
to change all of the configuration files for qa, demo, prod, etc.

The following will create a static name that will map to the chart name.  So in
all of the configuration files we can just reference mongo by the name: `mongodb`

Since this is usually a one time activity, we have punted the automation of this until
later.

Manual process:

1. Edit the file `service-static-name.yml`.  Change all reference of `demo-mongo`
to your helm chart name.

1. Apply it
```
kubectl -n ${KUBE_NAMESPACE} apply -f ./kubernetes/pods/mongodb-helm/service-static-name.yml
```

# Creating users:

Logging into mongo:
```
mongo

use admin
db.auth("mongo", "mongo123" )
```

Create a user:
```
use admin
db.createUser(
  {
    user: "myapp",
    pwd: "myapp_Pass321",
    roles: [ { role: "dbOwner", db: "coupon-management" } ]
  }
)


use coupon-management
db.auth("myapp", "myapp_Pass321" )
```

## Verifying the replicaset

Exec-ing in, you should expect to see similar output:

```
mongodb@demo-demo-mongo-mongodb-replicaset-0:/$ mongo
MongoDB shell version v3.6.4
connecting to: mongodb://127.0.0.1:27017
MongoDB server version: 3.6.4
Welcome to the MongoDB shell.
For interactive help, type "help".
For more comprehensive documentation, see
	http://docs.mongodb.org/
Questions? Try the support group
	http://groups.google.com/group/mongodb-user
rs0:PRIMARY>
rs0:PRIMARY>
rs0:PRIMARY> use admin
switched to db admin
rs0:PRIMARY> db.auth("mongo", "mongo123" )
1
rs0:PRIMARY> rs.isMaster()
{
	"hosts" : [
		"demo-demo-mongo-mongodb-replicaset-0.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017",
		"demo-demo-mongo-mongodb-replicaset-1.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017",
		"demo-demo-mongo-mongodb-replicaset-2.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017"
	],
	"setName" : "rs0",
	"setVersion" : 3,
	"ismaster" : true,
	"secondary" : false,
	"primary" : "demo-demo-mongo-mongodb-replicaset-0.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017",
	"me" : "demo-demo-mongo-mongodb-replicaset-0.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017",
	"electionId" : ObjectId("7fffffff0000000000000002"),
	"lastWrite" : {
		"opTime" : {
			"ts" : Timestamp(1526611348, 1),
			"t" : NumberLong(2)
		},
		"lastWriteDate" : ISODate("2018-05-18T02:42:28Z"),
		"majorityOpTime" : {
			"ts" : Timestamp(1526611348, 1),
			"t" : NumberLong(2)
		},
		"majorityWriteDate" : ISODate("2018-05-18T02:42:28Z")
	},
	"maxBsonObjectSize" : 16777216,
	"maxMessageSizeBytes" : 48000000,
	"maxWriteBatchSize" : 100000,
	"localTime" : ISODate("2018-05-18T02:42:35.268Z"),
	"logicalSessionTimeoutMinutes" : 30,
	"minWireVersion" : 0,
	"maxWireVersion" : 6,
	"readOnly" : false,
	"ok" : 1,
	"operationTime" : Timestamp(1526611348, 1),
	"$clusterTime" : {
		"clusterTime" : Timestamp(1526611348, 1),
		"signature" : {
			"hash" : BinData(0,"SpeJhJb4yfOFKPfYwG//y2B9uUM="),
			"keyId" : NumberLong("6556745100397903873")
		}
	}
}
rs0:PRIMARY>
rs0:PRIMARY>
rs0:PRIMARY>
rs0:PRIMARY>
rs0:PRIMARY> rs.status()
{
	"set" : "rs0",
	"date" : ISODate("2018-05-18T02:42:44.721Z"),
	"myState" : 1,
	"term" : NumberLong(2),
	"heartbeatIntervalMillis" : NumberLong(2000),
	"optimes" : {
		"lastCommittedOpTime" : {
			"ts" : Timestamp(1526611358, 1),
			"t" : NumberLong(2)
		},
		"readConcernMajorityOpTime" : {
			"ts" : Timestamp(1526611358, 1),
			"t" : NumberLong(2)
		},
		"appliedOpTime" : {
			"ts" : Timestamp(1526611358, 1),
			"t" : NumberLong(2)
		},
		"durableOpTime" : {
			"ts" : Timestamp(1526611358, 1),
			"t" : NumberLong(2)
		}
	},
	"members" : [
		{
			"_id" : 0,
			"name" : "demo-demo-mongo-mongodb-replicaset-0.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017",
			"health" : 1,
			"state" : 1,
			"stateStr" : "PRIMARY",
			"uptime" : 169,
			"optime" : {
				"ts" : Timestamp(1526611358, 1),
				"t" : NumberLong(2)
			},
			"optimeDate" : ISODate("2018-05-18T02:42:38Z"),
			"electionTime" : Timestamp(1526611196, 1),
			"electionDate" : ISODate("2018-05-18T02:39:56Z"),
			"configVersion" : 3,
			"self" : true
		},
		{
			"_id" : 1,
			"name" : "demo-demo-mongo-mongodb-replicaset-1.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017",
			"health" : 1,
			"state" : 2,
			"stateStr" : "SECONDARY",
			"uptime" : 122,
			"optime" : {
				"ts" : Timestamp(1526611358, 1),
				"t" : NumberLong(2)
			},
			"optimeDurable" : {
				"ts" : Timestamp(1526611358, 1),
				"t" : NumberLong(2)
			},
			"optimeDate" : ISODate("2018-05-18T02:42:38Z"),
			"optimeDurableDate" : ISODate("2018-05-18T02:42:38Z"),
			"lastHeartbeat" : ISODate("2018-05-18T02:42:44.337Z"),
			"lastHeartbeatRecv" : ISODate("2018-05-18T02:42:43.407Z"),
			"pingMs" : NumberLong(1),
			"syncingTo" : "demo-demo-mongo-mongodb-replicaset-0.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017",
			"configVersion" : 3
		},
		{
			"_id" : 2,
			"name" : "demo-demo-mongo-mongodb-replicaset-2.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017",
			"health" : 1,
			"state" : 2,
			"stateStr" : "SECONDARY",
			"uptime" : 74,
			"optime" : {
				"ts" : Timestamp(1526611358, 1),
				"t" : NumberLong(2)
			},
			"optimeDurable" : {
				"ts" : Timestamp(1526611358, 1),
				"t" : NumberLong(2)
			},
			"optimeDate" : ISODate("2018-05-18T02:42:38Z"),
			"optimeDurableDate" : ISODate("2018-05-18T02:42:38Z"),
			"lastHeartbeat" : ISODate("2018-05-18T02:42:44.327Z"),
			"lastHeartbeatRecv" : ISODate("2018-05-18T02:42:43.225Z"),
			"pingMs" : NumberLong(0),
			"syncingTo" : "demo-demo-mongo-mongodb-replicaset-0.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017",
			"configVersion" : 3
		}
	],
	"ok" : 1,
	"operationTime" : Timestamp(1526611358, 1),
	"$clusterTime" : {
		"clusterTime" : Timestamp(1526611358, 1),
		"signature" : {
			"hash" : BinData(0,"kG9ge+Ui1i5tXKRQyzwid62navU="),
			"keyId" : NumberLong("6556745100397903873")
		}
	}
}
rs0:PRIMARY>
rs0:PRIMARY>
rs0:PRIMARY>
rs0:PRIMARY> rs.conf()
{
	"_id" : "rs0",
	"version" : 3,
	"protocolVersion" : NumberLong(1),
	"members" : [
		{
			"_id" : 0,
			"host" : "demo-demo-mongo-mongodb-replicaset-0.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017",
			"arbiterOnly" : false,
			"buildIndexes" : true,
			"hidden" : false,
			"priority" : 1,
			"tags" : {

			},
			"slaveDelay" : NumberLong(0),
			"votes" : 1
		},
		{
			"_id" : 1,
			"host" : "demo-demo-mongo-mongodb-replicaset-1.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017",
			"arbiterOnly" : false,
			"buildIndexes" : true,
			"hidden" : false,
			"priority" : 1,
			"tags" : {

			},
			"slaveDelay" : NumberLong(0),
			"votes" : 1
		},
		{
			"_id" : 2,
			"host" : "demo-demo-mongo-mongodb-replicaset-2.demo-demo-mongo-mongodb-replicaset.demo-demo.svc.cluster.local:27017",
			"arbiterOnly" : false,
			"buildIndexes" : true,
			"hidden" : false,
			"priority" : 1,
			"tags" : {

			},
			"slaveDelay" : NumberLong(0),
			"votes" : 1
		}
	],
	"settings" : {
		"chainingAllowed" : true,
		"heartbeatIntervalMillis" : 2000,
		"heartbeatTimeoutSecs" : 10,
		"electionTimeoutMillis" : 10000,
		"catchUpTimeoutMillis" : -1,
		"catchUpTakeoverDelayMillis" : 30000,
		"getLastErrorModes" : {

		},
		"getLastErrorDefaults" : {
			"w" : 1,
			"wtimeout" : 0
		},
		"replicaSetId" : ObjectId("5afe3cec8ac22172baa2c7a3")
	}
}
rs0:PRIMARY>
```
