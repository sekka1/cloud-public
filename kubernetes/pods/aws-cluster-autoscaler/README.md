AWS Cluster Autoscaler
========================

Project source: https://github.com/kubernetes/autoscaler/tree/master/cluster-autoscaler

Watches kubernetes API and adjusts the size of the Kubernetes cluster when:

* pods that failed to run in the cluster due to insufficient resources.
* nodes in the cluster are so underutilized, for an extended period of time, that they can be deleted and their pods will be easily placed on some other, existing nodes.

## AWS Deployment info

https://github.com/kubernetes/autoscaler/tree/master/cluster-autoscaler/cloudprovider/aws

## IAM permissions

* Kube Nodes need ASG Privs, attach the AutoScalingFullAccess to the IAM role

https://github.com/kubernetes/autoscaler/blob/master/cluster-autoscaler/cloudprovider/aws/README.md#permissions

```
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "autoscaling:DescribeAutoScalingGroups",
                "autoscaling:DescribeAutoScalingInstances",
                "autoscaling:SetDesiredCapacity",
                "autoscaling:TerminateInstanceInAutoScalingGroup",
                "autoscaling:DescribeLaunchConfigurations"
            ],
            "Resource": "*"
        }
    ]
}
```

## Kustomize tamplating usage

```
kustomize build overlays/dev | kubectl apply -f -
```
