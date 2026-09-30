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

- `Q_router_mode`: Vaadin 25 has two client routers: the React-based one (the default when
  React is on the classpath) and the plain Flow one (`reactEnable=false`). Which one does the
  testapp run? Test both? Each has its own link interception, so each can differ.
- `Q_older_versions`: the testapp runs only on 25. Is 25 enough evidence for the ticket, or
  should we check 24 (and 14, where our extension also ships) with a throwaway testapp
  variant?
- `Q_top_parent`: `_top` / `_parent` only matter inside an iframe. Should we add a demo route
  that iframes the app, or skip them? The ticket title names `_top`.
- `Q_modifier_click`: Ctrl/middle-click on a target-less RouterLink should already open a
  new tab (the browser's default). Worth one line in the ticket as "why not just
  Ctrl-click": users don't know it, and the app can't force it.

If `_blank` turns out to be intercepted, the ticket changes from "add API" to "client bug".
Rewrite section 2 accordingly, and fix our README bullet, which would then be promising
something that doesn't work.

## 2. The ticket comment (draft; fill in after section 1)

> The use case: open a route in a new tab while keeping `RouterLink`'s typed route API. That
> means the route class + parameters, `setQueryParameters()` and `HighlightCondition`. With
> `Anchor` I have to build the URL by hand through `RouteConfiguration.getUrl()`, and the
> highlighting is gone.
>
> Proposal, the same shape `SideNavItem` already has:
>
> ```java
> public void setTarget(String target)   // null or "" removes the attribute
> public String getTarget()              // null when unset
> ```
>
> `AnchorTargetValue` can't appear in the signature. It lives in `flow-html-components`,
> which depends on `flow-server`, where `RouterLink` lives.
>
> Verified on Vaadin <version>, <router mode>: with `target="_blank"` set on the element, the
> client router leaves the click alone, and the route opens in a new tab with a fresh UI. So
> this needs server API only, no client change. Workaround until then:
> `routerLink.getElement().setAttribute("target", "_blank")`.

- `Q_open_in_new_tab`: should we also ask for `setOpenInNewBrowserTab(boolean)`?
  `SideNavItem` has it, which makes it defensible. `Anchor` doesn't, though, and a second
  method gives the Flow team one more thing to bikeshed. Lean: mention it as optional, as a
  single sentence.
- `Q_who_implements`: file only the comment, or also offer a PR? `upstream-vaadin-prs.md`
  says tickets are filed from here and PRs are made elsewhere.

## Graduation

After the comment is posted, delete this file.
- The ticket state goes to the flow#5791 row of `upstream-vaadin-prs.md`.
- A verified browser behaviour worth keeping, such as `_self` being intercepted or not, goes
  to the `RouterLink.target` doc comment.
