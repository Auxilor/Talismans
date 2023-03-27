group = "com.willfp"
version = rootProject.version

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.19.3-R0.1-SNAPSHOT")
    compileOnly("com.github.ben-manes.caffeine:caffeine:3.0.5")
}

publishing {
    publications {
        register("maven", MavenPublication::class) {
            from(components["java"])
        }
    }
}

tasks {
    build {
        dependsOn(publishToMavenLocal)
    }
}
