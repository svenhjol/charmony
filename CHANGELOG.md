# Changelog

## 1.18.1

- Wire up to modmenu API

## 1.18.0

- API now bundled.

## 1.17.7

- Add smithing table events.

## 1.17.1

- Add helpers and registerables for client registry.

## 1.17.0

- Simplify entity registration.

## 1.16.1

- Move register step after boot step. This allows conditional registering to work properly.

## 1.16.0

- Change some registry helper methods to use suppliers to avoid nulls during registration.
- Add structure and structure piece registry helper methods.

## 1.15.11

- Add check step to feature.

## 1.15.10

- Add SyncedBlockEntity.

## 1.15.9

- Expose the isValid property of the base hud renderer.

## 1.15.8

- Add registration helper method to add blocks to block entities.

## 1.15.7

- Add BaseToast to client.

## 1.15.6

- Add BaseHudRenderer and client mixin.
- Registerable is now final.

## 1.15.5

- Further string emptiness checking in control panel.

## 1.15.4

- Tighten up side launching to match a mod's definition.
- Compare all values of a list when checking if config has been changed from default.

## 1.15.3

- Add entity, biome and mob spawning registry helper methods.

## 1.15.2

- Inverse boolean check in mixin config.

## 1.15.1

- Trying to get blocks and items to register in 1.21.4

## 1.15.0

- CommonRegistry and ClientRegistry now return a Registerable.
- Mod loads registerables to resolve suppliers.

## 1.14.1

- Add PlayerKilledDropCallback.
- Add helper methods for ClientRegistry.

## 1.14.0

- CommonRegistry helper methods now return a supplier.
- Changed "feature" to "sidedFeature" where applicable in Mod class.

## 1.13.5

- Add DataComponent

## 1.13.4

- Add tryFeature to fetch from another charmony mod.

## 1.13.3

- Port GenericTrades from Charm.

## 1.13.2

- Add WandererTrade helper method in registration.

## 1.13.1

- Add CommonRegistry helper methods and VillagerHelper for handling trade registrations.

## 1.13.0

- Remove client mode.
- Change length of control panel config keys.

## 1.12.3

- Set all sided features enabled flag to false when an encompassing feature is disabled.

## 1.12.2

- Add texture for custom selected_slot.

## 1.12.1

- Fix signature order issue.

## 1.12.0

- Change signature of ItemContainerTooltip to make specifying slot texture more flexible.

## 1.11.11

- Add ItemStackHelper and TagHelper.

## 1.11.10

- Update icon.

## 1.11.9

- Add ItemDragDropCallback.

## 1.11.8

- Control panel shows when a disabled-by-default feature has been enabled.

## 1.11.7

- Check feature is enabled during side runtime.

## 1.11.6

- Check feature is enabled in all configuration files before considering enabled.

## 1.11.5

- Make AdvancementHelper trigger check more permissive.

## 1.11.4

- Environment also checks client-mod.

## 1.11.3

- Mod side check for client-mode.

## 1.11.2

- Add EntityTickCallback and EntityKilledDropCallback events.

## 1.11.1

- Add PlayerTickCallback event.

## 1.11.0

- Build for 1.21.4.
- Update for new scrollbar code.

## 1.10.5

- Add material for on-take (testing).

## 1.10.4

- Separate behavior for on-take (testing).

## 1.10.3

- Combine anvil events into single class like the grindstone events.
- Add an on-take event (testing).

## 1.10.2

- Port custom anvil events from Charm.

## 1.10.1

- When in debug mode show the original exception during mod init failure.
- Sided test feature only shows in dev enrivonment.

## 1.10.0

- Add initial ClientRegistry and custom particle type.

## 1.9.6

- Port ColorHelper.

## 1.9.5

- Port CustomParticle and PlayerHelper.

## 1.9.4

- Make Feature description pick from the side that has the longest string.

## 1.9.3

- Add sided feature checks.

## 1.9.2

- Add enabledInConfig optional switch for sided features.
- Update feature definition annotation with details.

## 1.9.1

- Fix for trying to load core values from a non-charmony core config.

## 1.9.0

- Rewrite and simplify mixin config and conditional mixin loading.

## 1.8.7

- Port EnchantmentsHelper.

## 1.8.6

- Fix hardcoded modId in mixin config loader.

## 1.8.5

- Root node must be visible.

## 1.8.4

- Add player_joined criteria and root advancement node.

## 1.8.3

- Add a helper method to trigger a custom advancement.

## 1.8.2

- Add advancements feature, ported across from Charm and simplified.

## 1.8.1

- Don't show message about features being disabled if they're not included in the side.

## 1.8.0

- Split mixins into client and common subfolders for better filtering.
- Wire up "client mode" to disable non-client mixins.

## 1.7.5

- Allow mods that have no features to be displayed on the in-game options screen.

## 1.7.4

- Disable dev environment check for debug.
- Charmony server type message will now show in default log.

## 1.7.3

- Use common environment

## 1.7.2

- Helper method for the client to check if it's running on a charmony server.

## 1.7.1

- Rename diagnostics
- Network checking
- Fix todos in mixin config
- Add config stub for forcing client mode

## 1.7.0

- Build for 1.21.3

## 1.6.2

- Control panel only shows on in-game options.

## 1.6.1

- Show control panel feature even if it doesn't have config options.

## 1.6.0

- Build for 1.21.2-rc1
- Add control panel button to options screen.

## 1.5.0

- Build for 1.21.2-pre5

## 1.4.2

- Control panel mods list now shows mod icon.
- Update to loom 1.8.

## 1.4.0-1

- Control panel to adjust charmony mod settings in-game.
- Rename "scaffold" to "core".
- Add composite feature, move default feature to sided feature.
- Change some log suffixes.
- Update logo.

## 1.3.3

- Variable pack version

## 1.3.2

- Build for 1.21.2-pre2

## 1.3.1

- Refactor event mixin folder names to simplify.
- Add custom fabric events for listening to client player login and respawn.

## 1.3.0

- Build for 1.21.2-pre1

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