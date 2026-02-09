plugins {
    id("fabric-loom") version "1.15-SNAPSHOT"
    id("maven-publish")
}

version = "${rootProject.property("mod_version").toString()}-mc${rootProject.property("minecraft_version").toString()}"
group = rootProject.property("maven_group").toString()

base.archivesName.set("${rootProject.property("archives_base_name").toString()}-fabric")

repositories {
    // Add repositories to retrieve artifacts from in here.
    // You should only use this when depending on other mods because
    // Loom adds the essential maven repositories to download Minecraft and libraries from automatically.
    // See https://docs.gradle.org/current/userguide/declaring_repositories.html
    // for more information about repositories.

    maven("https://pkgs.dev.azure.com/djtheredstoner/DevAuth/_packaging/public/maven/v1")
}

dependencies {
    // To change the versions, see the gradle.properties file
    minecraft("com.mojang:minecraft:${rootProject.property("minecraft_version")}")
    mappings("net.fabricmc:yarn:${rootProject.property("mappings_version")}:v2")
    modImplementation("net.fabricmc:fabric-loader:${rootProject.property("fabric_loader_version")}")

    // Fabric API. This is technically optional, but you probably want it anyway.
    modImplementation("net.fabricmc.fabric-api:fabric-api:${rootProject.property("fabric_api_version")}")

    // DevAuth. Uncomment this if you would like to be logged into your Microsoft account when running the game.
    // After uncommenting this and reloading Gradle, you'll have to add -Ddevauth.enabled=true to your JVM arguments
    // in order to enable DevAuth.
    //modRuntimeOnly("me.djtheredstoner:DevAuth-fabric:${rootProject.property("devauth_version")}")

    // Import all local libraries including the Ultralight bindings
    implementation(fileTree("libs") { include("*.jar") })
}

tasks {
    processResources {
        val expandProperties = mapOf(
            "name" to rootProject.property("mod_name"),
            "version" to rootProject.property("mod_version"),
            "description" to rootProject.property("mod_description"),
            "id" to project.rootProject.property("mod_id"),
            "author" to rootProject.property("mod_author"),
            "icon_file" to rootProject.property("mod_icon_file"),
            "license" to rootProject.property("mod_license"),
            "minecraft_version" to rootProject.property("minecraft_version"),
            "fabric_api_version" to rootProject.property("fabric_api_version"),
            "fabric_loader_version" to rootProject.property("fabric_loader_version")
        )

        inputs.properties(expandProperties)

        filesMatching("fabric.mod.json") {
            expand(expandProperties)
        }
    }

    jar {
        from("LICENSE") {
            rename { "${it}_${base.archivesName.toString()}" }
        }
    }
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.release = 21
}

java {
    // Loom will automatically attach sourcesJar to a RemapSourcesJar task and to the "build" task
    // if it is present.
    // If you remove this line, sources will not be generated.
    withSourcesJar()

    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

// configure the maven publication
publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            artifactId = "${rootProject.property("archives_base_name")}-${project.name}"
            from(components["java"])
        }
    }

    // See https://docs.gradle.org/current/userguide/publishing_maven.html for information on how to set up publishing.
    repositories {
        // Add repositories to publish to here.
        // Notice: This block does NOT have the same function as the block in the top level.
        // The repositories here will be used for publishing your artifact, not for
        // retrieving dependencies.
    }
}