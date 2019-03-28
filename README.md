# Advanced AspNetCore Swagger Codegen library

## Installation

```
wget http://central.maven.org/maven2/io/swagger/codegen/v3/swagger-codegen-cli/3.0.5/swagger-codegen-cli-3.0.5.jar
wget https://github.com/darth10/asp-advanced-swagger-codegen/releases/download/1.0.8/asp-advanced-swagger-codegen-1.0.8.jar
```

## Usage

```
java -Dmodels \
    -cp asp-advanced-swagger-codegen-1.0.8.jar:swagger-codegen-cli-3.0.5.jar \
    io.swagger.codegen.Codegen generate \
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

## Contributing

To build the package, run the following command.
```
mvn package
```
