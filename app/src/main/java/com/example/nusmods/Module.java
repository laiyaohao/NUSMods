package com.example.nusmods;

import java.util.List;

public class Module {
    private String moduleCode;
    private String title;
    private int[] semesters;

    Module(String moduleCode,String title,int[] semesters) {
        this.moduleCode = moduleCode;
        this.title = title;
        this.semesters = semesters;
    }

    public String getModuleCode() {
        return moduleCode;
    }
    public String getTitle() {
        return title;
    }
    public int[] getSemesters() {
        return semesters;
    }
}
