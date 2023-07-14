plugins {
    id("java")
    id("io.ktor.plugin") version "2.3.2"
}

group = "kr.wiselight"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("kr.wiselight.i18n.Main")
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
