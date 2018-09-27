Kubernetes Service Accounts
===========================

# Giving a pod a service account to list pods
This will create a `Role`, `RoleBinding`, and a `ServiceAccount` that is local
to a namespace and only takes effect in that namespace.  You can put all of this
into one file and name it whatever you like but I usually name it `rbac.yaml`.

Create a role that is in the namespace that you want to use this in and with
the permission that are required.

```yaml
---
kind: Role
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  namespace: my-namespace
  name: my-role-name
rules:
- apiGroups: [""]
  resources: ["pods"]
  verbs: ["get", "list", "create"]
```

This will give the `Role` to the `pods` resource with `vers/action` to perform
`get`, `list`, and `create`.

Then you create a role binding:

```yaml
---
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: my-role-name
  namespace: my-namespace
subjects:
- kind: ServiceAccount
  name: my-role-name
  namespace: my-namespace
roleRef:
  kind: Role
  name: my-role-name
  apiGroup: ""
```

Create the servcie account:

```yaml
---
apiVersion: v1
kind: ServiceAccount
metadata:
  name: my-role-name
  namespace: my-namespace
```

# Using a ServiceAccount
The above simply creates a service account with the permissions that you want,
now you have to give this service account to a resource like a Deployment or Statefulset
to use it.

In the Deployment or Statefulset, you add in the name of the service account into
the `spec.template.spec` section:

```yaml
---
apiVersion: apps/v1
kind: Deployment
metadata:
  name: prometheus
spec:
  selector:
    matchLabels:
      app: prometheus
  template:
    metadata:
      name: prometheus
    spec:
      serviceAccountName: my-role-name
```
