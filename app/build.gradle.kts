import java.io.FileInputStream
import java.util.Properties

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(FileInputStream(localPropertiesFile))
}
val mapsApiKey = localProperties.getProperty("MAPS_API_KEY") ?: ""
plugins {
    with(libs.plugins) {
        id(application.get().pluginId)
        id(kotlinAndroid.get().pluginId)
        id(kotlinSerialization.get().pluginId)
        id(kotlinSerializationPlugin.get().pluginId)
        id(kotlinParcelize.get().pluginId)
        id(devtools.get().pluginId)
        id(hilt.android.get().pluginId)
        alias(libs.plugins.compose.compiler)
    }
}

android {
    namespace = "com.example.intervalsapp"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.intervalsapp"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    defaultConfig {
        manifestPlaceholders["MAPS_API_KEY"] = mapsApiKey
    }
}

dependencies {

    with(libs) {
//    #   Kotlin
        implementation(kotlin.serialization)


//    #   Androidx runtime-ktx
        implementation(androidx.lifecycle)
        implementation(androidx.lifecycle.viewmodel.compose)
        implementation(androidx.lifecycle.compose)

//    #   Datastore
        implementation(datastore.preferences)

//    #   Coroutines
        implementation(kotlin.android.coroutines)
        implementation(kotlin.coroutines)

//    #   AndroidxWork runtime
        implementation(androidxWork.runtime.ktx)

//    #   Google Maps
        implementation(google.maps.compose)
        implementation(google.maps.compose.utils)
        implementation(google.maps.compose.widgets)
        implementation(play.services.maps)
        implementation(play.services.location)
        implementation(places)

        //    #  Hilt
        ksp(hilt.compiler)
        implementation(hilt.android)
        implementation(androidx.hilt.navigation.compose)

//    #   Navigation
        implementation(navigation.compose)
        implementation(navigation.common)

//    #   Retrofit
        implementation(retrofit.core)
        implementation(converter.gson)
        implementation(converter.scalars)

//    #   OkHttp3
        implementation(okhttp.core)
        implementation(okhttp.logging.interceptor)
        
//    #   Compose
        implementation(compose.activity)
        implementation(platform(compose.bom))
        implementation(compose.material)
        runtimeOnly(compose.animation)
        runtimeOnly(compose.foundation)
        implementation("com.google.accompanist:accompanist-systemuicontroller:0.34.0")
        implementation(compose.ui)
        implementation(compose.ui.util)
        implementation(compose.ui.graphics)
        implementation(compose.ui.tooling.preview)
        debugImplementation(compose.ui.tooling)
        debugImplementation(test.compose.manifest)

        testImplementation(test.junit)
        androidTestImplementation(test.androidx.junit)
        androidTestImplementation(test.espresso)

        androidTestImplementation(platform(compose.bom))
        androidTestImplementation(test.compose.junit4)
    }
}