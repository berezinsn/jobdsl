MAVEN_PROJECT_VERSION=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec |sed 's/[a-zA-Z<>\/-]//g;s/[.]*$//')
TIMESTAMP=$(date "+%Y%m%d.%H%M%S")
GIT_HASH=$(git log -1 --pretty=%h)
MAVEN_UPDATED_PROJECT_VERSION="${MAVEN_PROJECT_VERSION}-${TIMESTAMP}-${GIT_HASH}"
echo "VERSION=${MAVEN_UPDATED_PROJECT_VERSION}" >> env.properties
