HELM_BINARY?=helm
KUBECTL_BINARY?=kubectl
KUBECONFIG?=~/.kube/config
KUBE_NAMESPACE?=monitoring

APPLICATION_NAME=prometheus-operator

BASE_PATH=.

APPLCATION_CHART_NAME=stable/prometheus-operator
# APPLICATION_VERSION=2.1.5

BASE_VALUES_FILE?=values.yaml
VALUES_FILE?=values-dev.yaml

TEMPLATE_OUTPUT_FILE?=/tmp/output.yaml

install:
	${HELM_BINARY} upgrade --install ${KUBE_NAMESPACE}-${APPLICATION_NAME} \
	--namespace ${KUBE_NAMESPACE} \
	--values ${BASE_PATH}/${BASE_VALUES_FILE} \
	--values ${BASE_PATH}/${VALUES_FILE} \
	./

template:
	${HELM_BINARY} template \
	--namespace ${KUBE_NAMESPACE} \
	--name ${KUBE_NAMESPACE}-${APPLICATION_NAME} \
	--values ${BASE_PATH}/${BASE_VALUES_FILE} \
	--values ${BASE_PATH}/${VALUES_FILE} \
	./ > ${TEMPLATE_OUTPUT_FILE}

upgrade:
	${HELM_BINARY} upgrade \
	--values ${BASE_PATH}/${BASE_VALUES_FILE} \
	--values ${BASE_PATH}/${VALUES_FILE} \
	--namespace ${KUBE_NAMESPACE} \
	${KUBE_NAMESPACE}-${APPLICATION_NAME} \
	./

delete:
	${HELM_BINARY} delete --purge ${KUBE_NAMESPACE}-${APPLICATION_NAME}
	# https://github.com/helm/charts/tree/master/stable/prometheus-operator#uninstalling-the-chart
	${KUBECTL_BINARY} delete crd prometheuses.monitoring.coreos.com
	${KUBECTL_BINARY} delete crd prometheusrules.monitoring.coreos.com
	${KUBECTL_BINARY} delete crd servicemonitors.monitoring.coreos.com
	${KUBECTL_BINARY} delete crd alertmanagers.monitoring.coreos.com

# delete-all:
# 	${KUBECTL_BINARY} -n ${KUBE_NAMESPACE} delete pvc ${KUBE_NAMESPACE}-postgresql
# 	${KUBECTL_BINARY} -n ${KUBE_NAMESPACE} delete configmap ingress-controller-leader-kong

list:
	${HELM_BINARY} list
