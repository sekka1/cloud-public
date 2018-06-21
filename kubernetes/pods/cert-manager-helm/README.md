Cert Manager
============

We are using this helm chart: https://github.com/kubernetes/charts/tree/master/stable/cert-manager

Using helm version: v2.9.0


# Installing:
```
export KUBE_NAMESPACE=infrastructure
```

```
helm repo add stable https://kubernetes-charts.storage.googleapis.com/

helm install \
--version 0.3.0 \
--namespace ${KUBE_NAMESPACE} \
--name ${KUBE_NAMESPACE}-cert-manager \
--values ./Kubernetes/pods/cert-manager-helm/values.yaml \
stable/cert-manager
```

# Updating

```
helm upgrade \
--version 0.3.0 \
--values ./Kubernetes/pods/cert-manager-helm/values.yaml \
${KUBE_NAMESPACE}-cert-manager \
stable/cert-manager
```

# Let's Encrypt Secret
When the `cert-manager` starts, it registers with Let's Encrypt with the email
address in the `issuer.yaml` file.  It will create a secret in the namespace
it was launched in named: `letsencrypt-private-key`.
