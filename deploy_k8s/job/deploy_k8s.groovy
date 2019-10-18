job('petclinic/Deploy-K8S') {
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
    parameters {
        stringParam('VERSION', '', 'HELM-chart version')
    }
    steps {
        // Deployment script .
        shell(readFileFromWorkspace('shell/deploy.sh'))
    }
}