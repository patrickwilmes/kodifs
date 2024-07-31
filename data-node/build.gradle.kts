plugins {
    kotlin("jvm") version "2.0.0"
    id(Plugins.KtorPlugin) version Plugins.KtorPluginVersion
    id(Plugins.KotlinxSerialization) version Plugins.KotlinxSerializationVersion
}

group = "com.bitlake"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("io.ktor.server.netty.EngineMain")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(Dependencies.KtorContentNegotiation)
    implementation(Dependencies.KtorCore)
    implementation(Dependencies.KtorKotlinxJson)
    implementation(Dependencies.KtorCors)
    implementation(Dependencies.KtorNetty)
    implementation(Dependencies.LogBack)
    implementation(Dependencies.KtorServerConfig)
    implementation(Dependencies.Coroutines)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}
