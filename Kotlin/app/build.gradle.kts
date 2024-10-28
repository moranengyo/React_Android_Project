plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.example.kotiln_tpj_yesim"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.kotiln_tpj_yesim"
        minSdk = 24
        targetSdk = 34
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    // Enable view binding
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")

    // OkHttp for logging Retrofit requests
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // ViewPager2
    implementation("androidx.viewpager2:viewpager2:1.0.0")

    // Glide for image loading (optional)
    implementation("com.github.bumptech.glide:glide:4.12.0")

    // RecyclerView dependency
    implementation("androidx.recyclerview:recyclerview:1.3.1")
    // Material Components for TabLayout
    implementation("com.google.android.material:material:1.10.0")
    implementation("com.google.android.material:material:1.3.0-alpha03")

    implementation("androidx.compose.ui:ui-text-google-fonts:1.7.0")

    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.3.0")

    implementation("com.tbuonomo:dotsindicator:4.2")
    implementation("com.squareup.picasso:picasso:2.71828")


}