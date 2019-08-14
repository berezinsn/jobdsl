job('petclinic/Create-Release-Branch') {
    label('slave')
    wrappers {
        sshAgent('git')
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
                credentials('GIT')
                url('https://github.com/berezinsn/spring-petclinic.git')
            }
            branch('dev')
            extensions {
                wipeOutWorkspace()
            }
        }
        gitSCM {
            branches {
                branchSpec {
                    // Specific branch in a repository.
                    name('dev')
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