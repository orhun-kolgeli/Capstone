package com.orhunkolgeli.capstone.interfaces;

import com.orhunkolgeli.capstone.utils.ProjectFilterValues;

public interface ProjectFilterListener  {
    void onActionFilterProjects(ProjectFilterValues projectFilterValues);
    void onPushOpen(String organizationId);
}
