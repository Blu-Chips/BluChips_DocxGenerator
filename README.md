# BluChips DocxGenerator 📱✨

A powerful Android document editor with **real-time collaborative editing** via WebSocket technology. Create, edit, and share documents across multiple devices seamlessly.

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack%20Compose-4285F4?style=for-the-badge&logo=jetpack-compose&logoColor=white)

---

## 🌟 Features

### 📝 Document Management
- **Create & Edit** documents with a rich text interface
- **Auto-save** functionality (saves 1 second after you stop typing)
- **Document list** with preview snippets
- **Persistent storage** using Room Database

### 🔄 Real-Time Collaboration
- **WebSocket Server** built-in for device-to-device communication
- **Live sync** - changes broadcast to all connected clients
- **Connection tracking** - see how many devices are connected
- **Status indicators** - visual feedback for sync status
- **IP address display** - easy connection setup

### 🎨 Modern UI/UX
- **Material Design 3** components
- **Large, readable fonts** for better accessibility
- **Color-coded status** (green = online, gray = offline)
- **Responsive layouts** optimized for different screen sizes
- **Intuitive navigation** between screens

---

## 📋 Requirements

- **Android SDK:** API 21+ (Android 5.0 Lollipop)
- **Target SDK:** API 35 (Android 15)
- **Compile SDK:** 35
- **Java Version:** 17
- **Kotlin:** 1.9+
- **Gradle:** 8.1.1+

### Development Tools
- Android Studio Ladybug or newer
- Gradle 8.1.1
- Kotlin 1.9.0

---

## 🚀 Getting Started

### 1. Clone the Repository
```bash
git clone https://github.com/yourusername/BluChips_DocxGenerator.git
cd BluChips_DocxGenerator
```

### 2. Open in Android Studio
1. Launch **Android Studio**
2. Select **File → Open**
3. Navigate to the cloned repository
4. Wait for Gradle sync to complete

### 3. Build and Run
```bash
# Using Gradle
./gradlew assembleDebug

# Or use Android Studio's Run button (Shift + F10)
```

---

## 📱 Installation

### Option A: Real Android Device (Recommended)
1. **Enable Developer Mode:**
   - Settings → About Phone → Tap "Build Number" 7 times
2. **Enable USB Debugging:**
   - Settings → Developer Options → USB Debugging (ON)
3. **Connect via USB** and run from Android Studio

### Option B: Android Emulator
- Create an emulator with **API 34+**
- For full functionality with native libraries, use **ARM64** system image
- For UI testing only, **x86_64** emulators are supported

---

## 🎯 How to Use

### Starting the Collaboration Server

1. **Launch the app** on your primary device
2. **Toggle the server switch** at the top of the home screen
3. **Note the IP address** displayed (e.g., `192.168.1.100:8080`)
4. **Share this address** with other devices on the same network

### Creating and Editing Documents

1. **Tap "+ New Document"** to create a new document
2. **Enter title and content** in the editor
3. **Auto-save** activates after 1 second of inactivity
4. **Use "SAVE" button** to manually save
5. **Use "SAVE & CLOSE"** to save and return to document list

### Collaborative Editing

1. **Start the server** on one device (host)
2. **Connect other devices** using the displayed IP:PORT
3. **Edit any document** - changes sync automatically
4. **See connected clients** in the top bar counter
5. **Watch real-time sync** indicator while editing

---

## 🏗️ Architecture

### Tech Stack

**Frontend:**
- **Jetpack Compose** - Modern declarative UI
- **Material Design 3** - Latest design system
- **Navigation Compose** - Type-safe navigation

**Backend:**
- **Room Database** - Local data persistence
- **Kotlin Coroutines** - Asynchronous programming
- **Flow** - Reactive data streams

**Networking:**
- **Java-WebSocket** - WebSocket server implementation
- **OkHttp** - HTTP client
- **JSON** - Data serialization

**Architecture Pattern:**
- **MVVM** (Model-View-ViewModel)
- **Repository Pattern**
- **Dependency Injection** (Manual)

