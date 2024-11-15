package net.staticstudios.testplugin;

import net.staticstudios.menus.StaticMenus;

import java.io.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class FileUtils {
    /**
     * Gets the file paths of files that are contained in the jar file under the provided directory path.
     *
     * @param path the path to look up files for.
     * @return a list of file paths that are stored in the directory.
     */
    public static List<String> getResourceDirectory(String path) {
        path = path.endsWith("/") ? path.substring(0, path.length() - 1) : path;
        List<String> children = new ArrayList<>();
        final File jarFile = new File(StaticMenus.getPlugin().getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
        try (final JarFile jar = new JarFile(jarFile)) {
            final Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                String entryName = entries.nextElement().getName();
                if (entryName.startsWith(path + '/') && !entryName.equalsIgnoreCase(path + '/')) {
                    children.add(entryName);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return children;
    }

    /**
     * Gets the contents of a resource as a reader.
     *
     * @param path the path to the resource.
     * @return a reader for the resource.
     */
    public static Reader getResourceContents(String path) {
        InputStream stream = StaticMenus.getPlugin().getResource(path);

        if (stream == null) {
            throw new IllegalArgumentException("Resource not found: " + path);
        }

        return new InputStreamReader(stream);
    }
}
