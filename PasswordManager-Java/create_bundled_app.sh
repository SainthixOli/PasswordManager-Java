#!/bin/bash
# selo_setup.sh

APP_NAME="Selo"
APP_DIR="$HOME/Desktop/$APP_NAME.app"
ICON_SOURCE="/Users/macbookair/.gemini/antigravity/brain/32b7a14f-6190-486c-a614-280f48766f3d/selo_app_icon_flat_1767895189738.png"
JAR_SOURCE="/Users/macbookair/PasswordManager-Java/PasswordManager-Java/target/password-manager-java-2.0-SNAPSHOT.jar"

echo "Creating $APP_DIR..."

# Create structure
mkdir -p "$APP_DIR/Contents/MacOS"
mkdir -p "$APP_DIR/Contents/Resources/Java"

# Copy JAR
echo "Copying JAR..."
cp "$JAR_SOURCE" "$APP_DIR/Contents/Resources/Java/"

# Create launcher
echo "Creating Launcher..."
cat > "$APP_DIR/Contents/MacOS/$APP_NAME" <<EOF
#!/bin/bash
DIR="\$( cd "\$( dirname "\${BASH_SOURCE[0]}" )" && pwd )"

# Run Server in background
/usr/bin/java -jar "\$DIR/../Resources/Java/password-manager-java-2.0-SNAPSHOT.jar" &
SERVER_PID=\$!

# Wait for server to be ready (max 30 seconds)
count=0
while ! nc -z localhost 8080 && [ \$count -lt 30 ]; do
  sleep 1
  count=\$((count+1))
done

# Open Browser
open "http://localhost:8080"

# Keep script running to maintain the process
wait \$SERVER_PID
EOF

chmod +x "$APP_DIR/Contents/MacOS/$APP_NAME"

# Create Info.plist
echo "Creating Info.plist..."
cat > "$APP_DIR/Contents/Info.plist" <<EOF
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "http://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CFBundleExecutable</key>
    <string>$APP_NAME</string>
    <key>CFBundleIconFile</key>
    <string>AppIcon</string>
    <key>CFBundleIdentifier</key>
    <string>com.sainthixoli.selo</string>
    <key>CFBundleName</key>
    <string>$APP_NAME</string>
    <key>CFBundlePackageType</key>
    <string>APPL</string>
    <key>CFBundleShortVersionString</key>
    <string>2.0</string>
    <key>LSUIElement</key>
    <true/> <!-- Hides from Dock if preferred, or false to show -->
</dict>
</plist>
EOF

# Handle Icon
# Note: sips works for png to icns conversion primarily if we rename it to .icns directly or use specific tools,
# but usually we need an iconset. A simple hack is using a png named AppIcon.icns often works on some mac versions 
# or creating a minimal iconset.
# Better approach: Create a temporary iconset
echo "Creating Icon..."
mkdir "Selo.iconset"
# Simple resize, sips usually detects png output from extension
sips -z 16 16     "$ICON_SOURCE" --out "Selo.iconset/icon_16x16.png"
sips -z 32 32     "$ICON_SOURCE" --out "Selo.iconset/icon_16x16@2x.png"
sips -z 32 32     "$ICON_SOURCE" --out "Selo.iconset/icon_32x32.png"
sips -z 64 64     "$ICON_SOURCE" --out "Selo.iconset/icon_32x32@2x.png"
sips -z 128 128   "$ICON_SOURCE" --out "Selo.iconset/icon_128x128.png"
sips -z 256 256   "$ICON_SOURCE" --out "Selo.iconset/icon_128x128@2x.png"
sips -z 256 256   "$ICON_SOURCE" --out "Selo.iconset/icon_256x256.png"
sips -z 512 512   "$ICON_SOURCE" --out "Selo.iconset/icon_256x256@2x.png"
sips -z 512 512   "$ICON_SOURCE" --out "Selo.iconset/icon_512x512.png"
cp "$ICON_SOURCE" "Selo.iconset/icon_512x512@2x.png"

iconutil -c icns "Selo.iconset"
cp "Selo.icns" "$APP_DIR/Contents/Resources/AppIcon.icns"

# Cleanup
rm -rf "Selo.iconset"
rm "Selo.icns"

# Force Icon Refresh
touch "$APP_DIR"

echo "Done! Selo.app is ready on your Desktop."
