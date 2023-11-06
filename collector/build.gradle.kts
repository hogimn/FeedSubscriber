dependencies {
    implementation(project(":common"))
    implementation(project(":database"))
    implementation("org.springframework.boot:spring-boot-starter-quartz")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
    implementation("org.springframework.cloud:spring-cloud-starter-netflix-eureka-client")
}

tasks.register("prepareKotlinBuildScriptModel") {}