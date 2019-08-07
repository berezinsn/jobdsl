job('petclinic/PR-build') {
    label('slave')
    wrappers {
        maskPasswords()
        colorizeOutput()
        timestamps()
        preBuildCleanup()
    }
    scm {
        git {
            remote {
                credentials('GIT')
                url('https://github.com/berezinsn/spring-petclinic.git')
                refspec('+refs/pull/*:refs/remotes/origin/pr/*')
            }
            branch('${sha1}')
            extensions {
                wipeOutWorkspace()
            }
        }
    }
    triggers {
        githubPullRequest {
            useGitHubHooks()
            permitAll()
            extensions {
                commitStatus {
                    triggeredStatus('starting deployment to staging site...')
                    startedStatus('deploying to staging site...')
                    completedStatus('SUCCESS', 'All is well')
                    completedStatus('FAILURE', 'Something went wrong. Investigate!')
                    completedStatus('PENDING', 'still in progress...')
                    completedStatus('ERROR', 'Something went really wrong. Investigate!')
                }
                buildStatus {
                    completedStatus('SUCCESS', 'There were no errors, go have a cup of coffee...')
                    completedStatus('FAILURE', 'There were errors, for info, please see...')
                    completedStatus('ERROR', 'There was an error in the infrastructure, please contact...')
                }
            }
        }
        steps {
            // Secured docker registry authentication with bind credentials .
//        shell(readFileFromWorkspace('build/shell/docker_login.sh'))
            // Generation of the file with the combined version.
//        shell(readFileFromWorkspace('build/shell/combined_version.sh'))
//        envInjectBuilder {
//            propertiesFilePath('env.properties')
//            propertiesContent('')
//        }
//        maven {
//            goals('versions:set -B')
//            property('newVersion', '${VERSION}')
//        }
            maven {
                goals('clean deploy -Pdocker -B')
            }
        }
    }