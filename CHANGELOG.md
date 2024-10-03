# Changelog

## 1.1.1

- Add PlaySoundCallback custom event and mixin.
- Add debug mode and mixin disable mode, with Diagnostics feature to configure them.
- Rename some references from `charmony-nano` to `charmony`.
- Expose `init()` method for child mods to ensure that charmony gets launched first.

## 1.1.0

- Restructure as charmony will inherit the same codebase as charmony-nano.
- Removed custom HudRenderEvent as it's already implemented by Fabric.
- Move Config, Mod, ModFeature and Log to `base` package.
- Mark some important classes final.

## 1.0.1

- `Side` now uses `StringRepresentable` rather than custom string method.

## 1.0.0

- Initial release to support client tweaks.