@file:Suppress("PropertyName", "FUNCTION_CALL_EXPECTED")

plugins {
    java

    alias(libs.plugins.quilt.loom)
}

val archives_base_name: String by project
base.archivesName.set(archives_base_name)

repositories {}

loom {
    mods {
        create("touchup") {
            sourceSet("main")
        }
    }
}

dependencies {
    minecraft(libs.minecraft)
    mappings(variantOf(libs.quilt.mappings) {
        classifier("intermediary-v2")
    })
    modImplementation(libs.quilt.loader)

    modImplementation(libs.quilted.fabric.api)
}

tasks {
    withType<JavaCompile>.configureEach {
        options.encoding = "UTF-8"
        options.release.set(17)
    }

    processResources {
        filteringCharset = "UTF-8"
        inputs.property("version", project.version)

        exclude("**/*.ase")

        filesMatching("quilt.mod.json") {
            expand("version" to project.version)
        }
    }

    wrapper {
        distributionType = Wrapper.DistributionType.BIN
    }

    jar {
        from("LICENSE.md") {
            rename { "LICENSE_$archives_base_name.md" }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17

    withSourcesJar()
}