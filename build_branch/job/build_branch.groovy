job('petclinic/Build-branch') {
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
    }
    parameters {
        gitParam('BRANCH') {
            description('Custom branch parameter')
            type('BRANCH')
            defaultValue('master')
        }
    }
    scm {
        git {
            remote {
                url('https://github.com/berezinsn/spring-petclinic.git')
            }
            branch('${BRANCH}')
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
        shell(readFileFromWorkspace('build/shell/docker_login.sh'))
        // Generation of the file with the combined version.
        shell(readFileFromWorkspace('build/shell/combined_version.sh'))
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