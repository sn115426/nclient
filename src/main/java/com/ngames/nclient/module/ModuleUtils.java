package com.ngames.nclient.module;

import com.ngames.nclient.NClient;

public class ModuleUtils {

    public static String getName(Module module) {
        return module.getClass().getAnnotation(ModuleType.class).name();
    }

    public static String getDescrption(Module module) {
        return module.getClass().getAnnotation(ModuleType.class).description();
    }

    public static Category getCategory(Module module) {
        return module.getClass().getAnnotation(ModuleType.class).category();
    }

    public static String getWords(Module module) {
        return module.getClass().getAnnotation(ModuleType.class).words();
    }

    public static int getDefaultKeyBind(Module module) {
        return module.getClass().getAnnotation(ModuleType.class).keyBind();
    }

    public static Module getModule(String name) {
        return NClient.getModule(name);
    }

    public static boolean doesModuleExist(String name) {
        return NClient.getModule(name) != null;
    }
}
