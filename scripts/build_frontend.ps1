#export dockerImage,dockerRepo,BuildTest,VERSION


$imageName = "${Env:dockerRepo}/${Env:dockerImage}:${Env:VERSION}"
echo "build image is: $imageName"


if (-not [string]::IsNullOrEmpty($env:BuildTest)) {
    echo "npm run build:test"
    npm run build:test
} else {
    echo "npm run build"
    npm run build
}

docker build -t $imageName .
docker push $imageName
