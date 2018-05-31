Clusters
===========


# Creating Users and kubeconfigs
With RBAC enabled on the system, we can create granular roles to allow a user or
a group of users to only be able to perform certain tasks.

For example:
* can create a role where a user can list, describe, get logs from
pods but can not list or view any of the secrets.
* can limit a user to a namespace
* can allow a user to only access one pod

## Steps to create a user and give it the appropriate permissions

### Set environment params
```
export RBAC_NAMESPACE=stage
export RBAC_NEW_AUTH_NAME=developer
export RBAC_CLUSTER_NAME=prod-1.k8s.devops.bot
```

### Get the root CA key and cert
You will need to sign the new user's keys with the root CA's private key since everything
is authenticated through that.

This is located in S3:

```
# CA Private key (ca.key)
s3://<kops store>/<cluster name>/pki/private/ca/xxxx.key

# CA Cert (ca.crt)
s3://<kops store>/<cluster name>/pki/issued/ca/xxxx.crt
```

Put this key with the names `ca.key` and `ca.crt` in the `keys` folder: `./kubernetes/cluster/<cluster name>/keys/`

There is a .gitignore that will not check in these items into github

### generate keys for the new user

For example if we are making a user named: developer that has access to the namespace developer

Starting from the root of this repository

```
export RBAC_KUBE_CLUSTER_ROOT_CERTS=./kubernetes/clusters/prod-1/keys
export RBAC_NEW_AUTH_WORKDIR=./kubernetes/namespaces/${RBAC_NAMESPACE}
mkdir -p ${RBAC_NEW_AUTH_WORKDIR}/auth
openssl genrsa -out ${RBAC_NEW_AUTH_WORKDIR}/auth/${RBAC_NEW_AUTH_NAME}.key 2048
```

### generate dev cert

```
openssl req -new -key "${RBAC_NEW_AUTH_WORKDIR}/auth/${RBAC_NEW_AUTH_NAME}.key" -out ${RBAC_NEW_AUTH_WORKDIR}/auth/${RBAC_NEW_AUTH_NAME}.csr -subj "/CN=${RBAC_NEW_AUTH_NAME}/O=devops"
```

### sign it with the ca.crt/key that kubernetes is using
```
openssl x509 -req -in ${RBAC_NEW_AUTH_WORKDIR}/auth/${RBAC_NEW_AUTH_NAME}.csr -CA ${RBAC_KUBE_CLUSTER_ROOT_CERTS}/ca.crt -CAkey ${RBAC_KUBE_CLUSTER_ROOT_CERTS}/ca.key -CAcreateserial -out ${RBAC_NEW_AUTH_WORKDIR}/auth/${RBAC_NEW_AUTH_NAME}.crt -days 356
```

### remove current default kube config
```
rm ~/.kube/config
```

### set context in kube config with the new certs
```
kubectl config set-credentials ${RBAC_NEW_AUTH_NAME} --client-certificate=${RBAC_NEW_AUTH_WORKDIR}/auth/${RBAC_NEW_AUTH_NAME}.crt  --client-key=${RBAC_NEW_AUTH_WORKDIR}/auth/${RBAC_NEW_AUTH_NAME}.key --embed-certs=true
```

### add cluster to ~/.kube/config

```
kubectl config set-cluster ${RBAC_CLUSTER_NAME} --server=https://api.${RBAC_CLUSTER_NAME} --certificate-authority=${RBAC_KUBE_CLUSTER_ROOT_CERTS}/ca.crt --embed-certs=true
```

### setting the context to the creds
```
kubectl config set-context developer-context --cluster=${RBAC_CLUSTER_NAME} --namespace=${RBAC_NAMESPACE} --user=${RBAC_NEW_AUTH_NAME}
```

### set it to the default/current context
```
kubectl config use-context developer-context
```

### copy kube config

```
mkdir -p ${RBAC_NEW_AUTH_WORKDIR}/kube-config/${RBAC_NEW_AUTH_NAME}
mv ~/.kube/config ${RBAC_NEW_AUTH_WORKDIR}/kube-config/${RBAC_NEW_AUTH_NAME}/config
```

### test listing pods and it should be denied
```
kubectl --kubeconfig ${RBAC_NEW_AUTH_WORKDIR}/kube-config/${RBAC_NEW_AUTH_NAME}/config get pods
```

### Adding a user to the role
If there is more than one user you will need to add the user to the namespace role
in: `./kubernetes/namespaces/{namespace}/rbac/role.yml`

```
- apiGroup: rbac.authorization.k8s.io
  kind: User
  name: gawkbox-chuck
```

### create role and binding
```
kubectl apply -f ${RBAC_NEW_AUTH_WORKDIR}/rbac/
#kubectl apply -f ${RBAC_NEW_AUTH_WORKDIR}/network/policy.yml
#kubectl apply -f ${RBAC_NEW_AUTH_WORKDIR}/quota.yml
```

### test listing pods and it should succeed
```
kubectl --kubeconfig ${RBAC_NEW_AUTH_WORKDIR}/kube-config/${RBAC_NEW_AUTH_NAME}/config get pods
```
