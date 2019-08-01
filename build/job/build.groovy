folder('petclinic') {
    displayName('Petclinic CI/CD')
    description('Folder for petclinic CI/CD jobs')
}

job('petclinic/Build-job') {
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
    scm {
        git {
            remote {
                url('https://github.com/berezinsn/spring-petclinic.git')
            }
            branch('master')
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
        shell('docker login -u ${LOGIN} -p ${PASS} registry.ci-cd.ru:16000')
        // Generation of the file with the combined version.
        maven {
            goals('clean deploy -Pdocker -B')
        }
    }
}
