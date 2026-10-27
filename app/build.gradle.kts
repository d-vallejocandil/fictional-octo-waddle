plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.fictional_octo_waddle"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.fictional_octo_waddle"
        minSdk = 30
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    val version = "3.0.0"
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.core.ktx)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    implementation("com.squareup.retrofit2:retrofit:$version")
    implementation("com.squareup.retrofit2:converter-gson:$version")
    implementation("com.squareup.picasso:picasso:2.8")
}