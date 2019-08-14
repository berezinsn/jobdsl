job('petclinic/Create-Release-Branch') {
    label('slave')
    wrappers {
        sshAgent('GIT_SSH')
        maskPasswords()
        colorizeOutput()
        timestamps()
        preBuildCleanup()
    }
    properties {
        githubProjectProperty {
            // Mandatory part
            projectUrlStr('https://github.com/berezinsn/spring-petclinic')
        }
    }
    scm {
        git {
            remote {
                url('https://github.com/berezinsn/spring-petclinic.git')
                credentials('GIT_SSH')
            }
            branch('dev')
            configure { node ->
                node / 'extensions' << 'hudson.plugins.git.extensions.impl.LocalBranch' {
                    localBranch('dev')
                    extensions {
                        wipeOutWorkspace()
                    }
                }
            }
        }
    }
    steps {
        shell(readFileFromWorkspace('shell/git_config.sh'))
        shell(readFileFromWorkspace('shell/release_versions.sh'))
        envInjectBuilder {
            propertiesFilePath('env.properties')
            propertiesContent('')
        }
        maven {
            goals('-B release:clean release:branch')
            property('branchName', '${BRANCH_NAME}')
            property('developmentVersion', '${DEVELOPMENT_VERSION}')
        }
        maven {
            goals('clean install -B')
        }
    }
}