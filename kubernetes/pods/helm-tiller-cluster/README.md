Helm Tiller
===============
This Tiller has access to the entire cluster

# RBAC
Since we are using RBAC, we need to give the Tiller admin permissions

```
kubectl create -f rbac.yaml
```

More information about various RBAC setup: https://github.com/kubernetes/helm/blob/master/docs/rbac.md

* Limit Tiller to a namespace
* Limit Tiller to run in one namespace and has access to another namespace

## Start tiller

## create
```
helm init --service-account tiller --node-selectors=k8s.info/application=infrastructure
```


## update
```
helm init --service-account tiller --upgrade --node-selectors=k8s.info/application=infrastructure
```
