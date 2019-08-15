docker pull registry.ci-cd.ru:16000/dev/petclinic:${VERSION}
docker tag registry.ci-cd.ru:16000/dev/petclinic:${VERSION} registry.ci-cd.ru:16000/release/petclinic:${VERSION}
docker push registry.ci-cd.ru:16000/release/petclinic:${VERSION}