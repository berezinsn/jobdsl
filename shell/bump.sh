#!/bin/bash
RELEASE_VERSION=release-$(mvn -q -Dexec.executable=echo -Dexec.args='${project.version}' --non-recursive exec:exec | sed 's/[a-zA-Z<>\/-]//g;s/[.]*$//' | head -c 3)
git checkout -b ${RELEASE_VERSION}

BUMPED_VERSION=`xpath pom.xml '/project/version' 2>/dev/null|sed 's/[a-zA-Z<>\/-]//g;s/[.]*$//'|awk -F "." '{$2+=1;OFS=".";print$0}'`

echo "Version $existing_version was bumped to $bumped_version"
