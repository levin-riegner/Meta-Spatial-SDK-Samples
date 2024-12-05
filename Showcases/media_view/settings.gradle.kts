// (c) Meta Platforms, Inc. and affiliates. Confidential and proprietary.

pluginManagement {
  // Retrieve Meta Spatial SDK Version from "gradle.properties"
  val metaSpatialSdkVersion: String by settings

  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
    // Libs from QA channel - intended only for release candidates
    maven {
      url = uri("https://s01.oss.sonatype.org/content/repositories/commeta-1077")
    }
    // Libs from QA channel - intended only for release candidates
    maven {
      url = uri("https://s01.oss.sonatype.org/content/repositories/commeta-1079")
    }
  }
  plugins { id("com.meta.spatial.plugin") version metaSpatialSdkVersion }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

  repositories {
    google()
    mavenCentral()
    // Libs from QA channel - intended only for release candidates
    maven {
      url = uri("https://s01.oss.sonatype.org/content/repositories/commeta-1077")
    }
    // Libs from QA channel - intended only for release candidates
    maven {
      url = uri("https://s01.oss.sonatype.org/content/repositories/commeta-1079")
    }
  }
}

rootProject.name = "Media View"

include(":app")
