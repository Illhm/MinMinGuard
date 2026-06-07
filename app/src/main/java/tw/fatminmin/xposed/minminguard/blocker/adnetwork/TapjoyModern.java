package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class TapjoyModern extends Blocker {

    private static final String BANNER_PREFIX = "com.tapjoy";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.tapjoy.TJPlacement", "requestContent", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, "com.tapjoy.TJPlacement", "showContent", lpparam);

        return result;
    }

    @Override
    public String getBannerPrefix() {
        return BANNER_PREFIX;
    }

    @Override
    public String getBanner() {
        return null;
    }
}
