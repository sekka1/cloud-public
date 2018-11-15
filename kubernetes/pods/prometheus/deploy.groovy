pipeline {
    agent {
      node {
        label 'allinone'
      }
    }

    parameters {
        string(name: 'BRANCH_NAME',      defaultValue: 'master',        description: 'What branch from github to use')
        string(name: 'ENVIRONMENT',      defaultValue: 'dev',           description: 'What env should this be deployed to.')
        string(name: 'KUBE_NAMESPACE',   defaultValue: 'devops',       description: 'What kube namespace should this be deployed to.')
        //string(name: 'DOCKER_TAG',       defaultValue: 'latest',        description: 'Tag or version of the image: ie, 1.0')
      }

    environment {
      ENVIRONMENT      = "${params.ENVIRONMENT}"
      KUBE_NAMESPACE   = "${params.KUBE_NAMESPACE}"
      BASE_DEPLOY_PATH = "./kubernetes/pods/prometheus"

      // CREDENTIAL_ID_* are jenkins secrets that are mapped to the ID names of it
      CREDENTIAL_ID_KUBE_SERVER_URL = "null"
      CREDENTIAL_ID_KUBE_USERNAME = "null"
      CREDENTIAL_ID_KUBE_PASSWORD = "null"

    }

    stages {

      stage('Deploy-rules') {
          steps {

            script {

              switch(ENVIRONMENT) {
                case "dev":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'DEV_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'DEV_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'DEV_KUBE_PASSWORD'
                  break
                case "test":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'TEST_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'TEST_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'TEST_KUBE_PASSWORD'
                  break
                case "stage":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'STAGE_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'STAGE_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'STAGE_KUBE_PASSWORD'
                  break
                case "preview":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'PREVIEW_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'PREVIEW_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'PREVIEW_KUBE_PASSWORD'
                  break
                case "prod":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'PROD_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'PROD_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'PROD_KUBE_PASSWORD'
                  break
              }
            }

            withCredentials([
                            string(credentialsId: CREDENTIAL_ID_KUBE_SERVER_URL, variable: 'KUBE_SERVER_URL'),
                            string(credentialsId: CREDENTIAL_ID_KUBE_USERNAME, variable: 'KUBE_USERNAME'),
                            string(credentialsId: CREDENTIAL_ID_KUBE_PASSWORD, variable: 'KUBE_PASSWORD')
                            ]) {

                  sh """
                    cd ${BASE_DEPLOY_PATH}/rules
                    ls -l ./

                    # concat all of the rules files.  Starting with the aaaa.yml
                    rm rules-configmap.* || true
                    rm rules.yml || true
                    rm rules.yaml || true
                    cat * > rules.yml

                    # Create configmap file
                    kubectl -s ${KUBE_SERVER_URL} --username=${KUBE_USERNAME}  --password=${KUBE_PASSWORD} --insecure-skip-tls-verify  --namespace ${KUBE_NAMESPACE} \
                    create configmap prometheus-rules --from-file=rules.yml --dry-run -o yaml | tee rules-configmap.yml

                    # apply configmap
                    kubectl -s ${KUBE_SERVER_URL} --username=${KUBE_USERNAME}  --password=${KUBE_PASSWORD} --insecure-skip-tls-verify  --namespace ${KUBE_NAMESPACE} \
                    apply -f rules-configmap.yml
                  """

                }

          }
      }

      stage('Deploy-prometheus') {
          steps {

            script {

              switch(ENVIRONMENT) {
                case "dev":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'DEV_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'DEV_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'DEV_KUBE_PASSWORD'
                  break
                case "test":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'TEST_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'TEST_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'TEST_KUBE_PASSWORD'
                  break
                case "stage":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'STAGE_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'STAGE_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'STAGE_KUBE_PASSWORD'
                  break
                case "preview":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'PREVIEW_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'PREVIEW_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'PREVIEW_KUBE_PASSWORD'
                  break
                case "prod":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'PROD_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'PROD_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'PROD_KUBE_PASSWORD'
                  break
              }
            }

            withCredentials([
                            string(credentialsId: CREDENTIAL_ID_KUBE_SERVER_URL, variable: 'KUBE_SERVER_URL'),
                            string(credentialsId: CREDENTIAL_ID_KUBE_USERNAME, variable: 'KUBE_USERNAME'),
                            string(credentialsId: CREDENTIAL_ID_KUBE_PASSWORD, variable: 'KUBE_PASSWORD')
                            ]) {

                  // Output templated files
                  sh "rm -rf  ${BASE_DEPLOY_PATH}/template_output"
                  sh "mkdir -p ${BASE_DEPLOY_PATH}/template_output"
                  template_replace("${BASE_DEPLOY_PATH}/deployment.template.yaml", "${BASE_DEPLOY_PATH}/template_output/deployment.yaml")
                  template_replace("${BASE_DEPLOY_PATH}/service.yaml", "${BASE_DEPLOY_PATH}/template_output/service.yaml")
                  template_replace("${BASE_DEPLOY_PATH}/ingress.template.yaml", "${BASE_DEPLOY_PATH}/template_output/ingress.yaml")
                  template_replace("${BASE_DEPLOY_PATH}/configmap.yaml", "${BASE_DEPLOY_PATH}/template_output/configmap.yaml")
                  sh "cp ${BASE_DEPLOY_PATH}/clusterrolebinding.yaml ${BASE_DEPLOY_PATH}/template_output/"

                  sh """
                    cd ${BASE_DEPLOY_PATH}/template_output
                    ls -l ./
                    cat *
                    kubectl -s ${KUBE_SERVER_URL} --username=${KUBE_USERNAME}  --password=${KUBE_PASSWORD} --insecure-skip-tls-verify  --namespace ${KUBE_NAMESPACE} \
                    apply -f ./
                  """

                }

          }
      }

      stage('Deploy-node-exporter') {
          steps {

            script {

              switch(ENVIRONMENT) {
                case "dev":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'DEV_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'DEV_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'DEV_KUBE_PASSWORD'
                  break
                case "test":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'TEST_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'TEST_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'TEST_KUBE_PASSWORD'
                  break
                case "stage":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'STAGE_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'STAGE_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'STAGE_KUBE_PASSWORD'
                  break
                case "preview":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'PREVIEW_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'PREVIEW_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'PREVIEW_KUBE_PASSWORD'
                  break
                case "prod":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'PROD_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'PROD_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'PROD_KUBE_PASSWORD'
                  break
              }
            }

            withCredentials([
                            string(credentialsId: CREDENTIAL_ID_KUBE_SERVER_URL, variable: 'KUBE_SERVER_URL'),
                            string(credentialsId: CREDENTIAL_ID_KUBE_USERNAME, variable: 'KUBE_USERNAME'),
                            string(credentialsId: CREDENTIAL_ID_KUBE_PASSWORD, variable: 'KUBE_PASSWORD')
                            ]) {

                  sh """
                    cd ${BASE_DEPLOY_PATH}/node-exporter
                    ls -l ./
                    cat *
                    kubectl -s ${KUBE_SERVER_URL} --username=${KUBE_USERNAME}  --password=${KUBE_PASSWORD} --insecure-skip-tls-verify  --namespace ${KUBE_NAMESPACE} \
                    apply -f ./
                  """

                }

          }
      }

      stage('Deploy-kube-state-metric') {
          steps {

            script {

              switch(ENVIRONMENT) {
                case "dev":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'DEV_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'DEV_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'DEV_KUBE_PASSWORD'
                  break
                case "test":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'TEST_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'TEST_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'TEST_KUBE_PASSWORD'
                  break
                case "stage":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'STAGE_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'STAGE_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'STAGE_KUBE_PASSWORD'
                  break
                case "preview":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'PREVIEW_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'PREVIEW_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'PREVIEW_KUBE_PASSWORD'
                  break
                case "prod":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'PROD_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'PROD_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'PROD_KUBE_PASSWORD'
                  break
              }
            }

            withCredentials([
                            string(credentialsId: CREDENTIAL_ID_KUBE_SERVER_URL, variable: 'KUBE_SERVER_URL'),
                            string(credentialsId: CREDENTIAL_ID_KUBE_USERNAME, variable: 'KUBE_USERNAME'),
                            string(credentialsId: CREDENTIAL_ID_KUBE_PASSWORD, variable: 'KUBE_PASSWORD')
                            ]) {

                  sh """
                    cd ${BASE_DEPLOY_PATH}/kube-state-metric
                    ls -l ./
                    cat *
                    kubectl -s ${KUBE_SERVER_URL} --username=${KUBE_USERNAME}  --password=${KUBE_PASSWORD} --insecure-skip-tls-verify  --namespace ${KUBE_NAMESPACE} \
                    apply -f ./
                  """

                }

          }
      }

      stage('Deploy-alert-manager') {
          steps {

            script {

              switch(ENVIRONMENT) {
                case "dev":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'DEV_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'DEV_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'DEV_KUBE_PASSWORD'
                  CREDENTIAL_PROMETHEUS_SLACK_WEBHOOK = 'DEV_PROMETHEUS_SLACK_WEBHOOK'
                  CREDENTIAL_PROMETHEUS_PAGERDUTY_SERVICE_KEY = 'DEV_PROMETHEUS_PAGERDUTY_SERVICE_KEY'
                  break
                case "test":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'TEST_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'TEST_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'TEST_KUBE_PASSWORD'
                  CREDENTIAL_PROMETHEUS_SLACK_WEBHOOK = 'TEST_PROMETHEUS_SLACK_WEBHOOK'
                  CREDENTIAL_PROMETHEUS_PAGERDUTY_SERVICE_KEY = 'TEST_PROMETHEUS_PAGERDUTY_SERVICE_KEY'
                  break
                case "stage":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'STAGE_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'STAGE_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'STAGE_KUBE_PASSWORD'
                  CREDENTIAL_PROMETHEUS_SLACK_WEBHOOK = 'STAGE_PROMETHEUS_SLACK_WEBHOOK'
                  CREDENTIAL_PROMETHEUS_PAGERDUTY_SERVICE_KEY = 'STAGE_PROMETHEUS_PAGERDUTY_SERVICE_KEY'
                  break
                case "preview":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'PREVIEW_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'PREVIEW_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'PREVIEW_KUBE_PASSWORD'
                  CREDENTIAL_PROMETHEUS_SLACK_WEBHOOK = 'PREVIEW_PROMETHEUS_SLACK_WEBHOOK'
                  CREDENTIAL_PROMETHEUS_PAGERDUTY_SERVICE_KEY = 'PREVIEW_PROMETHEUS_PAGERDUTY_SERVICE_KEY'
                  break
                case "prod":
                  CREDENTIAL_ID_KUBE_SERVER_URL = 'PROD_KUBE_SERVER_URL'
                  CREDENTIAL_ID_KUBE_USERNAME = 'PROD_KUBE_USERNAME'
                  CREDENTIAL_ID_KUBE_PASSWORD = 'PROD_KUBE_PASSWORD'
                  CREDENTIAL_PROMETHEUS_SLACK_WEBHOOK = 'PROD_PROMETHEUS_SLACK_WEBHOOK'
                  CREDENTIAL_PROMETHEUS_PAGERDUTY_SERVICE_KEY = 'PROD_PROMETHEUS_PAGERDUTY_SERVICE_KEY'
                  break
              }
            }

            withCredentials([
                            string(credentialsId: CREDENTIAL_ID_KUBE_SERVER_URL, variable: 'KUBE_SERVER_URL'),
                            string(credentialsId: CREDENTIAL_ID_KUBE_USERNAME, variable: 'KUBE_USERNAME'),
                            string(credentialsId: CREDENTIAL_ID_KUBE_PASSWORD, variable: 'KUBE_PASSWORD'),
                            string(credentialsId: CREDENTIAL_PROMETHEUS_SLACK_WEBHOOK, variable: 'PROMETHEUS_SLACK_WEBHOOK'),
                            string(credentialsId: CREDENTIAL_PROMETHEUS_PAGERDUTY_SERVICE_KEY, variable: 'PROMETHEUS_PAGERDUTY_SERVICE_KEY')
                            ]) {

                  // Output templated files
                  sh "rm -rf  ${BASE_DEPLOY_PATH}/template_output"
                  sh "mkdir -p ${BASE_DEPLOY_PATH}/template_output"
                  template_replace("${BASE_DEPLOY_PATH}/alertmanager/deployment.template.yaml", "${BASE_DEPLOY_PATH}/template_output/deployment.yaml")
                  template_replace("${BASE_DEPLOY_PATH}/alertmanager/service.yaml", "${BASE_DEPLOY_PATH}/template_output/service.yaml")
                  template_replace("${BASE_DEPLOY_PATH}/alertmanager/configmap.template.yaml", "${BASE_DEPLOY_PATH}/template_output/configmap.yaml")

                  sh """
                    cd ${BASE_DEPLOY_PATH}/template_output
                    ls -l ./
                    cat *
                    kubectl -s ${KUBE_SERVER_URL} --username=${KUBE_USERNAME}  --password=${KUBE_PASSWORD} --insecure-skip-tls-verify  --namespace ${KUBE_NAMESPACE} \
                    apply -f ./
                  """

                }

          }
      }


    }
    post {
        success {
	        //notifyBuild("SUCCESSFUL")
          sh "echo success"
        }
	    failure {
          //notifyBuild("FAILED")
          sh "echo fail"
	    }
    }
}


def template_replace(template_file, output_file) {
  //
  // This will replace all of the variables in the file ${variable} and
  // then output it to another file.
  //


  sh """
    envsubst < ${template_file} > ${output_file}
    echo "XXXXXXXXXXXXXXXXXXXXXX"
    echo "XXXXXXXXXXXXXXXXXXXXXX"
    cat ${output_file}
    echo "XXXXXXXXXXXXXXXXXXXXXX"
    echo "XXXXXXXXXXXXXXXXXXXXXX"
  """
}
