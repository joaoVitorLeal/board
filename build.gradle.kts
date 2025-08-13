plugins {
    id("java")
}

group = "br.com.dio"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.liquibase:liquibase-core:4.33.0")
    implementation("com.mysql:mysql-connector-j:9.4.0")
//    implementation("mysql:mysql-connector-java:8.0.33") // LEGACY
    implementation("org.projectlombok:lombok:1.18.34")
    implementation("org.apache.pdfbox:pdfbox:3.0.5")

    annotationProcessor("org.projectlombok:lombok:1.18.34")
}

tasks.test {
    useJUnitPlatform()
}
