# java-sandbox
Java Sandbox for R&amp;D


# Notes for further development
As described in https://docs.spring.io/initializr/docs/current-SNAPSHOT/reference/html/#create-instance-advanced-config-custom-project-request there is a possibility to customize spring initializr.

In com.intellias.devsandbox.playground package initial attempt for this is made. 
Via config([Config.java](src%2Fmain%2Fjava%2Fcom%2Fintellias%2Fdevsandbox%2Fplayground%2FConfig.java)) we define custom controller bean([CustomProjectGenerationController.java](src%2Fmain%2Fjava%2Fcom%2Fintellias%2Fdevsandbox%2Fplayground%2FCustomProjectGenerationController.java)), that replaces default one. For further customization in project generation flow [CustomProjectGenerationInvoker.java](src%2Fmain%2Fjava%2Fcom%2Fintellias%2Fdevsandbox%2Fplayground%2FCustomProjectGenerationInvoker.java) was introduced. Right now there is no implementation, but it can be used for invoking custom ProjectAssetGenerator for example.

Abstractions that can help further customization : 
* io.spring.initializr.generator.project.contributor.ProjectContributor