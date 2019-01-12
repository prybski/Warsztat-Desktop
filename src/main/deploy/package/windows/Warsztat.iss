;This file will be executed next to the application bundle image
;I.e. current directory will contain folder Warsztat with application files
[Setup]
AppId={{pl.edu.pwsztar}}
AppName=Warsztat
AppVersion=1.0
AppVerName=Warsztat
AppPublisher=Piotr Rybski
AppComments=Aplikacja warsztatowa stworzona na potrzeby pracy dyplomowej
AppCopyright=Copyright (C) 2019
;AppPublisherURL=http://java.com/
;AppSupportURL=http://java.com/
;AppUpdatesURL=http://java.com/
DefaultDirName={localappdata}\Warsztat
DisableStartupPrompt=Yes
DisableDirPage=No
DisableProgramGroupPage=Yes
DisableReadyPage=No
DisableFinishedPage=Yes
DisableWelcomePage=Yes
DefaultGroupName=Piotr Rybski
;Optional License
LicenseFile=
;WinXP or above
MinVersion=0,5.1 
OutputBaseFilename=Warsztat-1.0
Compression=lzma
SolidCompression=yes
PrivilegesRequired=lowest
SetupIconFile=Warsztat\Warsztat.ico
UninstallDisplayIcon={app}\Warsztat.ico
UninstallDisplayName=Warsztat
WizardImageStretch=No
WizardSmallImageFile=Warsztat-setup-icon.bmp   
ArchitecturesInstallIn64BitMode=x64
VersionInfoVersion=1.0.0.0
VersionInfoDescription=Aplikacja warsztatowa stworzona na potrzeby pracy dyplomowej

[Languages]
Name: "polish"; MessagesFile: "compiler:Languages\Polish.isl"
Name: "english"; MessagesFile: "compiler:Default.isl"

[Files]
Source: "Warsztat\Warsztat.exe"; DestDir: "{app}"; Flags: ignoreversion
Source: "Warsztat\*"; DestDir: "{app}"; Flags: ignoreversion recursesubdirs createallsubdirs

[Icons]
Name: "{group}\Warsztat"; Filename: "{app}\Warsztat.exe"; IconFilename: "{app}\Warsztat.ico"; Check: returnTrue()
Name: "{commondesktop}\Warsztat"; Filename: "{app}\Warsztat.exe";  IconFilename: "{app}\Warsztat.ico"; Check: returnFalse()

[Run]
Filename: "{app}\Warsztat.exe"; Parameters: "-Xappcds:generatecache"; Check: returnFalse()
Filename: "{app}\Warsztat.exe"; Description: "{cm:LaunchProgram,Warsztat}"; Flags: nowait postinstall skipifsilent; Check: returnTrue()
Filename: "{app}\Warsztat.exe"; Parameters: "-install -svcName ""Warsztat"" -svcDesc ""Warsztat"" -mainExe ""Warsztat.exe""  "; Check: returnFalse()

[UninstallRun]
Filename: "{app}\Warsztat.exe "; Parameters: "-uninstall -svcName Warsztat -stopOnUninstall"; Check: returnFalse()

[Code]
function returnTrue(): Boolean;
begin
  Result := True;
end;

function returnFalse(): Boolean;
begin
  Result := False;
end;

function InitializeSetup(): Boolean;
begin
// Possible future improvements:
//   if version less or same => just launch app
//   if upgrade => check if same app is running and wait for it to exit
//   Add pack200/unpack200 support? 
  Result := True;
end;  
