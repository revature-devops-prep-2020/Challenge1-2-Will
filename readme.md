# Challenges 1 and 2

## Description
This project will be a CI/CD pipeline using Jenkins and AWS EKS.  The master branch contains all the files needed to spin up Jenkins and the other branches will contain the sample applications sent through the pipeline.  Please note the "dev" branch's application has not been successfully pushed through the pipeline, though the "dev2" branch's application has been tested extensively and is working.

## Creation of the EKS cluster via commandline
There are various ways to create an EKS cluster from the commandline.  The aws CLI commands support it, but I have found it to be much easier to use the eksctl commands.

The following command was run to create my cluster.

``` 
 eksctl create cluster \
 --name myEKS \
 --version 1.17 \
 --region us-east-1 \
 --nodegroup-name devnodes \
 --node-type t2.medium \
 --nodes 2
 ```

## Creation of Jenkins server
The YAML files needed for Jenkins are all contained in the jenkins folder.  However, the allin1.yaml contains them all in one file.  Simply apply this file once your Kubernetes context is set to the EKS cluster.

```
kubectl apply -f allin1.yaml
```

After configuring security through AWS, you can now access the Jenkins server.  All necessary plugins are already installed.


## Jenkins pipeline
A Jenkinsfile is located on the root directory of the sample application.  It uses individual docker agents to perform each task, and this also allows us to use certain functions such as kubectl commands without having to install it on the Jenkins instance.
