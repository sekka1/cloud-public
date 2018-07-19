HELM_BINARY?=helm
KUBE_CONFIG_FILE?=~/.kube/config
KUBE_NAMESPACE?=devops

BASE_PATH=.

APPLCATION_CHART_NAME=stable/cert-manager
APPLICATION_NAME=cert-manager
APPLICATION_VERSION=0.4.0

VALUES_FILE?=values.yaml

install:
	${HELM_BINARY} install \
	--version ${APPLICATION_VERSION} \
	--namespace ${KUBE_NAMESPACE} \
	--name ${KUBE_NAMESPACE}-${APPLICATION_NAME} \
	--values ${BASE_PATH}/${VALUES_FILE} \
	${APPLCATION_CHART_NAME}

	kubectl -n ${KUBE_NAMESPACE} apply -f issuer.yaml

upgrade:
	${HELM_BINARY} upgrade \
	--version ${APPLICATION_VERSION} \
	--values ${BASE_PATH}/${VALUES_FILE} \
	${KUBE_NAMESPACE}-${APPLICATION_NAME} \
	${APPLCATION_CHART_NAME}

	kubectl -n ${KUBE_NAMESPACE} apply -f issuer.yaml

delete:
	${HELM_BINARY} delete --purge ${KUBE_NAMESPACE}-${APPLICATION_NAME}
	kubectl -n ${KUBE_NAMESPACE} delete secret letsencrypt-private-key

list:
	${HELM_BINARY} list
