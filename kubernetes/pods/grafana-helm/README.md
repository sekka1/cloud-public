Grafana
============

We are using this helm chart: https://github.com/kubernetes/charts/tree/master/stable/grafana

Using helm version: v2.9.0


# Installing:
```
export KUBE_NAMESPACE=devops
```

```
helm repo add stable https://kubernetes-charts.storage.googleapis.com/

helm install \
--version 1.9.1 \
--namespace ${KUBE_NAMESPACE} \
--name ${KUBE_NAMESPACE}-grafana \
--values ./kubernetes/pods/grafana-helm/values.yaml \
stable/grafana
```