### Project Structure
```
BluChips_DocxGenerator/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/example/docxgenerator/
│   │   │   │   ├── database/          # Room entities & DAOs
│   │   │   │   ├── ui/                # Compose UI screens
│   │   │   │   ├── viewmodel/         # ViewModels
│   │   │   │   ├── websocket/         # WebSocket server & manager
│   │   │   │   └── MainActivity.kt    # Entry point
│   │   │   ├── libs/                  # Native libraries (.so files)
│   │   │   └── AndroidManifest.xml
│   │   └── build.gradle
│   └── build.gradle
├── gradle.properties
└── README.md
```

---

## 🔧 Configuration

### Network Settings

The WebSocket server runs on **port 8080** by default. To change:

```kotlin
// In WebSocketManager.kt
companion object {
    private const val DEFAULT_PORT = 8080  // Change this
}
```

### Database Configuration

Room database is configured in `DocumentDatabase.kt`:
```kotlin
@Database(entities = [Document::class], version = 1)
abstract class DocumentDatabase : RoomDatabase()
```

---

## 🛠️ Troubleshooting

### Common Issues

#### **"Installation did not succeed: INSTALL_FAILED_NO_MATCHING_ABIS"**
- **Cause:** Emulator architecture mismatch with native libraries
- **Solution:** Use a real ARM device or see emulator support below

#### **Emulator crashes on startup**
- **Cause:** Insufficient disk space or ARM emulator on x86 host
- **Solution:** Free up disk space or use x86_64 emulator (UI only)

#### **WebSocket server won't start**
- **Cause:** Port already in use or network permissions denied
- **Solution:** Restart app or check firewall settings

#### **"Failed to read or create install properties file"**
- **Cause:** Insufficient permissions on SDK directory
- **Solution:** Run Android Studio as Administrator

---

## 🌐 WebSocket Protocol

### Message Format (JSON)

**Welcome Message:**
```json
{
  "type": "welcome",
  "message": "Connected to BluChips Document Server"
}
```

**Document Update:**
```json
{
  "type": "document_update",
  "docId": 123,
  "title": "Document Title",
  "content": "Document content...",
  "timestamp": 1234567890123
}
```

### Connecting External Clients

To connect from a web browser or external client:
```javascript
const ws = new WebSocket('ws://192.168.1.100:8080');

ws.onmessage = (event) => {
  const data = JSON.parse(event.data);
  console.log('Received:', data);
};

ws.send(JSON.stringify({
  type: 'document_update',
  docId: 1,
  title: 'My Title',
  content: 'My Content'
}));
```

---

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. **Fork** the repository
2. **Create** a feature branch: `git checkout -b feature/AmazingFeature`
3. **Commit** your changes: `git commit -m 'Add some AmazingFeature'`
4. **Push** to the branch: `git push origin feature/AmazingFeature`
5. **Open** a Pull Request

### Coding Standards
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add comments for complex logic
- Write unit tests for new features

---

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

## 🙏 Acknowledgments

- **Material Design 3** - UI components
- **Java-WebSocket** - WebSocket implementation
- **Jetpack Compose** - Modern Android UI toolkit
- **Room** - Database abstraction layer

---

## 📞 Support

For issues, questions, or suggestions:
- **Open an issue** on GitHub
- **Email:** support@bluchips.com
- **Documentation:** [Wiki](../../wiki)

---

## 🗺️ Roadmap

### Upcoming Features
- [ ] Export to DOCX format
- [ ] Rich text formatting (bold, italic, lists)
- [ ] Document sharing via QR code
- [ ] Cloud sync integration
- [ ] Multi-language support
- [ ] Dark mode enhancement
- [ ] Document versioning
- [ ] Collaborative cursor tracking

---

## 📊 Project Status

![Build Status](https://img.shields.io/badge/build-passing-brightgreen)
![Version](https://img.shields.io/badge/version-1.0.0-blue)
![License](https://img.shields.io/badge/license-MIT-green)

**Current Version:** 1.0.0  
**Last Updated:** October 2025  
**Status:** Active Development

---

Made with ❤️ by the BluChips Team

