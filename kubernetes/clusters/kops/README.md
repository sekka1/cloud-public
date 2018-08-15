Kops
===========
Project source: https://github.com/kubernetes/kops

Kops is a great tool to help you create and maintain the lifecycle of a Kubernetes
cluster on AWS.  In my opinion it is one of the best tools out there.  Far
better than GKE or EKS.

For creating a cluster, you can not get an easier experience than using GKE.  It
is dead simple to create and then get access to the cluster.

In my opinion creating the cluster is a small part of the Kubernetes Lifecycle.
You will then have to create node groups (node pools, instance groups, auto scaling group,
  whatever you want to call it).  This is where Kops really starts to shine and
  where GKE starts to fall apart for me.

I can create new node pools in GKE by scripting it out with the `gcloud` cli.  That
is cool, I guess.  You can try to use their `deployment manager` but then when i tried
it last, it didnt support multi zone GKE clusters.  So back to the cli method.  However,
what is worst is that you cannot roll a node pool.  For example, you want to update
the size of the instances in the node pool or you want to update the Kubernetes
version of it.  You simply cant do that.  You have to create a new node pool with the
new settings and then manually cordon and drain everything off of those and then delete the
new node pool.

With Kops, they call the node pools instance groups and on AWS they equate down to
an ASG (autoscaling group).  You can update an instance group, then use the kops
CLI to roll the group onto the new instances.  It will even drain and cordon the
nodes for you automatically!  Now this is really nice and way more automated.

# Kops usage

My preferred method on using kops is by creating and maintaining the cluster via
a cluster config that I keep in my code repository.  This way I can keep track of
the cluster's configuration easily.

The method here describes here uses the kops templating feature (https://github.com/kubernetes/kops/blob/master/docs/cluster_template.md).  There is
a templated kops cluster configuratoin in `./template/cluster.yaml`.  This template
yaml file has everything you need to create a fully functional cluster.

The `values.yaml` file in `./clusters/prod/values.yaml` provides to values for the
template.

You can run this command to output the templated out cluster config:

```
kops toolbox template --template ./template/cluster.yaml --values ./clusters/prod/values.yaml
```

This will output the entire configuration file to stdout where you can inspect it.

Redirecting it to an output file, we can then instantiate the cluster:

```
kops toolbox template --template ./template/cluster.yaml --values ./clusters/prod/values.yaml > /tmp/output.yaml
```

Creating the cluster:

```
kops create -f /tmp/output.yaml
```
