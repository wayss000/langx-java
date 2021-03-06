package com.jn.langx.util;

import com.jn.langx.util.reflect.Reflects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import static com.jn.langx.util.SystemPropertys.getJavaIOTmpDir;

public class Platform {
    private static final Logger logger = LoggerFactory.getLogger(Platform.class);
    public static boolean isWindows = isWindows0();
    public static int JAVA_VERSION_INT = javaVersion();
    public static boolean isAndroid = isAndroid0();
    private static final boolean IS_IVKVM_DOT_NET = isIkvmDotNet0();
    public static boolean isGroovyAvailable = isGroovyAvailable0();
    public static String processId = getProcessId0();

    private static boolean isWindows0() {
        return System.getProperty("os.name", "").toLowerCase(Locale.US).contains("win");
    }

    private static boolean isIkvmDotNet0() {
        String vmName = System.getProperty("java.vm.name", "").toUpperCase(Locale.US);
        return "IKVM.NET".equals(vmName);
    }

    private static boolean isAndroid0() {
        // Idea: Sometimes java binaries include Android classes on the classpath, even if it isn't actually Android.
        // Rather than check if certain classes are present, just check the VM, which is tied to the JDK.

        // Optional improvement: check if `android.os.Build.VERSION` is >= 24. On later versions of Android, the
        // OpenJDK is used, which means `Unsafe` will actually work as expected.

        // Android sets this property to Dalvik, regardless of whether it actually is.
        String vmName = System.getProperty("java.vm.name");
        boolean isAndroid = "Dalvik".equals(vmName);
        if (isAndroid) {
            logger.debug("Platform: Android");
        }
        return isAndroid;
    }

    private static int javaVersion() {
        final int majorVersion;

        if (isAndroid0()) {
            majorVersion = 6;
        } else {
            majorVersion = majorVersionFromJavaSpecificationVersion();
        }

        logger.debug("Java version: {}", majorVersion);

        return majorVersion;
    }

    // Package-private for testing only
    private static int majorVersionFromJavaSpecificationVersion() {
        return majorVersion(System.getProperty("java.specification.version", "1.6"));
    }

    // Package-private for testing only
    private static int majorVersion(final String javaSpecVersion) {
        final String[] components = javaSpecVersion.split("\\.");
        final int[] version = new int[components.length];
        for (int i = 0; i < components.length; i++) {
            version[i] = Integer.parseInt(components[i]);
        }

        if (version[0] == 1) {
            assert version[1] >= 6;
            return version[1];
        } else {
            return version[0];
        }
    }

    private static boolean isGroovyAvailable0() {
        ClassLoader loader = Platform.class.getClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        try {
            Class bindingClass = loader.loadClass("groovy.lang.Binding");
            return bindingClass != null;
        } catch (ClassNotFoundException ex) {
            return false;
        }
    }

    private static String getProcessId0() {
        try {
            if (isAndroid) {
                Object runtimeMXBean = Reflects.getDeclaredMethod(Class.forName("java.lang.management.ManagementFactory"), "getRuntimeMXBean").invoke(null);
                return Reflects.getDeclaredMethod(Class.forName("java.lang.management.RuntimeMXBean"), "getName").invoke(runtimeMXBean).toString().split("@")[0];
            } else {
                java.lang.management.RuntimeMXBean runtimeMXBean = java.lang.management.ManagementFactory.getRuntimeMXBean();
                return runtimeMXBean.getName().split("@")[0];
            }
        } catch (Throwable ex) {
            if (isAndroid) {
                try {
                    return new File("/proc/self").getCanonicalFile().getName();
                } catch (IOException e) {
                    return null;
                }
            }
            return null;
        }
    }

    public static boolean equals(byte[] bytes1, int startPos1, byte[] bytes2, int startPos2, int length) {
        final int end = startPos1 + length;
        for (; startPos1 < end; ++startPos1, ++startPos2) {
            if (bytes1[startPos1] != bytes2[startPos2]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns the path to the system temporary directory.
     *
     * @return the path to the system temporary directory.
     */
    public static String getTempDirectoryPath() {
        return getJavaIOTmpDir();
    }

    /**
     * Returns a {@link File} representing the system temporary directory.
     *
     * @return the system temporary directory.
     */
    public static File getTempDirectory() {
        return new File(getTempDirectoryPath());
    }

    /**
     * Returns the path to the user's home directory.
     *
     * @return the path to the user's home directory.
     */
    public static String getUserHomeDirectoryPath() {
        return SystemPropertys.getUserHome();
    }

    /**
     * Returns a {@link File} representing the user's home directory.
     *
     * @return the user's home directory.
     */
    public static File getUserHomeDirectory() {
        return new File(getUserHomeDirectoryPath());
    }
}
