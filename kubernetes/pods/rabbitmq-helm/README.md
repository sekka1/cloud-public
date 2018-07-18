RabbitMQ
============

We are using this helm chart: https://github.com/kubernetes/charts/tree/master/stable/rabbitmq-ha

Using helm version: 2.8.1

# Using with make file:
```
export KUBE_NAMESPACE=rabbitmq
```

## Install:
```
make install
```

## Updating:
```
make upgrade
```

## Deleting:
```
make delete
```

## Listing helm charts:
```
make list
```

# Using Manually:
```
export KUBE_NAMESPACE=rabbitmq
```

```
helm repo add stable https://kubernetes-charts.storage.googleapis.com/

helm install \
--version 1.6.1 \
--name ${KUBE_NAMESPACE}-rabbitmq \
--namespace ${KUBE_NAMESPACE} \
--values ./kubernetes/pods/rabbitmq-helm/values.yml \
--debug \
stable/rabbitmq-ha
```

# Updating
```
helm upgrade \
--version 1.6.1 \
--values ./kubernetes/pods/rabbitmq-helm/values.yml \
${KUBE_NAMESPACE}-rabbitmq \
stable/rabbitmq-ha
```

## Enabling SSL Support

RabbitMQ Documentation: https://www.rabbitmq.com/ssl.html#enabling-ssl

Add in the certificates in this section:

```
rabbitmqCert:
  enabled: true

  # Specifies an existing secret to be used for SSL Certs
  existingSecret: ""

  ## Create a new secret using these values
  cacertfile: |
  certfile: |
  keyfile: |
```

The certs has to be base64 encoded:
```
cat certfile.ca | base64 -w0
```

Enable the SSL configurations:
```
rabbitmqAmqpsSupport:
  enabled: true

  # NodePort
  amqpsNodePort: 5671

  # SSL configuration
  config: |
    listeners.ssl.default             = 5671
    ssl_options.cacertfile            = /etc/cert/cacert.pem
    ssl_options.certfile              = /etc/cert/cert.pem
    ssl_options.keyfile               = /etc/cert/key.pem
    ssl_options.verify                = verify_peer
    ssl_options.fail_if_no_peer_cert  = false
```
