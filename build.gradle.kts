plugins {
    java
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.0.13"
    id("org.beryx.jlink") version "3.0.1"
}

group = "com.example"
version = "1.0.0"

repositories {
    mavenCentral()
}

val junitVersion = "5.10.2"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

application {
    mainModule.set("com.example.desktopproj")
    mainClass.set("com.example.desktopproj.Application")
}

javafx {
    version = "21-ea+24"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
    implementation("org.xerial:sqlite-jdbc:3.49.1.0")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

jlink {
    imageZip.set(layout.buildDirectory.file("/distributions/app-${javafx.platform.classifier}.zip"))
    options.set(listOf("--strip-debug", "--no-header-files", "--no-man-pages"))
    launcher {
        name = "Finance"
    }
    jpackage {
        if (org.gradle.internal.os.OperatingSystem.current().isWindows) {
            imageOptions = listOf(
                "--icon",
                "${project.projectDir}/src/main/resources/com/example/desktopproj/icon/img.ico",
            )

            installerOptions = listOf(
                "--win-per-user-install",
                "--win-dir-chooser",
                "--win-menu",
                "--win-shortcut",
            )
        } else if (org.gradle.internal.os.OperatingSystem.current().isMacOsX) {
            imageOptions = listOf(
                "--icon",
                "${project.projectDir}/src/main/resources/com/example/desktopproj/icon/img.icns",
                "--mac-package-name",
                "Finance"
            )

            installerOptions = listOf(
                "--mac-package-identifier",
                "com.example.desktopproj",
                "--mac-package-signing-prefix",
                "com.example"
            )
        } else if (org.gradle.internal.os.OperatingSystem.current().isLinux) {
            imageOptions = listOf(
                "--icon",
                "${project.projectDir}/src/main/resources/com/example/desktopproj/icon/img.png"
            )

            installerOptions = listOf(
                "--linux-package-name",
                "finance",
                "--linux-shortcut",
                "--linux-menu-group",
                "Finance"
            )
        }

    }
}
