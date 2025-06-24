#!/bin/bash

APP_NAME="iso"
JAR_NAME="developertools-0.0.1-SNAPSHOT.jar"
TARGET_PATH="build/libs/${JAR_NAME}"

echo "🚧 Building project with Gradle (bootJar)..."
./gradlew clean bootJar -q

if [ ! -f "$TARGET_PATH" ]; then
    echo "❌ Build failed. JAR not found: $TARGET_PATH"
    exit 1
fi

echo "✅ Build successful."

BIN_FILE="/usr/local/bin/${APP_NAME}"

echo "🚀 Creating CLI launcher at ${BIN_FILE}..."

# Write shell script to globally callable binary
echo "#!/bin/bash" | sudo tee $BIN_FILE > /dev/null
echo "java -jar $(pwd)/$TARGET_PATH \"\$@\"" | sudo tee -a $BIN_FILE > /dev/null

sudo chmod +x $BIN_FILE

echo ""
echo "🎉 Installation complete! You can now use:"
echo ""
echo "    iso new notification Telegram"
echo ""
