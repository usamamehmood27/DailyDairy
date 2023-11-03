package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
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
import com.example.DailyDiary.databinding.FragmentLockBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.SharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.changepasswordmethod
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.confirmdialog
import kamai.app.ads.AdmobManager
import kamai.app.ads.AdsManager
import java.util.concurrent.Executor

class LockFragment : Fragment() {
    lateinit var binding: FragmentLockBinding
    private lateinit var executor: Executor
    private lateinit var promptInfo: BiometricPrompt.PromptInfo
    var password = ""
    var method = "6Digit"
    var question = "What is your favorite book?"
    var checkpass = 0
    var check = false
    val biometricPrompt by lazy {
        BiometricPrompt(this, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(
                    errorCode: Int,
                    errString: CharSequence
                ) {
                    super.onAuthenticationError(errorCode, errString)
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Authentication error: $errString", Toast.LENGTH_SHORT
                    )
                        .show()
                }

                override fun onAuthenticationSucceeded(
                    result: BiometricPrompt.AuthenticationResult
                ) {
                    super.onAuthenticationSucceeded(result)
                    SharedPref.putString("method", "Fingerprint")
                    controller.navigate(R.id.action_lockFragment_to_dashboardFragment)
                    SharedPref.putBolean("startscreen", true)
                    SharedPref.putString("method", "Fingerprint")
                    Toast.makeText(
                        requireContext().applicationContext,
                        "Fingerprint Successfull", Toast.LENGTH_SHORT
                    )

                        .show()
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    Toast.makeText(
                        requireContext().applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT
                    )
                        .show()
                }
            })
    }
    val controller by lazy {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLockBinding.inflate(layoutInflater)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!SharedPref.getBolean("resetpin", true))
            differentMethods(requireContext())
        binding.apply {
            AdmobManager.loadBannerAd1(requireActivity(),binding.bannerAd)

            fingerButton.setOnClickListener {
                biometricPrompt.authenticate(promptInfo)
            }
            backpres.setOnClickListener {
                requireActivity().onBackPressed()
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
            showPasswordCheckBox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    // Show password
                    passwordedit.transformationMethod =
                        HideReturnsTransformationMethod.getInstance()
                } else {
                    // Hide password
                    passwordedit.transformationMethod = PasswordTransformationMethod.getInstance()
                }
            }

            changemethod.setOnClickListener {
                differentMethods(requireContext())
            }

            check1.setOnClickListener {
                question = "What is your favorite book?"
                questionimage1.setImageResource(R.drawable.selectpink)
                questionimage2.setImageResource(R.drawable.selectwhite)
                questionimage3.setImageResource(R.drawable.selectwhite)
                questionimage4.setImageResource(R.drawable.selectwhite)
                questionimage5.setImageResource(R.drawable.selectwhite)
                questionimage6.setImageResource(R.drawable.selectwhite)
                questionimage7.setImageResource(R.drawable.selectwhite)
                questionimage8.setImageResource(R.drawable.selectwhite)

            }
            check2.setOnClickListener {
                question = "Where did you grew up?"
                questionimage1.setImageResource(R.drawable.selectwhite)
                questionimage2.setImageResource(R.drawable.selectpink)
                questionimage3.setImageResource(R.drawable.selectwhite)
                questionimage4.setImageResource(R.drawable.selectwhite)
                questionimage5.setImageResource(R.drawable.selectwhite)
                questionimage6.setImageResource(R.drawable.selectwhite)
                questionimage7.setImageResource(R.drawable.selectwhite)
                questionimage8.setImageResource(R.drawable.selectwhite)

            }
            check3.setOnClickListener {
                question = "What is your mother's name?"
                questionimage1.setImageResource(R.drawable.selectwhite)
                questionimage2.setImageResource(R.drawable.selectwhite)
                questionimage3.setImageResource(R.drawable.selectpink)
                questionimage4.setImageResource(R.drawable.selectwhite)
                questionimage5.setImageResource(R.drawable.selectwhite)
                questionimage6.setImageResource(R.drawable.selectwhite)
                questionimage7.setImageResource(R.drawable.selectwhite)
                questionimage8.setImageResource(R.drawable.selectwhite)

            }
            check4.setOnClickListener {
                question = "What was the name of your pet?"
                questionimage1.setImageResource(R.drawable.selectwhite)
                questionimage2.setImageResource(R.drawable.selectwhite)
                questionimage3.setImageResource(R.drawable.selectwhite)
                questionimage4.setImageResource(R.drawable.selectpink)
                questionimage5.setImageResource(R.drawable.selectwhite)
                questionimage6.setImageResource(R.drawable.selectwhite)
                questionimage7.setImageResource(R.drawable.selectwhite)
                questionimage8.setImageResource(R.drawable.selectwhite)

            }
            check5.setOnClickListener {
                question = "What is your favorite food?"
                questionimage1.setImageResource(R.drawable.selectwhite)
                questionimage2.setImageResource(R.drawable.selectwhite)
                questionimage3.setImageResource(R.drawable.selectwhite)
                questionimage4.setImageResource(R.drawable.selectwhite)
                questionimage5.setImageResource(R.drawable.selectpink)
                questionimage6.setImageResource(R.drawable.selectwhite)
                questionimage7.setImageResource(R.drawable.selectwhite)
                questionimage8.setImageResource(R.drawable.selectwhite)

            }
            check6.setOnClickListener {
                question = "What city were you born in?"
                questionimage1.setImageResource(R.drawable.selectwhite)
                questionimage2.setImageResource(R.drawable.selectwhite)
                questionimage3.setImageResource(R.drawable.selectwhite)
                questionimage4.setImageResource(R.drawable.selectwhite)
                questionimage5.setImageResource(R.drawable.selectwhite)
                questionimage6.setImageResource(R.drawable.selectpink)
                questionimage7.setImageResource(R.drawable.selectwhite)
                questionimage8.setImageResource(R.drawable.selectwhite)

            }
            check7.setOnClickListener {
                question = "Where did you go for vication?"
                questionimage1.setImageResource(R.drawable.selectwhite)
                questionimage2.setImageResource(R.drawable.selectwhite)
                questionimage3.setImageResource(R.drawable.selectwhite)
                questionimage4.setImageResource(R.drawable.selectwhite)
                questionimage5.setImageResource(R.drawable.selectwhite)
                questionimage6.setImageResource(R.drawable.selectwhite)
                questionimage7.setImageResource(R.drawable.selectpink)
                questionimage8.setImageResource(R.drawable.selectwhite)

            }
            check8.setOnClickListener {
                question = "Where did you meet your spouse?"
                questionimage1.setImageResource(R.drawable.selectwhite)
                questionimage2.setImageResource(R.drawable.selectwhite)
                questionimage3.setImageResource(R.drawable.selectwhite)
                questionimage4.setImageResource(R.drawable.selectwhite)
                questionimage5.setImageResource(R.drawable.selectwhite)
                questionimage6.setImageResource(R.drawable.selectwhite)
                questionimage7.setImageResource(R.drawable.selectwhite)
                questionimage8.setImageResource(R.drawable.selectpink)

            }
            next.setOnClickListener {
                answerLayout.visibility = View.VISIBLE
                askquestionLayout.visibility = View.GONE
                answerquestion.text = question
            }
            savequestion.setOnClickListener {
                if (!answertext.text.trim().isNullOrEmpty()) {
                    SharedPref.putString("answer", answertext.text.toString())
                    confirmdialog(requireContext(), controller, method, password, question)
                }
            }
            answertext.addTextChangedListener(object : TextWatcher {
                @SuppressLint("ResourceType")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isEmpty()) {
                        savequestion.setBackgroundResource(R.drawable.unlock_pink_btn)
                        savequestiontext.setTextColor(Color.parseColor("#66103D"))
                    } else {
                        savequestion.setBackgroundResource(R.drawable.unlock_white_btn)
                        savequestiontext.setTextColor(Color.parseColor("#B7005E"))
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
            passwordedit.addTextChangedListener(object : TextWatcher {
                @SuppressLint("ResourceType")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s.toString().isEmpty()) {
                        createpass.setBackgroundResource(R.drawable.unlock_pink_btn)
                        cratetext.setTextColor(Color.parseColor("#66103D"))
                        showpasstext.setTextColor(Color.parseColor("#950751"))
                        check = false
                    } else {
                        createpass.setBackgroundResource(R.drawable.unlock_white_btn)
                        cratetext.setTextColor(Color.parseColor("#B7005E"))
                        showpasstext.setTextColor(Color.parseColor("#FFFFFF"))
                        check = true
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
            create.setOnClickListener {
                changemethod.visibility = View.INVISIBLE
                createpassword(checkpass)
            }
            createpass.setOnClickListener {
                if (!passwordedit.text.isNullOrEmpty()) {
                    changemethod.visibility = View.GONE
                    checkpass++
                    createpassword(checkpass)
                    if (checkpass == 2)
                        checkpass = 1
                }

            }

        }
    }
    fun createpassword(number: Int) {
        binding.apply {
            if (check) {
                if (method == "6Digit") {
                    password=text1.text.toString()+text2.text.toString()+text3.text.toString()+text4.text.toString()+text5.text.toString()+text6.text.toString()
                    if (!SharedPref.getBolean("resetpin", true)) {
                        pinlayout.visibility = View.GONE
                        askquestionLayout.visibility = View.VISIBLE
                    } else {
                        confirmdialog(requireContext(), controller, method, password, "")
                    }

                } else if (method == "password") {
                    if (number == 1) {
                        password = passwordedit.text.toString()
                        SharedPref.putString("check", password)
                        passwordedit.text.clear()
                        passtext.text = "Confirm Password Locked"
                        if (!SharedPref.getBolean("resetpin", true)) {
                            paswordlayout.visibility = View.GONE
                            askquestionLayout.visibility = View.VISIBLE
                        } else {
                            confirmdialog(requireContext(), controller, method, password, "")
                        }
                    } else if (number == 2) {
                        password = passwordedit.text.toString()
                        if (password == SharedPref.getString("check", "check1")) {
                            if (!SharedPref.getBolean("resetpin", true)) {
                                paswordlayout.visibility = View.GONE
                                askquestionLayout.visibility = View.VISIBLE
                            } else {
                                confirmdialog(requireContext(), controller, method, password, "")
                            }
                        } else {
                            passtext.text = "Re-enter Password "
                            passtext1.text = "Your Password doesn't matched"
                            passtext2.text = ""
                        }
                    }

                }


            } else {
                Toast.makeText(
                    requireContext(),
                    "Enter Complete Password",
                    Toast.LENGTH_SHORT
                ).show()
            }

        }
    }

    fun putnumber(number: String) {
        binding.apply {
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
                create.setBackgroundResource(R.drawable.unlock_white_btn)
                check = true
            }

        }
    }

    fun clearnumber(number: String) {
        binding.apply {
            if (method == "6Digit") {
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
                if (text6.text.isNullOrEmpty()) {
                    create.setBackgroundResource(R.drawable.unlock_pink_btn)
                    check = false
                }
            } else if (method == "password") {
                if (passwordedit.text.isNullOrEmpty()) {
                    createpass.setBackgroundResource(R.drawable.unlock_pink_btn)
                    check = false
                }
            }

        }
    }

    fun differentMethods(context: Context) {
        changepasswordmethod(context) {
            method = it
            if (method == "6Digit") {
                binding.paswordlayout.visibility = View.GONE
                binding.fingerlayout.visibility = View.GONE
                binding.pinlayout.visibility = View.VISIBLE
            } else if (method == "password") {
                binding.paswordlayout.visibility = View.VISIBLE
                binding.fingerlayout.visibility = View.GONE
                binding.pinlayout.visibility = View.GONE
            } else if (method == "Fingerprint") {

                binding.fingerlayout.visibility = View.VISIBLE
                binding.paswordlayout.visibility = View.GONE
                binding.pinlayout.visibility = View.GONE
                promptInfo = BiometricPrompt.PromptInfo.Builder()
                    .setTitle("Biometric login for my app")
                    .setSubtitle("Log in using your biometric credential")
                    .setAllowedAuthenticators(
                        BiometricManager.Authenticators.BIOMETRIC_WEAK or
                                BiometricManager.Authenticators.DEVICE_CREDENTIAL
                    )
                    .build()
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
                                BIOMETRIC_STRONG or DEVICE_CREDENTIAL
                            )
                        }
                        startActivityForResult(enrollIntent, 0)
                    }
                }
                executor = ContextCompat.getMainExecutor(requireActivity())
                biometricPrompt.authenticate(promptInfo)
            }
        }
    }


}