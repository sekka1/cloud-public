pipeline {
    agent {
      node {
        label 'allinone'
      }
    }

    parameters {
        string(name: 'BRANCH_NAME',   defaultValue: 'master',            description: 'What Github branch to use')
        choice(name: 'ENVIRONMENT', choices: 'dev\ntest\nstage\nprod', description: 'What environment to deploy into?')
        string(name: 'KUBE_NAMESPACE',   defaultValue: 'default',            description: 'What kube namespace should this be deployed to.')
      }

    environment {
      KUBE_NAMESPACE   = "${params.KUBE_NAMESPACE}"
      ENVIRONMENT      = "${params.ENVIRONMENT}"

      // CREDENTIAL_ID_* are jenkins secrets that are mapped to the ID names of it
      CREDENTIAL_ID_KUBE_SERVER_URL = "null"
      CREDENTIAL_ID_KUBE_USERNAME = "null"
      CREDENTIAL_ID_KUBE_PASSWORD = "null"

      BASE_DEPLOY_PATH = "./kubernetes/pods/ingress"

    }

    stages {
        stage('Build') {
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
                          cd ${BASE_DEPLOY_PATH}
                          ls -l ./

                          kubectl -s ${KUBE_SERVER_URL} --username=${KUBE_USERNAME}  --password=${KUBE_PASSWORD} --insecure-skip-tls-verify  --namespace default get pods -o wide
                          kubectl -s ${KUBE_SERVER_URL} --username=${KUBE_USERNAME}  --password=${KUBE_PASSWORD} --insecure-skip-tls-verify  --namespace default apply -f .
                          kubectl -s ${KUBE_SERVER_URL} --username=${KUBE_USERNAME}  --password=${KUBE_PASSWORD} --insecure-skip-tls-verify  --namespace default get pods -o wide
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
