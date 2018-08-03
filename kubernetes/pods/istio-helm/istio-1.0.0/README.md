Istio Install via Helm
========================

Clone out the Istio repository.  Then switch to this tag's release:

```
cd istio
git clone https://github.com/istio/istio.git
git fetch
git checkout 1.0.0
```

Istio recommends using Helm as the way to install for production use.

Main Helm install documentation page: https://istio.io/docs/setup/kubernetes/helm-install/


# Using the Helm template method
This method does not require the Tiller

```
helm template install/kubernetes/helm/istio --name istio --namespace istio-system --values install/kubernetes/helm/istio/values.yaml > istio-1.0.0.yaml

kubectl create namespace istio-system
kubectl create -f istio-1.0.0.yaml
```

## Deleting via the Helm template method

```
kubectl delete -f istio-1.0.0.yaml
```

# Automatic side car injection:
The deployment will enable automatic side car injection but it has to be enabled
on the namespace that you want it to apply to:

Label the default namespace with istio-injection=enabled

```
kubectl label namespace default istio-injection=enabled
kubectl get namespace -L istio-injection
```
