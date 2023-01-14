import java.nio.charset.StandardCharsets

@Suppress("DSL_SCOPE_VIOLATION", "ForbiddenComment")
// TODO: Remove once https://github.com/gradle/gradle/issues/22797 is fixed
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.kapt)
    alias(libs.plugins.versions)
    alias(libs.plugins.version.catalog.update)
    alias(libs.plugins.detekt)
    alias(libs.plugins.spotless)
}

group = "net.yewton"
version = "1.0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.picocli.core)
    implementation(libs.jsqlparser)
    implementation(libs.kotlinx.coroutines.core.jvm)
    implementation(libs.hoplite.core)
    implementation(libs.hoplite.yaml)
    kapt(libs.picocli.codegen)
    detektPlugins(libs.detekt.formatting)
}

kapt {
    arguments {
        arg("project", "${project.group}/${project.name}")
    }
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(17)
}

tasks.dependencyUpdates {
    checkConstraints = true
    checkForGradleUpdate = true
    resolutionStrategy {
        componentSelection {
            all {
                val rejected = listOf("alpha", "beta", "rc", "cr", "m", "preview", "b", "ea")
                    .map { qualifier -> Regex("(?i).*[.-]$qualifier[.\\d-+]*") }
                    .any { it.matches(candidate.version) }
                if (rejected) {
                    reject("Release candidate")
                }
            }
        }
    }
}

spotless {
    format("text") {
        target(
            ".gitignore",
            ".gitattributes",
            "**/*.toml",
            "**/*.properties",
            "**/*.yml"
        )
        trimTrailingWhitespace()
        endWithNewline()
        encoding(StandardCharsets.UTF_8.name())
    }
}

detekt {
    config = files("config/detekt/detekt.yml")
    basePath = rootProject.projectDir.absolutePath
    buildUponDefaultConfig = true
    source = files(rootDir)
}
