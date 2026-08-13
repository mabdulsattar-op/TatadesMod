$ErrorActionPreference = 'Stop'
$distVersion = 'gradle-8.4.1-bin'
$distUrl = "https://services.gradle.org/distributions/$distVersion.zip"
$root = Split-Path -Parent $MyInvocation.MyCommand.Path
$zip = Join-Path $root "$distVersion.zip"
$dest = Join-Path $root 'gradle'

if (-Not (Test-Path $dest)) {
    Write-Host "Downloading Gradle distribution $distUrl ..."
    try {
        Invoke-WebRequest -Uri $distUrl -OutFile $zip -UseBasicParsing -ErrorAction Stop
    } catch {
        Write-Error "Download failed: $_"
        exit 1
    }
    Write-Host "Extracting to $dest ..."
    if (Test-Path $dest) { Remove-Item -Recurse -Force $dest }
    New-Item -ItemType Directory -Path $dest | Out-Null
    Expand-Archive -Path $zip -DestinationPath $dest -Force
    Remove-Item $zip -Force
}

# find inner gradle folder inside the dest directory
$inner = Get-ChildItem -Path $dest -Directory | Where-Object { $_.Name -like 'gradle-*' } | Select-Object -First 1
if (-not $inner) { throw "Gradle distribution not found inside $dest after extraction" }
$bin = Join-Path $inner.FullName 'bin\gradle.bat'
if (-not (Test-Path $bin)) { throw "gradle.bat not found at $bin" }

# pass through all args
$argList = $args -join ' '
& "$bin" $argList
exit $LASTEXITCODE
