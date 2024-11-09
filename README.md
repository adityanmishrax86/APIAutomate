# REST Assured Test Automation Framework

## Overview
This framework provides a robust solution for API testing using REST Assured and Allure Reports.

## Features
- Maven-based project structure
- Allure reporting integration
- Multiple environment support
- Flexible authentication methods
- SOLID principles implementation
- Detailed logging and reporting

## Setup Instructions
1. Clone the repository
2. Install dependencies: `mvn clean install`
3. Run tests: `mvn clean test`
4. Generate Allure report: `mvn allure:serve`

## Configuration
- Update `src/test/resources/config.properties` with your environment settings
- Set environment using `-Denv=qa` parameter

## Writing Tests
1. Create a new test class extending TestBase
2. Use ApiClient for making API calls
3. Add Allure annotations for better reporting

## CI/CD Integration
Add the following to your pipeline:
```yaml
steps:
  - name: Run Tests
    run: mvn clean test
  - name: Generate Report
    run: mvn allure:report
  - name: Archive Results
    uses: actions/upload-artifact@v2
    with:
      name: allure-results
      path: target/allure-results
```