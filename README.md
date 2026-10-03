# PhoneDeck

A starter phone-as-Stream-Deck controller for Mac.

## Architecture

- `android/` — Android controller app (Kotlin + Jetpack Compose starter)
- `mac-receiver/` — Mac receiver (Node.js + TypeScript)
- Phone and Mac communicate over WebSocket.
- The Mac receiver maps commands to keyboard shortcuts/macros.

## MVP

1. Run the Mac receiver.
2. Put your Mac and Android phone on the same Wi-Fi.
3. Enter the Mac's local IP in the Android app.
4. Tap a button to send a command.
5. Add your own shortcut mappings in `mac-receiver/src/mappings.ts`.

## Quick start

### Mac receiver

```bash
cd mac-receiver
npm install
npm run dev
```

The receiver listens on port `8765`.

### Android

Open the `android` folder in Android Studio and run the app on an Android device/emulator.

> This starter is intentionally small and safe: it sends named commands over the network. Add real keyboard automation only after you verify the connection and mappings.

## Security

This is designed for a trusted local network. Do not expose the receiver port directly to the public internet.
