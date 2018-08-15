Kustomize
============

Project Source: https://github.com/kubernetes-sigs/kustomize

Good:
* Simple to customize values
* If you have a define workflow and these files will be the only way you use it, then it works great and it is simple

Not so good:
* Doesnt support patching of the `Ingress` resource
* Not as flexible as Golang Templating.  No `if` statements
  * You wont be able to let the user choose if they want an option enabled or not
* To add a namespace to all files, you have to replicate all files and add the namespace key/val pair

# Running

```
kustomize build overlays/production | kubectl apply -f -

```

# Examples:

An extension to this example: https://github.com/kubernetes-sigs/kustomize/tree/master/examples/helloWorld


## Production overlay

```
kustomize build overlays/production
```

Notice:

The deployment:
* adds in the common labels
* adds in the common annotations
* adds in the cpu/mem resource section
* adds in the nodeSelector
* adds in the tolerations


## staging overlay

```
kustomize build overlays/staging
```

The name:
* uses the `namePrefix` and this is prefixing all of the names

The ingress:
* Is not patch aware yet (https://github.com/kubernetes-sigs/kustomize/issues/169).  It wipes out the entire section and replaces it with what you explicitly put down.  The `http` section is gone.
