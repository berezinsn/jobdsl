export TILLER_NAMESPACE=kube-system
GOOGLE_APPLICATION_CREDENTIALS=/account.json
export GOOGLE_APPLICATION_CREDENTIALS
gcloud auth activate-service-account --key-file=/account.json

# show authorized account (to check the correct account has been choosed)
gcloud auth list
gcloud --version
gcloud container clusters get-credentials udc --project consul-vault-infra --zone=europe-west1-b
kubectl get pods
curl -X GET -u ${LOGIN}:${PASS} https://nexus.ci-cd.ru/repository/tgz/${VERSION} -O
helm init --client-only
helm install ${VERSION}