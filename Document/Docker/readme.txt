#save image to local file system as a tar file
docker save centos > /home/sokasyn/Documents/docker/saved/centos_base.tar

#load image tar file into docker
docker load < /home/sokasyn/Documents/docker/saved/centos_base.tar

