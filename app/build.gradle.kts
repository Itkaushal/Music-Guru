plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
	alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.app.kaushalprajapati.musicguru"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.app.kaushalprajapati.musicguru"
        minSdk = 26
        targetSdk = 35
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

    buildFeatures{
        viewBinding = true
    }

}

dependencies {

    // circle image view dependency
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // picasso for image loading dependency
    implementation ("com.squareup.picasso:picasso:2.71828")

    // google sign in dependency
    implementation ("com.google.android.gms:play-services-auth:21.3.0")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // lifecycle and view model
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")

    // networking
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    

    implementation("com.github.bumptech.glide:glide:4.16.0")

    // video player
    implementation("com.google.android.exoplayer:exoplayer:2.19.1")


    // swiperefreshlayout
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    // lottie animations
    implementation("com.airbnb.android:lottie:5.2.0")

    // youtube player
    implementation ("com.pierfrancescosoffritti.androidyoutubeplayer:core:11.1.0")

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
	implementation(libs.androidx.mediarouter)
	implementation(libs.firebase.auth)
	implementation(libs.androidx.credentials)
	implementation(libs.androidx.credentials.play.services.auth)
	implementation(libs.googleid)
	testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}