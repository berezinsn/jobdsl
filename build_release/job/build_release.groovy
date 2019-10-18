job('petclinic/Build-Release') {
    label('slave')
    wrappers {
        maskPasswords()
        colorizeOutput()
        timestamps()
        preBuildCleanup()
        credentialsBinding {
            usernamePassword {
                // Name of an environment variable to be set to the username during the build.
                usernameVariable('LOGIN')
                // Name of an environment variable to be set to the password during the build.
                passwordVariable('PASS')
                // Credentials of an appropriate type to be set to the variable.
                credentialsId('REGISTRY')
            }
        }
    }
    properties {
        githubProjectProperty {
            // Mandatory part
            projectUrlStr('https://github.com/berezinsn/spring-petclinic/')
        }
        promotions {
            promotion {
                name('Create release candidate')
                restrict('slave')
                conditions {
                    manual('')
                }
                actions {
                    copyArtifacts('${PROMOTED_JOB_FULL_NAME}') {
                        includePatterns('env.properties')
                        buildSelector {
                            buildNumber('${PROMOTED_NUMBER}')
                        }
                    }
                    downstreamParameterized {
                        trigger('Create-Release-Candidate') {
                            parameters {
                                propertiesFile('env.properties')
                            }
                        }
                    }
                }
            }
        }
    }
    scm {
        git {
            remote {
                credentials('GIT')
                url('https://github.com/berezinsn/spring-petclinic.git')
                refspec('+refs/heads/*:refs/remotes/origin/*')
            }
            branch('*/release-*.*')
            extensions {
                wipeOutWorkspace()
            }
        }
    }
    triggers {
        scm('H/15 * * * *')
    }
    steps {
        // Secured docker registry authentication with bind credentials .
        shell(readFileFromWorkspace('shell/docker_login.sh'))
        // Generation of the file with the combined version.
        shell(readFileFromWorkspace('shell/build_version.sh'))
        envInjectBuilder {
            propertiesFilePath('env.properties')
            propertiesContent('')
        }
        maven {
            goals('versions:set -B')
            property('newVersion', '${VERSION}')
        }
        maven {
            goals('clean deploy -Pdocker -B')
        }
    }
}
