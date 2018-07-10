Istio Install via Helm
========================

Download the relase you want here: https://github.com/istio/istio/releases

Istio recommends using Helm as the way to install for production use.

Main Helm install documentation page: https://istio.io/docs/setup/kubernetes/helm-install/


# Using the Helm template method
This method does not require the Tiller

```
helm template install/kubernetes/helm/istio --name istio --namespace istio-system > $HOME/istio.yaml


kubectl create namespace istio-system
kubectl create -f $HOME/istio.yaml
```

# Automatic side car injection:
The deployment will enable automatic side car injection but it has to be enabled
on the namespace that you want it to apply to:

Label the default namespace with istio-injection=enabled

```
kubectl label namespace default istio-injection=enabled
kubectl get namespace -L istio-injection
```
