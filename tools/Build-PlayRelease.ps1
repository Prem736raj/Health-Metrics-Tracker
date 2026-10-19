param(
    [string]$SigningConfig = (Join-Path $env:USERPROFILE '.android\health-metrics-tracker-upload-2026-10-18\signing.json'),
    [switch]$VerifyOnly
)
$ErrorActionPreference = 'Stop'
$root = Split-Path $PSScriptRoot -Parent
if (-not $env:JAVA_HOME) { throw 'Set JAVA_HOME to JDK 17 before running this script.' }
$config = Get-Content -LiteralPath $SigningConfig -Raw | ConvertFrom-Json
if (-not (Test-Path -LiteralPath $config.KeystoreFile)) { throw 'The configured upload keystore is missing.' }
$secure = ConvertTo-SecureString $config.ProtectedPassword
$credential = [PSCredential]::new('upload', $secure)
$names = @('RELEASE_STORE_FILE', 'RELEASE_STORE_PASSWORD', 'RELEASE_KEY_ALIAS', 'RELEASE_KEY_PASSWORD')
$previous = @{}
foreach ($name in $names) { $previous[$name] = [Environment]::GetEnvironmentVariable($name, 'Process') }
Push-Location $root
try {
    $env:RELEASE_STORE_FILE = $config.KeystoreFile
    $env:RELEASE_STORE_PASSWORD = $credential.GetNetworkCredential().Password
    $env:RELEASE_KEY_ALIAS = $config.KeyAlias
    $env:RELEASE_KEY_PASSWORD = $env:RELEASE_STORE_PASSWORD
    & (Join-Path $env:JAVA_HOME 'bin\keytool.exe') -list -keystore $config.KeystoreFile -alias $config.KeyAlias -storepass:env RELEASE_STORE_PASSWORD
    if ($LASTEXITCODE -ne 0) { throw 'Cannot unlock the configured upload key.' }
    $tasks = @('verifyPlayReleaseSigning')
    if (-not $VerifyOnly) { $tasks = @('bundlePlayRelease', 'assembleRelease') }
    & .\gradlew.bat @tasks --max-workers=1 --no-daemon --console=plain '-Dorg.gradle.jvmargs=-Xmx3072m -XX:MaxMetaspaceSize=1024m -XX:+UseG1GC -Dfile.encoding=UTF-8'
    if ($LASTEXITCODE -ne 0) { throw 'Play release Gradle gate failed.' }
    if (-not $VerifyOnly) {
        $bundle = Join-Path $root 'app\build\outputs\bundle\release\app-release.aab'
        $signature = & (Join-Path $env:JAVA_HOME 'bin\jarsigner.exe') -verify $bundle 2>&1
        $signature | Write-Output
        if ($LASTEXITCODE -ne 0 -or ($signature -join "`n") -notmatch 'jar verified\.') { throw 'The bundle signature could not be verified.' }
        Get-FileHash -LiteralPath $bundle -Algorithm SHA256
    }
} finally {
    foreach ($name in $names) { [Environment]::SetEnvironmentVariable($name, $previous[$name], 'Process') }
    Pop-Location
}
