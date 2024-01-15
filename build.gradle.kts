import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    `java-library`
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("io.papermc.paperweight.userdev") version "1.5.11"
}

group = "xyz.raitaki"
version = "1.0.0-SNAPSHOT"
description = "Enhancements and Features for playlegend.net Application"

repositories{
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
    maven { url = uri( "https://jitpack.io") }
    maven {
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        url = uri("https://nexus.iridiumdevelopment.net/repository/maven-releases/")
    }
    maven {
        url = uri("https://repo.minebench.de/")
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
    paperweight.paperDevBundle("1.20.1-R0.1-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    implementation("com.iridium:IridiumColorAPI:1.0.8")
    implementation("de.themoep:inventorygui:1.6.1-SNAPSHOT")
    compileOnly("me.clip:placeholderapi:2.11.5")

}

tasks {
    assemble {
        dependsOn(reobfJar)
    }
    shadowJar{
        relocate("com.iridium", "xyz.raitaki.iridium")
        relocate("de.themoep.inventorygui", "xyz.raitaki.inventorygui")
    }
    compileJava {
        options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name()
    }
    processResources {
        filteringCharset = Charsets.UTF_8.name()
        val props = mapOf(
                "name" to project.name,
                "version" to project.version,
                "description" to project.description,
                "apiVersion" to "1.20"
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }


    /*
    reobfJar {
      // This is an example of how you might change the output location for reobfJar. It"s recommended not to do this
      // for a variety of reasons, however it"s asked frequently enough that an example of how to do it is included here.
      outputJar.set(layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar"))
    }
     */
}