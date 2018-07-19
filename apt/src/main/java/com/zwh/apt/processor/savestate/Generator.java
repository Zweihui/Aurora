package com.zwh.apt.processor.savestate;

import com.squareup.javapoet.JavaFile;

public interface Generator {
    JavaFile createSaveStateSourceFile();
}
