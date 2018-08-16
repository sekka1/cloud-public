Spinnaker
=============

Helm Chart: https://github.com/helm/charts/tree/master/stable/spinnaker

Using helm version: 2.9.1

# Using with make file:
The Makefile encapsulates the command that needs to run to perform and action.

```
export KUBE_NAMESPACE=spinnaker
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
export KUBE_NAMESPACE=spinnaker
```

```
helm repo add stable https://kubernetes-charts.storage.googleapis.com/

helm install \
--version 0.6.0 \
--name ${KUBE_NAMESPACE}-spinnaker \
--namespace ${KUBE_NAMESPACE} \
--values ./values.yaml \
--debug \
stable/spinnaker


# Spinnaker how to guide:

* For more info on the Kubernetes integration for Spinnaker, visit:
  http://www.spinnaker.io/docs/kubernetes-source-to-prod

* Kubernetes Source To Prod (Manifest Based) :     https://www.spinnaker.io/guides/tutorials/codelabs/kubernetes-v2-source-to-prod/
