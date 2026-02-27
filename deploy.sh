#!/bin/zsh

# Usage: ./deploy.sh <version>
# Example: ./deploy.sh 2.0.0

VERSION=$1

if [ -z "$VERSION" ]; then
  echo "❌ Error: Version is required."
  echo "Usage: ./deploy.sh <version>"
  echo "Example: ./deploy.sh 2.0.0"
  exit 1
fi

echo ""
echo "🎯 Choose deployment track:"
echo "  1) Beta (Open Testing)"
echo "  2) Production"
echo ""
printf "Enter choice (1/2): "
read TRACK_CHOICE

if [ "$TRACK_CHOICE" = "1" ]; then
  TRACK="beta"
  TAG="v$VERSION-beta"
  TRACK_LABEL="Open Testing (Beta)"
elif [ "$TRACK_CHOICE" = "2" ]; then
  TRACK="production"
  TAG="v$VERSION"
  TRACK_LABEL="Production"
else
  echo "❌ Invalid choice. Please enter 1 or 2."
  exit 1
fi

echo ""
echo "🚀 Tagging $TRACK_LABEL build: $TAG"

git tag -a "$TAG" -m "$TRACK_LABEL build $TAG"

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
echo "🔄 GitHub Actions will now build and deploy to Play Store $TRACK_LABEL track."
