Istio Install via Helm
========================

Download the relase you want here: https://github.com/istio/istio/releases

Istio recommends using Helm as the way to install for production use.

Main Helm install documentation page: https://istio.io/docs/setup/kubernetes/helm-install/


# Using the Helm template method
This method does not require the Tiller

Change directory to the istio release directory:
```
cd <to the istio release download path>
```

For the istio-1.0.0-snapshot.0 we have to change the `install/kubernetes/helm/istio/values.yaml`
we have to change the tag from `0.8.latest` to `1.0.0-snapshot.0`.
```
helm template install/kubernetes/helm/istio --name istio --namespace istio-system --values install/kubernetes/helm/istio/values.yaml > istio.yaml

kubectl create namespace istio-system
kubectl create -f $HOME/istio.yaml
```

## Deleting via the Helm template method

```
kubectl delete -f $HOME/istio.yaml
```

# Automatic side car injection:
The deployment will enable automatic side car injection but it has to be enabled
on the namespace that you want it to apply to:

Label the default namespace with istio-injection=enabled

```
kubectl label namespace default istio-injection=enabled
kubectl get namespace -L istio-injection
```
