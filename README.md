# Enterprise Playwright Java Automation Framework

An enterprise-style UI automation framework built with **Playwright Java**, **Java 22**, **TestNG**, and **Maven**. This project is part of my hands-on learning journey to explore modern automation framework design using industry best practices.

The framework is being developed incrementally, with each milestone focusing on a specific enterprise automation capability.

## Tech Stack

* Java 22
* Playwright Java
* TestNG
* Maven
* Jackson
* Log4j2
* Allure Report

## Current Features

* Configuration-driven framework
* Page Object Model (POM)
* Reusable BasePage
* Playwright Inspector support
* Logging with Log4j2
* Allure reporting
* Screenshot capture on failures
* TestNG listeners
* JSON data-driven testing
* POJO-based test data models
* Generic JSON reader
* TestNG DataProviders
* Thread-safe parallel execution using ThreadLocal

## Project Structure

```text
src/main/java
├── config
├── factory
├── listeners
├── models
├── pages
├── reporting
└── utils

src/test/java
├── dataproviders
├── tests
└── resources
```

## Learning Roadmap

Completed

* Framework Foundation
* Configuration Management
* Page Object Model
* Reusable Framework Layer
* Logging & Reporting
* JSON Data-Driven Testing
* Parallel Execution

Planned

* Environment Management
* API Automation
* Component Object Pattern
* GitHub Actions CI/CD
* Docker Execution
* Playwright Trace Viewer

## Running the Tests

```bash
mvn clean test
```

Run using a specific browser:

```bash
mvn test -Dbrowser=firefox
```

Run in headless mode:

```bash
mvn test -Dheadless=true
```

## Why This Project?

The goal of this project is to build and understand an enterprise-grade Playwright automation framework from scratch rather than relying on templates. Each feature is implemented step by step to gain practical experience with framework architecture, maintainability, scalability, and modern automation practices.

Feedback and suggestions are always welcome.
