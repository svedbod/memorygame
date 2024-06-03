plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.0" // Uppdatera till den senaste stabila versionen
    id("application")
}

application {
    mainClass.set(project.findProperty("mainClass")?.toString() ?: "se.mindlab.GameUI")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.google.guava:guava:30.1.1-jre")  // Exempel på ett beroende, byt ut eller lägg till beroende på ditt behov
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.1")
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.test {
    useJUnitPlatform()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = project.findProperty("mainClass")?.toString() ?: "se.mindlab.GameUI"
    }
}

tasks.register<Jar>("fatJar") {
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = project.findProperty("mainClass")?.toString() ?: "se.mindlab.GameUI"
    }
    from(sourceSets.main.get().output)

    dependsOn(configurations.runtimeClasspath)
    from({
        configurations.runtimeClasspath.get().filter { it.name.endsWith("jar") }.map { zipTree(it) }
    })
}
