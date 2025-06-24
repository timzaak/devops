$imageName = "${Env:dockerRepo}/${Env:dockerImage}:${Env:VERSION}"
echo "build image is: $imageName"

docker build -t $imageName .
docker push $imageName