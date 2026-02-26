#!/bin/zsh

# Usage: ./tag-open-testing.sh <version>
# Example: ./tag-open-testing.sh 1.6.1

VERSION=$1

if [ -z "$VERSION" ]; then
  echo "❌ Error: Version is required."
  echo "Usage: ./tag-open-testing.sh <version>"
  echo "Example: ./tag-open-testing.sh 1.6.1"
  exit 1
fi

TAG="v$VERSION-open-testing"

echo "🚀 Tagging open testing build: $TAG"

git tag -a "$TAG" -m "Open testing build $TAG"

if [ $? -ne 0 ]; then
  echo "❌ Failed to create tag. Make sure the tag doesn't already exist."
  exit 1
fi

git push origin "$TAG"

if [ $? -ne 0 ]; then
  echo "❌ Failed to push tag."
  exit 1
fi

echo "✅ Successfully tagged and pushed: $TAG"
echo "🔄 GitHub Actions will now build and deploy to Play Store Open Testing track."

