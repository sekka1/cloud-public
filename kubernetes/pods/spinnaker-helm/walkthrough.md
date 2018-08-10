# Spinnaker Walk Through

## Important Installation Considerations

Spinnaker will allow you to orchestrate the continuous deployment of applications into your cluster, for this, Spinnaker need to have access to the docker registries of this applications.

You need to specify the credentials and the path to which docker repositories you want to enable before installing in the [values.yaml](values.yaml#L15-L19)

As you can see, by default the only enabled libraries are the following:

```yaml
accounts:
- name: dockerhub
  address: https://index.docker.io
  repositories:
    - library/alpine
    - library/ubuntu
    - library/centos
    - library/nginx
```

## Installation

For installing spinnaker just follow [this](README.md) instructions.

## Post installation step

### Enable permission to the service account

Once installed, Spinnaker will require for us to grant it access to the cluster, for this we will create a ClusterRoleBinding:

```bash
$ kubectl apply -f ClusterRoleBinding.yaml
clusterrolebinding "spinnaker" created
```

Note that this will give Spinnaker admin access to the cluster, you might want to limit the permissions in a production deployment.

### Create namespace for your deployment

Spinnaker cannot create namespaces, because of this we will first create a namespace for our deployments:

```bash
$ kubectl create namespace prod
namespace "prod" created
```

## Deploying and Scaling a Containerized Application through Spinnaker

Assuming that you have cli access to the cluster, you can access Spinnaker deck with:

```bash
export DECK_POD=$(kubectl get pods --namespace spinnaker -l "component=deck,app=spinnaker-spinnaker-spin" -o jsonpath="{.items[0].metadata.name}")
kubectl port-forward --namespace spinnaker $DECK_POD 9000 &
open http://localhost:9000
```

Once opened you will see the following screen:

![screen shot 2018-08-10 at 12 59 23](https://user-images.githubusercontent.com/1569751/43954557-3882efe0-9c9d-11e8-8bfe-f1d51efeefe4.png)

Let’s get familiar with Spinnaker concepts and terminology by deploying a simple Nginx web server.

Start by creating an application by clicking on Create Application under the Actions menu on the right top corner.

An “application” here is a logical collection of various resources such as Load Balancers, Security Groups, Server Groups, and Clusters.

![screen shot 2018-08-10 at 12 55 35](https://user-images.githubusercontent.com/1569751/43954409-b39dd312-9c9c-11e8-8839-53c88bdb7ec4.png)

Let’s create a Load Balancer through which the application can be accessed. The Spinnaker Load Balancer would be similar to a Service in Kubernetes.

Click on the Load Balancer on the top menu bar, and then click on the Create Load Balancer button.

![screen shot 2018-08-10 at 13 01 23](https://user-images.githubusercontent.com/1569751/43954632-83020042-9c9d-11e8-8dd8-495a69bd66cd.png)

When creating a new Load Balancer, select prod as the namespace, 80 for Target Port, and choose NodePort as Type. Click on Create button when done.

![screen shot 2018-08-10 at 13 12 56](https://user-images.githubusercontent.com/1569751/43955116-205b6d32-9c9f-11e8-82b6-038ce9a371dd.png)

Now lets create a deployment of nginx for our service or load balancer, under Clusters, click on Create Server Group.
                                                                       
![screen shot 2018-08-10 at 13 08 54](https://user-images.githubusercontent.com/1569751/43954935-90b5f31e-9c9e-11e8-9b85-0a393d3f45da.png)

When creating a new Server Group, select prod as the namespace. Choose nginx:latest as the container from the dropdown. Choose nginx as the Load Balancer, which we created in the previous step. Type 10 as capacity.

![screen shot 2018-08-10 at 13 15 22](https://user-images.githubusercontent.com/1569751/43955211-75173dd8-9c9f-11e8-9b2d-f0942deb6d68.png)

Wait for the instances under the Server Group to become available. The red colored blocks indicate that the instances are not ready yet.

![screen shot 2018-08-10 at 13 16 10](https://user-images.githubusercontent.com/1569751/43955253-90d9f13c-9c9f-11e8-9ec3-708e79610575.png)

![screen shot 2018-08-10 at 13 21 07](https://user-images.githubusercontent.com/1569751/43955396-4317e232-9ca0-11e8-84f0-84168d6b6417.png)

Switch to the terminal, and run the following command:

```bash
$ kubectl get pods -n prod
NAME               READY     STATUS    RESTARTS   AGE
nginx-v000-2kb7w   1/1       Running   0          1m
nginx-v000-4xsdw   1/1       Running   0          1m
nginx-v000-9jtnt   1/1       Running   0          1m
nginx-v000-d77wp   1/1       Running   0          1m
nginx-v000-fzfnx   1/1       Running   0          1m
nginx-v000-j4q4m   1/1       Running   0          5m
nginx-v000-kbvcm   1/1       Running   0          1m
nginx-v000-kj2zp   1/1       Running   0          1m
nginx-v000-nnrmb   1/1       Running   0          1m
nginx-v000-v2pwg   1/1       Running   0          1m
$ kubectl get services -n prod
NAME      TYPE       CLUSTER-IP     EXTERNAL-IP   PORT(S)        AGE
nginx     NodePort   10.96.28.126   <none>        80:30887/TCP   4m
```

