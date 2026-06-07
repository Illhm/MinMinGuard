# Hook Discovery Guide

This document explains how to use MinMinGuard's Hook Discovery feature to find and block unsupported ad networks.

## 1. Enabling Discovery Mode

To enable discovery mode for a specific app:
1. Ensure the app is checked in the MinMinGuard UI.
2. Manually add a boolean flag `[package_name]_discovery` set to `true` in MinMinGuard's `shared_prefs` XML file.
3. Restart the target app.

## 2. Reading Logcat

Once enabled, MinMinGuard will log suspicious class names when they are added to the view hierarchy (`ViewGroup.addView`).

Run the following command via ADB to see the discovery logs:
```bash
adb logcat | grep HookDiscovery
```

You should see output similar to:
```
[HookDiscovery] [com.example.app] Suspicious child view added: com.newadnetwork.ads.BannerView
```

## 3. Creating a New Blocker

Once you have identified the package or class prefix of the new ad network, create a new `Blocker` subclass in `app/src/main/java/tw/fatminmin/xposed/minminguard/blocker/adnetwork/`.

Example `NewAdNetwork.java`:
```java
package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class NewAdNetwork extends Blocker {
    @Override
    public String getBanner() {
        return null; // Exact class name
    }

    @Override
    public String getBannerPrefix() {
        return "com.newadnetwork.ads."; // Prefix to block
    }
}
```

## 4. Registering the Blocker

Open `app/src/main/java/tw/fatminmin/xposed/minminguard/Main.java`.
Import your new class and add it to the `blockers` array:

```java
import tw.fatminmin.xposed.minminguard.blocker.adnetwork.NewAdNetwork;

public static Blocker[] blockers = {
    // ... existing blockers ...
    new NewAdNetwork(),
};
```

## 5. Testing with Debug APK

1. Build a debug APK with `./gradlew clean :app:assembleDebug`.
2. Install the new APK on your device.
3. Verify that the new ad network views are now blocked successfully and spaces are removed.
4. Disable discovery mode in preferences when done testing.
