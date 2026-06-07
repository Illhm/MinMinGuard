package tw.fatminmin.xposed.minminguard.blocker.adnetwork;

import de.robv.android.xposed.callbacks.XC_LoadPackage;
import tw.fatminmin.xposed.minminguard.blocker.ApiBlocking;
import tw.fatminmin.xposed.minminguard.blocker.Blocker;

public class Adcolony extends Blocker
{
    private static final String Adcolony_Storage = "com.adcolony.sdk.ab";

    private static final String INTER_ADS = "com.adcolony.sdk.AdColonyInterstitial";

    @Override
    public boolean handleLoadPackage(String packageName, XC_LoadPackage.LoadPackageParam lpparam)
    {
        boolean result = false;

        // Refactored to use standard safe default mechanism
        result = ApiBlocking.blockAdFunctionWithSafeDefault(packageName, INTER_ADS, "isExpired", lpparam);
        result |= ApiBlocking.blockAdFunctionWithSafeDefault(packageName, INTER_ADS, "show", lpparam);

        return result;
    }

    @Override
    public String getBanner()
    {
        return null;
    }

    @Override
    public String getBannerPrefix()
    {
        return null;
    }
}
