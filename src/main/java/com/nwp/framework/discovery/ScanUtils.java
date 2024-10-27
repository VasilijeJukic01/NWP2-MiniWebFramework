package com.nwp.framework.discovery;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ScanUtils {

    public static List<Class<?>> findAllClasses(String packageName) {
        String path = packageName.replace(".", "/");
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(path);
            return Collections.list(resources).stream()
                    .map(URL::getFile)
                    .map(File::new)
                    .flatMap(file -> findClasses(file, packageName).stream())
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return Collections.emptyList();
        }
    }

    public static List<Class<?>> findClasses(File directory, String packageName) {
        if (!directory.exists()) return Collections.emptyList();

        return Arrays.stream(directory.listFiles())
                .flatMap(file -> {
                    if (file.isDirectory()) {
                        return findClasses(file, packageName + "." + file.getName()).stream();
                    }
                    else if (file.getName().endsWith(".class")) {
                        String className = packageName + "." + file.getName().substring(0, file.getName().length() - 6);
                        try {
                            return Stream.of(Class.forName(className));
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                            return Stream.empty();
                        }
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toList());
    }

}
