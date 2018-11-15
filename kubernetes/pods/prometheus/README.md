Wercker Prometheus
=======================

# Deploying

## Create the rules.yaml

Worker dir should be where this README.md file is.

```
cat ./kubernetes/pods/prometheus/rules/* > /tmp/prometheus_all_rules.yml

# Creates the config map
kubectl --namespace devops create configmap prometheus-rules --from-file=/tmp/prometheus_all_rules.yml --dry-run -o yaml > /tmp/prometheus_all_rules_config_map.yml

# Apply the config map
kubectl --namespace devops apply -f /tmp/prometheus_all_rules_config_map.yml
```
