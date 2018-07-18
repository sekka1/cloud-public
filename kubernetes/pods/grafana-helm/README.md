Grafana
============

We are using this helm chart: https://github.com/kubernetes/charts/tree/master/stable/grafana

Using helm version: v2.9.0

# Using with make file:
```
export KUBE_NAMESPACE=infrastructure
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

# Installing Manually:
```
export KUBE_NAMESPACE=devops
```

```
helm repo add stable https://kubernetes-charts.storage.googleapis.com/

helm install \
--version 1.9.1 \
--namespace ${KUBE_NAMESPACE} \
--name ${KUBE_NAMESPACE}-grafana \
--values ./kubernetes/pods/grafana-helm/values.yaml \
stable/grafana
```

# Updating
```
helm upgrade \
--version 1.9.1 \
--values ./Kubernetes/pods/grafana-helm/values.yaml \
${KUBE_NAMESPACE}-grafana \
stable/grafana
```

# Adding dashboards
Dashboards can be added as code into this repository and updated on the Kubernetes system.

In the `values.yaml` file you can add the dashboard in a json format:

```
dashboards:
  some-dashboard:
    json: |
      $RAW_JSON
```

# Dashboard format
The dasboard format is not just the json exported from the Grafana UI.  This will
describe a likely workflow that you might go through when creating a new Grafana
dashboard.

Go into a Grafana UI and create the dashboard with the items you want.  Export
the dashboard in the settings menu and copy the json.

You will need to put this json inside of a "Grafana Dashboard json".

The file name of the dashboard needs to end with `-dashboard.json`.  The grafana
watcher is looking for these files.

The file name of the datasources needs to end with `-datasource.json`.  The Grafana
watcher is looking for these files.

```
{
  "__inputs": [
    {
      "name": "DS_PROMETHEUS",
      "label": "prometheus",
      "description": "",
      "type": "datasource",
      "pluginId": "prometheus",
      "pluginName": "Prometheus"
    }
  ],
  "__requires": [
    {
      "type": "grafana",
      "id": "grafana",
      "name": "Grafana",
      "version": "4.6.2"
    },
    {
      "type": "panel",
      "id": "graph",
      "name": "Graph",
      "version": ""
    },
    {
      "type": "datasource",
      "id": "prometheus",
      "name": "Prometheus",
      "version": "1.0.0"
    },
    {
      "type": "panel",
      "id": "singlestat",
      "name": "Singlestat",
      "version": ""
    },
    {
      "type": "panel",
      "id": "text",
      "name": "Text",
      "version": ""
    }
  ],
  ...
  ...
  < Put the dashboard json here >
  ...
  ...
}
```

This allows you to automatically set it to the datastore that is being used.
