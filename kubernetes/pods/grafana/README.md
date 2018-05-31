


Grafana Configuration:

http://docs.grafana.org/installation/configuration/




Scripted Dashboards:
  http://docs.grafana.org/reference/scripting/


Dashboard json
http://docs.grafana.org/reference/dashboard/#dashboard-json


## Variable style
Use the double bracket (`[[var]]`) variable style instead of the dollar sign (`${var}`).

Example:
```
[[pod_name]]
```

## Wrapping the dashboard in the `dashboard` context:

```
{
  "dashboard": {
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
    ]
    <<< Insert dashboard here >>>

  },
  "inputs": [
    {
      "name": "DS_PROMETHEUS",
      "pluginId": "prometheus",
      "type": "datasource",
      "value": "prometheus"
    }
  ],
  "overwrite": true
}
```

## Install plugin at start
http://docs.grafana.org/installation/docker/#installing-plugins-for-grafana
