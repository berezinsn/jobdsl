job('petclinic/Build-Dev') {
    label('slave')
 //   publishers {
 //       archiveArtifacts('env.properties')
 //   }
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
    promotions {
        promotion("Development") {
            icon("star-red")
            conditions {
                manual('')
            }
            actions {
                downstreamParameterized {
                    trigger("deploy-application", "SUCCESS", false, ["buildStepFailure": "FAILURE", "failure": "FAILURE", "unstable": "UNSTABLE"]) {
                        predefinedProp("ENVIRONMENT", "test-server")
                        predefinedProp("APPLICATION_NAME", "\${PROMOTED_JOB_FULL_NAME}")
                        predefinedProp("BUILD_ID", "\${PROMOTED_NUMBER}")
                    }
                }
            }
        }
    }
    scm {
        git {
            remote {
                url('https://github.com/berezinsn/spring-petclinic.git')
            }
            branch('dev')
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
