param(
    [string]$OutputDirectory = (Join-Path $env:USERPROFILE '.android\health-metrics-tracker-upload-2026-10-18')
)
$ErrorActionPreference = 'Stop'
if (Test-Path -LiteralPath $OutputDirectory) { throw 'Output directory already exists; existing keys will not be overwritten.' }
if (-not $env:JAVA_HOME) { throw 'Set JAVA_HOME to JDK 17 before running this script.' }
$keytool = Join-Path $env:JAVA_HOME 'bin\keytool.exe'
if (-not (Test-Path -LiteralPath $keytool)) { throw 'keytool is missing from JAVA_HOME.' }
New-Item -ItemType Directory -Path $OutputDirectory | Out-Null
# Restrict private material to the current Windows account and SYSTEM.
$acl = Get-Acl -LiteralPath $OutputDirectory
$acl.SetAccessRuleProtection($true, $false)
$identity = [Security.Principal.WindowsIdentity]::GetCurrent().User
foreach ($sid in @($identity, [Security.Principal.SecurityIdentifier]::new('S-1-5-18'))) {
    $rule = [Security.AccessControl.FileSystemAccessRule]::new($sid, 'FullControl', 'ContainerInherit,ObjectInherit', 'None', 'Allow')
    $acl.AddAccessRule($rule)
}
Set-Acl -LiteralPath $OutputDirectory -AclObject $acl
$password = [Convert]::ToBase64String([Security.Cryptography.RandomNumberGenerator]::GetBytes(32))
$keystore = Join-Path $OutputDirectory 'health-metrics-upload.jks'
$alias = 'health-metrics-upload'
$previousPassword = $env:HMT_NEW_UPLOAD_PASSWORD
try {
    $env:HMT_NEW_UPLOAD_PASSWORD = $password
    & $keytool -genkeypair -keystore $keystore -storetype JKS -keyalg RSA -keysize 3072 -validity 10000 -alias $alias -dname 'CN=Health Metrics Tracker, O=Prem736raj' -storepass:env HMT_NEW_UPLOAD_PASSWORD -keypass:env HMT_NEW_UPLOAD_PASSWORD
    if ($LASTEXITCODE -ne 0) { throw 'Upload key generation failed.' }
    $protected = ConvertFrom-SecureString (ConvertTo-SecureString $password -AsPlainText -Force)
    @{ KeystoreFile = $keystore; KeyAlias = $alias; ProtectedPassword = $protected } |
        ConvertTo-Json | Set-Content -LiteralPath (Join-Path $OutputDirectory 'signing.json') -Encoding utf8
    # Recovery is intentionally outside Git and restricted by the directory ACL.
    @("Keystore: health-metrics-upload.jks", "Alias: $alias", "Store and key password: $password", '',
      'Back up the keystore AND this password to a secure password manager or encrypted offline backup.',
      'signing.json is encrypted for this Windows account; it cannot recover the password on another machine.') |
        Set-Content -LiteralPath (Join-Path $OutputDirectory 'RECOVERY-PRIVATE.txt') -Encoding utf8
    Write-Output "Upload key and private recovery files saved to $OutputDirectory"
} finally {
    $env:HMT_NEW_UPLOAD_PASSWORD = $previousPassword
    $password = $null
}
