#!/bin/zsh

# Usage: ./deploy.sh <version>
# Example: ./deploy.sh 2.0.0
#
# NOTE: Before running this, make sure to update release notes in:
# distribution/whatsnew/whatsnew-en-US

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

  # Find the latest beta tag for this version (e.g., v2.0.0-beta01)
  LATEST_BETA=$(git tag -l "v$VERSION-beta*" | sort -V | tail -n 1)

  if [ -z "$LATEST_BETA" ]; then
    # If no tag exists, start from 01
    BETA_NUMBER="01"
  else
    # If tag exists, extract the number after 'beta' and increment it
    # Use 10# to force the number to be treated as decimal (not octal)
    LAST_NUM=$(echo $LATEST_BETA | sed 's/.*beta//')
    NEXT_NUM=$((10#$LAST_NUM + 1))
    BETA_NUMBER=$(printf "%02d" $NEXT_NUM)
  fi

  TAG="v$VERSION-beta$BETA_NUMBER"
  TRACK_LABEL="Open Testing (Beta $BETA_NUMBER)"
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

# --- Deployment Summary & Confirmation ---
echo ""
echo "------------------------------------------"
echo "🚀 DEPLOYMENT SUMMARY"
echo "------------------------------------------"
echo "Version:         $VERSION"
echo "Tag:             $TAG"
echo "Track:           $TRACK_LABEL"
echo "Play Store:      $([ "$UPLOAD_PLAY_STORE" = "true" ] && echo "✅ Yes" || echo "❌ No")"
echo "GitHub Release:  $([ "$UPLOAD_GITHUB_RELEASE" = "true" ] && echo "✅ Yes" || echo "❌ No")"
echo "------------------------------------------"
echo ""
printf "Confirm deployment? (y/n): "
read CONFIRM

if [ "$CONFIRM" != "y" ] && [ "$CONFIRM" != "Y" ]; then
  echo "❌ Deployment cancelled."
  exit 0
fi

echo ""
echo "🚀 Tagging $TRACK_LABEL build: $TAG"

# Remove existing local tag if present
if git tag -l "$TAG" | grep -q "$TAG"; then
  echo "⚠️  Local tag $TAG already exists, removing it..."
  git tag -d "$TAG"
fi

# Remove existing remote tag if present
if git ls-remote --tags origin "refs/tags/$TAG" | grep -q "$TAG"; then
  echo "⚠️  Remote tag $TAG already exists, removing it..."
  git push origin ":refs/tags/$TAG"
fi

git tag -a "$TAG" -m "$TRACK_LABEL build $TAG upload_play_store=$UPLOAD_PLAY_STORE upload_github_release=$UPLOAD_GITHUB_RELEASE"

if [ $? -ne 0 ]; then
  echo "❌ Failed to create tag."
  exit 1
fi

git push origin "$TAG"

if [ $? -ne 0 ]; then
  echo "❌ Failed to push tag."
  exit 1
fi

echo "✅ Successfully tagged and pushed: $TAG"
if [ "$UPLOAD_PLAY_STORE" = "true" ]; then
  echo "🔄 GitHub Actions will build and deploy to Play Store - $TRACK_LABEL track."
fi
if [ "$UPLOAD_GITHUB_RELEASE" = "true" ]; then
  echo "📦 GitHub Release will also be created for $TAG."
fi