package io.swagger.codegen;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.samskivert.mustache.Mustache;
import io.swagger.codegen.*;
import io.swagger.codegen.languages.AbstractCSharpCodegen;
import io.swagger.models.*;
import io.swagger.models.properties.*;
import io.swagger.util.Json;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.*;
import java.util.Map.Entry;
import static java.util.UUID.randomUUID;

public class AspAdvancedGenerator extends AbstractCSharpCodegen {

    private String packageGuid = "{" + randomUUID().toString().toUpperCase() + "}";

    @SuppressWarnings("hiding")
    protected Logger LOGGER = LoggerFactory.getLogger(AspAdvancedGenerator.class);

    public AspAdvancedGenerator() {
        super();

        setSourceFolder("src");
        outputFolder = "generated-code" + File.separator + this.getName();

        modelTemplateFiles.put("model.mustache", ".cs");
        apiTemplateFiles.put("controller.mustache", ".cs");

        // contextually reserved words
        // NOTE: C# uses camel cased reserved words, while models are title cased. We don't want lowercase comparisons.
        reservedWords.addAll(
            Arrays.asList("var", "async", "await", "dynamic", "yield")
            );

        cliOptions.clear();

        // CLI options
        addOption(CodegenConstants.PACKAGE_NAME,
                  "C# package name (convention: Title.Case).",
                  this.packageName);

        addOption(CodegenConstants.PACKAGE_VERSION,
                  "C# package version.",
                  this.packageVersion);

        addOption(CodegenConstants.OPTIONAL_PROJECT_GUID,
                  CodegenConstants.OPTIONAL_PROJECT_GUID_DESC,
                  null);

        addOption(CodegenConstants.SOURCE_FOLDER,
                  CodegenConstants.SOURCE_FOLDER_DESC,
                  sourceFolder);

        addOption(CodegenConstants.PRESERVE_COMMENT_NEWLINES,
                  "Preserve newlines in comments",
                  String.valueOf(this.preserveNewLines));

        // CLI Switches
        addSwitch(CodegenConstants.SORT_PARAMS_BY_REQUIRED_FLAG,
                  CodegenConstants.SORT_PARAMS_BY_REQUIRED_FLAG_DESC,
                  this.sortParamsByRequiredFlag);

        addSwitch(CodegenConstants.USE_DATETIME_OFFSET,
                  CodegenConstants.USE_DATETIME_OFFSET_DESC,
                  this.useDateTimeOffsetFlag);

        addSwitch(CodegenConstants.USE_COLLECTION,
                  CodegenConstants.USE_COLLECTION_DESC,
                  this.useCollection);

        addSwitch(CodegenConstants.RETURN_ICOLLECTION,
                  CodegenConstants.RETURN_ICOLLECTION_DESC,
                  this.returnICollection);
    }

    @Override
    public CodegenType getTag() {
        return CodegenType.SERVER;
    }

    @Override
    public String getName() {
        return "AspAdvanced";
    }

    @Override
    public String getHelp() {
        return "Generates an ASP.NET Core Web API server.";
    }

