package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.DraftLayoutBinding
import com.example.DailyDiary.databinding.FragmentDairyPageBinding
import com.google.android.material.internal.ViewUtils.hideKeyboard
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.DrawView
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.DrawView.Companion.currentColor
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.LockableScrollView
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.SharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Draftdataclass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Notes
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments.SaveNotes_Fragment.Companion.checkingState
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.interfaces.Onbackpress
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.BottomSheet_Utils.Companion.backgroundbottomsheet
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.BottomSheet_Utils.Companion.edittextbottomsheet
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.BottomSheet_Utils.Companion.stikerbottomsheet
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.GalleryDialog
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.Dialog_Utils.Companion.yourMood
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.back
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.getImagesByBucket
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.getMonthName
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.saveDrawnViewToStorage
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.setAnimation
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.showToast
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.Notes_ViewModel
import kamai.app.ads.AdmobManager
import kamai.app.ads.AdsManager
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class DairyPageFragment : Fragment(), Onbackpress {
    lateinit var binding: FragmentDairyPageBinding
    var imagelist = mutableListOf<Any>()
    var backgroundlist = mutableListOf<Any>()
    var gradiantlist = mutableListOf<Any>()
    lateinit var viemodel: Notes_ViewModel
    var backgroundid = 0
    var fontid = 0
    var fontsize = 0
    var fontposition = 0
    var stikerid = 0
    var moodrid = 0
    var imagepath = ""
    var gliterpath = ""
    var todaydate = ""
    var time = ""
    var monthName = ""
    var dayOfMonth = 0
    var monthofyear = 0
    var oldId: Int? = null
    var oldFolder: String? = null
    var xDelta = 0
    var yDelta = 0
    var gliterstate = false
    var stikerpositionx = 0
    var stikerpositiony = 0
    var imagepositionx = 0
    var imagepositiony = 0
    var containerWidth = 0
    var containerHeight = 0
    var containerimageWidth = 0
    var containerimageHeight = 0
    var checkbackpress = "cancel"
    val controller by lazy {
        Navigation.findNavController(requireActivity(), R.id.fragmentContainerView)
    }
    private var activityResultLauncher: ActivityResultLauncher<Array<String>> =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { result ->
            var allAreGranted = true
            for (b in result.values) {
                allAreGranted = allAreGranted && b
            }
            if (allAreGranted) {
                AdsManager.showIntersWithClick(requireActivity()) {
                    imagelist = getImagesByBucket(requireActivity())
                    GalleryDialog(requireActivity(), imagelist, viemodel) {
                        if (it != "") {
                            binding.frameContainerimage.visibility = View.VISIBLE
                            binding.frameContainerimage.setBackgroundResource(R.drawable.container)
                            binding.crossIconimage.visibility = View.VISIBLE
                            binding.tickimage.visibility = View.VISIBLE
                            binding.noteimage.visibility = View.VISIBLE
                            imagepath = it
                            binding.noteimage.setImageURI(Uri.parse(imagepath))
                        }
                    }
                }
            } else {
                showToast(requireActivity(), "Permission Denied")
            }
        }

    override fun onStart() {
        super.onStart()
        if (SharedPref.getBolean("skipmood", false)) {
            binding.mood.visibility = View.GONE
        }
    }

    @SuppressLint("SetTextI18n", "ClickableViewAccessibility")
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDairyPageBinding.inflate(layoutInflater)

        util.logAnalytic(requireContext(), "WYD_landed")
        viemodel = ViewModelProvider(requireActivity())[Notes_ViewModel::class.java]
        back = this
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @SuppressLint("ClickableViewAccessibility", "RestrictedApi")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val calendar = Calendar.getInstance()
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        monthName = getMonthName(calendar)
        monthofyear = calendar.get(Calendar.MONTH)
        var year = calendar.get(Calendar.YEAR)
        calendar.set(year, monthofyear, dayOfMonth).toString()
        val formattedDate =
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        todaydate = formattedDate
        val hour: Int = calendar.get(Calendar.HOUR_OF_DAY)
        val minute: Int = calendar.get(Calendar.MINUTE)

        time = if (calendar.get(Calendar.AM_PM) == Calendar.AM) {
            "${hour}:${minute} AM"
        } else {
            "${hour}:${minute} PM"
        }
        viemodel.getbackgroundl(requireContext())
        viemodel.getGradiantlist(requireContext())
        viemodel.backgroundlist.observe(viewLifecycleOwner) {
            backgroundlist = it
        }
        viemodel.graidiantlist.observe(viewLifecycleOwner) {
            gradiantlist = it
        }
        binding.apply {
            AdmobManager.loadBannerAd1(requireActivity(), bannerAd)

            viemodel.onenote.observe(viewLifecycleOwner) {
                if (checkingState == "SavedNotes" || checkingState == "FolderNotes") {
                    it as Notes
                    oldId = it.id
                    oldFolder = it.folderId
                    tittle.setText(it.tittle)
                    createnote.setText(it.content)
                    monthName = it.month
                    dayOfMonth = it.day.toInt()
                    time = it.time
                    if (it.background > 0) {
                        mainlayout.setBackgroundResource(it.background)
                        backgroundid = it.background
                    }
                    if (it.mood >= 1) {
                        mood.setImageResource(it.mood)
                        moodrid = it.mood
                    }
                    if (it.image != "") {
                        frameContainerimage.visibility = View.VISIBLE
                        if (it.imagewidth != 0 && it.imageheight != 0) {
                            val containerlayoutParams = frameContainerimage.layoutParams
                            containerlayoutParams.width = it.imagewidth
                            containerlayoutParams.height = it.imageheight
                            frameContainerimage.layoutParams = containerlayoutParams
                            val layoutParams = noteimage.layoutParams
                            layoutParams.width = it.imagewidth
                            layoutParams.height = it.imageheight
                            noteimage.layoutParams = layoutParams
                            val parentView = noteimage.parent as View
                            parentView.requestLayout()

                        }
                        imagecontainer.gravity = it.imagex
                        noteimage.setImageURI(Uri.parse(it.image))
                        imagepath = it.image
                        containerHeight = it.imagewidth
                        containerHeight = it.imagewidth
                        imagepositionx = it.imagex

                    }
                    date.text = it.day + it.month
                    if (it.stiker > 0) {
                        if (it.gliterx != 0 && it.glitery != 0) {
                            val layoutParams = updateimageView.layoutParams
                            layoutParams.width = it.gliterx
                            layoutParams.height = it.glitery
                            updateimageView.layoutParams = layoutParams
                            val parentView = updateimageView.parent as View
                            parentView.requestLayout()
                        }
                        updateimageView.visibility = View.VISIBLE
                        val layoutParams =
                            updateimageView.layoutParams as ConstraintLayout.LayoutParams
                        layoutParams.leftMargin = it.x
                        layoutParams.topMargin = it.y
                        stikerpositionx = it.x
                        stikerpositiony = it.y
                        updateimageView.layoutParams = layoutParams
                        updateimageView.setImageResource(it.stiker)
                        stikerid = it.stiker
                    }
                    if (it.gliterimage != "") {
                        gliterimageview.visibility = View.VISIBLE
                        gliterimageview.setImageURI(Uri.parse(it.gliterimage))
                        gliterpath = it.gliterimage
                    }
                    fontid = it.font
                    fontsize = it.fontsize
                    fontposition = it.fontposition
                    if (fontid != 0)
                        binding.createnote.typeface =
                            ResourcesCompat.getFont(requireContext(), fontid)
                    if (fontsize != 0)
                        binding.createnote.setTextSize(
                            TypedValue.COMPLEX_UNIT_SP,
                            fontsize.toFloat()
                        )
                    if (fontposition != 0)
                        binding.createnote.gravity = fontposition
                    save.text = ("Update")
                } else if (checkingState == "DraftNotes") {
                    it as Draftdataclass
                    oldId = it.id
                    tittle.setText(it.tittle)
                    createnote.setText(it.content)
                    if (it.background > 0) {
                        mainlayout.setBackgroundResource(it.background)
                        backgroundid = it.background
                    }
                    if (it.mood >= 1) {
                        mood.setImageResource(it.mood)
                        moodrid = it.mood
                    }
                    if (it.image != "") {
                        frameContainerimage.visibility = View.VISIBLE
                        if (it.imagex != 0 && it.imagey != 0) {
                            val layoutParams = updateimageView.layoutParams
                            layoutParams.width = it.imagex
                            layoutParams.height = it.imagey
                            noteimage.layoutParams = layoutParams
                            val parentView = noteimage.parent as View
                            parentView.requestLayout()
                        }
                        imagecontainer.gravity = it.fontposition
                        noteimage.setImageURI(Uri.parse(it.image))
                        imagepath = it.image
                        containerHeight = it.imagex
                        containerHeight = it.imagex
                        imagepositionx = it.fontposition
                    }
                    date.text = it.day + it.month
                    if (it.stiker > 0) {
                        if (it.gliterx != 0 && it.glitery != 0) {
                            val layoutParams = updateimageView.layoutParams
                            layoutParams.width = it.gliterx
                            layoutParams.height = it.glitery
                            updateimageView.layoutParams = layoutParams
                            val parentView = updateimageView.parent as View
                            parentView.requestLayout()
                        }
                        updateimageView.visibility = View.VISIBLE
                        val layoutParams =
                            updateimageView.layoutParams as ConstraintLayout.LayoutParams
                        layoutParams.leftMargin = it.x
                        layoutParams.topMargin = it.y
                        stikerpositionx = it.x
                        stikerpositiony = it.y
                        updateimageView.layoutParams = layoutParams
                        updateimageView.setImageResource(it.stiker)
                        stikerid = it.stiker
                    }
                    if (it.gliterimage != "") {
                        gliterimageview.visibility = View.VISIBLE
                        gliterimageview.setImageURI(Uri.parse(it.gliterimage))
                        gliterpath = it.gliterimage
                    }
                    fontid = it.font
                    fontsize = it.fontsize
                    fontposition = it.fontposition
                    if (fontid != 0)
                        binding.createnote.typeface =
                            ResourcesCompat.getFont(requireContext(), fontid)
                    if (fontsize != 0)
                        binding.createnote.setTextSize(
                            TypedValue.COMPLEX_UNIT_SP,
                            fontsize.toFloat()
                        )
                    if (fontposition != 0)
                        binding.createnote.gravity = fontposition
                } else
                    save.text = ("Save")
            }
            frameContainer.setOnTouchListener { view, event ->
                val x = event.rawX.toInt()
                val y = event.rawY.toInt()
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        val layoutParams =
                            frameContainer.layoutParams as ConstraintLayout.LayoutParams
                        xDelta = x - layoutParams.leftMargin
                        yDelta = y - layoutParams.topMargin
                    }

                    MotionEvent.ACTION_MOVE -> {
                        val layoutParams =
                            frameContainer.layoutParams as ConstraintLayout.LayoutParams
                        layoutParams.leftMargin = x - xDelta
                        layoutParams.topMargin = y - yDelta
                        frameContainer.layoutParams = layoutParams
                        stikerpositionx = x - xDelta
                        stikerpositiony = y - yDelta
                    }
                }
                true
            }
            maincontainer.setOnClickListener {
                createnote.isEnabled = true
                createnote.isFocusableInTouchMode = true
                createnote.requestFocus()
                val imm =
                    requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.showSoftInput(createnote, InputMethodManager.SHOW_IMPLICIT)
                Log.d("mckdkmckdm", "onViewCreated: ")
            }
            crossIcon.setOnClickListener {
                frameContainer.visibility = View.GONE
                imageView.visibility = View.GONE
                stikerid = 0
            }
            tick.setOnTouchListener { view, event ->

                when (event.action) {
                    MotionEvent.ACTION_MOVE -> {
                        val currentWidth = frameContainer.width
                        val currentHeight = frameContainer.height
                        val newWidth = currentWidth + event.x.toInt()
                        val newHeight = currentHeight + event.y.toInt()
                        frameContainer.layoutParams.width = newWidth
                        frameContainer.layoutParams.height = newHeight
                        frameContainer.requestLayout()
                    }
                }
                true
            }

            frameContainer.addOnLayoutChangeListener { _, left, top, right, bottom, _, _, _, _ ->
                containerWidth = right - left
                containerHeight = bottom - top

            }

            mood.setOnClickListener {
                util.logAnalytic(requireContext(), "WYD_mood_select_click")
                yourMood(requireContext(), binding) {
                    if (it >= 1) {
                        moodrid = it
                        binding.mood.setImageResource(it)
                    }

                }
            }

            createdon.text = time
            date.text = "${monthName}  ${dayOfMonth}"
            add.setOnClickListener {
                hideKeyboard(binding.maincontainer)
                editview.visibility = View.VISIBLE
                checkbackpress = "addbutton"
                setAnimation(photos, 0f, -920f)
                setAnimation(stiker, 0f, -740f)
                setAnimation(background, 0f, -560f)
                setAnimation(edittext, 0f, -380f)
                setAnimation(gliters, 0f, -200f)
            }
            closeedit.setOnClickListener {
                setAnimation(photos, -920f, 0f)
                setAnimation(stiker, -740f, 0f)
                setAnimation(background, -560f, 0f)
                setAnimation(edittext, -380f, 0f)
                setAnimation(gliters, -200f, 0f)
                Handler(Looper.myLooper()!!).postDelayed({
                    checkbackpress = "cancel"
                    editview.visibility = View.GONE
                }, 850)
            }
            stiker.setOnClickListener {
                util.logAnalytic(requireContext(), "WYD_sticker_click")
                editview.visibility = View.GONE
                stikerbottomsheet(requireActivity(), binding) {
                    stikerid = it
                    binding.updateimageView.visibility = View.GONE
                    binding.imageView.visibility = View.VISIBLE
                    binding.frameContainer.visibility = View.VISIBLE
                    binding.imageView.setImageResource(stikerid)
                }
            }
            photos.setOnClickListener {
                util.logAnalytic(requireContext(), "WYD_photo_click")
                editview.visibility = View.GONE
                if (requireActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED || requireActivity().checkSelfPermission(
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    AdsManager.showIntersWithClick(requireActivity()) {

                        imagelist = getImagesByBucket(requireActivity())
                        GalleryDialog(requireActivity(), imagelist, viemodel) {
                            if (it != "") {
                                frameContainerimage.visibility = View.VISIBLE
                                frameContainerimage.setBackgroundResource(R.drawable.container)
                                crossIconimage.visibility = View.VISIBLE
                                tickimage.visibility = View.VISIBLE
                                noteimage.visibility = View.VISIBLE
                                imagepath = it
                                noteimage.setImageURI(Uri.parse(imagepath))
                            }
                        }
                    }
                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
                        activityResultLauncher.launch(
                            arrayOf(
                                Manifest.permission.READ_MEDIA_IMAGES
                            )
                        )
                    else
                        activityResultLauncher.launch(
                            arrayOf(
                                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE
                            )
                        )
                }


            }

            crossIconimage.setOnClickListener {
                frameContainerimage.setBackgroundColor(Color.parseColor("#00000000"))
                frameContainerimage.visibility = View.GONE
                crossIconimage.visibility = View.GONE
                tickimage.visibility = View.GONE
                imagepath = ""
            }
            tickimage.setOnTouchListener { view, event ->
                when (event.action) {
                    MotionEvent.ACTION_MOVE -> {
                        val currentWidth = frameContainerimage.width
                        val currentHeight = frameContainerimage.height
                        val newWidth = currentWidth + event.x.toInt()
                        val newHeight = currentHeight + event.y.toInt()
                        frameContainerimage.layoutParams.width = newWidth
                        frameContainerimage.layoutParams.height = newHeight
                        frameContainerimage.requestLayout()
                    }
                }
                true
            }


            frameContainerimage.addOnLayoutChangeListener { _, left, top, right, bottom, _, _, _, _ ->
                containerimageWidth = right - left
                containerimageHeight = bottom - top
            }

            background.setOnClickListener {
                util.logAnalytic(requireContext(), "WYD_background")
                editview.visibility = View.GONE
                backgroundbottomsheet(requireActivity(), binding, backgroundlist, gradiantlist) {
                    backgroundid = it

                }

            }
            edittext.setOnClickListener {
                util.logAnalytic(requireContext(), "WYD_edit_click")
                editview.visibility = View.GONE
                edittextbottomsheet(requireActivity(), binding) {
                    if (it.fontposition != 0) {
                        fontposition = it.fontposition
                        imagepositionx = it.fontposition
                    } else {
                        binding.createnote.gravity = Gravity.START
                    }
                    fontid = it.fontid
                    fontsize = it.fontsize
                }
            }
            createnote.addTextChangedListener(object : TextWatcher {
                @SuppressLint("ResourceType")
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isEmpty()) {
                        save.setTextColor(Color.parseColor("#1C1B1F"))
                    } else {
                        save.setBackgroundResource(R.drawable.save_button)
                        save.setTextColor(Color.parseColor("#B7005E"))
                    }
                }


            })
            gliters.setOnClickListener {
                checkbackpress = "gliter"
                scrollView.isScrollingEnabled=false
                gliterstate = true
                editview.visibility = View.GONE
                cancelImage.visibility = View.GONE
                crossIconimage.visibility = View.GONE
                tickimage.visibility = View.GONE
                titletext.visibility = View.GONE
                backCancel.visibility = View.VISIBLE
                glitercolor.visibility = View.VISIBLE
                closegliter.visibility = View.VISIBLE
                undo.visibility = View.VISIBLE
                redo.visibility = View.VISIBLE
                draw.visibility = View.VISIBLE
                draw.setDrawingEnabled(true)
                linearLayout2.visibility = View.GONE
                createnote.visibility = View.GONE
                imageView.visibility = View.GONE
                frameContainer.visibility = View.INVISIBLE
                frameContainerimage.setBackgroundColor(Color.parseColor("#00000000"))
                noteimage.visibility = View.GONE
                save.text = "Done"
                save.setBackgroundResource(R.drawable.save_button)
                tittle.isEnabled = false
                edittext.isEnabled = false
                mood.isClickable = false
                mood.isFocusable = false
            }

            currentColor = Color.BLACK
            black.setOnClickListener {
                // draw.setCurrentColor(Color.BLACK)
                currentColor = Color.BLACK
            }
            gray.setOnClickListener {
                // draw.setCurrentColor(Color.GRAY)
                currentColor = Color.GRAY
            }
            pink.setOnClickListener {
                currentColor = Color.RED
            }
            yellow.setOnClickListener {
                currentColor = Color.YELLOW
            }
            undo.setOnClickListener {
                draw.undo()
            }
            redo.setOnClickListener {
                draw.redo()
            }
            closegliter.setOnClickListener {
                if (checkbackpress == "gliter") {
                    scrollView.isScrollingEnabled=true
                    cleardrawing()
                } else
                    editview.visibility = View.GONE
            }
            cancel.setOnClickListener {
                AdsManager.countInterstitialCapping(requireActivity())
                if (gliterstate) {
                    scrollView.isScrollingEnabled=true
                    cleardrawing()
                } else {
                    requireActivity().onBackPressed()
                }
            }
            save.setOnClickListener {
                util.logAnalytic(requireContext(), "WYD_svd_click")
                AdsManager.showIntersWithClick(requireActivity()) {
                    if (checkingState == "CreateNote" || checkingState == "DraftNotes") {
                        if (gliterstate) {
                            saveGliter("Save")
                        } else {
                            if (!tittle.text.isNullOrEmpty() || !createnote.text.isNullOrEmpty()) {
                                viemodel.addNote(
                                    Notes(
                                        0,
                                        "",
                                        tittle = tittle.text.toString(),
                                        background = backgroundid,
                                        mood = moodrid,
                                        stiker = stikerid,
                                        font = fontid,
                                        fontsize = fontsize,
                                        fontposition = fontposition,
                                        x = stikerpositionx,
                                        y = stikerpositiony,
                                        containerimageWidth,
                                        containerimageHeight,
                                        fontposition,
                                        gliterx = containerWidth,
                                        glitery = containerHeight,
                                        image = imagepath,
                                        gliterimage = gliterpath,
                                        month = monthName,
                                        day = dayOfMonth.toString(),
                                        time = time,
                                        content = createnote.text.toString(),
                                        completedate = todaydate
                                    )
                                )
                                if (checkingState == "DraftNotes") {
                                    viemodel.deletedraftNotes(oldId!!)
                                }
                                controller.navigate(R.id.action_dairyPageFragment_to_dashboardFragment)
                            } else {
                                showToast(requireActivity(), "Enter something First")
                            }
                        }

                    } else if (checkingState == "SavedNotes") {
                        if (gliterstate) {
                            saveGliter("Update")
                        } else {
                            viemodel.updateNote(
                                Notes(
                                    oldId!!,
                                    oldFolder!!,
                                    tittle = tittle.text.toString(),
                                    background = backgroundid,
                                    mood = moodrid,
                                    stiker = stikerid,
                                    font = fontid,
                                    fontsize = fontsize,
                                    fontposition = fontposition,
                                    x = stikerpositionx,
                                    y = stikerpositiony,
                                    containerimageWidth,
                                    containerimageHeight,
                                    fontposition,
                                    gliterx = containerWidth,
                                    glitery = containerHeight,
                                    image = imagepath,
                                    gliterimage = gliterpath,
                                    month = monthName,
                                    day = dayOfMonth.toString(),
                                    time = time,
                                    content = createnote.text.toString(),
                                    completedate = todaydate
                                )
                            )
                            controller.navigate(R.id.action_dairyPageFragment_to_saveNotes_Fragment)
                        }
                    } else if (checkingState == "FolderNotes") {
                        if (gliterstate) {
                            saveGliter("Update")
                        } else {
                            viemodel.updateNote(
                                Notes(
                                    oldId!!,
                                    oldFolder!!,
                                    tittle = tittle.text.toString(),
                                    background = backgroundid,
                                    mood = moodrid,
                                    stiker = stikerid,
                                    font = fontid,
                                    fontsize = fontsize,
                                    fontposition = fontposition,
                                    x = stikerpositionx,
                                    y = stikerpositiony,
                                    containerimageWidth,
                                    containerimageHeight,
                                    fontposition,
                                    gliterx = containerWidth,
                                    glitery = containerHeight,
                                    image = imagepath,
                                    gliterimage = gliterpath,
                                    month = monthName,
                                    day = dayOfMonth.toString(),
                                    time = time,
                                    content = createnote.text.toString(),
                                    completedate = todaydate
                                )
                            )
                            controller.navigate(R.id.action_dairyPageFragment_to_dashboardFragment)
                        }
                    }

                }
            }
        }


    }

    fun saveGliter(name: String) {
        binding.apply {

            val imagePath = saveDrawnViewToStorage(requireContext(), binding)
            gliterpath = imagePath
            cancelImage.visibility = View.VISIBLE
            titletext.visibility = View.VISIBLE
            backCancel.visibility = View.GONE
            glitercolor.visibility = View.GONE
            undo.visibility = View.GONE
            redo.visibility = View.GONE
            gliterstate = false
            save.setText(name)
            tittle.isEnabled = true
            draw.setDrawingEnabled(false)
            edittext.isEnabled = true
            mood.isClickable = true
            mood.isFocusable = true
            closegliter.visibility = View.GONE
            linearLayout2.visibility = View.VISIBLE
            createnote.visibility = View.VISIBLE
            if (!imagepath.isNullOrEmpty())
                noteimage.visibility = View.VISIBLE
            if (stikerid != 0)
                imageView.visibility = View.VISIBLE
            scrollView.isScrollingEnabled=true
        }

    }



    fun cleardrawing() {
        binding.apply {
            draw.setDrawingEnabled(false)
            draw.clearDrawing()
            draw.visibility = View.GONE
            cancelImage.visibility = View.VISIBLE
            backCancel.visibility = View.GONE
            glitercolor.visibility = View.GONE
            titletext.visibility = View.VISIBLE
            undo.visibility = View.GONE
            redo.visibility = View.GONE
            closegliter.visibility = View.GONE
            linearLayout2.visibility = View.VISIBLE
            createnote.visibility = View.VISIBLE
            if (imagepath != "")
                noteimage.visibility = View.VISIBLE
            if (stikerid != 0)
                imageView.visibility = View.VISIBLE
            gliterstate = false
            gliterpath = ""
            if (checkingState == "CreateNote")
                save.setText("Save")
            if (createnote.text.isNullOrEmpty())
                save.setBackgroundResource(R.drawable.unlock_pink_btn)
            checkbackpress = "cancel"
            tittle.isEnabled = true
            edittext.isEnabled = true
            mood.isClickable = true
            mood.isFocusable = true
            scrollView.isScrollingEnabled=true

        }
    }


    fun draftDialog(context: Context) {
        val inflater =
            context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val dialogueBinding = DraftLayoutBinding.inflate(inflater)
        val dialog =
            Dialog(context, R.style.Custm_Dialod_Save)
        dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
        dialog.setContentView(dialogueBinding.root)
        dialogueBinding.apply {
            delete.setOnClickListener {
                controller.navigate(R.id.action_dairyPageFragment_to_dashboardFragment)
                dialog.dismiss()
            }

            save.setOnClickListener {
                AdsManager.showIntersWithClick(requireActivity()) {
                    viemodel.adddraftNote(
                        Draftdataclass(
                            0,
                            tittle = binding.tittle.text.toString(),
                            background = backgroundid,
                            mood = moodrid,
                            stiker = stikerid,
                            font = fontid,
                            fontsize = fontsize,
                            fontposition = fontposition,
                            x = stikerpositionx,
                            y = stikerpositiony,
                            containerimageWidth,
                            containerimageHeight,
                            gliterx = containerWidth,
                            glitery = containerHeight,
                            image = imagepath,
                            gliterimage = gliterpath,
                            month = monthName,
                            day = dayOfMonth.toString(),
                            time = time,
                            content = binding.createnote.text.toString(),
                            completedate = todaydate
                        )
                    )
                    controller.navigate(R.id.action_dairyPageFragment_to_dashboardFragment)
                    dialog.dismiss()
                }
            }

        }
        dialog.show()
    }

    override fun click() {
        if (checkbackpress == "cancel") {
            if (!binding.tittle.text.isNullOrEmpty() || !binding.createnote.text.isNullOrEmpty())
                draftDialog(requireContext())
            else
                controller.navigate(R.id.action_dairyPageFragment_to_dashboardFragment)
        } else if (checkbackpress == "gliter") {
            binding.scrollView.isScrollingEnabled=true
            cleardrawing()
        } else if (checkbackpress == "addbutton") {
            checkbackpress = "cancel"
            binding.editview.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        TODO("Not yet implemented")
    }


}