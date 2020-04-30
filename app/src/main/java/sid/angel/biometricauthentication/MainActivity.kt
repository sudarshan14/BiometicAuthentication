package sid.angel.biometricauthentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var bioMetricManager: androidx.biometric.BiometricManager
    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bioMetricManager = BiometricManager.from(this)

        checkBiometricStatus(bioMetricManager)
        executor = ContextCompat.getMainExecutor(this)

        biometricPrompt =
            BiometricPrompt(this, executor, object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                }
            })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Hello")
            .setDescription("There you are")
            .setNegativeButtonText("CANCEL")
            .build()

        biometricPrompt.authenticate(promptInfo)


    }

    private fun checkBiometricStatus(bioMetricManager: BiometricManager) {

        when (bioMetricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> Log.d(
                TAG,
                "checkBiometricStatus: BIOMETRIC_ERROR_HW_UNAVAILABLE"
            )
            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> Log.d(
                TAG,
                "checkBiometricStatus: BIOMETRIC_ERROR_NONE_ENROLLED"
            )
            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> Log.d(
                TAG,
                "checkBiometricStatus: BIOMETRIC_ERROR_NO_HARDWARE"
            )
            BiometricManager.BIOMETRIC_SUCCESS -> Log.d(
                TAG,
                "checkBiometricStatus: BIOMETRIC_SUCCESS"
            )
        }

    }
}
