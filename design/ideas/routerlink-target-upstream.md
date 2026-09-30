# RouterLink target: verify in a browser, then comment on flow#5791

[flow#5791](https://github.com/vaadin/flow/issues/5791) asks for `_blank`/`_self`/`_top` on
`RouterLink`. It has been open since 2019. pleku asked what UX is wanted and suggested `Anchor`, and
nobody answered. We ship the gap as `RouterLink.target` / `setOpenInNewTab()`
(`karibu-tools/src/main/kotlin/RouterUtils.kt`). The plan: prove in a real browser that setting the
attribute is enough, then answer the ticket with the use case and an API shape.

## 1. Playwright check against the testapp

Run `SERVER_PORT=8765 ./gradlew :testapp:run` (8080 is taken on the owner's machine), then open
http://localhost:8765/. Each link goes to `/link-destination`, which prints the UI id it was
rendered in. The UI id tells in-place router navigation from a page load:

| Link (id)                             | Expected                                                        |
|---------------------------------------|-----------------------------------------------------------------|
| No target (`noTarget`)                | same tab, **same** UI id: the router navigated in place         |
| `target=_self` (`self`)               | same as above; if the UI id changes, the router refuses `_self` |
| `setOpenInNewTab()` (`newTab`)        | new tab, **new** UI id; original tab unchanged                  |
| `Anchor.setOpenInNewTab()` (`anchorNewTab`) | baseline for the row above: must behave identically       |

Also record, for the ticket: the click event isn't sent to the server as a router `link`
navigation for `_blank`. The server log or the dev tools network tab shows no navigation RPC
in the original tab.

**Result (2026-09-30, Vaadin 25.2.9, React router, headless Chromium via Playwright):** all four
rows as expected.

| Link           | Original tab after click                        | New tab                          |
|----------------|-------------------------------------------------|----------------------------------|
| `noTarget`     | `/link-destination`, UI id 1 → 1, one `ui-navigate` RPC with `trigger: "link"`, no document load | — |
| `self`         | same as `noTarget` (UI id 2 → 2): `_self` is routed in place | — |
| `newTab`       | stays on `/`, UI id 3, **no POST at all**       | `/link-destination`, UI id 4     |
| `anchorNewTab` | stays on `/`, UI id 5, no POST at all           | `/link-destination`, UI id 6     |

`RouterLink` and `Anchor` with `_blank` render the same `<a href="link-destination" target="_blank">`.
The source agrees: `Flow.tsx` `extractURL()` (flow-server 25.2) returns early on
`anchor.target && anchor.target.toLowerCase() !== '_self'`, and before that on a non-primary
button or any of Shift/Ctrl/Alt/Meta.

Decided with the owner:
- Router mode: the default (React) router is enough; the `reactEnable=false` one is not tested.
- Older versions: none. The PR targets Vaadin 25, so 25 is the evidence.
- `_top` / `_parent`: not tested. They only matter inside an iframe, and `extractURL()` skips
  them like `_blank`.
- Ctrl/middle-click: goes into the ticket as one line ("why not just Ctrl-click").

## 2. The ticket comment (draft)

> The use case: open a route in a new tab while keeping `RouterLink`'s typed route API. That
> means the route class + parameters, `setQueryParameters()` and `HighlightCondition`. With
> `Anchor` I have to build the URL by hand through `RouteConfiguration.getUrl()`, and the
> highlighting is gone. Ctrl/middle-click already opens a `RouterLink` in a new tab, but users
> don't know it, and the app can't ask for it.
>
> Proposal, the same shape `SideNavItem` already has:
>
> ```java
> public void setTarget(String target)   // null or "" removes the attribute
> public String getTarget()              // null when unset
> public void setOpenInNewBrowserTab(boolean openInNewBrowserTab)  // sets or removes "_blank"
> public boolean isOpenInNewBrowserTab()
> ```
>
> `AnchorTargetValue` can't appear in the signature. It lives in `flow-html-components`,
> which depends on `flow-server`, where `RouterLink` lives.
>
> Verified on Vaadin 25.2.9 with the React router: with `target="_blank"` set on the element, the
> client router leaves the click alone (`Flow.tsx` `extractURL()` skips any target other than
> `_self`), no RPC reaches the server, and the route opens in a new tab with a fresh UI. `_self`
> is still routed in place. So this needs server API only, no client change. Workaround until then:
> `routerLink.getElement().setAttribute("target", "_blank")`.
>
> If this shape is OK, I'll send a PR against 25.

- `setOpenInNewBrowserTab(boolean)`: asked for too. `SideNavItem` has the same pair next to
  `setTarget` (checked on 25.2), and that precedent outweighs `Anchor` not having it.
- Who implements: we do, as a PR against Vaadin 25. Per `upstream-vaadin-prs.md`, the PR is
  made elsewhere, so the comment can say a PR follows.

## Graduation

After the comment is posted, delete this file.
- The ticket state goes to the flow#5791 row of `upstream-vaadin-prs.md`.
- A verified browser behaviour worth keeping, such as `_self` being intercepted or not, goes
  to the `RouterLink.target` doc comment.
