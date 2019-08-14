BRANCH_NAME=release-$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec | sed 's/[a-zA-Z<>\/-]//g;s/[.]*$//' | head -c 3)
echo "BRANCH_NAME=${BRANCH_NAME}" >> env.properties
DEVELOPMENT_VERSION=$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec | awk -F "." '{$2+=1;OFS=".";print$0}')
echo "DEVELOPMENT_VERSION=${DEVELOPMENT_VERSION}" >> env.properties
