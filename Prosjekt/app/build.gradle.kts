import org.jetbrains.kotlin.konan.properties.Properties
// 'Warnings' her indikerer at det finnes oppdateringer for ulike verktøy/biblioteker/rammeverk.
// Vi velger å beholde nåværende versjoner for å sikre stabilitet i forkant av levering av prosjektet.

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("de.mannodermaus.android-junit5") version "1.10.0.0" // https://github.com/mannodermaus/android-junit5 tredjeparts plugin som gir bedre støtte for JUnit5 for Android
}

android {
    namespace = "no.uio.ifi.in2000.team32.prosjekt"
    compileSdk = 34

    defaultConfig {
        applicationId = "no.uio.ifi.in2000.team32.prosjekt"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildFeatures {
            viewBinding = true
            buildConfig = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation("com.google.android.gms:play-services-maps:18.2.0")

    // JUnit Jupiter API og Engine
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")

    // JUnit 4 for legacy
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.01"))
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    //Testing og coroutines (testing av repository)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.0")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1-Beta")  //<-- For å få tilgang til 'runTest'!

    //MockK
    testImplementation("io.mockk:mockk:1.13.10") // Nyeste versjon: https://mvnrepository.com/artifact/io.mockk/mockk

    //Navigation import
    val navVersion = "2.7.7"
    implementation("androidx.navigation:navigation-compose:$navVersion")

    //Location services
    implementation("com.google.android.gms:play-services-location:21.2.0")

    //Mapbox
    implementation("com.mapbox.maps:android:11.2.0")
    implementation("com.mapbox.extension:maps-compose:11.2.0")
    implementation("com.mapbox.mapboxsdk:mapbox-sdk-services:5.8.0")

    //Ktor
    val ktorVersion = "2.3.8"
    implementation("io.ktor:ktor-client-core:$ktorVersion")
    implementation("io.ktor:ktor-client-android:$ktorVersion")
    implementation("io.ktor:ktor-client-content-negotiation:$ktorVersion")
    implementation("io.ktor:ktor-serialization-gson:$ktorVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0-RC2")

    //Coil
    implementation("io.coil-kt:coil-compose:2.5.0")

    //RememberScaffoldState
    implementation("androidx.compose.material:material:1.6.6")

    //Legger til viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    //Observe as State
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

    //Livedata
    implementation("androidx.compose.runtime:runtime-livedata:1.6.6")

    //For å unngå feilmelding ved terminalprint
    implementation("org.slf4j:slf4j-simple:1.7.30")


    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.1")
    // Kotlin Coroutines Play Services for .await() support
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.5.2")


    //MpAndroidChart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")


    implementation("androidx.core:core-ktx:1.13.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-compose:1.9.0")
    implementation(platform("androidx.compose:compose-bom:2024.04.01"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
}