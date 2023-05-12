# Authenticator
## _Project for Java Authenticator_

## Contempla:

- JAVA 17
- Spring Boot 3.0.5
- Spring HATEOAS
- Unit Tests
- JWT Authenticate
- Lombok
- H2

## Plugins VSCode:

- Coverage Gutters
- GitLens — Git supercharged
- Lombok Annotations Support for VS Code
- SonarLint
- VsCode Action Buttons
- vscode-icons

## Sugestões para configs do VSCode

settings.json
```json
{
    "java.configuration.updateBuildConfiguration": "interactive",
    "files.exclude": {
        "**/.git": true,
        "**/.svn": true,
        "**/.hg": true,
        "**/CVS": true,
        "**/.DS_Store": true,
        "**/target": true,
        "**mvn**": true
    },
    "java.compile.nullAnalysis.mode": "automatic",
    "spring-boot.ls.java.home": "{path}\\jdk-17",
    "java.jdt.ls.java.home": "{path}\\jdk-17",
    "actionButtons": {
        "commands": [
            {
                "name": "MVN_TEST",
                "color": "yellow",
                "command": "mvn -o test"
            },
            {
                "name": "JACOCO",
                "color": "yellow",
                "command": "mvn jacoco:report"
            }
        ]
    }
}
```

launch.json
``` json
{
    // Use IntelliSense to learn about possible attributes.
    // Hover to view descriptions of existing attributes.
    // For more information, visit: https://go.microsoft.com/fwlink/?linkid=830387
    "version": "0.2.0",
    "configurations": [
        {
            "type": "java",
            "name": "authenticator",
            "request": "launch",
            "cwd": "${workspaceFolder}",
            "console": "internalConsole",
            "mainClass": "com.moraes.authenticator.AuthenticatorApplication",
            "projectName": "authenticator",
            "env": {
                "server.port": 8080
            }
        }
    ]
}
```


## License

MIT

**Free Software, Hell Yeah!**

[![Linkedin](https://media.licdn.com/dms/image/C4D16AQHC7cR9vVD6Ow/profile-displaybackgroundimage-shrink_350_1400/0/1662490166630?e=1686787200&v=beta&t=1KkYwyylM7tx9nd4GE0sT4W-I1o3rU2EHJLi9c2vDTg)](https://www.linkedin.com/in/suleiman-alves-de-moraes-14b449145/)
