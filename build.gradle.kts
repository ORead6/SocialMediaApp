buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")

        classpath("org.jetbrains.dokka:dokka-gradle-plugin:1.9.20")
    }

}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.1.2" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}
