# PowerShell script to standardize Application.java files across Web projects
$baseDir = "d:\Project\kcloudy\java\WebApi"
$template = @'
package kc.webapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import kc.database.repository.TreeNodeRepositoryFactoryBean;

@SpringBootApplication
@ServletComponentScan
@EntityScan({ "kc.framework.base", "kc.model" })
@ComponentScan({ "kc.mapping.*", "kc.service.*", "kc.service.webapiservice", "kc.web.*", "kc.web.multitenancy",
		"kc.webapi.*" })
@EnableJpaRepositories(basePackages = { "kc.dataaccess.*",
		"kc.database" }, repositoryFactoryBeanClass = TreeNodeRepositoryFactoryBean.class)
public class Application extends kc.web.WebApiSecurityConfig {
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(Application.class);
		app.addListeners(new ApplicationPidFileWriter());
		app.run(args);
	}
}
'@

# Map of module names to their model package names
$moduleModelMap = @{
    "account" = "account"
    "app" = "app"
    "codegenerate" = "codegenerate"
    "config" = "config"
    "dict" = "dict"
    "offering" = "offering"
    "portal" = "portal"
    "training" = "training"
}

# Process each module
foreach ($module in $moduleModelMap.Keys) {
    $appFile = Join-Path $baseDir "kc.webapi.$module\src\main\java\kc\webapi\Application.java"
    $modelPackage = $moduleModelMap[$module]
    
    # Create the content with the correct model package
    $content = $template -replace "MODULE", $modelPackage
    
    # Write the file with UTF-8 encoding
    [System.IO.File]::WriteAllText($appFile, $content, [System.Text.Encoding]::UTF8)
    Write-Host "Updated: $appFile"
}

Write-Host "All Application.java files have been updated successfully."