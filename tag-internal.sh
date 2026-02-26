#!/bin/zsh

# Usage: ./tag-internal.sh <version>
# Example: ./tag-internal.sh 1.6.0

VERSION=$1

if [ -z "$VERSION" ]; then
  echo "❌ Error: Version is required."
  echo "Usage: ./tag-internal.sh <version>"
  echo "Example: ./tag-internal.sh 1.6.0"
  exit 1
fi

TAG="v$VERSION-internal"

echo "🚀 Tagging internal testing build: $TAG"

git tag -a "$TAG" -m "Internal testing build $TAG"

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
echo "🔄 GitHub Actions will now build and deploy to Play Store Internal Testing track."

