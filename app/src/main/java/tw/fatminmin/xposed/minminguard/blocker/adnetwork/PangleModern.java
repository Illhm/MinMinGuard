package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class PangleModern extends Blocker {

    private static final String BANNER_PREFIX = "com.bytedance.sdk.openadsdk";

    public boolean handleLoadPackage(final String packageName, LoadPackageParam lpparam) {
        boolean result = false;

        String[] inters = {
            "com.bytedance.sdk.openadsdk.TTAdNative",
            "com.bytedance.sdk.openadsdk.TTFullScreenVideoAd",
            "com.bytedance.sdk.openadsdk.TTNativeExpressAd"
        };
        for (String i : inters) {
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, i, "loadBannerAd", lpparam);
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, i, "loadNativeAd", lpparam);
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, i, "loadNativeExpressAd", lpparam);
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, i, "loadFullScreenVideoAd", lpparam);
            result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, i, "showFullScreenVideoAd", lpparam);
        }

        result |= ApiBlocking.removeBanner(packageName, "com.bytedance.sdk.openadsdk.core.nativeexpress.NativeExpressView", "render", lpparam);

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
