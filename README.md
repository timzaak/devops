# DevOps Automation Utilities

This project provides a collection of Scala-based utilities and shell scripts designed to automate common DevOps tasks.

These tools help with managing Docker registries, transferring Docker images, initializing remote hosts, running scripts on multiple servers, and sending notifications to WeChat Work.

You can refer to deepwiki for more details.

## Use with jenkins pipeline

```groovy
parameters {
    string(
            name: "SCRIPT",
            trim:true,
            description: "script name, for example: DockerRegistryCleaner"
    )
    string(
            name: "PARAM",
            trim:true,
            description: "script parameters"
    )
}
stages {
    stage('compile') {
        steps {
            script  {
                // 1. checkout devops repo
                // 2. compile and archive
                sh "rm -rf script && mkdir script"
                sh "sbt --error stage"
                sh "cp -r target/universal/stage/* ./script/"
                sh "cp -r scripts ./script/"
                // archive artifact, other pipeline can use it with copy Artifact plugin 
                archiveArtifacts artifacts: 'script/**/*', followSymlinks: false
            }
        }
    }
    
    
    // 
    stage('run script') {
        steps {
            script {
                dir("script") {
                    // use config file provider plugin to load application.conf file
                    configFileProvider([configFile(fileId: '???', targetLocation: 'application.conf')]) {
                        sh "./bin/${params.SCRIPT} -Dconfig.file=application.conf -- ${params.PARAM}"
                    }

                }

            }
        }
    }
}




```
