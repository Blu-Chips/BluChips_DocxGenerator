package com.example.docxgenerator.util

import android.os.Build

object DeviceHelper {
    
    /**
     * Check if the current device supports native ARM libraries
     * @return true if ARM architecture, false for x86/x86_64 (emulator)
     */
    fun isArmArchitecture(): Boolean {
        val supportedAbis = Build.SUPPORTED_ABIS
        return supportedAbis.any { 
            it.startsWith("arm") || it.startsWith("aarch64")
        }
    }
    
    /**
     * Check if running on an emulator
     */
    fun isEmulator(): Boolean {
        return (Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic"))
                || Build.FINGERPRINT.startsWith("generic")
                || Build.FINGERPRINT.startsWith("unknown")
                || Build.HARDWARE.contains("goldfish")
                || Build.HARDWARE.contains("ranchu")
                || Build.MODEL.contains("google_sdk")
                || Build.MODEL.contains("Emulator")
                || Build.MODEL.contains("Android SDK built for x86")
                || Build.MANUFACTURER.contains("Genymotion")
                || Build.PRODUCT.contains("sdk_google")
                || Build.PRODUCT.contains("google_sdk")
                || Build.PRODUCT.contains("sdk")
                || Build.PRODUCT.contains("sdk_x86")
                || Build.PRODUCT.contains("vbox86p")
                || Build.PRODUCT.contains("emulator")
                || Build.PRODUCT.contains("simulator")
    }
    
    /**
     * Get device architecture name
     */
    fun getArchitectureName(): String {
        return Build.SUPPORTED_ABIS.firstOrNull() ?: "Unknown"
    }
    
    /**
     * Get device info string
     */
    fun getDeviceInfo(): String {
        return buildString {
            append("Device: ${Build.MANUFACTURER} ${Build.MODEL}\n")
            append("Architecture: ${getArchitectureName()}\n")
            append("Android: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})\n")
            append("Type: ${if (isEmulator()) "Emulator" else "Physical Device"}")
        }
    }
    
    /**
     * Check if native libraries are available
     */
    fun areNativeLibsAvailable(): Boolean {
        return try {
            // Try to load a native library if you have one
            // For now, just check if it's ARM architecture
            isArmArchitecture()
        } catch (e: UnsatisfiedLinkError) {
            false
        }
    }
}

