# Runbook

This document contains the common tips and guidelines for users that may be interested in
the application development acceleration through the `Java Sandbox` project utilizing. 

For the project description, see the [README.md](README.md).

## Building an Application with Java Sandbox Project

### Application Initialization

To start from scratch, follow these steps:

-  clone the `Java Sandbox` project from the [public GitHub repository](https://github.com/IntelliasLabs/java-sandbox):
   ```
   git clone https://github.com/IntelliasLabs/java-sandbox.git
   ```

-  create a new development branch and checkout on it:
   ```
   git checkout -b YOUR_DEV_BRANCH_NAME
   ```

-  rename the following properties in the [pom.xml](pom.xml):
   - `groupId`
   - `artifactId`
   - `name`
   - `description`

-  rename the project packages to align them with the [pom.xml](pom.xml) properties that were changed on the previous step.

-  start implementing your business logic.