package pkg;
public class Test {
@SuppressWarnings("unchecked")
@Messages({
"VAL_projectLocationTextField=Project Location",
"VAL_projectNameTextField=Project Name",
"VAL_ArtifactId=ArtifactId",
"VAL_Version=Version",
"VAL_GroupId=GroupId",
"VAL_Package=Package",
"ERR_Project_Folder_cannot_be_created=Project Folder cannot be created.",
"ERR_Project_Folder_is_not_valid_path=Project Folder is not a valid path.",
"ERR_Project_Folder_is_UNC=Project Folder cannot be located on UNC path.",
"ERR_Package_ends_in_dot=Package name can not end in '.'.",
"# {0} - version", "ERR_old_maven=Maven {0} is too old, version 2.0.7 or newer is needed.",
"ERR_Project_Folder_exists=Project Folder already exists and is not empty.",
"ERR_Project_Folder_not_directory=Project Folder is not a directory."
})
BasicPanelVisual(BasicWizardPanel panel, Archetype arch) {
this.panel = panel;
this.arch = arch;
initComponents();
SwingValidationGroup.setComponentName(projectLocationTextField, VAL_projectLocationTextField());
SwingValidationGroup.setComponentName(projectNameTextField, VAL_projectNameTextField());
SwingValidationGroup.setComponentName(txtArtifactId, VAL_ArtifactId());
SwingValidationGroup.setComponentName(txtVersion, VAL_Version());
SwingValidationGroup.setComponentName(txtGroupId, VAL_GroupId());
SwingValidationGroup.setComponentName(txtPackage, VAL_Package());
projectNameTextField.getDocument().addDocumentListener(this);
projectLocationTextField.getDocument().addDocumentListener(this);
txtArtifactId.getDocument().addDocumentListener(this);
txtGroupId.getDocument().addDocumentListener(this);
txtVersion.getDocument().addDocumentListener(this);
txtPackage.getDocument().addDocumentListener(this);
tblAdditionalProps.setVisible(false);
lblAdditionalProps.setVisible(false);
jScrollPane1.setVisible(false);
btnSetupNewer.setVisible(false);
getAccessibleContext().setAccessibleDescription(LBL_CreateProjectStep2());
txtGroupId.setText(MavenSettings.getDefault().getLastArchetypeGroupId());
txtVersion.setText(MavenSettings.getDefault().getLastArchetypeVersion());
vg = ValidationGroup.create();
runInAWT(new Runnable() {
@Override
public void run() {
vg.add(txtGroupId, MavenValidators.createGroupIdValidators());
vg.add(txtArtifactId, MavenValidators.createArtifactIdValidators());
vg.add(txtVersion, MavenValidators.createVersionValidators());
vg.add(txtPackage, ValidatorUtils.merge(
StringValidators.JAVA_PACKAGE_NAME,
new AbstractValidator<String>(String.class) {
@Override
public void validate(Problems problems, String compName, String model)
{
if(model != null && !model.isEmpty() && model.charAt(model.length() - 1) == '.')
problems.add(ERR_Package_ends_in_dot());
}}));
vg.add(projectNameTextField, ValidatorUtils.merge(
MavenValidators.createArtifactIdValidators(),
StringValidators.REQUIRE_VALID_FILENAME
));
vg.add(projectLocationTextField,
new AbstractValidator<String>(String.class) {
@Override public void validate(Problems problems, String compName, String model) {
File fil = FileUtil.normalizeFile(new File(model));
File projLoc = fil;
while (projLoc != null && !projLoc.exists()) {
projLoc = projLoc.getParentFile();
}
if (projLoc == null || !projLoc.canWrite()) {
problems.add(ERR_Project_Folder_cannot_be_created());
return;
}
if (FileUtil.toFileObject(projLoc) == null) {
problems.add(ERR_Project_Folder_is_not_valid_path());
return;
}
if (Utilities.isWindows() && fil.getAbsolutePath().startsWith("\\\\")) {
problems.add(ERR_Project_Folder_is_UNC());
}
}
});
vg.addItem(new ValidationListener<Void>(Void.class, ValidationUI.NO_OP, null) {
@Override
protected void performValidation(Problems problems) {
boolean tooOld = isMavenTooOld();
btnSetupNewer.setVisible(tooOld);
if (tooOld) {
problems.add(ERR_old_maven(getCommandLineMavenVersion()));
return;
}
File destFolder = FileUtil.normalizeFile(new File(new File(projectLocationTextField.getText().trim()), projectNameTextField.getText().trim()).getAbsoluteFile());
if(destFolder.exists() && !destFolder.isDirectory()) {
problems.add(ERR_Project_Folder_not_directory());
return;
}
File[] kids = destFolder.listFiles();
if (destFolder.exists() && kids != null && kids.length > 0) {
problems.add(ERR_Project_Folder_exists());
}
}
}, true);
}
});
}
}