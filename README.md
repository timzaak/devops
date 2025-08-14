# DevOps Automation Utilities

This project provides a collection of Scala-based utilities and shell scripts designed to automate common DevOps tasks.

These tools help with managing Docker registries, transferring Docker images, initializing remote hosts, running scripts on multiple servers, and sending notifications to WeChat Work.

## WeChat Work Webhook (`WeChatWorkSender.scala`)

This utility provides functionality to send messages to WeChat Work (企业微信) groups via webhook.

It supports multiple message types including text, Markdown, news articles, and system alerts.

**Configuration:**

Configure the webhook URL in `application.conf`:

```hocon
wechat {
  work {
    webhook {
      url = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=your-webhook-key"
      # Or use environment variable: ${?WECHAT_WORK_WEBHOOK_URL}
    }
  }
}
```

**How to Run:**

Send different types of messages:

```bash
# Send text message
sbt "runMain WeChatWorkSender sendText -m 'Hello from DevOps!'"

# Send Markdown message
sbt "runMain WeChatWorkSender sendMarkdown -m '## Alert\n**Status**: OK'"

# Send news article
sbt "runMain WeChatWorkSender sendNews -t 'System Update' -d 'New version deployed' -u 'https://example.com'"

# Send system alert
sbt "runMain WeChatWorkSender sendAlert -s 'web-service' -l 'error' -m 'Service is down'"
```

For detailed usage instructions, see [WECHAT_WEBHOOK.md](WECHAT_WEBHOOK.md).


## Use with jenkins pipeline
sbt native packager would change script file name, so we need to rename it to camel case with script: `scripts/rename_name_to_camel.sh` after `sbt stage`.


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
                dir('script/bin') {
                    sh '''
                    to_camel() {
                        name="$1"
                        ext="${name##*.}"
                        base="${name%.*}"

                        camel=$(echo "$base" | sed -E 's/[^a-zA-Z0-9]+/ /g' | awk '{for(i=1;i<=NF;i++){ $i=toupper(substr($i,1,1)) substr($i,2)}; print}' | tr -d ' ')
                        
                        if [ "$base" != "$name" ]; then
                            echo "$camel.$ext"
                        else
                            echo "$camel"
                        fi
                    }

                    for file in *; do
                        [ -f "$file" ] || continue
                        newname=$(to_camel "$file")
                        if [ "$file" != "$newname" ]; then
                            mv "$file" "$newname"
                            echo "Renamed $file -> $newname"
                        fi
                    done
'''
                }
                // archive artifact, other pipeline can use it with copy Artifact plugin 
                archiveArtifacts artifacts: 'script/**/*', followSymlinks: false
            }
        }
    }
    
    
    
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
