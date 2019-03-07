# Advanced AspNetCore Swagger Codegen library

## Installation

```
wget http://central.maven.org/maven2/io/swagger/swagger-codegen-cli/2.4.2/swagger-codegen-cli-2.4.2.jar
wget https://github.com/darth10/asp-advanced-swagger-codegen/releases/download/1.0.0/AspAdvanced-swagger-codegen-1.0.0.jar
```

## Building

```
mvn package
```

## Usage

```
java -Dmodels \
    -cp AspAdvanced/target/AspAdvanced-swagger-codegen-1.0.0.jar:swagger/swagger-codegen-cli-2.4.2.jar \
    io.swagger.codegen.SwaggerCodegen generate \
    -i swagger.yaml -o ./gen -l AspAdvanced
```

On Windows, use `;` instead of `:` in the classpath (`-cp`) argument.
Config files are also supported with the `-c` argument.
Run the `config-help -l AspAdvanced` command for options.
