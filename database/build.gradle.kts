dependencies {
    implementation(project(":common"))
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
}

tasks.register("prepareKotlinBuildScriptModel") {}

tasks.named("bootJar") {
    enabled = false
}