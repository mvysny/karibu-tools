# Audit — field & overlay utilities vs. upstream (flow-components main = 25.4-SNAPSHOT, clone 2026-09-28)

Evidence methods: `javap` = bisected against Maven Central jars (one version per resolution); `src` = grep of the master clone;
`git` = `git log -S` + `git tag --contains` in ~/work/vaadin/flow-components; `mcp` = Vaadin MCP javadoc.
Bisection used a private helper (one Gradle configuration per coordinate), so the vjar.sh same-module bug did not affect these results.

Buckets: A PR candidate · B already upstream · C Kotlin-only · D upstream rejected · E obsolete/superseded.
Tags: `wc-side` = needs a vaadin/web-components change; `theme` = Lumo/Aura-bound.

| Symbol | file:line | README | Upstream on master | First version (x.y) | Tickets | Bucket | Note |
|---|---|---|---|---|---|---|---|
| `ComboBoxAlign` enum | Comboboxes.kt:6 | partially (via textAlign) | `ComboBoxVariant.ALIGN_LEFT/CENTER/RIGHT` (+ `ALIGN_START/END`) | 22.0 (LUMO_ALIGN_*), neutral 25.1 | fc#454, fc#2089 closed-completed 2021 | B | only a value type for `textAlign` |
| `ComboBox.textAlign` | Comboboxes.kt:14 | yes | `addThemeVariants(ComboBoxVariant.ALIGN_*)` | 22.0 javap; neutral 25.1 javap | fc#2089 closed-completed | B | property sugar over variants; `theme` |
| `ComboBox.isSmall` | Comboboxes.kt:36 | yes | `ComboBoxVariant.SMALL` (LUMO_SMALL) | 22.0 javap; neutral 25.1 javap | fc#2089 | B | `theme`; Aura styles `small` (aura/src/size.css) |
| `ComboBox.isHelperAboveField` | Comboboxes.kt:46 | yes | `ComboBoxVariant.HELPER_ABOVE` | 22.0 javap; neutral 25.1 javap | fc#2089 | B | `theme` |
| `ComboBoxVariant` enum | Comboboxes.kt:55 | yes | `com.vaadin.flow.component.combobox.ComboBoxVariant` | 22.0 javap | fc#454, fc#2089, fc#2090 closed-completed 2021 | B | Vaadin 14 backfill only; clashes by simple name with upstream in 22+ |
| `ComboBox.addThemeVariants(ComboBoxVariant)` | Comboboxes.kt:62 | yes | `HasThemeVariant.addThemeVariants` | 22.0 javap | fc#2089 | B | member wins over extension in 22+ |
| `ComboBox.removeThemeVariants` | Comboboxes.kt:69 | yes | `HasThemeVariant.removeThemeVariants` | 22.0 javap | fc#2089 | B | |
| `ComboBox.dropdownWidth` | Comboboxes.kt:77 | yes | `ComboBox.setOverlayWidth(String)` / `(float, Unit)` (setter only, no getter) | 24.4 javap (24.3 no) | fc#2331 closed-completed 2024-01-15 | B | README "Vote for #2331" is stale; same CSS var `--vaadin-combo-box-overlay-width` |
| `ComboBox.prefixComponent` | Comboboxes.kt:86 | yes | `HasPrefix.setPrefixComponent/getPrefixComponent` | 24.0 javap (23.3 no) | — | B | README already says 24+ |
| `Select.prefixComponent` | Selects.kt:9 | yes | `HasPrefix` | 24.0 javap | — | B | |
| `Select.textAlign` | Selects.kt:19 | yes | `SelectVariant.ALIGN_*` | 23.1 javap; neutral 25.1 | — | B | `theme` |
| `Select.isSmall` | Selects.kt:41 | yes | `SelectVariant.SMALL` | 23.1 javap; neutral 25.1 | — | B | `theme` |
| `Select.isHelperAboveField` | Selects.kt:51 | yes | `SelectVariant.HELPER_ABOVE` | 23.1 javap; neutral 25.1 | — | B | `theme` |
| `SelectVariant` enum | Selects.kt:60 | yes | `com.vaadin.flow.component.select.SelectVariant` | 23.1 javap (23.0 no) | — | B | README "23.1+" confirmed |
| `Select.addThemeVariants(SelectVariant)` | Selects.kt:67 | yes | `HasThemeVariant.addThemeVariants` | 23.1 javap | — | B | |
| `Select.removeThemeVariants` | Selects.kt:74 | yes | `HasThemeVariant.removeThemeVariants` | 23.1 javap | — | B | (upstream also has `Select.setOverlayWidth` since 24.5 — karibu has no Select counterpart) |
| `DatePicker.prefixComponent` | DatePickers.kt:9 | yes | `HasPrefix` | 24.0 javap | — | B | |
| `ListBoxBase.setItemLabelGenerator` | ListBoxes.kt:14 | yes | `ListBoxBase.setItemLabelGenerator` | 23.0 javap (22.0 no) | platform#2601 closed-completed 2022-03-02; flow#12699 closed 2022 | B | |
| `RadioButtonGroup.setItemLabelGenerator` | RadioButtons.kt:14 | yes | `RadioButtonGroup.setItemLabelGenerator` | 23.0 javap (22.0 no) | fc#1681 closed-completed 2022-02-02 | B | README says "Since 0.6. Implemented in newer Vaadin" → 23.0 |
| `selectAll()` | TextFieldUtils.kt:11 | yes | none (only `TextFieldBase.setAutoselect`) | — | fc#1377 open "TextField Selection API"; wc#1375 open | A | on `TextFieldBase` / a `HasSelection`-style mixin; flow-side `executeJs` on `inputElement` works, wc method nicer |
| `selectNone()` | TextFieldUtils.kt:20 | yes | none | — | fc#1377, fc#1152 open (get/setCursorPosition) | A | same PR family as selectAll |
| `setCursorLocation(Int)` | TextFieldUtils.kt:30 | yes | none | — | fc#1152 open | A | upstream name would be `setCursorPosition` per fc#1152 |
| `select(IntRange)` | TextFieldUtils.kt:38 | yes | none | — | fc#1377 open, wc#1375 open | A | Java shape `setSelectionRange(int start, int end)`; `IntRange` is Kotlin-only |
| `Upload.isEnabled` | UploadUtils.kt:12 | yes | `Upload implements HasEnabled` | 24.6 javap/git (fc PR #6787) | fc#2182 closed-completed 2025-04-03 | B | README "Vote for #2182" stale; karibu toggles maxFiles 0/1, upstream disables the component; member shadows extension on 24.6+ |
| `Upload.clear()` | UploadUtils.kt:23 | yes | `Upload.clearFileList()` | 23.0 javap (22.0 no) | fc#1572 closed-completed 2022-02-07 | B | README: "Present as clearFileList() in newer Vaadin" → 23.0 |
| `Upload.buttonCaption` | UploadUtils.kt:35 | **no** | none; `UploadI18N.AddFiles` + `setUploadButton(Component)` | — | fc#3542 closed-completed 2022-11-25: web-padawan — "We don't have `setCaption()` in any of the Flow components", `setText()` would confuse with drop label | D | upstream declined caption-style API; README bullet missing |
| `AbstractLogin.i18n` (getter+setter) | AbstractLoginUtils.kt:11 | **no** | `setI18n` public; `getI18n()` exists but **package-private** | setter 14.0 (baseline); getter n/a | fc#9923 open "never install a default i18n"; fc#1028/#1089/#1710 closed 2026-08-21 | A (weak) | PR: make `AbstractLogin.getI18n()` public; check fc#9923 direction first |
| `AbstractLogin.setErrorMessage(title, message)` | AbstractLoginUtils.kt:35 | yes | `AbstractLogin.showErrorMessage(String, String)` | 24.4 javap/git (fc PR #5749) | fc#1525 closed-completed 2024-01-11 | B | README "Vote for #1525" stale; same body as karibu's |
| `FormLayout.addRowBreak()` | FormLayouts.kt:12 | **no** | none; superseded by `FormLayout.addFormRow()` / `FormRow` (autoResponsive mode) | FormRow 24.8 javap | fc#7682 (user's) closed 2026-09-23 as obsolete: rolfsmeds — responsiveSteps deprecated in V26, FormRow is the way | E | README bullet missing; mention superseded by FormRow 24.8 |
| `getAllDialogs()` | DialogUtils.kt:14 | yes | none | — | none found | A (weak) | low value; probably `UI`-level API — expect "API pollution" pushback |
| `UI.getAllDialogs()` | DialogUtils.kt:20 | yes | none | — | none found | A (weak) | same as above |
| `Dialog.center()` | DialogUtils.kt:27 | yes | partial: `Dialog.setTop/setLeft` | setTop/setLeft 24.6 javap (24.5 no) | vaadin-dialog#220 → moved to **wc#601** open "Allow dialog centering after width/height changes" | A, `wc-side` | README link points to archived repo, update to wc#601; karibu impl pokes `this.$.overlay.$.overlay` (shadow internals) |
| `Dialog.requestClose(fromClient)` | DialogUtils.kt:43 | yes | none | — | **fc#6027** open (user's) "Allow programmatic trigger of dialog close procedure" | A | pure flow-side; `DialogCloseActionEvent` ctor is public |
| `Notification.getText()` | Notifications.kt:25 | yes | none (only `setText`; testbench `NotificationElement.getText`) | — | wc#2446 → moved to **fc#2088** "Implement Notification.getText()" (user's) → **fc#10271 merged, 25.4** | B | upstream since 25.4 |
| `Notification.addCloseButton()` | Notifications.kt:42 | yes | none | — | wc#438 open "[notification] Add close/dismiss button"; fc#5531 open "Add a close button for notifications" | A, `wc-side`, `theme` | flow-side composite uses LUMO_ICON/LUMO_TERTIARY; proper fix is in the web component |
| `Button.setPrimary()` | Buttons.kt:9 | yes | `ButtonVariant.PRIMARY` (theme-neutral) / `LUMO_PRIMARY` / deprecated `AURA_PRIMARY` | LUMO 14.0; AURA_PRIMARY 25.0; neutral PRIMARY 25.1 javap | fc#8126 closed-completed 2025-11-04 "Add base and Aura variants"; fc#8651 closed-completed 2026-02-13 "Shared theme variants across Lumo and Aura" | E | the theme-neutral variant is the upstream answer; a boolean-ish setter goes against the variant API; still `LUMO_PRIMARY` here — could switch to PRIMARY on 25.1+ |
| `Button.addThemeVariantsCompat` | Buttons.kt:14 | **no** | n/a — reflective V14/V24 compat shim (`HasThemeVariant` absent in 14) | — | — | E | internal shim leaked as public; not upstream material |
| `Button.setDanger()` | Buttons.kt:33 | **no** | `ButtonVariant.ERROR` + `PRIMARY` (AURA_DANGER deprecated → ERROR) | ERROR neutral 25.1 | wc#792 open "[button] Add 'danger' theme variant" | E, `theme` | README bullet missing; wc#792 is the upstream track |
| `Badge` class | Badge.kt:10 | **no** | `com.vaadin.flow.component.badge.Badge` (`<vaadin-badge>`, `vaadin-badge-flow`) | 25.1 javap/mcp (25.0: no artifact) | platform#8530 closed-completed 2026-03-18; wc#5379 closed-completed 2026-05-27; wc#12718 closed-not-planned 2026-09-10 (`theme="badge"` span unsupported in Aura) | B, `theme` | karibu Badge = Lumo `theme="badge"` Span → invisible under Aura; name clash on 25.1+ |
| `Badge.addThemeVariants` | Badge.kt:20 | no | `Badge.addThemeVariants(BadgeVariant...)` (HasThemeVariant) | 25.1 | as above | B | |
| `Badge.removeThemeVariants` | Badge.kt:27 | no | `HasThemeVariant.removeThemeVariants` | 25.1 | as above | B | |
| `BadgeVariant` enum | Badge.kt:36 | no | `com.vaadin.flow.component.badge.BadgeVariant` (CONTRAST, DOT, ERROR, FILLED, ICON_ONLY, NUMBER_ONLY, SUCCESS, WARNING, SMALL) | 25.1 | as above | B, `theme` | upstream has no PRIMARY / PILL |
| `IconName` data class (+ `createComponent`, `isVaadinIcon`, `asVaadinIcon`, `toString`, `of`, `fromString`, `fromComponent`) | IconUtils.kt:15 | partially (only via `icon.iconName`) | partial: `Icon.getIcon()` / `getCollection()` / `setIcon(String, String)` | 24.4 javap (24.3 no) | none found | E | value type is karibu's own; upstream now exposes both halves as strings |
| `Icon.iconName` | IconUtils.kt:83 | yes | `Icon.getCollection()` + `Icon.getIcon()` / `setIcon(collection, icon)` | 24.4 javap | none found | B (partial) | |
| `Icon.setIcon(VaadinIcon?)` | IconUtils.kt:89 | yes | `Icon.setIcon(VaadinIcon)` | 24.4 javap (24.3 no) | none found | B | member shadows extension on 24.4+ (non-null) |
| `VaadinIcon.iconName` | IconUtils.kt:96 | **no** | none (`VaadinIcon.create()` only) | — | — | E | only meaningful with `IconName` |

## Theme findings (Q_theme_specific)

- **Aura ships `primary`**: `packages/aura/src/components/button.css` styles `theme~='primary'`; also `error`/`danger` (color.css), `small` (size.css, generic), align/helper-above-field via the base field styles (upstream has `AURA_ALIGN_*`, `AURA_HELPER_ABOVE_FIELD` since 25.0).
- **Theme-neutral variant constants exist on master** and were introduced in **25.1** (javap: 25.0 has only LUMO_* + AURA_*):
  `ButtonVariant.PRIMARY/TERTIARY/SUCCESS/WARNING/ERROR/SMALL/LARGE` (AURA_PRIMARY/TERTIARY/DANGER deprecated),
  `ComboBoxVariant` & `SelectVariant` `.ALIGN_LEFT/CENTER/RIGHT/START/END`, `.HELPER_ABOVE`, `.SMALL`, `.LABEL_ASIDE` (master only).
  Tickets: fc#8126 (closed 2025-11-04), fc#8651 (closed 2026-02-13).
- **Upstream Badge component**: yes — `vaadin-badge-flow`, `@since 25.1`; Lumo `theme="badge"` on a Span is unsupported under Aura and won't be (wc#12718 not planned). karibu `Badge` breaks under Aura.
- "Button primary helper" feature request: no ticket for a `setPrimary()`-style shorthand found (searched setPrimary / "primary button" / "Button primary API" across flow, flow-components, web-components, platform). The associated upstream work is fc#8126 + fc#8651 (theme-neutral `PRIMARY`). The related open one is wc#792 (danger variant, for `setDanger`).

## README "Vote for" link recheck (in scope)

| README link | Current state |
|---|---|
| fc#2331 ComboBox dropdown width | closed-completed 2024-01-15 → `setOverlayWidth` 24.4 |
| fc#2182 Upload enabled | closed-completed 2025-04-03 → `HasEnabled` 24.6 |
| fc#1572 Upload clear (only in KDoc) | closed-completed 2022-02-07 → `clearFileList` 23.0 |
| fc#1525 Login error message | closed-completed 2024-01-11 → `showErrorMessage` 24.4 |
| vaadin-dialog#220 Dialog center | repo archived; moved to **wc#601**, open |
| wc#2446 Notification getText | moved to **fc#2088**, closed by fc#10271 (25.4) |
| wc#438 Notification close button | open (also fc#5531 open) |
| platform#2601 / fc#1681 item label generators (KDoc only) | closed-completed 2022 → 23.0 |
| fc#7682 FormLayout row break (KDoc only) | closed 2026-09-23 as obsolete (FormRow) |

## README gaps in scope

Missing bullets: `Upload.buttonCaption`, `AbstractLogin.i18n`, `FormLayout.addRowBreak()`, `Button.setDanger()`, `Button.addThemeVariantsCompat`, `Badge`/`BadgeVariant`, `VaadinIcon.iconName`, `IconName` (only implied).
