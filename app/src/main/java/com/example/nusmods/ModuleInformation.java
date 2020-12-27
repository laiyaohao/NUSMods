package com.example.nusmods;

public class ModuleInformation {
    private String moduleCode;
    private String moduleTitle;
    private int[] moduleSemesters;
    private String moduleDepFacCred;
    private String moduleDescription;

    ModuleInformation (String moduleCode,
                       String moduleTitle,
                       int[] moduleSemesters,
                       String moduleDepFacCred,
                       String moduleDescription){
        this.moduleCode = moduleCode;
        this.moduleTitle = moduleTitle;
        this.moduleSemesters = moduleSemesters;
        this.moduleDepFacCred = moduleDepFacCred;
        this.moduleDescription = moduleDescription;
    }
    public String getModuleCode() {
        return moduleCode;
    }
    public String getModuleTitle() {
        return moduleTitle;
    }
    public String getModuleSemesters() {
        String semesterString = "";
        for (int semester: moduleSemesters) {
            if (semester != 0) {
//                semesterString.append("Semester ").append(Integer.toString(semester)).append(" ");
                semesterString += "Semester " + Integer.toString(semester) + " ";
            }
        }
        return semesterString;
    }
    public String getModuleDepFacCred() {
        return moduleDepFacCred;
    }
    public String getModuleDescription() {
        return moduleDescription;
    }
}
