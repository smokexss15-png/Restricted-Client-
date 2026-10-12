# Restricted Client — Fabric 1.21.11 starter

A source project for a purple-glass ClickGUI and basic client HUD widgets.

## Current functionality
- Right Shift opens the ClickGUI.
- Purple translucent panel styling and category buttons.
- HUD label, keystroke display, CPS display slot, armor durability display.
- Optional sprint toggle.
- ESP, Storage ESP, and Animations are currently UI placeholders: their toggles do not yet draw world outlines or change item animations.
- CPS is a display counter hook; mouse clicks are not yet wired to it.
- Practice category is a placeholder, not an aim-assist/reach/autoclicker module.

## Build
Requirements: JDK 21, Gradle, and internet access for Gradle to download Minecraft/Fabric dependencies.

1. Extract this ZIP.
2. Open Command Prompt or PowerShell in the `RestrictedClient` folder.
3. Run `gradle build`.
4. The compiled mod should appear in `build/libs/` as `restricted-client-1.0.0.jar` (ignore the `-sources.jar`).
5. Put the JAR in `%AppData%\.minecraft\mods` alongside Fabric API for Minecraft 1.21.11.

This ZIP does not include Gradle Wrapper files. If `gradle` is not recognized, install Gradle first and verify `gradle --version` uses Java 21.

## Important
This source project has not been compiled or tested here, so I cannot claim the JAR is already working. If Gradle reports a missing dependency or API mismatch, copy the complete error output and we can fix it. Test in a separate Minecraft instance first.
