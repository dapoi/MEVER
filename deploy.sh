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
echo "  1) Internal Testing"
echo "  2) Beta (Open Testing)"
echo "  3) Production & Beta"
echo "  4) Cancel"
echo ""
printf "Enter choice (1/2/3/4): "
read TRACK_CHOICE

if [ "$TRACK_CHOICE" = "1" ]; then
  TRACK="internal"

  LATEST_INTERNAL=$(git tag -l "v$VERSION-internal*" | sort -V | tail -n 1)

  if [ -z "$LATEST_INTERNAL" ]; then
    INTERNAL_NUMBER="01"
  else
    LAST_NUM=$(echo $LATEST_INTERNAL | sed 's/.*internal//')
    NEXT_NUM=$((10#$LAST_NUM + 1))
    INTERNAL_NUMBER=$(printf "%02d" $NEXT_NUM)
  fi

  TAG="v$VERSION-internal$INTERNAL_NUMBER"
  echo "🔍 Suggested Tag: $TAG"
  printf "Is this internal version correct? (y/n): "
  read INTERNAL_CONFIRM

  if [ "$INTERNAL_CONFIRM" != "y" ] && [ "$INTERNAL_CONFIRM" != "Y" ]; then
    printf "Enter internal number manually (e.g., 1, 2, 3): "
    read INTERNAL_INPUT
    if [[ ! "$INTERNAL_INPUT" =~ ^[0-9]+$ ]]; then
      echo "❌ Error: Internal number must be a digit."
      exit 1
    fi
    INTERNAL_NUMBER=$(printf "%02d" $INTERNAL_INPUT)
    TAG="v$VERSION-internal$INTERNAL_NUMBER"
  fi

  TRACK_LABEL="Internal Testing"
  TARGET_BUILDS="  • Internal Track   ➜  versionName: ${TAG#v}"
  UPLOAD_PLAY_STORE="true"

elif [ "$TRACK_CHOICE" = "2" ]; then
  TRACK="beta"

  # Find the latest beta tag for this version (e.g., v2.0.0-beta01)
  LATEST_BETA=$(git tag -l "v$VERSION-beta*" | sort -V | tail -n 1)

  if [ -z "$LATEST_BETA" ]; then
    BETA_NUMBER="01"
  else
    LAST_NUM=$(echo $LATEST_BETA | sed 's/.*beta//')
    NEXT_NUM=$((10#$LAST_NUM + 1))
    BETA_NUMBER=$(printf "%02d" $NEXT_NUM)
  fi

  TAG="v$VERSION-beta$BETA_NUMBER"
  echo "🔍 Suggested Tag: $TAG"
  printf "Is this beta version correct? (y/n): "
  read BETA_CONFIRM

  if [ "$BETA_CONFIRM" != "y" ] && [ "$BETA_CONFIRM" != "Y" ]; then
    printf "Enter beta number manually (e.g., 1, 2, 3): "
    read BETA_INPUT
    if [[ ! "$BETA_INPUT" =~ ^[0-9]+$ ]]; then
      echo "❌ Error: Beta number must be a digit."
      exit 1
    fi
    BETA_NUMBER=$(printf "%02d" $BETA_INPUT)
    TAG="v$VERSION-beta$BETA_NUMBER"
  fi

  TRACK_LABEL="Beta (Open Testing)"
  TARGET_BUILDS="  • Beta Track       ➜  versionName: ${TAG#v}"
  UPLOAD_PLAY_STORE="true"

elif [ "$TRACK_CHOICE" = "3" ]; then
  TRACK="production"
  TAG="v$VERSION"
  TRACK_LABEL="Production & Beta (Dual Parallel Builds)"
  TARGET_BUILDS="  • Beta Track       ➜  versionName: ${VERSION}-beta01\n  • Production Track ➜  versionName: ${VERSION}"
  UPLOAD_PLAY_STORE="true"

elif [ "$TRACK_CHOICE" = "4" ]; then
  echo "❌ Deployment cancelled."
  exit 0
else
  echo "❌ Invalid choice. Please enter 1, 2, 3, or 4."
  exit 1
fi

# --- Deployment Summary & Confirmation ---
echo ""
echo "----------------------------------------------------"
echo "🚀 DEPLOYMENT SUMMARY"
echo "----------------------------------------------------"
echo "App Version:     $VERSION"
echo "Git Tag:         $TAG"
echo "Target Track:    $TRACK_LABEL"
echo "Build Outputs:"
echo -e "$TARGET_BUILDS"
echo "----------------------------------------------------"
echo ""
printf "Confirm deployment? (y/n): "
read CONFIRM

if [ "$CONFIRM" != "y" ] && [ "$CONFIRM" != "Y" ]; then
  echo "❌ Deployment cancelled."
  exit 0
fi

# Cleanup old tags for internal or beta
if [ "$TRACK" = "internal" ]; then
  OLD_TAGS=$(git tag -l "v$VERSION-internal*" | grep -v "$TAG")
elif [ "$TRACK" = "beta" ]; then
  OLD_TAGS=$(git tag -l "v$VERSION-beta*" | grep -v "$TAG")
else
  OLD_TAGS=""
fi

if [ -n "$OLD_TAGS" ]; then
  echo "🧹 Cleaning up previous $TRACK tags for v$VERSION..."
  for old_tag in ${(f)OLD_TAGS}; do
    echo "  🗑️ Deleting remote tag: $old_tag"
    git push origin --delete "$old_tag" 2>/dev/null || true
    git tag -d "$old_tag" 2>/dev/null || true
  done
fi

echo ""
echo "🚀 Tagging build: $TAG"

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

git tag -a "$TAG" -m "$TRACK_LABEL build $TAG upload_play_store=$UPLOAD_PLAY_STORE"

if [ $? -ne 0 ]; then
  echo "❌ Failed to create tag $TAG."
  exit 1
fi

git push origin "$TAG"

if [ $? -ne 0 ]; then
  echo "❌ Failed to push tag $TAG."
  exit 1
fi

echo "✅ Successfully tagged and pushed: $TAG"
if [ "$TRACK_CHOICE" = "3" ]; then
  echo "🎉 GitHub Actions will run two parallel builds (Beta & Production) under a single Action run."
else
  echo "🎉 GitHub Actions will build and deploy $TRACK_LABEL to Play Store."
fi