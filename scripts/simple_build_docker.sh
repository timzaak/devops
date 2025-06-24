imageName="${dockerRepo}/${dockerImage}:${VERSION}"
echo "build image is: $imageName"

docker build -t "$imageName" .
docker push "$imageName"