package com.zwh.plugin

import org.gradle.api.Plugin
import org.gradle.api.Project

class SaveStatePlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {

        if (project.plugins.hasPlugin("com.android.application")
                || project.plugins.hasPlugin("com.android.library")) {
            project.extensions.create('saveState', SaveState)
            project.android.registerTransform(new SaveStateTransform(project))
        }
    }
}
