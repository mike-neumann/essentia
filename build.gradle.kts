plugins {
    java
    `maven-publish`
}

allprojects {
    group = "me.xra1ny.essentia"
    version = "1.0"

    apply<JavaPlugin>()
    apply<MavenPublishPlugin>()

    repositories {
        mavenCentral()
    }

    dependencies {
        implementation("org.reflections:reflections:0.10.2")

        compileOnly("org.jetbrains:annotations:24.1.0")
        compileOnly("org.projectlombok:lombok:1.18.30")
        testCompileOnly("org.projectlombok:lombok:1.18.30")
        annotationProcessor("org.projectlombok:lombok:1.18.30")
        testAnnotationProcessor("org.projectlombok:lombok:1.18.30")
    }

    java {
        withSourcesJar()
        withJavadocJar()
    }

    publishing {
        publications {
            create<MavenPublication>("maven") {
                groupId = group.toString()
                artifactId = project.name
                version = version

                from(components["java"])
            }
        }
    }

    tasks.javadoc {
        (options as StandardJavadocDocletOptions)
            .tags(
                "apiNote:a:API Note:",
                "implSpec:a:Implementation Requirements:",
                "implNote:a:Implementation Note:"
            )
    }
}