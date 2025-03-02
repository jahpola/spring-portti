#!/bin/bash

./gradlew build
./gradlew bootBuildImage --imagePlatform=linux/arm64 --imageName=public.ecr.aws/kerminator/spring-portti:arm64
./gradlew bootBuildImage --imagePlatform=linux/amd64 --imageName=public.ecr.aws/kerminator/spring-portti:amd64

docker push public.ecr.aws/kerminator/spring-portti:arm64
docker push public.ecr.aws/kerminator/spring-portti:amd64

docker manifest create public.ecr.aws/kerminator/spring-portti public.ecr.aws/kerminator/spring-portti:arm64 public.ecr.aws/kerminator/spring-portti:amd64
docker manifest annotate --arch arm64do

#docker manifest push public.ecr.aws/kerminator/spring-portti