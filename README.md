# Playwright Java Automation Framework

Enterprise-oriented browser and API automation framework scaffold built with Maven and organized around Playwright for Java.

## Current Status

The repository currently contains the intended package structure only. Framework classes, Playwright dependencies, test runners, configuration resources, and executable tests have not yet been implemented.

The existing `pom.xml` defines Java 22 and contains the Maven archetype's JUnit 4.11 dependency and older plugin-management defaults. These are placeholders to be modernized during implementation.

## Project Structure

```text
playwright-framework/
|-- pom.xml
|-- README.md
|-- Architecture.md
|-- Roadmap.md
`-- src/
    |-- main/
    |   `-- java/com/playwright/framework/
    |       |-- annotations/
    |       |-- api/
    |       |-- components/
    |       |-- config/
    |       |-- constants/
    |       |-- exceptions/
    |       |-- factory/
    |       |-- listeners/
    |       |-- models/
    |       |-- pages/
    |       |-- reporting/
    |       `-- utils/
    `-- test/
        `-- java/com/playwright/framework/
            |-- data/
            |-- runners/
            `-- tests/
```

Generated Maven output under `target/` is intentionally excluded from the source tree.

## Package Responsibilities

| Package | Purpose |
|---|---|
| `annotations` | Framework-specific metadata for tests, features, ownership, retries, or reporting. |
| `api` | API clients, request builders, response validation, and API test support. |
| `components` | Reusable UI fragments such as navigation bars, dialogs, tables, and forms. |
| `config` | Typed configuration loading, environment selection, and validation. |
| `constants` | Stable framework constants and shared enumerations. |
| `exceptions` | Domain-specific exceptions with actionable failure context. |
| `factory` | Controlled creation and lifecycle management of Playwright, browser, context, and page objects. |
| `listeners` | Test lifecycle hooks for diagnostics, logging, artifacts, and reporting events. |
| `models` | Immutable configuration, test-data, API, and domain data objects. |
| `pages` | Page Objects that expose business actions and page-level assertions. |
| `reporting` | Report adapters and attachment handling for screenshots, traces, videos, and logs. |
| `utils` | Small, stateless utilities that do not belong to a domain package. |
| `data` | Test data providers, builders, fixtures, and test-only model factories. |
| `runners` | Suite definitions, tags, profiles, or other test execution entry points. |
| `tests` | Test scenarios organized by product feature or business capability. |

## Planned Capabilities

- Thread-safe Playwright and browser lifecycle management
- Environment-aware, typed configuration
- Page Object and reusable component layers
- Parallel and cross-browser execution
- UI and API test support
- Failure screenshots, traces, videos, and structured logs
- Extensible reporting and CI-friendly result publishing
- Test data builders and external data sources
- Tags, suites, retries, and execution profiles

See [Architecture.md](Architecture.md) for design constraints and [Roadmap.md](Roadmap.md) for the phased implementation plan.

