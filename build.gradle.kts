plugins {
  alias(libs.plugins.kotlin.jvm)
  alias(libs.plugins.ktor)
  alias(libs.plugins.dokka)
  alias(libs.plugins.kotlinx.serialization)
  id("org.flywaydb.flyway") version "9.8.1" // Add this line
}

group = "com.example"
version = "0.0.1"

application {
  mainClass.set("com.example.ApplicationKt")

  val isDevelopment: Boolean = project.ext.has("development")
  applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(libs.bundles.ktor)
  implementation(libs.bundles.exposed)
  implementation(libs.bundles.logging)
  implementation(libs.bundles.serialization)
  implementation(libs.postgresql)
  implementation(libs.jbcrypt)
  testImplementation(libs.bundles.testing)
  dokkaHtmlPlugin(libs.dokka.html.plugin)
  implementation("io.github.cdimascio:dotenv-kotlin:6.4.1")
  implementation(libs.bundles.database)
  implementation("org.postgresql:postgresql:42.3.1")
  implementation("org.flywaydb:flyway-core:9.8.1")
}

tasks.dokkaHtml {
  outputDirectory.set(layout.buildDirectory.dir("documentation/html"))
}

tasks {
  create("stage").dependsOn("installDist")
}

flyway {
  url = System.getenv("DATABASE_URL") ?: "jdbc:postgresql://localhost:5432/quotes_app_db"
  user = System.getenv("DATABASE_USER") ?: "adn_user"
  password = System.getenv("DATABASE_PASSWORD") ?: "adn_password"
}
