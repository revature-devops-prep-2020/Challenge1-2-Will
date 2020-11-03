## Challenge 1

# Creation of the EKS cluster via commandline
There are various ways to create an EKS cluster from the commandline.  The aws CLI commands support it, but I have found it to be much easier to use the eksctl commands.

The following command was run to create my cluster.

``` 
 eksctl create cluster \
 --name myEKS \
 --version 1.17 \
 --region us-east-1 \
 --nodegroup-name devnodes \
 --node-type t2.micro \
 --nodes 2
 ```

