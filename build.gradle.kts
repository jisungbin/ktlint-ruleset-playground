plugins {
  kotlin("jvm") version "2.1.0" apply false
}

allprojects {
  repositories {
    mavenCentral()
    gradlePluginPortal()
    google()
  }
}