plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.compose.compiler)
    id("com.chaquo.python")





    id("com.google.gms.google-services")
    alias(libs.plugins.jetbrains.kotlin.android)
}


android {
    namespace = "com.example.lifeline24_7"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.lifeline24_7"
        minSdk = 24
        //noinspection EditedTargetSdkVersion
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        ndk {
            // On Apple silicon, you can omit x86_64.
            abiFilters += listOf("arm64-v8a", "x86_64")
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
    buildFeatures{
        viewBinding =true
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
    flavorDimensions += "pyVersion"
    productFlavors {
        create("py310") { dimension = "pyVersion" }
        create("py311") { dimension = "pyVersion" }
    }
}

chaquopy {
    defaultConfig { }
    productFlavors { }
    sourceSets { }
}
chaquopy {
    defaultConfig {
        version = "3.8"

        buildPython("C:/Users/himanshu/AppData/Local/Programs/Python/Python38/python.exe")
    }

    chaquopy {
        sourceSets {
            getByName("main") {
                srcDir("src/main/python")
            }
        }
    }

    //modules installer
    chaquopy {
        defaultConfig {
            pip {
                // A requirement specifier, with or without a version number:
                install("opencv-python-headless")

//                install("requests==2.24.0")

                // An sdist or wheel filename, relative to the project directory:
//                install("MyPackage-1.2.3-py2.py3-none-any.whl")

                // A directory containing a setup.py, relative to the project
                // directory (must contain at least one slash):
//                install("./MyPackage")

                // "-r"` followed by a requirements filename, relative to the
                // project directory:
//                install("-r", "requirements.txt")
            }
        }
    }

    //proxy
//    chaquopy {
//        defaultConfig {
//            staticProxy("module.one", "module.two")
//        }
//    }

}


dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(platform(libs.firebase.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.credentials)
    implementation(libs.lottie)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.material.v170)
    implementation(libs.play.services.location)
    implementation(libs.firebase.database.ktx)
    testImplementation(libs.junit)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.play.services.location.v1800)
    implementation(libs.androidx.constraintlayout.v210)
    implementation(libs.firebase.analytics)
    androidTestImplementation(libs.androidx.junit)
    implementation(libs.firebase.messaging)

    implementation(libs.google.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.storage)
    implementation(libs.firebase.firestore.v2400)
    implementation(libs.firebase.storage.v2000)
    implementation(libs.material.v140)
    implementation(libs.squareup.picasso    )
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}