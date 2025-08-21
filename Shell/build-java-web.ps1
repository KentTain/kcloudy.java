<#
.SYNOPSIS
Build and deploy .NET Web application to Docker container

.DESCRIPTION
This script is used to build .NET Web application, create Docker image, and run container.

.PARAMETER solutionType
Type of the solution to build, default is "Web"

.PARAMETER solutionName
Name of the solution to build

.PARAMETER versionNum
Version number, default is 1

.PARAMETER httpPort
http port to run the container on

.PARAMETER httpsPort
HTTPS port to run the container on, default is 0 (not specified)

.PARAMETER env
Deployment environment, default is "prod"

.EXAMPLE
.\build-java-web.ps1 -solutionType "Web" -solutionName "kc.web.account" -versionNum 1 -httpPort 2001 -httpsPort 0 -env "prod"
#>

param(
    [Parameter(Mandatory=$false)]
    [string]$solutionType,

    [Parameter(Mandatory=$false)]
    [string]$solutionName,

    [Parameter(Mandatory=$false)]
    [int]$versionNum,

    [Parameter(Mandatory=$false)]
    [int]$httpPort,

    [Parameter(Mandatory=$false)]
    [int]$httpsPort = 0,

    [Parameter(Mandatory=$false)]
    [string]$env = "prod"
)

# Set console output encoding to UTF-8
[Console]::OutputEncoding = [System.Text.Encoding]::UTF8
$OutputEncoding = [System.Text.Encoding]::UTF8
[System.Console]::OutputEncoding = [System.Text.Encoding]::UTF8
[System.Console]::InputEncoding = [System.Text.Encoding]::UTF8

# Set error action preference
$ErrorActionPreference = "Stop"

# Get current hostname
$CURRENT_HOST = $env:COMPUTERNAME

# Define output functions
function Write-Info {
    param([string]$message)
    Write-Host "💡 [INFO]: $message" -ForegroundColor Cyan
}

function Write-Success {
    param([string]$message)
    Write-Host "✅ [SUCCESS] $message" -ForegroundColor Green
}

function Write-Warning {
    param([string]$message)
    Write-Host "⚠️ [WARNING] $message" -ForegroundColor Yellow
}

function Write-Error {
    param([string]$message)
    Write-Host "❌ [ERROR] $message" -ForegroundColor Red
}

