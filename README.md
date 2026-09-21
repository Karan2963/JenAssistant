# Jen Assistant V2
For Samsung Galaxy J7 Max / Android 8.1.

Features: voice input, Hindi TTS, GPT backend/tool calling, flashlight, battery, reminders, WhatsApp prepared-message flow, accessibility and notification service scaffolds.

IMPORTANT: The OpenAI key shared in chat is a secret. Do NOT put it in the APK. Revoke that key and create a fresh one. Put the fresh key only in backend/.env.

PC backend:
cd C:\JenAssistant_V2\backend
npm install
copy .env.example .env
notepad .env
npm start

Find PC IP:
ipconfig

In Jen enter:
http://YOUR_PC_IP:3000/chat

Phone and PC must be on same Wi-Fi.

Build APK in Android Studio: Build > Build APK(s)

APK:
C:\JenAssistant_V2\app\build\outputs\apk\debug\app-debug.apk

Install:
cd "C:\Users\ADMIN\Desktop\platform-tools-latest-windows\platform-tools"
.\adb devices
.\adb install -r "C:\JenAssistant_V2\app\build\outputs\apk\debug\app-debug.apk"

V2 intentionally does not embed the API key. Full automatic WhatsApp contact/send and always-on wake word require further Samsung/Android-specific testing.
