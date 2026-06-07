package tw.fatminmin.xposed.minminguard.blocker;

public class AdHeuristic {

    public static boolean isAdLike(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        String lowerName = name.toLowerCase();

        // Exclude false positives
        if (lowerName.contains("adapter") ||
            lowerName.contains("header") ||
            lowerName.contains("shadow") ||
            lowerName.contains("badge")) {
            return false;
        }

        return lowerName.contains("adview") ||
               lowerName.contains(".ads.") ||
               lowerName.contains(".ad.") ||
               lowerName.contains("banner") ||
               lowerName.contains("nativead") ||
               lowerName.contains("interstitial") ||
               lowerName.contains("rewarded") ||
               lowerName.contains("sponsored") ||
               lowerName.contains("applovin") ||
               lowerName.contains("vungle") ||
               lowerName.contains("unityads") ||
               lowerName.contains("ironsource") ||
               lowerName.contains("pangle") ||
               lowerName.contains("chartboost");
    }

    public static boolean isRewarded(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        String lowerName = name.toLowerCase();
        return lowerName.contains("reward") ||
               lowerName.contains("serversideverification");
    }

    public static boolean shouldAutoBlock(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }
        // Default: rewarded ads are detected but not blocked automatically
        if (isRewarded(name)) {
            return false;
        }

        return isAdLike(name);
    }
}
