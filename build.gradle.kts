// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.ktlint)
    jacoco
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")
    apply(plugin = "jacoco")

    ktlint {
        version.set("1.0.1")
        verbose.set(true)
        outputToConsole.set(true)
        ignoreFailures.set(false)

        filter {
            exclude("**/generated/**")
            exclude("**/build/**")
        }
    }

    // Disable function-naming rule for Composables
    configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
        additionalEditorconfig.set(
            mapOf(
                "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
            ),
        )
    }

    jacoco {
        toolVersion = "0.8.12"
    }

    tasks.withType<Test> {
        finalizedBy(tasks.withType<JacocoReport>())
    }

    tasks.withType<JacocoReport> {
        reports {
            xml.required.set(true)
            html.required.set(true)
        }
    }
}

tasks.register("testAll") {
    group = "verification"
    description = "Run tests in all modules"
    dependsOn(
        subprojects.mapNotNull {
            it.tasks.findByName("test")
        },
    )
}

tasks.register("jacocoTestReportAll") {
    group = "verification"
    description = "Generate JaCoCo reports for all modules"
    dependsOn(
        subprojects.mapNotNull {
            it.tasks.findByName("jacocoTestReport")
        },
    )
}
