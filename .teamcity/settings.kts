import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.XmlReport
import jetbrains.buildServer.configs.kotlin.buildFeatures.xmlReport
import jetbrains.buildServer.configs.kotlin.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.buildSteps.script
import jetbrains.buildServer.configs.kotlin.triggers.schedule
import jetbrains.buildServer.configs.kotlin.triggers.vcs

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2022.10"

project {

    buildType(Blahblah)
    buildType(Build)
}

object Blahblah : BuildType({
    name = "Blahblah"

    vcs {
        root(DslContext.settingsRoot)
    }

    dependencies {
        snapshot(Build) {
        }
    }
})

object Build : BuildType({
    name = "Build"

    artifactRules = "target/*.jar"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            enabled = false
            goals = "clean package"
            dockerImage = "maven:latest"
        }
        script {
            name = "qqq"
            scriptContent = "docker run --rm -v ${'$'}PWD:/workdir jetbrains/intellij-http-client -D run.http --report"
        }
    }

    triggers {
        vcs {
        }
        schedule {
            schedulingPolicy = weekly {
            }
            triggerBuild = always()
        }
    }

    features {
        xmlReport {
            reportType = XmlReport.XmlReportType.SUREFIRE
            rules = "reports/**/*.xml"
        }
    }
})
