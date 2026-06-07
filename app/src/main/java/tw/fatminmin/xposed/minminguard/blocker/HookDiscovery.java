package tw.fatminmin.xposed.minminguard.blocker;

import android.view.View;
import android.view.ViewGroup;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class HookDiscovery {

    private static final Set<String> loggedClasses = Collections.synchronizedSet(new HashSet<String>());

    public static void enableDiscovery(final String packageName, final LoadPackageParam lpparam) {
        if (lpparam == null || lpparam.classLoader == null || packageName == null) return;

        try {
            Util.hookAllMethods("android.view.ViewGroup", lpparam.classLoader, "addView", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) {
                    if (param.args == null || param.args.length == 0 || param.args[0] == null) return;

                    try {
                        View child = (View) param.args[0];
                        ViewGroup parent = (ViewGroup) param.thisObject;

                        if (child == null || parent == null) return;

                        String childClass = child.getClass().getName();
                        String parentClass = parent.getClass().getName();

                        if (isSuspicious(childClass)) {
                            logDiscovery(packageName, "Suspicious child view added", childClass);
                        }
                        if (isSuspicious(parentClass)) {
                            logDiscovery(packageName, "Suspicious parent view container", parentClass);
                        }

                    } catch (Throwable t) {
                        // ignore errors during discovery to prevent crashing the target app
                    }
                }
            });
            Util.log("HookDiscovery", "Enabled discovery mode for " + packageName);
        } catch (Throwable t) {
            Util.log("HookDiscovery", "Failed to hook addView for discovery in " + packageName + ": " + t.getMessage());
        }
    }

    private static boolean isSuspicious(String className) {
        if (className == null) return false;

        // Filter out safe core android namespaces
        if (className.startsWith("android.") ||
            className.startsWith("androidx.") ||
            className.startsWith("com.google.android.material.")) {
            return false;
        }

        String lowerClass = className.toLowerCase();
        return lowerClass.contains("ad") ||
               lowerClass.contains("ads") ||
               lowerClass.contains("banner") ||
               lowerClass.contains("native") ||
               lowerClass.contains("interstitial") ||
               lowerClass.contains("reward") ||
               lowerClass.contains("splash");
    }

    private static void logDiscovery(String packageName, String message, String className) {
        String key = packageName + ":" + className;
        if (!loggedClasses.contains(key)) {
            loggedClasses.add(key);
            Util.log("HookDiscovery", "[" + packageName + "] " + message + ": " + className);
        }
    }
}
