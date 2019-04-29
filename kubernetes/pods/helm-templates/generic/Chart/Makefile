HELM_BINARY?=helm
KUBECONFIG?=~/.kube/config
KUBE_NAMESPACE?=dev

UNIT_NAME?=api

ENVIRONMENT?=dev

BASE_VALUES_FILE=values.yaml
COMMON_VALUES_FILE=values.yaml
VALUES_FILE?=values.yaml

TEMPLATE_OUTPUT=./templated-${UNIT_NAME}-helm.yaml

template:
	${HELM_BINARY} template \
	--name ${UNIT_NAME} \
	--values ./${BASE_VALUES_FILE} \
	--values ./${COMMON_VALUES_FILE} \
	--values ./${VALUES_FILE} \
	./

apply:
	${HELM_BINARY} template \
	--name ${UNIT_NAME} \
	--values ./${BASE_VALUES_FILE} \
	--values ./${COMMON_VALUES_FILE} \
	--values ./${VALUES_FILE} \
	./ > ${TEMPLATE_OUTPUT}
	kubectl apply -f ${TEMPLATE_OUTPUT}

delete:
	${HELM_BINARY} template \
	--name ${UNIT_NAME} \
	--values ./${BASE_VALUES_FILE} \
	--values ./${COMMON_VALUES_FILE} \
	--values ./${VALUES_FILE} \
	./ > ${TEMPLATE_OUTPUT}
	kubectl delete -f ${TEMPLATE_OUTPUT}
