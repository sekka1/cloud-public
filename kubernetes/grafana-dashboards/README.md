Grafana Dashboards
===================

This folder holds dashboards that we are using and maintaining.

# Creating a dashboard
These dashboards can be uploaded to the Grafana Dashboard store or used in the
Grafana Helm Chart.

## 1) Create/update a dashboard in the Grafana UI
Set up the dashboard to how you like it to be.  

Then go to the gear icon on the top right and on the left hand side, click `View JSON`

Copy this JSON

## 2) Add inputs and requires into the JSON
Add this to the top of the JSON.  This is assuming you are using Prometheus as the
datasource

```
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
```

## 3) search and replace
In your JSON search and replace:

	"datasource": "Prometheus"   to:  "datasource": "${DS_PROMETHEUS}"

  uid and id to: null
