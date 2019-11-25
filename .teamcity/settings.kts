import jetbrains.buildServer.configs.kotlin.v2018_2.*
import jetbrains.buildServer.configs.kotlin.v2018_2.buildFeatures.investigationsAutoAssigner
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.maven
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.placeholder
import jetbrains.buildServer.configs.kotlin.v2018_2.buildSteps.script
import jetbrains.buildServer.configs.kotlin.v2018_2.triggers.vcs
import jetbrains.buildServer.configs.kotlin.v2018_2.vcs.GitVcsRoot

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

version = "2019.1"

project {
    defaultTemplate = RelativeId("MyTemplate")

    vcsRoot(HttpsGithubComAntonarhipovDemoLibRefsHeadsMaster)
    vcsRoot(HttpsGithubComAntonarhipovDemoTestsGroupBRefsHeadsMaster)
    vcsRoot(HttpsGithubComAntonarhipovDemoTestsGroupARefsHeadsMaster)

    buildType(Application)
    buildType(Library)
    buildType(Report)
    buildType(Test1)
    buildType(Test2)

    template(MyTemplate)
}

object Application : BuildType({
    name = "Application"

    vcs {
        root(DslContext.settingsRoot)
    }

    steps {
        maven {
            id = "RUNNER_32"
            goals = "clean package"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }

    features {
        investigationsAutoAssigner {
            id = "InvestigationsAutoAssigner"
            defaultAssignee = "admin"
        }
    }

    dependencies {
        dependency(Library) {
            snapshot {
            }

            artifacts {
                id = "ARTIFACT_DEPENDENCY_4"
                artifactRules = "demo-lib-1.0-SNAPSHOT.jar"
            }
        }
    }
})

object Library : BuildType({
    name = "Library"

    artifactRules = "target/demo-*.jar"

    vcs {
        root(HttpsGithubComAntonarhipovDemoLibRefsHeadsMaster)
    }

    steps {
        maven {
            id = "RUNNER_30"
            goals = "clean package"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
            coverageEngine = jacoco {
                classLocations = "+:target/classes"
                excludeClasses = "+:org.demo.lib.*"
            }
        }
        maven {
            id = "RUNNER_31"
            enabled = false
            goals = "deploy"
        }
    }
})

object Report : BuildType({
    name = "Report"

    type = BuildTypeSettings.Type.COMPOSITE

    vcs {
        showDependenciesChanges = true
    }

    triggers {
        vcs {
            id = "vcsTrigger"
            branchFilter = ""
            watchChangesInDependencies = true
        }
    }

    dependencies {
        snapshot(Test1) {
        }
        snapshot(Test2) {
        }
    }
})

object Test1 : BuildType({
    name = "Test1"

    vcs {
        root(HttpsGithubComAntonarhipovDemoTestsGroupARefsHeadsMaster)
    }

    steps {
        maven {
            id = "RUNNER_33"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }

    dependencies {
        snapshot(Application) {
        }
    }
})

object Test2 : BuildType({
    name = "Test2"

    vcs {
        root(HttpsGithubComAntonarhipovDemoTestsGroupBRefsHeadsMaster)
    }

    steps {
        maven {
            id = "RUNNER_34"
            goals = "clean test"
            runnerArgs = "-Dmaven.test.failure.ignore=true"
        }
    }

    dependencies {
        snapshot(Application) {
        }
    }
})

object MyTemplate : Template({
    name = "MyTemplate"

    steps {
        script {
            id = "RUNNER_35"
            scriptContent = """echo "Hello""""
        }
        placeholder {
            id = "RUNNER_37"
        }
        script {
            id = "RUNNER_36"
            scriptContent = """echo "Bye""""
        }
    }
})

object HttpsGithubComAntonarhipovDemoLibRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/antonarhipov/demo-lib#refs/heads/master"
    url = "https://github.com/antonarhipov/demo-lib"
    authMethod = password {
        userName = "antonarhipov"
        password = "credentialsJSON:16a0cdd2-1097-4165-a711-4865b87cfa7c"
    }
})

object HttpsGithubComAntonarhipovDemoTestsGroupARefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/antonarhipov/demo-tests-group-a#refs/heads/master"
    url = "https://github.com/antonarhipov/demo-tests-group-a"
    authMethod = password {
        userName = "antonarhipov"
        password = "credentialsJSON:16a0cdd2-1097-4165-a711-4865b87cfa7c"
    }
})

object HttpsGithubComAntonarhipovDemoTestsGroupBRefsHeadsMaster : GitVcsRoot({
    name = "https://github.com/antonarhipov/demo-tests-group-b#refs/heads/master"
    url = "https://github.com/antonarhipov/demo-tests-group-b"
    authMethod = password {
        userName = "antonarhipov"
        password = "credentialsJSON:16a0cdd2-1097-4165-a711-4865b87cfa7c"
    }
})
