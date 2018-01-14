Selecting instance types to run on
=================================

We provide AWS On Demand and Spot instances.  You can specifically select
which type of instance you want your pod to run on by adding `nodeSelector`.

# Selecting an AWS Spot instance

Add this node selector to your pod file:

```
nodeSelector:
  k8s.info/isSpot: "true"
```

# Selecting an AWS On Demand instance

Add this node selector to your pod file:

```
nodeSelector:
  k8s.info/isSpot: "false"
```
