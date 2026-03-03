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
echo "  3) GitHub Release only"
echo ""
printf "Enter choice (1/2/3): "
read TRACK_CHOICE

if [ "$TRACK_CHOICE" = "1" ]; then
  TRACK="beta"
  TAG="v$VERSION-beta"
  TRACK_LABEL="Open Testing (Beta)"
  UPLOAD_PLAY_STORE="true"
  UPLOAD_GITHUB_RELEASE="false"
elif [ "$TRACK_CHOICE" = "2" ]; then
  TRACK="production"
  TAG="v$VERSION"
  TRACK_LABEL="Production"
  UPLOAD_PLAY_STORE="true"
  UPLOAD_GITHUB_RELEASE="false"
elif [ "$TRACK_CHOICE" = "3" ]; then
  TRACK="none"
  TAG="v$VERSION"
  TRACK_LABEL="GitHub Release"
  UPLOAD_PLAY_STORE="false"
  UPLOAD_GITHUB_RELEASE="true"
else
  echo "❌ Invalid choice. Please enter 1, 2, or 3."
  exit 1
fi

if [ "$TRACK_CHOICE" = "1" ] || [ "$TRACK_CHOICE" = "2" ]; then
  echo ""
  printf "📦 Also upload to GitHub Release? (y/n): "
  read GITHUB_RELEASE_CHOICE
  if [ "$GITHUB_RELEASE_CHOICE" = "y" ] || [ "$GITHUB_RELEASE_CHOICE" = "Y" ]; then
    UPLOAD_GITHUB_RELEASE="true"
  fi
fi

echo ""
echo "🚀 Tagging $TRACK_LABEL build: $TAG"

git tag -a "$TAG" -m "$TRACK_LABEL build $TAG upload_play_store=$UPLOAD_PLAY_STORE upload_github_release=$UPLOAD_GITHUB_RELEASE"

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
if [ "$UPLOAD_PLAY_STORE" = "true" ]; then
  echo "🔄 GitHub Actions will build and deploy to Play Store ($TRACK_LABEL) track."
fi
if [ "$UPLOAD_GITHUB_RELEASE" = "true" ]; then
  echo "📦 GitHub Release will also be created for $TAG."
fi
