Kube-dashboard
==================

From: https://github.com/kubernetes/kops/tree/master/addons/kubernetes-dashboard

One quirk with this is that in the URL you have to put in your namespace name.


https://kd-demo.demo.devops.bot/#!/overview?namespace=my-namespace-name


## Basic auth
This Kubernetes Dashboard has basic auth configured for the ingress.  You first
need to create a basic auth secret using the `../http-auth` instructions.
