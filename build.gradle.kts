// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.devtools.ksp) apply false
    alias(libs.plugins.ktlint)
    alias(libs.plugins.kover)
}

subprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

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
