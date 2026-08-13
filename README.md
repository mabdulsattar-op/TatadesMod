Anticheat Test Suite - Fabric client QA mod

This project targets Minecraft 1.21.11 (client-only) and Java 21.

Purpose
-------
This mod is a transparent, intentionally non-obfuscated QA tool to reproduce common anticheat scenarios on private test servers. It provides configurable modules that simulate behaviors for server-side detection testing. It does not include any anti-anticheat evasion features.

Modules & behavior
------------------
- AutoClicker: Simulates configurable click rates (CPS), with randomization and hold-only modes. Use to verify server clickrate/flood detection.
- AutoAim: Simulates aim rotation behaviors, smooth or instant, with range and target filters. Use to test aim assist detection.
- ESP: Renders bounding boxes and labels for entities and items to simulate client-side visibility for debugging.
- Freecam: Allows moving the camera independently of the player to test position/visibility handling.

Quick build (recommended: Codespaces or CI)
-------------------------------------------
1. Preferred: Use GitHub Codespaces or the included GitHub Actions workflow which builds on a hosted runner.
2. Locally: place gradle-8.6-bin.zip in the project root (see PLACE_GRADLE_ZIP_HERE.txt) and run:
   - Windows: gradlew.bat build
   - Linux/macOS: chmod +x ./gradlew && ./gradlew build

Output
------
The built JAR will be at: build/libs/tatadesmod-1.0.0.jar

Usage & safety
--------------
Use this mod only on servers you own or explicitly have permission to test. It is intended for QA and debugging; it should not be used to evade anticheat systems in production servers.
