plugins {
  kotlin("jvm")
  id("com.vanniktech.maven.publish") version "0.32.0"
}

tasks.withType<Test> {
  useJUnitPlatform()
}

kotlin {
  jvmToolchain(21)
}

dependencies {
  implementation("com.pinterest.ktlint:ktlint-cli-ruleset-core:1.6.0")
  implementation("com.pinterest.ktlint:ktlint-rule-engine-core:1.6.0")

  testImplementation(kotlin("reflect"))
  testImplementation(kotlin("test-junit5"))
  testImplementation("com.pinterest.ktlint:ktlint-test:1.6.0")
  testRuntimeOnly("org.slf4j:slf4j-nop:2.0.17")
}
