# ADR-0002: Two-folder architecture (`ui/`, `feature/`)

## Status

Accepted — 2026-06-03

## Context

ADR-0001 established three folders: `ui/`, `api/`, `db/`. This assumed Room for local persistence, DAO interfaces, and database migrations.

During design grilling, we confirmed: the MVP stores zero data locally. Token generation and validation is pure computation (SHA-256 over salt + timestamp). The `api/` and `db/` folders would be empty for the entire MVP — dead weight.

## Decision

Replace the three-folder structure with two:

| Package | Responsibility |
|---------|----------------|
| `ui/`   | ViewModels and Jetpack Compose components. Android-aware. |
| `feature/` | Pure Kotlin domain logic. Zero `android.*` imports. |

`feature/` is **pure Kotlin** — testable on JVM without Robolectric or Android test runner. `ui/` depends on `feature/`; the reverse is never true.

## Rationale

- **Folder == lived-in.** Empty folders mislead AI navigators and new contributors. An `api/` with no DAOs and a `db/` with no migrations signal "something should be here" when nothing should.
- **Testability.** `feature/token/TokenGenerator` can be unit-tested with `kotlin.test` in milliseconds — no Android device, no emulator, no Robolectric.
- **Locality.** Token logic lives in exactly one place: `feature/token/`. A future token algorithm change touches that folder and nothing else.
- **Room for growth.** When the app later needs persistence, we'll add `feature/data/` or a new top-level `db/` folder — but only when the code justifies it.

## Consequences

- ADR-0001 is superseded.
- `feature/` modules must have zero imports from `android.*`. Platform dependencies are injected by `ui/` callers as plain Kotlin lambdas or interfaces.
- If Room becomes needed in the future, a new ADR will decide where entities and DAOs live — potentially a third folder, or a sub-package inside `feature/`.
