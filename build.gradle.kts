buildscript {
    dependencies {
        classpath("com.google.gms:google-services:4.4.0")

    }
}
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("org.jetbrains.kotlin.jvm") version "1.7.20"
    id("com.android.application") version "8.1.0" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
//    id("com.google.gms.google-services") version "4.3.15" apply false
}