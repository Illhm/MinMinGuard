package tw.fatminmin.xposed.minminguard.blocker;

import android.net.Uri;
import android.os.Build;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;

import java.io.ByteArrayInputStream;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;
import tw.fatminmin.xposed.minminguard.Main;

public class WebViewRequestBlocking {

    public static void handle(final String packageName, final LoadPackageParam lpparam) {
        if (lpparam == null || lpparam.classLoader == null || packageName == null) return;

        try {
            Class<?> webViewClientClass = XposedHelpers.findClassIfExists("android.webkit.WebViewClient", lpparam.classLoader);
            if (webViewClientClass == null) return;

            // Hook for older APIs (API < 21)
            try {
                XposedHelpers.findAndHookMethod(webViewClientClass, "shouldInterceptRequest", "android.webkit.WebView", String.class, new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        try {
                            if (param.args == null || param.args.length < 2) return;

                            // the signature is shouldInterceptRequest(WebView view, String url)
                            if (param.args[1] instanceof String) {
                                String url = (String) param.args[1];
                                if (shouldBlock(url)) {
                                    param.setResult(createEmptyResponse());
                                }
                            }
                        } catch (Throwable t) {
                            // ignore
                        }
                    }
                });
            } catch (Throwable t) {
                Util.log("WebViewRequestBlocking", "Failed to hook shouldInterceptRequest(WebView, String) in " + packageName);
            }

            // Hook for newer APIs (API >= 21)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                try {
                    XposedHelpers.findAndHookMethod(webViewClientClass, "shouldInterceptRequest", "android.webkit.WebView", WebResourceRequest.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            try {
                                if (param.args == null || param.args.length < 2) return;

                                // the signature is shouldInterceptRequest(WebView view, WebResourceRequest request)
                                if (param.args[1] instanceof WebResourceRequest) {
                                    WebResourceRequest request = (WebResourceRequest) param.args[1];
                                    Uri uri = request.getUrl();
                                    if (uri != null) {
                                        String url = uri.toString();
                                        if (shouldBlock(url)) {
                                            param.setResult(createEmptyResponse());
                                        }
                                    }
                                }
                            } catch (Throwable t) {
                                // ignore
                            }
                        }
                    });
                } catch (Throwable t) {
                    Util.log("WebViewRequestBlocking", "Failed to hook shouldInterceptRequest(WebView, WebResourceRequest) in " + packageName);
                }
            }

            Util.log("WebViewRequestBlocking", "Enabled WebView request interception for " + packageName);

        } catch (Throwable t) {
            Util.log("WebViewRequestBlocking", "Failed to hook WebViewClient in " + packageName + ": " + t.getMessage());
        }
    }

    private static boolean shouldBlock(String url) {
        if (url == null || url.trim().isEmpty()) return false;

        if (AdHeuristic.isRewarded(url)) {
            Util.log("WebViewRequestBlocking", "Allowed rewarded ad: " + url);
            return false;
        }

        try {
            Uri uri = Uri.parse(url);
            String host = uri.getHost();

            // Strip query string for logging and simpler matching
            String cleanUrl = url;
            int queryIndex = url.indexOf('?');
            if (queryIndex > 0) {
                cleanUrl = url.substring(0, queryIndex);
            }
            cleanUrl = cleanUrl.toLowerCase().trim();

            if (host != null) {
                host = host.toLowerCase().trim();
                for (String adUrl : Main.patterns) {
                    if (adUrl == null || adUrl.trim().isEmpty()) continue;
                    String pattern = adUrl.toLowerCase().trim();
                    if (host.contains(pattern)) {
                        return true;
                    }
                }
            }

            for (String adUrl : Main.patterns) {
                if (adUrl == null || adUrl.trim().isEmpty()) continue;
                String pattern = adUrl.toLowerCase().trim();
                if (cleanUrl.contains(pattern)) {
                    return true;
                }
            }
        } catch (Throwable t) {
            // fallback if URI parse fails
            String cleanUrl = url.toLowerCase().trim();
            for (String adUrl : Main.patterns) {
                if (adUrl == null || adUrl.trim().isEmpty()) continue;
                String pattern = adUrl.toLowerCase().trim();
                if (cleanUrl.contains(pattern)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static WebResourceResponse createEmptyResponse() {
        return new WebResourceResponse("text/plain", "UTF-8", new ByteArrayInputStream("".getBytes()));
    }
}
