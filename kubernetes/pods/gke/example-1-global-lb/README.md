Example deployment with a Google Global Load Balancer
==========================

https://cloud.google.com/kubernetes-engine/docs/tutorials/http-balancer#step_1_deploy_a_web_application

This also adds a static IP that has to be reserved before creating named: `web-static-ip`

Topology:

```
Internet <--> GLB <--> (nodeport) pod
```

Good:
* Using the GLB which is world wide

Bad:
* Does not have nginx functionalities
* Need one LB for each application
* Loose nginx prometheus metrics
* Can do automatic certs with Let's Encrypt


## Using the `type: LoadBalancer`
Using a service of `type: LoadBalancer`, will startup a TCP load balancer in
the current region your cluster is in.

This will be equivalent to an AWS Network Load Balancer.
