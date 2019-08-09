job('petclinic/Create-Release-Branch') {
    label('slave')
    wrappers {
        maskPasswords()
        colorizeOutput()
        timestamps()
        preBuildCleanup()
    }
    properties {
        githubProjectProperty {
            // Mandatory part
            projectUrlStr('https://github.com/berezinsn/spring-petclinic/')
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
    }
    steps {
//        shell(readFileFromWorkspace('shell/combined_version.sh'))
//        envInjectBuilder {
//            propertiesFilePath('env.properties')
//            propertiesContent('')
//        }
        maven {
            goals('-B release:clean release:branch')
            property('branchName', 'release-2.0')
            property('developmentVersion', '2.0.0-SNAPSHOT')
        }
        maven {
            goals('clean install -B')
        }
    }
}