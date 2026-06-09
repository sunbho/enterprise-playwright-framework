# Framework Architecture

## Architectural Goal

The framework is intended to provide a maintainable automation platform for browser and API testing. It should keep tests business-readable while isolating Playwright lifecycle, configuration, diagnostics, and reporting concerns behind stable framework APIs.

## Current Baseline

- Maven single-module project
- Java 22 configured in `pom.xml`
- Empty production and test package structure
- No Playwright dependency or implementation classes
- Placeholder JUnit 4.11 dependency
- No resources, configuration files, CI workflow, or executable tests

## Folder Tree

```text
src/
|-- main/
|   `-- java/com/playwright/framework/
|       |-- config/
|       |-- factory/
|       |-- pages/
|       |-- components/
|       |-- utils/
|       |-- listeners/
|       |-- reporting/
|       |-- constants/
|       |-- models/
|       |-- api/
|       |-- exceptions/
|       `-- annotations/
`-- test/
    `-- java/com/playwright/framework/
        |-- tests/
        |-- data/
        `-- runners/
```

## Package Design

### Core Infrastructure

| Package | Ownership |
|---|---|
| `config` | Reads system properties, environment variables, and configuration files into validated models. It must not depend on test classes. |
| `factory` | Owns Playwright resource creation and cleanup. It should make parallel execution explicit and prevent shared mutable browser state. |
| `constants` | Holds genuinely stable values. Runtime configuration must remain in `config`, not be disguised as constants. |
| `exceptions` | Defines meaningful framework errors for configuration, browser startup, page interaction, API, and reporting failures. |
| `annotations` | Provides optional declarative metadata consumed by listeners or runners. |

### Interaction Layer

| Package | Ownership |
|---|---|
| `pages` | Models full application pages and exposes business-level operations. Locators remain private implementation details. |
| `components` | Models reusable portions of pages. Components may be composed by pages but should not own browser lifecycle. |
| `api` | Encapsulates HTTP clients, authentication, endpoint operations, serialization, and response validation. |

### Supporting Services

| Package | Ownership |
|---|---|
| `models` | Contains immutable data contracts shared by configuration, pages, API clients, and tests. |
| `listeners` | Observes test lifecycle events and delegates artifact or report work to dedicated services. |
| `reporting` | Translates framework events and artifacts into the selected reporting system. |
| `utils` | Contains focused, stateless helpers. It must not become a catch-all for business logic or lifecycle management. |

### Test Layer

| Package | Ownership |
|---|---|
| `tests` | Contains scenario orchestration and assertions. Tests should consume page, component, API, and data abstractions. |
| `data` | Supplies test fixtures, builders, parameter sources, and test-only datasets. |
| `runners` | Defines execution suites, tags, profiles, or integration points required by the chosen test engine. |

## Intended Dependency Direction

```text
tests/runners
      |
      v
pages/components/api <--- data
      |
      v
factory/config/models
      |
      v
Playwright and external libraries

listeners ---> reporting
exceptions/constants/annotations ---> shared where appropriate
utils ---> leaf-level helpers only
```

Dependencies should point toward stable infrastructure. Production packages must never depend on `src/test` packages. Page Objects should not invoke test runner APIs, and tests should not directly manage Playwright resources.

## Lifecycle Model

The future lifecycle should be deterministic:

1. Load and validate execution configuration.
2. Create one Playwright instance per suitable execution scope.
3. Launch or connect to the configured browser.
4. Create isolated browser contexts for tests.
5. create pages and inject them into Page Objects.
6. Capture configured artifacts on relevant lifecycle events.
7. Close pages, contexts, browsers, and Playwright resources in reverse order.

Parallel execution must use isolated state, preferably through per-thread or per-test context holders rather than static shared `Page` instances.

## Design Principles

- Prefer composition over deep Page Object inheritance.
- Expose business actions instead of low-level click and fill wrappers.
- Use Playwright's locator and auto-waiting model; avoid unconditional sleeps.
- Keep assertions in tests or explicit assertion helpers unless an action requires an invariant.
- Make configuration immutable after startup.
- Separate artifact capture from report rendering.
- Produce actionable errors with environment and locator context while excluding secrets.
- Keep credentials outside source control.
- Introduce abstractions only where they remove duplication or enforce lifecycle rules.

## Future Runtime Structure

Implementation will likely add resource directories without changing the package ownership above:

```text
src/
|-- main/resources/
|   |-- config/
|   `-- logback.xml
`-- test/resources/
    |-- test-data/
    `-- suites/
```

Exact libraries and file formats should be selected during the foundation phase and recorded as architecture decisions.

