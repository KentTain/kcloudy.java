# build-all-webs.ps1

# load Build-Java-Web function
. "$PSScriptRoot\build-java-web.ps1"

# define project list (structured way)
$projects = @(
    [PSCustomObject]@{ Name = "kc.web.account";     HttpPort = 2001; HttpsPort = 0; Enabled = $true  },
    [PSCustomObject]@{ Name = "kc.web.app";         HttpPort = 1105; HttpsPort = 0; Enabled = $true },
    [PSCustomObject]@{ Name = "kc.web.codegenerate";HttpPort = 1007; HttpsPort = 0; Enabled = $true },
    [PSCustomObject]@{ Name = "kc.web.config";      HttpPort = 1101; HttpsPort = 0; Enabled = $true },
    [PSCustomObject]@{ Name = "kc.web.dict";        HttpPort = 1103; HttpsPort = 0; Enabled = $true },
    [PSCustomObject]@{ Name = "kc.web.offering";    HttpPort = 3005; HttpsPort = 0; Enabled = $true },
    [PSCustomObject]@{ Name = "kc.web.portal";      HttpPort = 2007; HttpsPort = 0; Enabled = $true },
    [PSCustomObject]@{ Name = "kc.web.training";    HttpPort = 6001; HttpsPort = 0; Enabled = $true }
)

# filter enabled projects
$targetProjects = $projects | Where-Object { $_.Enabled }

# loop build
foreach ($project in $targetProjects) {
    Write-Host "🚀 build: $($project.Name)" -ForegroundColor Cyan
    try {
        Build-Java-Web `
            -solutionType "Web" `
            -solutionName $project.Name `
            -httpPort $project.HttpPort `
            -httpsPort $project.HttpsPort `
            -versionNum 1 `
            -env "prod"

        Write-Host "✅ success: $($project.Name)" -ForegroundColor Green
        Set-Location "D:\Project\kcloudy\java\Shell"
    }
    catch {
        Write-Host "❌ failed: $($project.Name) - error: $_`n" -ForegroundColor Red
        Set-Location "D:\Project\kcloudy\java\Shell"
    }
}
Write-Host "✅ all projects built successfully" -ForegroundColor Green