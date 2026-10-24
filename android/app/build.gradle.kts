import org.gradle.api.tasks.Copy

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.zakou.kernel"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.zakou.kernel"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }
}

val webRoot = rootProject.projectDir.parentFile

val syncWebAssets = tasks.register<Copy>("syncWebAssets") {
    from(webRoot) {
        include("index.html", "icon.svg", "icon-user.jpg", "manifest.webmanifest", "sw.js", "update.json", "vendor/**")
    }
    into(layout.buildDirectory.dir("generated/webassets/www"))
}

android.sourceSets["main"].assets.srcDir(layout.buildDirectory.dir("generated/webassets"))

tasks.named("preBuild") {
    dependsOn(syncWebAssets)
}

dependencies {
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("androidx.webkit:webkit:1.11.0")
}
