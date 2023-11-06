dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.16.0-rc1")
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.0-rc1")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.0-rc1")
    implementation("com.fasterxml.jackson.core:jackson-annotations:2.16.0-rc1")
}

tasks.register("prepareKotlinBuildScriptModel") {}
