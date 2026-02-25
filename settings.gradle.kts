pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }

        mavenCentral()
        gradlePluginPortal()
        mavenCentral()
     //   maven { url = uri("https://jcenter.bintray.com" )  }
       // maven { url = uri("https://jitpack.io" )  }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        //maven { url = uri(" https://jcenter.bintray.com" )  }
        maven { url = uri(" https://jitpack.io" )  }
        //jcenter()
    }
}

rootProject.name = "SHSinfo2025"
include(":app")
