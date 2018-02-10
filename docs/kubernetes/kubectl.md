kubectl
=========

`kubectl` is how you would interact with the Kubernetes cluster.  The documentation
and the cli `--help` is very helpful

# Download and install kubectl

Go here and follow the instructions:

https://kubernetes.io/docs/tasks/tools/install-kubectl/#install-kubectl-binary-via-curl


# Setting up tab completion
I highly recommend that you do this.  It is very useful since there are a lot of
commands available.

Follow the instructions here: https://kubernetes.io/docs/tasks/tools/install-kubectl/#enabling-shell-autocompletion

# You kube config file
The kube config file is your authentication file to talk to any kubernetes clusters.
Your cluster administrator will be able to give you your kube config file.

The default kube config file is located in: `~/.kube/config`

The `kubectl` cli will by default look here if you do not pass in the `--kubeconfig` parameter
to it to specify a kube config file location.

Once your administrator has given you a kube config file.  Copy it to `~/.kube/config`

# Sample usage:

## Listing running pods:
```
kubectl get pods -o wide
```

## Looking in a pod
You can get more information about what is in a pod.  For example, it will show you information about what container and images it is using, options that were passed into it, and kubernetes events.

From listing a pod, you can `describe` anyone of those pods.

```
kubectl describe pod <pod name>
```