    @Override
    public void processOpts() {
        super.processOpts();

        if (additionalProperties.containsKey(CodegenConstants.OPTIONAL_PROJECT_GUID)) {
            setPackageGuid((String) additionalProperties.get(CodegenConstants.OPTIONAL_PROJECT_GUID));
        }
        additionalProperties.put("packageGuid", packageGuid);

        additionalProperties.put("dockerTag", this.packageName.toLowerCase());

        apiPackage = packageName + ".Controllers";
        modelPackage = packageName + ".Model";

        String packageFolder = sourceFolder + File.separator + packageName;

        supportingFiles.add(new SupportingFile("NuGet.Config", "", "NuGet.Config"));
        supportingFiles.add(new SupportingFile("build.sh.mustache", "", "build.sh"));
        supportingFiles.add(new SupportingFile("build.bat.mustache", "", "build.bat"));
        supportingFiles.add(new SupportingFile("README.mustache", "", "README.md"));
        supportingFiles.add(new SupportingFile("Solution.mustache", "", this.packageName + ".sln"));
        supportingFiles.add(new SupportingFile("Dockerfile.mustache", packageFolder, "Dockerfile"));
        supportingFiles.add(new SupportingFile("gitignore", packageFolder, ".gitignore"));
        supportingFiles.add(new SupportingFile("appsettings.json", packageFolder, "appsettings.json"));

        supportingFiles.add(new SupportingFile("Startup.mustache", packageFolder, "Startup.cs"));
        supportingFiles.add(new SupportingFile("Program.mustache", packageFolder, "Program.cs"));
        supportingFiles.add(new SupportingFile("validateModel.mustache", packageFolder + File.separator + "Attributes", "ValidateModelStateAttribute.cs"));
        supportingFiles.add(new SupportingFile("web.config", packageFolder, "web.config"));

        supportingFiles.add(new SupportingFile("Project.csproj.mustache", packageFolder, this.packageName + ".csproj"));

        supportingFiles.add(new SupportingFile("Properties" + File.separator + "launchSettings.json", packageFolder + File.separator + "Properties", "launchSettings.json"));

        supportingFiles.add(new SupportingFile("Filters" + File.separator + "BasePathFilter.mustache", packageFolder + File.separator + "Filters", "BasePathFilter.cs"));
        supportingFiles.add(new SupportingFile("Filters" + File.separator + "GeneratePathParamsValidationFilter.mustache", packageFolder + File.separator + "Filters", "GeneratePathParamsValidationFilter.cs"));

        supportingFiles.add(new SupportingFile("wwwroot" + File.separator + "README.md", packageFolder + File.separator + "wwwroot", "README.md"));
        supportingFiles.add(new SupportingFile("wwwroot" + File.separator + "index.html", packageFolder + File.separator + "wwwroot", "index.html"));
        supportingFiles.add(new SupportingFile("wwwroot" + File.separator + "web.config", packageFolder + File.separator + "wwwroot", "web.config"));

        supportingFiles.add(new SupportingFile("wwwroot" + File.separator + "swagger-original.mustache", packageFolder + File.separator + "wwwroot", "swagger-original.json"));
    }

    @Override
    public void setSourceFolder(final String sourceFolder) {
        if(sourceFolder == null) {
            LOGGER.warn("No sourceFolder specified, using default");
            this.sourceFolder =  "src" + File.separator + this.packageName;
        } else if(!sourceFolder.equals("src") && !sourceFolder.startsWith("src")) {
            LOGGER.warn("ASP.NET Core requires source code exists under src. Adjusting.");
            this.sourceFolder =  "src" + File.separator + sourceFolder;
        } else {
            this.sourceFolder = sourceFolder;
        }
    }

    public void setPackageGuid(String packageGuid) {
        this.packageGuid = packageGuid;
    }

    @Override
    public String apiFileFolder() {
        return outputFolder + File.separator + sourceFolder + File.separator + packageName + File.separator + "Controllers";
    }

    @Override
    public String modelFileFolder() {
        return outputFolder + File.separator + sourceFolder + File.separator + packageName + File.separator  + "Model";
    }


    @Override
    public Map<String, Object> postProcessSupportingFileData(Map<String, Object> objs) {
        Swagger swagger = (Swagger)objs.get("swagger");
        if(swagger != null) {
            try {
                objs.put("swagger-json", Json.pretty().writeValueAsString(swagger).replace("\r\n", "\n"));
            } catch (JsonProcessingException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return super.postProcessSupportingFileData(objs);
    }


    @Override
    protected void processOperation(CodegenOperation operation) {
        super.processOperation(operation);

        // HACK: Unlikely in the wild, but we need to clean operation paths for MVC Routing
        if (operation.path != null) {
            String original = operation.path;
            operation.path = operation.path.replace("?", "/");
            if (!original.equals(operation.path)) {
                LOGGER.warn("Normalized " + original + " to " + operation.path + ". Please verify generated source.");
            }
        }

        // Converts, for example, PUT to HttpPut for controller attributes
        operation.httpMethod = "Http" + operation.httpMethod.substring(0, 1) + operation.httpMethod.substring(1).toLowerCase();
    }

    @Override
    public Mustache.Compiler processCompiler(Mustache.Compiler compiler) {
        // To avoid unexpected behaviors when options are passed programmatically such as { "useCollection": "" }
        return super.processCompiler(compiler).emptyStringIsFalse(true);
    }

    @Override
    public String toEnumVarName(String name, String datatype) {
        if (name.length() == 0) {
            return "Empty";
        }

        // for symbol, e.g. $, #
        if (getSymbolName(name) != null) {
            return camelize(getSymbolName(name));
        }

        String enumName = name.toLowerCase();
        enumName = sanitizeName(camelize(enumName));
        enumName = enumName.replaceFirst("^_", "");
        enumName = enumName.replaceFirst("_$", "");

        if (enumName.matches("\\d.*")) { // starts with number
            return "_" + enumName;
        } else {
            return enumName;
        }
    }

    @Override
    public String toEnumName(CodegenProperty property) {
        return sanitizeName(camelize(property.name));
    }
}
