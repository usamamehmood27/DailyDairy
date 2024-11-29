package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.text.Editable
import android.text.TextWatcher
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.FragmentEnterenceBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.SharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces.EnterenceBack
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.back
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.showToast
import java.util.concurrent.Executor


class Enterence_Fragment : Fragment(), EnterenceBack {
    lateinit var binding: FragmentEnterenceBinding
    private lateinit var executor: Executor
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    var password = ""
    val biometricPrompt by lazy {
        BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    showToast(requireActivity(),"Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    controller.navigate(R.id.action_enterence_Fragment_to_dashboardFragment)
                   showToast(requireActivity(),"Authentication succeeded!")
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    showToast(requireActivity(),"Authentication failed")
                }
            })
    }
    val controller by lazy {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View{
        binding = FragmentEnterenceBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        util.enterenceback =this
        if (!SharedPref.getBolean("startscreen", false)) {
            controller.navigate(R.id.action_enterence_Fragment_to_dashboardFragment)
        } else {

            executor = ContextCompat.getMainExecutor(requireActivity())
            start()
        }
        binding.apply {

            fingerButton.setOnClickListener {
                biometricPrompt.authenticate(promptInfo)
            }
            one.setOnClickListener {
                putnumber("1")
            }
            clear.setOnClickListener {
                clearnumber("")
            }
            two.setOnClickListener {
                putnumber("2")
            }
            three.setOnClickListener {
                putnumber("3")
            }
            four.setOnClickListener {
                putnumber("4")
            }
            five.setOnClickListener {
                putnumber("5")

            }
            six.setOnClickListener {
                putnumber("6")
            }
            seven.setOnClickListener {
                putnumber("7")

            }
            eight.setOnClickListener {
                putnumber("8")

            }
            nine.setOnClickListener {
                putnumber("9")

            }
            zero.setOnClickListener {
                putnumber("0")
            }
            checkboxImage.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Show password
                    passwordedit.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                } else {
                    // Hide password
                    passwordedit.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }
            passwordedit.addTextChangedListener(object : TextWatcher {
                @SuppressLint("ResourceType")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isEmpty()) {
                        createpass.setBackgroundResource(R.drawable.unlock_pink_btn)
                        checkboxText.setTextColor(Color.parseColor("#950751"))

                    } else {
                        createpass.setBackgroundResource(R.drawable.unlock_white_btn)
                        checkboxText.setTextColor(Color.parseColor("#FFFFFF"))
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {}

            })
            forget.setOnClickListener {
                paswordlayout.visibility = View.GONE
                fingerlayout.visibility = View.GONE
                forget.visibility = View.INVISIBLE
                pinlayout.visibility = View.GONE
                answerLayout.visibility = View.VISIBLE
                backpres.visibility = View.VISIBLE
                answerquestion.text=SharedPref.getString("question","")
                setext.text="Recover PIN"
            }
            reset.setOnClickListener {
                if (binding.answertext.text.toString()==SharedPref.getString("answer","")){
                    SharedPref.putBolean("resetpin",true)
                    controller.navigate(R.id.action_enterence_Fragment_to_lockFragment)
                }else{
                    incorrect.visibility=View.VISIBLE
                }
            }
            answertext.addTextChangedListener(object : TextWatcher {
                @SuppressLint("ResourceType")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isEmpty()) {
                        reset.setBackgroundResource(R.drawable.unlock_pink_btn)
                        resettext.setTextColor(Color.parseColor("#66103D"))
                    } else {
                        reset.setBackgroundResource(R.drawable.unlock_white_btn)
                        resettext.setTextColor(Color.parseColor("#B7005E"))
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {}

            })
            backpres.setOnClickListener {
                start()
            }
            createpass.setOnClickListener {
                Enterpassword()
            }


        }
    }

    fun putnumber(number: String) {
        binding.apply {
           if (SharedPref.getString("method", "6Digit") == "6Digit") {
                if (text1.text.isNullOrEmpty()) {
                    text1.text = number
                    text1.setBackgroundResource(R.drawable.circle_light)
                } else if (text2.text.isNullOrEmpty()) {
                    text2.text = number
                    text2.setBackgroundResource(R.drawable.circle_light)
                } else if (text3.text.isNullOrEmpty()) {
                    text3.text = number
                    text3.setBackgroundResource(R.drawable.circle_light)
                } else if (text4.text.isNullOrEmpty()) {
                    text4.text = number
                    text4.setBackgroundResource(R.drawable.circle_light)
                } else if (text5.text.isNullOrEmpty()) {
                    text5.text = number
                    text5.setBackgroundResource(R.drawable.circle_light)
                } else if (text6.text.isNullOrEmpty()) {
                    text6.text = number
                    text6.setBackgroundResource(R.drawable.circle_light)
                }
                if (!text1.text.isNullOrEmpty() && !text2.text.isNullOrEmpty() && !text3.text.isNullOrEmpty() && !text4.text.isNullOrEmpty() && !text5.text.isNullOrEmpty() && !text6.text.isNullOrEmpty()) {
                    if (SharedPref.getString("method", "4Digit") == "6Digit") {
                        password =
                            "${text1.text}${text2.text}${text3.text}${text4.text}${text5.text}${text6.text}"
                        if (SharedPref.getString("password") == password) {
                            controller.navigate(R.id.action_enterence_Fragment_to_dashboardFragment)
                        } else {
                            showToast(requireActivity(),"Wronge Password")
                        }
                    }
                }

            }

        }
    }

    fun clearnumber(number: String) {
        binding.apply {
        if (SharedPref.getString("method", "6Digit") == "6Digit") {
                if (!text6.text.isNullOrEmpty()) {
                    text6.text = number
                    text6.setBackgroundResource(R.drawable.circle_transparent)
                } else if (!text5.text.isNullOrEmpty()) {
                    text5.text = number
                    text5.setBackgroundResource(R.drawable.circle_transparent)
                } else if (!text4.text.isNullOrEmpty()) {
                    text4.text = number
                    text4.setBackgroundResource(R.drawable.circle_transparent)
                } else if (!text3.text.isNullOrEmpty()) {
                    text3.text = number
                    text3.setBackgroundResource(R.drawable.circle_transparent)
                } else if (!text2.text.isNullOrEmpty()) {
                    text2.text = number
                    text2.setBackgroundResource(R.drawable.circle_transparent)
                } else if (!text1.text.isNullOrEmpty()) {
                    text1.text = number
                    text1.setBackgroundResource(R.drawable.circle_transparent)
                }

            } else if (SharedPref.getString("method", "4Digit") == "password") {
                if (passwordedit.text.isNullOrEmpty()) {
                    createpass.setBackgroundResource(R.drawable.create_button)
                }
            }

        }
    }

    fun Enterpassword() {
        binding.apply {
            if (SharedPref.getString("method", "6Digit") == "password") {
                password = passwordedit.text.toString()
                if (SharedPref.getString("password") == password) {
                    controller.navigate(R.id.action_enterence_Fragment_to_dashboardFragment)
                } else {
                    showToast(requireActivity(),"Wronge Password")
                }
            }
        }
    }
    fun start(){
        Log.d("kifhnhfnhfienh", "start: "+SharedPref.getString("method", "4Digit"))
        if (SharedPref.getString("method", "4Digit") == "6Digit") {
            binding.paswordlayout.visibility = View.GONE
            binding.fingerlayout.visibility = View.GONE
            binding.answerLayout.visibility = View.GONE
            binding.pinlayout.visibility = View.VISIBLE
            binding.forget.visibility = View.VISIBLE
        } else if (SharedPref.getString("method", "4Digit") == "password") {
            binding.paswordlayout.visibility = View.VISIBLE
            binding.pinlayout.visibility = View.GONE
            binding.answerLayout.visibility = View.GONE
            binding.fingerlayout.visibility = View.GONE
            binding.forget.visibility = View.VISIBLE
        }else if (SharedPref.getString("method", "4Digit") == "Fingerprint") {
            promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setAllowedAuthenticators(
                    BiometricManager.Authenticators.BIOMETRIC_WEAK or
                            BiometricManager.Authenticators.DEVICE_CREDENTIAL
                )
                .build()
            binding.fingerlayout.visibility = View.VISIBLE
            binding.paswordlayout.visibility = View.GONE
            binding.pinlayout.visibility = View.GONE
            binding.answerLayout.visibility = View.GONE
            binding.forget.visibility = View.VISIBLE
            biometricPrompt.authenticate(promptInfo)
            val biometricManager = BiometricManager.from(requireActivity())
            when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
                BiometricManager.BIOMETRIC_SUCCESS ->
                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.")

                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE ->
                    Log.e("MY_APP_TAG", "No biometric features available on this device.")

                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE ->
                    Log.e("MY_APP_TAG", "Biometric features are currently unavailable.")

                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
                    // Prompts the user to create credentials that your app accepts.
                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
                        putExtra(
                            Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
                            android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG or android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
                        )
                    }
                    startActivityForResult(enrollIntent, 0)
                }
            }

        }
    }

    override fun onBack() {
        if (binding.setext.text=="Recover PIN"){
            binding.backpres.visibility=View.GONE
            start()
        }else {
            requireActivity().finish()
        }
    }
}