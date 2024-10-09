# Changelog

## 1.2.6

- Add RenderScreenCallback custom event.
- Add SetupScreenCallback custom event.

## 1.2.5

- Fix condition where config exists but doesn't yet have the new feature.

## 1.2.4

- Add buttons to widget folder.

## 1.2.3

- Add core/common resources and buttons.

## 1.2.2

- Fix boot runnables not being loaded properly.

## 1.2.1

- Fix null pointer when loading config.

## 1.2.0

- Update mod setup to include priority and simplify registration in setup classes.

## 1.1.7

- Simple feature resolver

## 1.1.6

- Add WorldHelper.

## 1.1.5

- Bump pack version for latest snapshot.

## 1.1.4

- Add icon.

## 1.1.3

- Add ItemContainerTooltip for other mods to use as a base.
- Add graphical assets for default containers.
- Add HoverOverItemTooltip and RenderTooltipComponent custom events.

## 1.1.2

- Update to snapshot 24w40a.

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