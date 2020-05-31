package sid.angel.biometricauthentication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {
    private val TAG = "MainActivity"
    private lateinit var bioMetricManager: BiometricManager

    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bioMetricManager = BiometricManager.from(this)

        biometricPrompt = createBioMetricPrompt()

        promptInfo = createPromptInfo()

     //   biometricPrompt.authenticate(promptInfo)

        if (bioMetricManager
                .canAuthenticate() == BiometricManager.BIOMETRIC_SUCCESS
        ) {
            biometricPrompt.authenticate(promptInfo)
        } else {
            checkBiometricStatus(bioMetricManager)
        }

    }

    private fun createBioMetricPrompt(): BiometricPrompt {

        val executor = ContextCompat.getMainExecutor(this)
        val callback = object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                super.onAuthenticationError(errorCode, errString)
                Log.d(TAG, "{$errorCode = $errString}")

                type.text = "{$errorCode = $errString}"

            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
                Log.d(TAG, "Authentication failed for an unknown reason")
                type.text = "Authentication failed for an unknown reason."
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                super.onAuthenticationSucceeded(result)

                Log.d(TAG, "Authentication was successful")
                // Proceed with viewing the private encrypted message.
                type.text = "Authentication was successful."
            }

        }

        return BiometricPrompt(this, executor, callback)
    }


    private fun createPromptInfo(): BiometricPrompt.PromptInfo {

        return BiometricPrompt.PromptInfo.Builder()
            .setTitle("Biometric Authentication")
            .setSubtitle("Please Take SS of this screen")
            .setDescription(
                "Please email me the ss. "
            )
            .setConfirmationRequired(true)
            .setNegativeButtonText("Click here.")
            .build()
    }

    private fun checkBiometricStatus(bioMetricManager: BiometricManager) {

        when (bioMetricManager.canAuthenticate()) {
            BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                type.text = "checkBiometricStatus: BIOMETRIC_ERROR_HW_UNAVAILABLE"

            BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                type.text =
                    "checkBiometricStatus: BIOMETRIC_ERROR_NONE_ENROLLED"

            BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> type.text =
                "checkBiometricStatus: BIOMETRIC_ERROR_NO_HARDWARE"

            BiometricManager.BIOMETRIC_SUCCESS -> type.text =
                "checkBiometricStatus: BIOMETRIC_SUCCESS"

        }

    }
}
