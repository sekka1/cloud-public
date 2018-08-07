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


# Installing CRD

If using a Helm version prior to 2.10.0
```
kubectl -n istio-system apply -f install/kubernetes/helm/istio/templates/crds.yaml
```

Cert manager:
```
kubectl -n istio-system apply -f install/kubernetes/helm/istio/charts/certmanager/templates/crds.yaml
```

# Using the Helm template method
This method does not require the Tiller

```
helm template install/kubernetes/helm/istio --name istio --namespace istio-system --values install/kubernetes/helm/istio/values.yaml > istio-1.0.0.yaml

kubectl create namespace istio-system
kubectl apply -f istio-1.0.0.yaml
```

## Deleting via the Helm template method

```
kubectl delete -f istio-1.0.0.yaml
kubectl -n istio-system delete job --all
kubectl -n istio-system delete -f install/kubernetes/helm/istio/templates/crds.yaml
```

# Automatic side car injection:
The deployment will enable automatic side car injection but it has to be enabled
on the namespace that you want it to apply to:

Label the default namespace with istio-injection=enabled

```
kubectl label namespace default istio-injection=enabled
kubectl get namespace -L istio-injection
```


# Installing with Helm Tiller

https://istio.io/docs/setup/kubernetes/helm-install/#option-2-install-with-helm-and-tiller-via-helm-install

Helm release: https://github.com/helm/helm/releases/tag/v2.10.0-rc.2


```
kubectl -n istio-system apply -f ./helm-tiller/rbac.yaml

helm init --service-account tiller --tiller-namespace istio-system

helm install \
--name istio \
--tiller-namespace istio-system \
--namespace istio-system \
--values ./kubernetes/pods/istio-helm/istio-1.0.0/values.yaml \
../istio/install/kubernetes/helm/istio
```

This didnt work.  It wanted to create the CRDs like twice and kept on failing on the `helm install`. =(
