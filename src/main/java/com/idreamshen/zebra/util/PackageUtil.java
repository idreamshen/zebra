package com.idreamshen.zebra.util;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.UnknownFormatFlagsException;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class PackageUtil {

    private PackageUtil() {
    }

    public static Collection<Class> scanClasses(URL url) {
        if (url == null) throw new IllegalArgumentException("url 不能为空");

        Set<Class> classes = new HashSet<>();

        String protocol = url.getProtocol();

        if ("file".equals(protocol)) {
            File file = new File(url.getPath());
            loadClassesFromFile(file, classes);
        } else if ("jar".equals(protocol)) {
            try {
                JarFile jarFile = ((JarURLConnection) url.openConnection()).getJarFile();
                loadClassesFromJar(jarFile, classes);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            throw new UnknownFormatFlagsException(String.format("未知的 protocol=%s", protocol));
        }

        return classes;

    }

    private static void loadClassesFromFile(File file, Collection<Class> classes) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                loadClassesFromFile(f, classes);
            }
        } else {
            if (file.getName().endsWith(".class")) {
                String filePath = file.getAbsolutePath();
                String tmp = filePath.split("classes")[1].substring(1);
                tmp = tmp.substring(0, tmp.length() - 6);
                String className = tmp.replaceAll("/", ".");
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private static void loadClassesFromJar(JarFile jarFile, Collection<Class> classes) {
        Enumeration<JarEntry> jarEntries = jarFile.entries();

        while (jarEntries.hasMoreElements()) {
            JarEntry jarEntry = jarEntries.nextElement();

            if (jarEntry.getName().endsWith(".class")) {
                String tmp = jarEntry.getName().substring(0, jarEntry.getName().length() - 6);
                String className = tmp.replaceAll("/", ".");
                try {
                    classes.add(Class.forName(className));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
