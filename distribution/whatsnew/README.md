# Release Notes Guide

The `whatsnew-en-US` file is used to display release notes on the Google Play Store and GitHub Releases.

## Writing Format
You can write in two ways, and the system will automatically clean it up:

### Option A (Standard List)
```text
- Added new features
- Bug fixes
```

### Option B (Numbered List)
```text
1:- Added new features
2:- Bug fixes
```

> **Important Notes:** 
> - The CI/CD system (GitHub Actions) will automatically remove the `1:- ` format to keep the Play Store display clean.
> - The maximum character limit for the Play Store is **500 characters**.
