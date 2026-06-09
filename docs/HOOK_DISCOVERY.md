# Hook Discovery Guide

## Enabling Discovery Mode

To enable Hook Discovery mode, set the preference `packageName + "_discovery"` to `true`. This enables logging for suspicious view containers added within the app.

## Reading Logcat

You can read the logged items using `logcat`. The output will look something like this:
`[tw.fatminmin.xposed.minminguard.HookDiscovery] [com.example.app] Suspicious child view added: com.example.ads.BannerAdView`

## Creating a new Blocker

Once you've identified the suspicious ad classes from the discovery mode output, you can turn these findings into a new `Blocker` subclass and register it in `Main.blockers`.

## Reward Ad Policy

Our reward ad policy aims to protect rewarded ads. If a class, method, or URL appears to be associated with rewarded ads, our heuristic (`AdHeuristic.isRewarded`) will flag it and it will not be blocked by default. MinMinGuard's auto-blocking respects rewarded ads to prevent breaking app functionality while maintaining standard ad-blocking behavior.
