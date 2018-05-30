# Config

For the external-dns controller to work the nginx-controller must be started with the following:
* image: `gcr.io/google_containers/nginx-ingress-controller:0.9.0-beta.3`
  * adds arg `- --publish-service=$(POD_NAMESPACE)/ingress-lb`
  * changes `nginx-configmap` to `--configmap` in this version


# Internal and External Ingress

## Documentation on running multiple ingresses
For example, running an internal and external ingress:

https://github.com/kubernetes/ingress-nginx#running-multiple-ingress-controllers

## How we are using this:

We are separating the internal and external ingresses and each ingress will have
only control the ingress route for one of these.

```
*.{env}.domain.com --> External Ingress
*.internal.{env}.domain.com --> Internal Ingress
```

To achieve this, each ingress has a new flag added to it so that it only watches
the ingress for it:

External:
```
--ingress-class=nginx-external
```

Internal:
```
--ingress-class=nginx-internal
```

An ingress will not be applied without an annotation selecting which ingress it wants:

External:
```
kubernetes.io/ingress.class: "nginx-external"
```
Internal:
```
kubernetes.io/ingress.class: "nginx-internal"
```
