# Advanced AspNetCore Swagger Codegen library

## Installation

```
wget http://central.maven.org/maven2/io/swagger/swagger-codegen-cli/2.4.2/swagger-codegen-cli-2.4.2.jar
wget https://github.com/darth10/asp-advanced-swagger-codegen/releases/download/1.0.3/asp-advanced-swagger-codegen-1.0.3.jar
```

## Building

```
mvn package
```

## Usage

```
java -Dmodels \
    -cp asp-advanced-swagger-codegen-1.0.3.jar:swagger-codegen-cli-2.4.2.jar \
    io.swagger.codegen.SwaggerCodegen generate \
    -i swagger.yaml -o ./gen -l AspAdvanced
```

On Windows, use `;` instead of `:` in the classpath (`-cp`) argument.
Config files are also supported with the `-c` argument.
Run the `config-help -l AspAdvanced` command for options.

For building only the models, use the following package references.
```
<ItemGroup>
  <PackageReference Include="System.Runtime.Serialization.Formatters" Version="4.3.0" />
  <PackageReference Include="System.ComponentModel.Annotations" Version="4.5.0" />
  <PackageReference Include="Newtonsoft.Json" Version="9.0.1" />
</ItemGroup>
```
