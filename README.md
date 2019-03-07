# Advanced AspNetCore Swagger Codegen library

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
