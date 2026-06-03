# ADR-0001: Three-folder architecture (`ui/`, `api/`, `db/`)

## Status

Superseded by [ADR-0002](./0002-two-folder-ui-feature.md) — 2026-06-03

## Context

Ganambro is starting as a fresh Jetpack Compose + Room scaffold. Without an explicit module boundary plan, entities, DAOs, migrations, and UI code tend to scatter across flat packages or become tightly coupled.

We need a separation that gives each concern a clear home, makes dependency direction obvious, and keeps the codebase navigable for both humans and AI.

## Decision

Code is organized into **three top-level packages** under `com.example.ganambro`:

| Package | Responsibility |
|---------|----------------|
| `ui/`   | ViewModels and Jetpack Compose components (screens, shared widgets, theme). |
| `api/`  | Contracts: Room `@Entity` data classes, Room `@Dao` interfaces, and network routing interfaces. |
| `db/`   | Database schema evolution: `@Database` class and `Migration` objects. |

## Rules

1. `ui/` depends on `api/` (via DAOs and entities) — **never** on `db/`.
2. `db/` imports entity types from `api/` — it does not define entities.
3. `api/` has zero dependency on `ui/` or `db/`. It is pure contract.
4. Migrations are tested independently with Room's migration test helpers.

## Rationale

- **Locality**: a schema change touches only `db/migrations/` and the entity in `api/entity/`. No UI code is involved.
- **Leverage**: the `api/` package is a small interface surface — a handful of DAOs and entities — behind which Room generates the persistence implementation and Retrofit generates the networking implementation.
- **Testability**: DAOs can be tested with an in-memory Room database without any Compose test rule. ViewModels can be unit-tested with fake DAOs.
- **AI navigability**: three folders with non-overlapping responsibilities make exploration trivial — an AI agent can determine where to look from the problem description alone.

## Consequences

- Adding a new feature requires touching at least `ui/` and `api/`. This is intentional.
- Cross-cutting types (e.g., shared value objects used by multiple entities) must live in `api/` — they are part of the contract surface.
- If the project grows to need a domain-logic layer between UI and persistence, it would be introduced as a new top-level package (e.g., `domain/`) rather than folded into existing folders.
