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

# We automatically add cpu/mem request and limits

If no resource limits are set, we will set it.  
The reason we do this is so that we can accurately bill you for what you want to use

## Default memory request and limits:
This is the config we apply to your namespace.

It will set a memory request for: 25mi
It will set a memory limit for: 512Mi

```
---
# https://kubernetes.io/docs/tasks/administer-cluster/memory-default-namespace/
apiVersion: v1
kind: LimitRange
metadata:
  name: mem-limit-range
spec:
  limits:
    # default memory limit
  - default:
      memory: 512Mi
    # default memory request
    defaultRequest:
      memory: 25Mi
    type: Container
```    

## Default CPU request and limits:
This is the config we apply to your namespace.

It will set a cpu request for: 100m
It will set a cpu limit for: 500m

```
---
apiVersion: v1
kind: LimitRange
metadata:
  name: cpu-limit-range
spec:
  limits:
    # default cpu limit
  - default:
      cpu: 500m
    # default cpu request
    defaultRequest:
      cpu: 100m
    type: Container
```
