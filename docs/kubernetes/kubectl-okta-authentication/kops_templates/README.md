Kops Templates
=================


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
