package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.DeletDraftLayoutBinding
import com.example.DailyDiary.databinding.DeletFolderLayoutBinding
import com.example.DailyDiary.databinding.ExitDialogBinding
import com.example.DailyDiary.databinding.FragmentDairyPageBinding
import com.example.DailyDiary.databinding.GalleryLayoutBinding
import com.example.DailyDiary.databinding.MoodLayoutBinding
import com.example.DailyDiary.databinding.MoveFolderLayoutBinding
import com.example.DailyDiary.databinding.NewFolderLayoutBinding
import com.example.DailyDiary.databinding.PrivacypolicydialogueBinding
import com.example.DailyDiary.databinding.RatusDialogBinding
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter.Background_Apater
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter.FolderAdapter
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter.moveFolderAdp
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.SharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Draftdataclass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Folder
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.GallerY_DataClass
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.Notes
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.showToast
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.Notes_ViewModel
import kamai.app.ads.AdmobManager
import kamai.app.ads.AdsManager
import java.io.File
import java.io.IOException
import kotlin.system.exitProcess

class Dialog_Utils {
    companion object {
        var int = 0
        fun createFolder(context: Context, viemodel: Notes_ViewModel, clcik: (String) -> Unit) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogueBinding = NewFolderLayoutBinding.inflate(inflater)
            val dialog =
                Dialog(context, R.style.Custm_Dialod_Save)
            dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
            dialog.setContentView(dialogueBinding.root)
            dialogueBinding.apply {
                cancel.setOnClickListener {
                    dialog.dismiss()
                }
                editText.addTextChangedListener(object : TextWatcher {
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
                            create.setBackgroundResource(R.drawable.unlock_pink_btn)
                            create.setTextColor(Color.parseColor("#000000"))
                        } else {
                            create.setBackgroundResource(R.drawable.dialog_cancel)
                            create.setTextColor(Color.parseColor("#B7005E"))
                        }
                    }

                })
                create.setOnClickListener {
                    if (!editText.text.trim().isNullOrEmpty()) {
                        viemodel.addfolder(Folder(0, editText.text.toString()))
                        clcik.invoke("create")
                        editText.text.clear()
                        dialog.dismiss()
                    } else {
                        Toast.makeText(context, "Enter Folder Name", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            dialog.show()
        }

        @SuppressLint("NotifyDataSetChanged")
        fun deleteFolder(
            context: Activity,
            viemodel: Notes_ViewModel,
            folder: Folder,
            position: Int,
            click: (Int) -> Unit
        ) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogueBinding = DeletFolderLayoutBinding.inflate(inflater)
            val dialog =Dialog(context, R.style.Custm_Dialod_Save)
            dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)

            dialog.setContentView(dialogueBinding.root)
            dialogueBinding.apply {
                keepit.setOnClickListener {
                    dialog.dismiss()
                }

                delete.setOnClickListener {
                    AdsManager.showIntersWithClick(context) {
                        viemodel.deletefolder(Folder(folder.id, folder.name))
                        click.invoke(position)
                        showToast(context, "Folder Deleted")

                        dialog.dismiss()
                    }
                }

            }
            dialog.show()
        }

        fun deleteFileFromMediaStore(contentResolver: ContentResolver, file: File) {
            var canonicalPath: String? = null
            try {
                canonicalPath = file.canonicalPath
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val uri = MediaStore.Files.getContentUri("external")
            val result = contentResolver.delete(
                uri,
                MediaStore.Files.FileColumns.DATA + "=?", arrayOf(canonicalPath)
            )
            if (result == 0) {
                val absolutePath = file.absolutePath
                if (absolutePath != canonicalPath) {
                    contentResolver.delete(
                        uri,
                        MediaStore.Files.FileColumns.DATA + "=?",
                        arrayOf(absolutePath)
                    )
                }
            }
        }

        @SuppressLint("NotifyDataSetChanged")
        fun deletedraft(
            context: Activity,
            currentItem: Any,
            viemodel: Notes_ViewModel,
            position: Int,
            list: MutableList<Any>,
            click: (Int) -> Unit
        ) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogueBinding = DeletDraftLayoutBinding.inflate(inflater)
            val dialog =
                Dialog(context, R.style.Custm_Dialod_Save)
            dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)

