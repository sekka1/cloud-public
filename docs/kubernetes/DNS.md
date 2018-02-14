DNS
========

Where should you CNAME entries to:

`elb-1.prod-1.us-east-1.k8s.devops.bot`

This points to the main ELB which goes to the common ingress controller.

Path:
```
Internet <--> (elb-1.prod-1.us-east-1.k8s.devops.bot) AWS ELB <--> Ingress/nginx <--> Pod
```
