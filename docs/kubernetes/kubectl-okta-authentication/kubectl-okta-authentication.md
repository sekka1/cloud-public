Kubectl Okta Authentication
=================
This procedure walks you through how you would setup your local machine to be able
to authenticate through Okta when using kubectl.

# Tools to download:
Put all of the tools within your system path and make it executable.

## aws-okta

Installation: https://github.com/segmentio/aws-okta/wiki/Installation

```
chmod +x aws-okta-v0.19.4-linux-amd64
cp aws-okta-v0.19.4-linux-amd64 /usr/local/bin/aws-okta
```

## aws-iam-authenticator

Installation: https://github.com/kubernetes-sigs/aws-iam-authenticator/releases

```
chmod +x aws-iam-authenticator_0.4.0-alpha.1_linux_amd64
cp aws-iam-authenticator_0.4.0-alpha.1_linux_amd64 /usr/local/bin/aws-iam-authenticator
```

## kubectl

Installation: https://kubernetes.io/docs/tasks/tools/install-kubectl/

```
chmod +x kubectl
cp kubectl /usr/local/bin/kubectl
```

## Add your Okta credentials to your keyring
This will ask for your okta credentials and save it to your local keyring.

```
root@80e7dc7eae74:/opt# aws-okta add
Okta organization: devops.bot
Okta username: <your user name>
Okta password:
Enter passphrase to unlock /root/.aws-okta/: INFO[0021] Added credentials for user
root@80e7dc7eae74:/opt#

```

## Kube config
Add the kubeconfig for this cluster.  This kubeconfig now contains no secret and
can be safely passed around publicly.  However, a unique kubeconfig is still needed
for a cluster due to the certificate on the k8s master.

You can find it here relative to this path: `./kubeconfig/kubeconfig`

Set your kube config.  You might hvae it in the default `~/.kube/config` location
already.  If you do, you do not need to do this step or if you have some other method
to set your kubeconfig context.

Append this kube config to your `KUBECONFIG` path:
```
export KUBECONFIG=$KUBECONFIG:./kubeconfig/kubeconfig
```

To view all of your kube config's contexts:

```
kubectl config view
```

View current context:

```
kubectl config current-context
```

Switch to this cluster's context:

```
kubectl config use-context dev2.devops.bot
```

## AWS config

Add this content into your AWS config file `~/.aws/config`

```
[okta]
aws_saml_url = home/amazon_aws/0oa6kbgg95juqUgTY1t7/272
role_arn = arn:aws:iam::354114410416:role/Engineering
```

## Run command that will authenticate and execute a kubectl command

```
root@80e7dc7eae74:/opt# ./aws-okta-v0.19.4-linux-amd64 --debug exec okta -- kubectl -n default get serviceaccounts
DEBU[0000] Parsing config file /root/.aws/config        
Enter passphrase to unlock /root/.aws-okta/: DEBU[0001]  Using session 3X7M, expires in 59m51.035888184s
NAME      SECRETS   AGE
default   1         4d
```
