plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "kodifs"
include(":client")
include(":data-node")
include(":main-node")
include(":commons")
