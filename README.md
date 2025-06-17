# DevOps Automation Utilities

This project provides a collection of Scala-based utilities and shell scripts designed to automate common DevOps tasks.

These tools help with managing Docker registries, transferring Docker images, initializing remote hosts, and running scripts on multiple servers.

## Docker Registry Cleaner (`DockerRegistryCleaner.scala`)

This utility helps keep your private Docker registry tidy by removing older image tags.

For each repository, it retains only the 10 most recently pushed tags and deletes the rest.

**Configuration:**

The cleaner is configured through the `application.conf` file (typically located in `src/main/resources/`).

You'll need to define the properties for your private Docker registry under a path like `repository.private.office`. This includes the registry URL, username, and password.

Example configuration snippet in `application.conf`:

```hocon
repository {
  private {
    office {
      url = "https://your-private-registry.example.com"
      username = "your-username"
      password = "your-password"
    }
  }
}
```

**How to Run:**

You can run the cleaner using `sbt`:

```bash
sbt "runMain DockerRegistryCleaner"
```

## Docker Image Transfer (`DockerTransfer.scala`)

This script automates the process of transferring Docker images from one registry to another. It performs the following steps:
1. Pulls the specified image from a source registry (e.g., `quay.io`).
2. Retags the image for the target registry.
3. Push the retagged image to the target registry.
4. Removes both the original and retagged images from the local machine.

**Configuration:**

The target Docker host (registry) is configured in `application.conf` via the `repository.docker.host` property.

Example configuration snippet in `application.conf`:

```hocon
repository {
  docker {
    host = "your-target-registry.example.com"
  }
}
```

**How to Run:**

Execute the script using `sbt`, providing the full image name(s) (including tag) as arguments:

```bash
sbt "runMain DockerTransfer user/image:tag"
```

You can specify multiple images to transfer:

```bash
sbt "runMain DockerTransfer quay.io/user/image1:tag1 user/image2:latest"
```

The script will adjust the image name if it's from `quay.io` by removing the `quay.io/` prefix before tagging it for the target repository.

## Host Initialization (`InitHost.scala`)

The `InitHost` utility is used to initialize remote hosts by adding a specified public SSH key to their `~/.ssh/authorized_keys` file.

This allows for passwordless SSH access to the configured servers.

**Configuration:**

Configuration for `InitHost` is managed in `application.conf`:

*   **Public Key**: The SSH public key to be deployed is specified under `office.devops.public`.
*   **Server List**: The target servers are defined under `office.servers`. Each server entry should include details like hostname, port, and username. The script can filter servers based on their name (e.g., excluding "monitor" or "jenkins" as seen in the current implementation).

Example configuration snippet in `application.conf`:

```hocon
office {
  devops {
    public = "ssh-rsa AAAA..." # Your public SSH key
  }
  servers = [
    { name = "server1", host = "192.168.1.10", port = 22, username = "admin" },
    { name = "server2", host = "server2.example.com", port = 2222, username = "user" },
    # Add other servers here
  ]
}
```

**How to Run:**

You can run the host initialization script using `sbt`:

```bash
sbt "runMain initHost"
```
The script will iterate through the configured servers (respecting any filters) and attempt to add the public key.

## Run Remote Script (`RunRemoteScript.scala`)

This utility provides a way to execute a script on one or more remote hosts that are defined in the configuration. It leverages `ConfigShellRun.remoteRun` to achieve this.

**Configuration:**

The remote hosts and their SSH connection details (hostname, port, username, private key path or password) need to be configured in `application.conf`. The `ConfigShellRun.remoteRun` method likely expects a specific structure for this configuration, often a list of servers similar to `InitHost`. (Refer to the `com.timzaak.devops.scripts.ConfigShellRun` source or its usage for exact configuration key names if not documented elsewhere).

A hypothetical configuration might look like:

```hocon
remote {
  execution {
    servers = [
      { name = "target1", host = "target1.example.com", username = "ops", privateKey = "/path/to/key.pem" },
      { name = "target2", host = "192.168.1.20", username = "ops", password = "securepassword" }
    ]
    # Default settings for scripts_path, etc. might also be here
  }
}
```
*(Note: The exact HOCON path for server configuration for `ConfigShellRun` needs to be verified from its implementation or existing `application.conf` files.)*

**How to Run:**

To execute a script on the configured remote hosts, use `sbt` and provide the name or path of the script as an argument:

```bash
sbt "runMain RunRemoteScript your_script_name_or_path_in_config"
```

For example, if you have a script entry named `my_setup_script` defined in your configuration that points to `/opt/scripts/setup.sh` on the remote machines:

```bash
sbt "runMain RunRemoteScript my_setup_script"
```

## Included Shell Scripts (`scripts/`)

The project includes the following shell scripts in the `scripts/` directory:


### `net_stable_test.sh`

This script is designed for network stability testing. It repeatedly sends HTTP GET requests to a specified URL and reports statistics on success, failure, and response times.

**Usage:**

```bash
./scripts/net_stable_test.sh [options] <URL>
```

**Options:**

*   `-n <number>`: Total number of requests (default: 100).
*   `-s <seconds>`: Sleep time in seconds between requests (default: 1).
*   `-t <seconds>`: Timeout in seconds for each request (default: 5).
*   `-r <HOST:PORT:IP>`: Custom DNS resolution (e.g., `example.com:443:1.2.3.4`). This allows you to bypass public DNS and test a server at a specific IP address.
*   `-h`: Display help information.

**Example:**

```bash
# Perform 50 requests to https://www.example.com/health, with 0.5s interval
./scripts/net_stable_test.sh -n 50 -s 0.5 https://www.example.com/health

# Test with custom DNS resolution
./scripts/net_stable_test.sh -r 'www.example.com:443:1.2.3.4' https://www.example.com/health
```
This script requires `curl` and `bc` to be installed on the system where it's run.

## Configuration

This project uses the HOCON (Human-Optimized Config Object Notation) format for configuration.

The main configuration file is typically `src/main/resources/application.conf`.

## How to Build

This project is built using [sbt](https://www.scala-sbt.org/) (Simple Build Tool).

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
