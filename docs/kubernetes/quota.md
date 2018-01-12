Quota
======
To view the resource quota's you have in your namespace:


# Getting quota objects

```
$ kubectl get quota
NAME           AGE
object-quota   3d
```

# Describe the quota to see each values

```
kubectl describe quota object-quota
Name:                   object-quota
Namespace:              jason-monroe
Resource                Used   Hard
--------                ----   ----
configmaps              0      10
cpu                     400m   4
limits.cpu              3100m  4
limits.memory           800Mi  4Gi
memory                  200Mi  4Gi
persistentvolumeclaims  1      5
pods                    4      25
replicationcontrollers  2      25
requests.cpu            400m   4
requests.memory         200Mi  4Gi
requests.storage        1Gi    10Gi
resourcequotas          1      1
secrets                 5      10
services                4      10
services.loadbalancers  0      0
services.nodeports      1      25

```
