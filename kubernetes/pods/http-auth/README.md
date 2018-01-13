http auth
=============
This is used with the ingress basic auth

Documentation on how to use this:  https://github.com/kubernetes/contrib/tree/master/ingress/controllers/nginx/examples/auth

## Installing htpasswd without Apache

### Ubuntu
```
apt-get install -y apache2-utils
```

## Create http basic auth secret
Assumes the current directory you are in is where this REAME.md resides

```
$ htpasswd -c ./auth kube-user
New password: <bar>
New password:
Re-type new password:
Adding password for user kube-user
```

You can append more users to this basic auth to give each user a unique username/password.

You can also have your new user run this command and give you the output. The output is a
hash which is not the password in clear text.

## Create the secret

    kubectl create secret generic basic-auth --from-file=./auth
