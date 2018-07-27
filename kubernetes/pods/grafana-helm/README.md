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


# Dashboard format
The dashboard format is not just the json exported from the Grafana UI.  This will
describe a likely workflow that you might go through when creating a new Grafana
dashboard.

This allows you to automatically set it to the datastore that is being used.

You should first create your dashboard, in the Grafana UI.

## Getting the dashboard JSON via the API
If you want to make your dashboard persistent and managed by a config you will have
to output the JSON in a correct format that the Grafana Watcher can send to Grafana
when it starts.

Documentation: http://docs.grafana.org/http_api/dashboard/

### 1) Get the dashboard ID
Go to the Grafana web UI and go to your dashboard.  In the URL there is an ID.

```
https://grafana.example.com/d/_shG5vKik/my-dashboard?orgId=1
```

In this case, the ID is: `_shG5vKik`

### 2) Get the dashboard JSON
In the same browser where you have logged into Grafana, you can go to this URL to
get the JSON:

```
https://grafana.example.com/api/dashboards/uid/_shG5vKik
```

This will output a JSON.  Save it to a text file on your local computer: `/tmp/dashboard.json`

### 3) Edit the JSON

Run this command to edit the JSON:

```
cat /tmp/dashboard.json | jq 'del(.meta)' | jq '.dashboard |= . + {"folderId":0, "overwrite": true, "uid": null, "id": null}'
```

Note: this command requires `jq`.  You can download it here: https://stedolan.github.io/jq/download/

This also dont parameterize the datasources