            dialog.setContentView(dialogueBinding.root)
            dialogueBinding.apply {
                keepit.setOnClickListener {
                    dialog.dismiss()
                }

                delete.setOnClickListener {
                    AdsManager.showIntersWithClick(context) {
                        if (currentItem is Draftdataclass) {
                            viemodel.deletedraftNotes(currentItem.id)
                            list.removeAt(position)
                            click.invoke(position)
                            showToast(context, "Draft Deleted")

                        } else if (currentItem is Notes) {
                            viemodel.deleteNotes(currentItem.id)
                            list.removeAt(position)
                            click.invoke(position)
                            showToast(context, "Note Deleted")

                        }

                        dialog.dismiss()
                    }


                }

            }
            dialog.show()
        }

        @SuppressLint("NotifyDataSetChanged")
        fun deleteAlldraft(context: Activity, viemodel: Notes_ViewModel, click: () -> Unit) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogueBinding = DeletDraftLayoutBinding.inflate(inflater)
            val dialog =
                Dialog(context, R.style.Custm_Dialod_Save)
            dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)

            dialog.setContentView(dialogueBinding.root)
            dialogueBinding.apply {
                deletetxt.text = "Clear All?"
                line2.text = "Are you sure you want to clear all drafts?"
                keepit.text = "Don't Clear"
                delete.text = "Yes, Clear"
                keepit.setOnClickListener {
                    dialog.dismiss()
                }

                delete.setOnClickListener {
                    viemodel.deletealldraftNotes()
                    showToast(context, "All Drafts Deleted")
                    click.invoke()
                    dialog.dismiss()
                }

            }
            dialog.show()
        }

        @SuppressLint("NotifyDataSetChanged")
        fun allFolders(
            context: Context,
            viemodel: Notes_ViewModel,
            list: MutableList<Any>,
            notes: Notes,
            click: (String) -> Unit
        ) {
            var foldername = ""
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogueBinding = MoveFolderLayoutBinding.inflate(inflater)
            val folderdialog = Dialog(context, R.style.Custm_Dialod_Save)
            folderdialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
            folderdialog.setContentView(dialogueBinding.root)
            folderdialog.show()
            dialogueBinding.apply {
                if (list.isNullOrEmpty()) {
                    folderrecyle.visibility = View.GONE
                    nofolder.visibility = View.VISIBLE
                    move.text = "Create"
                } else {
                    folderrecyle.layoutManager = LinearLayoutManager(context)
                    folderrecyle.adapter = moveFolderAdp(context, viemodel, list) {
                        move.setBackgroundResource(R.drawable.save_button)
                        move.setTextColor(Color.parseColor("#B7005E"))
                        foldername = it
                    }
                }


                cancel.setOnClickListener {
                    folderdialog.dismiss()
                }
                if (move.text == "Move") {
                    move.setOnClickListener {
                        folderdialog.dismiss()
                        if (!foldername.isNullOrEmpty()) {
                            notes.folderId = foldername
                            viemodel.updateNote(notes)
                            viemodel.searchNotesByFolder(foldername)
                            click.invoke(foldername)
                        }

                    }
                } else if (move.text == "Create") {
                    move.setBackgroundResource(R.drawable.save_button)
                    move.setTextColor(Color.parseColor("#B7005E"))
                    move.setOnClickListener {
                        folderdialog.cancel()
                        click.invoke("")
                    }

                }

            }
        }

        @SuppressLint("Recycle")
        fun GalleryDialog(
            context: Activity,
            list: MutableList<Any>,
            viemodel: Notes_ViewModel,
            click: (String) -> Unit
        ) {
            var imagepath = ""
            var state = false
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogueBinding = GalleryLayoutBinding.inflate(inflater)
            val dialog =
                Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
            dialog.setContentView(dialogueBinding.root)
            dialogueBinding.apply {
                galleryrecycle.layoutManager = GridLayoutManager(context, 3)
                galleryrecycle.setHasFixedSize(true)
                AdmobManager.loadBannerAd1(context,bannerAd)
                galleryrecycle.adapter = Background_Apater(context, list) { background, file ->
                    imagepath = file
                    next.setBackgroundResource(R.drawable.menu_button)
                    state = false
                    folderrecycle.visibility = View.GONE
                    dropdown.setImageResource(R.drawable.arrow_drop_up)
                }
                listmenue.setOnClickListener {
                    if (!state) {
                        state = true
                        folderrecycle.visibility = View.VISIBLE
                        dropdown.setImageResource(R.drawable.arrow_drop_down)
                        folderrecycle.layoutManager = LinearLayoutManager(context)
                        folderrecycle.adapter = FolderAdapter(
                            context,
                            viemodel,
                            util.getImageDirectories(context)
                        ) { path, folder ->
                            state = false
                            folderrecycle.visibility = View.GONE
                            dropdown.setImageResource(R.drawable.arrow_drop_up)
                            list.clear()
                            val file = File(path)
                            val listimages = file.listFiles()
                            for (i in listimages) {
                                list.add(GallerY_DataClass(i.path))
                            }
                            galleryrecycle.adapter =
                                Background_Apater(context, list) { background, file ->
                                    click.invoke(file)
                                    imagepath = file
                                    next.setBackgroundResource(R.drawable.menu_button)
                                    state = false
                                    folderrecycle.visibility = View.GONE
                                    dropdown.setImageResource(R.drawable.arrow_drop_up)
                                }
                        }

                    } else {
                        state = false
                        folderrecycle.visibility = View.GONE
                        dropdown.setImageResource(R.drawable.arrow_drop_up)
                    }
                }
                cancel.setOnClickListener {
                    AdsManager.showIntersWithClick(context) {
                        imagepath = ""
                        dialog.dismiss()
                    }
                }
                next.setOnClickListener {
                    if (!imagepath.isNullOrEmpty())
                        AdsManager.showIntersWithClick(context) {
                            click.invoke(imagepath)
                            dialog.dismiss()
                        }

                }
            }

            dialog.show()

        }

        fun yourMood(context: Context, binding: FragmentDairyPageBinding, click: (Int) -> Unit) {
            val bottomSheetDialog = BottomSheetDialog(context)
            val bottomSheetBinding = MoodLayoutBinding.inflate(LayoutInflater.from(context))
            bottomSheetDialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
            bottomSheetDialog.setContentView(bottomSheetBinding.root)
            bottomSheetDialog.show()

            bottomSheetBinding.apply {
                imagge1.setOnClickListener {
                    int = R.drawable.emojigood
                    binding.mood.setImageResource(int)

                }
                imagge2.setOnClickListener {
                    int = R.drawable.emojihappy
                    binding.mood.setImageResource(int)

                }
                imagge3.setOnClickListener {
                    int = R.drawable.emojiconfused
                    binding.mood.setImageResource(int)


                }
                imagge4.setOnClickListener {
                    int = R.drawable.emojisad
                    binding.mood.setImageResource(int)

                }
                imagge5.setOnClickListener {
                    int = R.drawable.emojicrying
                    binding.mood.setImageResource(int)

                }
                okay.setOnClickListener {
                    click.invoke(int)
                    bottomSheetDialog.dismiss()
                }
                cancel.setOnClickListener {
                    binding.mood.setImageResource(int)
                    bottomSheetDialog.dismiss()
                }
                bottomSheetDialog.setOnCancelListener {
                    binding.mood.setImageResource(int)
                }
            }

        }

        fun changepasswordmethod(context: Context, click: (String) -> Unit) {
            var method = "6Digit"
            val dailog = AlertDialog.Builder(context, R.style.Custm_Dialod_Save)
            val view = LayoutInflater.from(context).inflate(R.layout.change_password_method, null)
            dailog.setView(view)
            val alertDialog = dailog.create()
            alertDialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
            var cancel = view.findViewById<TextView>(R.id.cancel)
            var okay = view.findViewById<TextView>(R.id.okay)
            var pin_sellect = view.findViewById<ImageView>(R.id.pin_sellect)
            var pass_select = view.findViewById<ImageView>(R.id.pass_select)
            var fingerprit_sellect = view.findViewById<ImageView>(R.id.fingerprit_sellect)
            SharedPref.putString("method", "6Digit")
            pin_sellect.setOnClickListener {
                method = "6Digit"
                pin_sellect.setImageResource(R.drawable.sellect_icon)
                pass_select.setImageResource(R.drawable.unsellect_icon)
                fingerprit_sellect.setImageResource(R.drawable.unsellect_icon)
                okay.setBackgroundResource(R.drawable.white_gradient)
                okay.setTextColor(Color.parseColor("#B7005E"))
            }
            pass_select.setOnClickListener {
                method = "password"
                pin_sellect.setImageResource(R.drawable.unsellect_icon)
                pass_select.setImageResource(R.drawable.sellect_icon)
                fingerprit_sellect.setImageResource(R.drawable.unsellect_icon)
                okay.setBackgroundResource(R.drawable.white_gradient)
                okay.setTextColor(Color.parseColor("#B7005E"))
            }
            fingerprit_sellect.setOnClickListener {
                method = "Fingerprint"
                pin_sellect.setImageResource(R.drawable.unsellect_icon)
                pass_select.setImageResource(R.drawable.unsellect_icon)
                fingerprit_sellect.setImageResource(R.drawable.sellect_icon)
                okay.setBackgroundResource(R.drawable.white_gradient)
                okay.setTextColor(Color.parseColor("#B7005E"))
            }
            cancel.setOnClickListener {
                alertDialog.dismiss()
            }
            okay.setOnClickListener {
                click.invoke(method)
                alertDialog.dismiss()

            }
            alertDialog.show()
        }

        fun confirmdialog(
            context: Context,
            controller: NavController,
            method: String,
            password: String,
            question: String
        ) {
            val dailog = AlertDialog.Builder(context, R.style.Custm_Dialod_Save)
            val view = LayoutInflater.from(context).inflate(R.layout.lockconfirm_layout, null)
            dailog.setView(view)
            val alertDialog = dailog.create()
            alertDialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
            val cancel = view.findViewById<TextView>(R.id.cancel)
            val okay = view.findViewById<TextView>(R.id.okay)
            cancel.setOnClickListener {
                SharedPref.putBolean("startscreen", false)
                SharedPref.putString("password", "")
                SharedPref.putString("method", "$method")
                alertDialog.dismiss()
            }
            okay.setOnClickListener {
                SharedPref.putString("password", "$password")
                SharedPref.putBolean("startscreen", true)
                SharedPref.putString("method", "$method")
                if (question != "")
                    SharedPref.putString("question", question)
                alertDialog.dismiss()
                controller.navigate(R.id.action_lockFragment_to_dashboardFragment)
            }
            alertDialog.show()
        }

        fun renamefolder(
            context: Activity,
            viemodel: Notes_ViewModel,
            folder: Folder,
            click: () -> Unit
        ) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogueBinding = NewFolderLayoutBinding.inflate(inflater)
            val dialog =
                Dialog(context, R.style.Custm_Dialod_Save)
            dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
            dialog.setContentView(dialogueBinding.root)
            dialogueBinding.apply {
                foldertext.text = StringBuilder("Rename Folder")
                create.text = StringBuilder("Rename")
                val name = folder.name
                val seprated = name.split(".").toTypedArray()
                editText.setText(seprated[0])
                var extension = ""
                if (seprated.size > 1) {
                    extension = "." + seprated[1]
                }
                val finalExtension = extension
                cancel.setOnClickListener {
                    dialog.dismiss()
                }
                editText.setOnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        create.setBackgroundResource(R.drawable.dialog_cancel)
                        create.setTextColor(Color.parseColor("#B7005E"))
                    } else {
                        create.setBackgroundResource(R.drawable.unlock_pink_btn)
                        create.setTextColor(Color.parseColor("#000000"))
                    }

                }
                create.setOnClickListener {
                    AdsManager.showIntersWithClick(context) {
                        viemodel.updatefolder(Folder(folder.id, editText.text.toString()))
                        click.invoke()
                        dialog.dismiss()
                    }
                }

            }
            dialog.show()
        }

        fun exitDialog(context: Activity) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogueBinding = ExitDialogBinding.inflate(LayoutInflater.from(context))
            val dialog =
                Dialog(context, R.style.Custm_Dialod_Save)
            dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)

            dialog.setContentView(dialogueBinding.root)
            dialogueBinding.apply {
                AdsManager.showSmallNative(context,nativeAddialog,"exitDialog")
                no.setOnClickListener {
                    util.logAnalytic(context, "not_now")
                    dialog.dismiss()
                }

                exit.setOnClickListener {
                    util.logAnalytic(context, "yes_exit")
                    context.finishAffinity()
                    exitProcess(0)

                }

            }
            dialog.show()
        }

        fun rateusDialog(context: Context) {
            var number = 1
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogueBinding = RatusDialogBinding.inflate(inflater)
            val dialog =
                Dialog(context, R.style.Custm_Dialod_Save)
            dialog.window?.decorView?.setBackgroundColor(Color.TRANSPARENT)
            dialog.setContentView(dialogueBinding.root)
            dialogueBinding.apply {
                start1.setOnClickListener {
                    number = 1
                    start1.setImageResource(R.drawable.star)
                    start2.setImageResource(R.drawable.start_unsellected)
                    start3.setImageResource(R.drawable.start_unsellected)
                    start4.setImageResource(R.drawable.start_unsellected)
                    start5.setImageResource(R.drawable.start_unsellected)
                }
                start2.setOnClickListener {
                    number = 2
                    start1.setImageResource(R.drawable.star)
                    start2.setImageResource(R.drawable.star)
                    start3.setImageResource(R.drawable.start_unsellected)
                    start4.setImageResource(R.drawable.start_unsellected)
                    start5.setImageResource(R.drawable.start_unsellected)
                }
                start3.setOnClickListener {
                    number = 3
                    start1.setImageResource(R.drawable.star)
                    start2.setImageResource(R.drawable.star)
                    start3.setImageResource(R.drawable.star)
                    start4.setImageResource(R.drawable.start_unsellected)
                    start5.setImageResource(R.drawable.start_unsellected)
                }
                start4.setOnClickListener {
                    number = 4
                    start1.setImageResource(R.drawable.star)
                    start2.setImageResource(R.drawable.star)
                    start3.setImageResource(R.drawable.star)
                    start4.setImageResource(R.drawable.star)
                    start5.setImageResource(R.drawable.start_unsellected)
                }
                start5.setOnClickListener {
                    number = 5
                    start1.setImageResource(R.drawable.star)
                    start2.setImageResource(R.drawable.star)
                    start3.setImageResource(R.drawable.star)
                    start4.setImageResource(R.drawable.star)
                    start5.setImageResource(R.drawable.star)
                }
                notnow.setOnClickListener {
                    dialog.dismiss()
                }

                rate.setOnClickListener {
                    util.logAnalytic(context, "rate_app")
                    if (number >= 4) {
                        try {
                            val uri =
                                Uri.parse("https://play.google.com/store/apps/details?id="+context.applicationContext.packageName)
                            val rateAppIntent = Intent(Intent.ACTION_VIEW, uri)
                            context.startActivity(rateAppIntent)
                        } catch (e: java.lang.Exception) {
                            Toast.makeText(context, "" + e.message, Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(context, "Thanks for your Feedback", Toast.LENGTH_SHORT)
                            .show()
                    }
                    dialog.dismiss()
                }

            }
            dialog.show()
        }

        fun showDialogPrivacyPOlicy(context: Context) {
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val dialogueBinding = PrivacypolicydialogueBinding.inflate(inflater)
            val dialog =
                Dialog(context, android.R.style.Theme_DeviceDefault_Light_NoActionBar_Fullscreen)
            dialog.setContentView(dialogueBinding.root)
            Log.d("LAY_OUT_BIN", "CHECK :" + dialog);
            dialogueBinding.imgBackArrow.setOnClickListener {
                dialog.dismiss()
            }
            val webView = dialogueBinding.webView
            webView.loadUrl("https://sites.google.com/view/my-daily-dairy-lock/home")
            dialogueBinding.imgBackArrow.setOnClickListener {
                dialog.dismiss();
            }
            dialog.show()
        }
    }


}