---
title: "CustomerSync-Refactoring-Kata"
author: "Gero Hillebrandt"
date: "2023-11-01"
header-includes:
  - "<link rel='stylesheet' type='text/css' href='eib.css' />"
---

## Overview

This PR adds bonus points sync logic, introduces refactoring in the customer data handling and synchronization logic and adds some dev tooling.

<div style="text-align: center;">
[Link to repository](https://github.com/ghillb/CustomerSync-Refactoring-Kata)

[Link to pull request](https://github.com/ghillb/CustomerSync-Refactoring-Kata/pull/1)
</div>


---

## Key Changes
- **New Logic: Bonus Points synchronization**
   - Add logic to synchronize the additional field `bonusPoints` from the ExternalCustomer to the Customer.

## Refactoring
- **New Class: `CustomerBusinessLogic`**
   - Centralizes business logic related to customer processing.

- **New Class: `CustomerMatchMaker`**
   - Dedicated to matching external customer data with existing records.

- **Modified `CustomerDataAccess` Class**
   - Enhanced to include new methods for updating duplicate customer records.

- **Refactored `CustomerSync` Class**
   - Streamlined by offloading responsibilities to `CustomerBusinessLogic`.

## Dev Tooling
- **New `.pre-commit-config.yaml`**
   - Integrates pre-commit hook to automate unit test run before commits are made.

- **New GitHub Action Workflow for CI**
   - Automatically runs tests on push and pull request events.

---

## Rationale

- **Improved Code Organization:** The addition and modification of classes aims to segregate data access, business logic, and synchronization tasks, promoting cleaner code and easier maintenance.