# Define build and deploy function
function Build-Java-Web {
    [CmdletBinding()]
    param(
        [Parameter(Mandatory=$true)]
        [string]$solutionType,

        [Parameter(Mandatory=$true)]
        [string]$solutionName,

        [Parameter(Mandatory=$true)]
        [int]$versionNum,

        [Parameter(Mandatory=$true)]
        [int]$httpPort,

        [Parameter(Mandatory=$false)]
        [int]$httpsPort = 0,

        [Parameter(Mandatory=$false)]
        [string]$env = "prod"
    )

    # Define variables
    $projectPath = "\$solutionType\$solutionName\"
    $localRepository = "https://nexus.kcloudy.com/repository/maven-public/"
    $publicRepository = "https://maven.aliyun.com/repository/central"
    $containerName = $solutionName.ToLower()
    $acrUrl = "registry.cn-zhangjiakou.aliyuncs.com"
    $acrName = "kcloudy-java"  # Your ACR name
    $imageName = "$acrName/$containerName"
    $newVersion = "1.0.0.$versionNum"
    $lastNum = $versionNum - 1
    $lastVersion = "1.0.0.$lastNum"
    $webDir = "D:\Publish\Java\$solutionName\v-$newVersion"
    $oldwebDir = "D:\Publish\Java\$solutionName\v-$lastVersion"
    $archivesDir = "D:\Publish\Java\archives"

    Write-Info "Update project: $solutionName from lastVersion: $lastVersion to newVersion: $newVersion"

    # Create and clean publish directory
    Write-Info "Publishing java project: $solutionName to directory: $webDir"
    if (!(Test-Path -Path $webDir)) {
        New-Item -ItemType Directory -Path $webDir -Force | Out-Null
    } else {
        Remove-Item -Path "$webDir\*" -Recurse -Force
    }

    # judge solutionType is Web or not, if not Web, then use 5.WebApi
    $projectRoot = Split-Path -Path $PSScriptRoot -Parent
    if ($solutionType -ne "Web") {
        $projectPath = "\$solutionType\$solutionName\"
    }

    # Build java project
    cd $projectRoot
    
    # replace version
    #mvn clean versions:set -DnewVersion=$newVersion
    Write-Info "Building project: mvn clean package -DskipTests --projects :$solutionName --also-make --no-transfer-progress --quiet"
    mvn clean package -DskipTests --projects :$solutionName --also-make --no-transfer-progress --quiet

    # copy jar to publish directory
    $targetDir = "$projectRoot\$projectPath\target"
    if (Test-Path $targetDir) {
        Write-Info "Copying jar file: from $targetDir/$solutionName-$newVersion.jar to $webDir"
        Copy-Item -Path "$targetDir/$solutionName-$newVersion.jar" -Destination $webDir -Recurse -Force
    } else {
        Write-Warning "Jar file not found at: $targetDir"
    }

    # Copy font files if they exist
    $fontsSourceDir = "$projectRoot\Fonts"
    $fontsDestDir = "$webDir\Fonts"
    if (Test-Path $fontsSourceDir) {
        Write-Info "Copying font files: from $fontsSourceDir to $fontsDestDir"
        if (-not (Test-Path $fontsDestDir)) {
            New-Item -ItemType Directory -Path $fontsDestDir -Force | Out-Null
        }
        Copy-Item -Path "$fontsSourceDir\*" -Destination $fontsDestDir -Recurse -Force
    } else {
        Write-Warning "Fonts directory not found at: $fontsSourceDir"
    }

    # Copy Dockerfile if it exists
    $dockerfileSource = "$projectRoot\$projectPath\Dockerfile"
    if (Test-Path $dockerfileSource) {
        Write-Info "Copying Dockerfile: from $dockerfileSource to $webDir"
        Copy-Item -Path $dockerfileSource -Destination $webDir -Force
    } else {
        Write-Warning "Dockerfile not found at: $dockerfileSource"
    }

    # Set environment variable
    $env:SPRING_PROFILES_ACTIVE = $env

    # Archive published files
    if (!(Test-Path -Path $archivesDir)) {
        New-Item -ItemType Directory -Path $archivesDir -Force | Out-Null
    }
    Write-Info "Creating archive file..."
    #Compress-Archive -Path "$webDir\*" -DestinationPath "$archivesDir\$solutionName-$newVersion.zip" -Force

    # Check and stop/remove existing container
    $existingContainer = docker ps -aq -f "name=$containerName"
    if ($existingContainer) {
        Write-Warning "Stopping and removing docker container: docker stop $containerName"
        docker stop $containerName | Out-Null
        docker rm -f $containerName | Out-Null
    }

    # Remove old image before Build new image
    $existingImage = docker images -q "$imageName`:$lastVersion"
    if ($existingImage) {
        Write-Warning "Removing image: docker rmi -f $imageName`:$lastVersion"
        docker rmi -f "$imageName`:$lastVersion" | Out-Null
    }
    Write-Info "Building new image: docker build -t $imageName`:$newVersion --build-arg env=$env --build-arg port=$httpPort ."
    Set-Location $webDir
    docker build -t "$imageName`:$newVersion" --build-arg "env=$env" --build-arg "port=$httpPort" .
    #docker images

    # Function to check if port is available
    function Test-PortAvailable {
        param([int]$Port)
        try {
            $listener = [System.Net.Sockets.TcpListener]::new([System.Net.IPAddress]::Any, $Port)
            $listener.Start()
            $listener.Stop()
            return $true
        } catch {
            return $false
        }
    }

    # Check if port is available, if not try next port
    $originalPort = $httpPort
    $maxAttempts = 10
    $attempt = 0
    $portAvailable = $false

    do {
        $attempt++
        if (Test-PortAvailable -Port $httpPort) {
            $portAvailable = $true
            break
        }
        Write-Warning "Port $httpPort is not available, trying next port..."
        $httpPort++
    } while ($attempt -lt $maxAttempts -and -not $portAvailable)

    if (-not $portAvailable) {
        throw "Could not find an available port after $maxAttempts attempts. Last port tried: $httpPort"
    }

    # Notify if port was changed
    if ($httpPort -ne $originalPort) {
        Write-Warning "Using port $httpPort instead of $originalPort"
    }

    # Add HTTPS port mapping if explicitly specified (not default value)
    if ($httpsPort -ge 0) {
        $dockerRunCmd = @("run", "-d", "-p", "${httpPort}:${originalPort}", "-p", "${httpsPort}:${httpsPort}")
    } else {
        $dockerRunCmd = @("run", "-d", "-p", "${httpPort}:${originalPort}")
    }

    # Add container name and image
    $dockerRunCmd += @("--name", $containerName, "$imageName`:$newVersion")

    # Log the command and run it
    $commandString = "docker " + ($dockerRunCmd -join " ")
    # Write-Info "Running container: $commandString"
    # & docker $dockerRunCmd

    # Show container logs
    #docker logs $containerName

    # Tag and push to Alibaba Cloud Container Registry
    $registryImage = "$acrUrl/$acrName/$containerName`:$newVersion"
    Write-Info "Tagging image for ACR: docker tag $imageName`:$newVersion $registryImage"
    docker tag "$imageName`:$newVersion" $registryImage

    # Check if image exists in registry and remove it
    $imageExists = $false
    try {
        # First check if the image exists in the registry
        $manifest = docker manifest inspect $registryImage 2>&1
        if ($LASTEXITCODE -eq 0) {
            $imageExists = $true
            Write-Warning "Image $registryImage already exists in registry. Removing..."

            # Use ACR CLI to delete the image from registry
            $repoName = $containerName
            $imageTag = $newVersion

            # Delete the specific tag from ACR
            az acr repository delete --name $acrName --image "${repoName}:${imageTag}" --yes

            # Also remove from local cache if it exists
            $localImageId = docker images -q $registryImage 2>&1
            if ($localImageId) {
                docker rmi -f $localImageId 2>&1 | Out-Null
            }

            # Wait a moment for the delete to complete
            Start-Sleep -Seconds 1
        }
    } catch {
        # If manifest inspect fails, the image doesn't exist in registry
        $imageExists = $false
        Write-Info "Image $registryImage does not exist in registry or couldn't be checked"
    }

    # Push the new image
    Write-Info "Pushing image to ACR: docker push $registryImage"
    $maxRetries = 3
    $retryCount = 0
    $pushSuccess = $false

    do {
        $retryCount++
        try {
            docker push $registryImage
            if ($LASTEXITCODE -eq 0) {
                $pushSuccess = $true
                break
            }
        } catch {
            Write-Warning "Failed to push image (attempt $retryCount of $maxRetries): $_"
            if ($retryCount -lt $maxRetries) {
                $waitTime = [math]::Pow(2, $retryCount) # Exponential backoff
                Write-Info "Retrying in $waitTime seconds..."
                Start-Sleep -Seconds $waitTime
            }
        }
    } while ($retryCount -lt $maxRetries)

    if (-not $pushSuccess) {
        throw "Failed to push image to ACR after $maxRetries attempts"
    }

    # Clean up old version
    if (Test-Path -Path $oldwebDir) {
        Write-Warning "Cleaning up old version folder: $oldwebDir"
        Remove-Item -Path $oldwebDir -Recurse -Force
    }

    # Remove the new version image
    $existingNewImage = docker images -q "$imageName`:$newVersion"
    if ($existingNewImage) {
        Write-Warning "Removing image: docker rmi -f $imageName`:$newVersion"
        docker rmi -f "$registryImage" | Out-Null
        docker rmi -f "$imageName`:$newVersion" | Out-Null
    }

    Write-Success "Deployment completed successfully!"
}

# Main script
try {
    Build-Java-Web -solutionType $solutionType -solutionName $solutionName -versionNum $versionNum -httpPort $httpPort -httpsPort $httpsPort -env $env
    Set-Location "D:\Project\kcloudy\java\Shell"
    exit 0
} catch {
    Write-Error "Script execution failed: $_"
    Set-Location "D:\Project\kcloudy\java\Shell"
    exit 1
}
