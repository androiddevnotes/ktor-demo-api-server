plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    alias(libs.plugins.dokka)
    alias(libs.plugins.kotlinx.serialization)
}

group = "com.example"
version = "0.0.1"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

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
}

tasks.dokkaHtml {
    outputDirectory.set(layout.buildDirectory.dir("documentation/html"))
}
