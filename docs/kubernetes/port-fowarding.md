Kubernetes Port Forwarding with kubectl
==========================
Kubernetes port forwarding with `kubectl` allows to you to forward a pods port
to your local machine so that you can interact with it via `localhost`.

## Prerequisites
Follow the directions on how to download `kubectl`: https://kubernetes.io/docs/tasks/tools/install-kubectl/

Get the kubeconfig from your administrator and put it here: `~/.kube/config`

## Postgres example:
For example, if you are running a Postgres database and want to access it without
having to expose it to the internet, you can do it via port forwarding.

Lets say your db pod is named: `db-0`

To setup a port foward you would use this command:

```
export NAMESPACE=your-namespace
kubectl -n ${NAMESPACE} port-forward db-0 5432:5432
```

Now you can connect to the Postgres database via localhost:

```
psql -h localhost -p 5432 -U <username> -W <password> <database>
```
