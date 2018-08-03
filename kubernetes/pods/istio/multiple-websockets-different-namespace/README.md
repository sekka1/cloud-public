Multiple Websockets in different namespace example
===================================================

This is a follow up on the sample `websockets` here: https://github.com/istio/istio/tree/master/samples/websockets

This example uses two different Kubernetes namespaces and two Istio Gateways.

Note: I was not able to use one Istio Gateway that can handle traffic for multiple
namespaces.  I was also not able to find information about how that would work.
What I have found was that if you have an Istio Ingress Gateway for each namespace
and then the Gateway resource for each namespace, Istio was able to route traffic
into each namespace.

Topology:

Namespace 1:
```
ELB-1 <--> Istio Ingress Gateway - 1 <--> websockets-1
```

Namespace 2:
```
ELB-2 <--> Istio Ingress Gateway - 2 <--> websockets-2
```
