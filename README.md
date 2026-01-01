# 🐾 API Automation Framework (BDD Approach)

**Status:** Work in Progress 🚧

This repository showcases an **API Test Automation Framework** built using **Cucumber** and **TestNG**, following a **Behavior-Driven Development (BDD)** methodology.  
The framework is designed to validate REST services exposed by the [Swagger Petstore](https://petstore.swagger.io/), with a focus on **Add Pet** and **Get Pet by ID** operations.

---
## 🎯 Objectives

- Provide a scalable and maintainable automation framework for API testing.
- Enable cross-environment execution (SIT / UAT / DEV) with minimal configuration changes.
- Deliver clear, actionable test reports for stakeholders.
- Integrate with CI/CD pipelines (Jenkins/GitHub Actions).

---
## 🚀 Services Covered

- **Add Pet**
  - Adds new pet details to the store.
  - Accepts XML/JSON payloads.
  - Returns `200 OK` with a generated `PetID` when successful.

- **Get Pet by ID**
  - Fetches details of a pet using its `PetID`.
  - Returns `200 OK` if found, or `404 Not Found` otherwise.

---

## 🛠️ Tech Stack

- **Frameworks:** Cucumber, TestNG  
- **Build Tool:** Maven  
- **Language:** Java  
- **Service Invocation:** RestAssured → for making and validating HTTP requests
- **Database:** H2 (for persisting test data)  
- **Utilities & Libraries:**
  - Apache POI → Excel data management
  - Apache Velocity → Dynamic payload generation
  - Jackson → JSON serialization/deserialization
  - Date/Time libraries → Data enrichment
  - Custom helper utilities → DB operations, formatting, etc.

---
## ⚙️ Configurations

- Environment endpoints are set dynamically via the system property flag `env`.  
- This allows the same test scripts to run seamlessly across **SIT**, **UAT**, and **DEV** environments.

---

## 📊 Test Data Strategy

- Test data is maintained in **Excel sheets**, mapped to unique test case IDs referenced in feature files.  
- Payloads (XML/JSON) are generated dynamically using **Apache Velocity templates**.  
- Certain runtime data is persisted in **H2 DB tables** to be reused in dependent test scenarios.

---
## 🧩 Features & Implementation

- **Feature Files:** Define test scenarios in plain English (BDD style).  
- **Step Definitions:** Implement reusable automation logic.  
- **Utilities:** Handle data formatting, enrichment, and DB operations.  
- **Assertions:** Validate API responses against expected outcomes.

---
## 📑 Reporting

- **Built-in Cucumber Reports** (HTML/JSON).  
- **Maven Cucumber Reporting Plugin** → Generates enhanced, visually appealing HTML reports for stakeholders.
---

## 🚧 Roadmap / Work In Progress

- Extend coverage to additional Petstore services (Update/Delete Pet).  
- Integrate with CI/CD pipelines (Jenkins/GitHub Actions).  
- Add support for parallel test execution.
