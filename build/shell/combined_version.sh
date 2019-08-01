MAVEN_PROJECT_VERSION=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec |sed 's/[a-zA-Z<>\/-]//g;s/[.]*$//')
TIMESTAMP=$(date "+%Y%m%d.%H%M%S")
GIT_HASH=$(git log -1 --pretty=%h)
VERSION="${MAVEN_PROJECT_VERSION}-${TIMESTAMP}-${GIT_HASH}"
