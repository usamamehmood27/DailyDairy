package com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.akexorcist.localizationactivity.ui.LocalizationActivity
import com.example.DailyDiary.R
import com.example.DailyDiary.databinding.FragmentLanguageBinding
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.adapter.LanguageAdapter
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.classes.SharedPref
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.dataclasses.ModelClasLanguges
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.utils.util.Companion.logAnalytic
import com.mydiary.diary.diarywithlock.secretdiary.personaldiary.easynote.privatejournal.dailydiary.viewmodel.Notes_ViewModel


class LanguageFragment : Fragment() {
    lateinit var binding: FragmentLanguageBinding
    lateinit var viewmodel: Notes_ViewModel
    var languageAdapter: LanguageAdapter? = null
    var listadp: MutableList<Any> = ArrayList()
    var log=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLanguageBinding.inflate(layoutInflater)
        viewmodel = ViewModelProvider(requireActivity())[Notes_ViewModel::class.java]

        return binding.root
    }

//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        binding.apply {
//            backpress.setOnClickListener {
//                AdsManager.countInterstitialCapping(requireActivity())
//                logAnalytic(requireContext(), "Event_LanguageBackPress")
//                requireActivity().onBackPressed()
//            }
//            listOfCountries(listadp)
//            binding.langRecyclerView.layoutManager = LinearLayoutManager(requireContext())
//            languageAdapter = LanguageAdapter(requireActivity(), listadp) { code, position ->
//                try {
//                    log+="clicked \n"
//                    val model: ModelClasLanguges = listadp[position] as ModelClasLanguges
//                    log+="setting model \n"
//                    (requireActivity() as LocalizationActivity).setLanguage(code)
//                    log+="setting language \n"
//                    SharedPref.putString("PHOTO_LANG_SELECTED", model.language_code!!)
//                    log+="setting shared prefrence \n"
//                    requireActivity().onBackPressed()
//                    log+="setting backpress \n"
//                    binding.logCheck.text=log
//                }catch (e:InvocationTargetException){
//                    binding.logCheck.text=""
//                }
//
//            }
//            binding.langRecyclerView.adapter = languageAdapter
//        }
//    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            backpress.setOnClickListener {
                logAnalytic(requireContext(), "Event_LanguageBackPress")
                requireActivity().onBackPressed()
            }
            listOfCountries(listadp)
            binding.langRecyclerView.layoutManager = LinearLayoutManager(requireContext())
            languageAdapter = LanguageAdapter(requireActivity(), listadp) { code, position ->
                val model: ModelClasLanguges = listadp[position] as ModelClasLanguges
                (requireActivity() as LocalizationActivity).setLanguage(code)
                SharedPref.putString("PHOTO_LANG_SELECTED", model.language_code!!)
                requireActivity().onBackPressed()
            }
            binding.langRecyclerView.adapter = languageAdapter
        }
    }


    fun listOfCountries(list: MutableList<Any>) {
        list.add(ModelClasLanguges("American", false, "en", R.drawable.flag_en))
        list.add(ModelClasLanguges("Chinese", false, "zh", R.drawable.flag_hmn))
        list.add(ModelClasLanguges("Dutch", false, "nl", R.drawable.flag_lb))
        list.add("")
        list.add(ModelClasLanguges("French", false, "fr", R.drawable.flag_fr_fr))
        list.add(ModelClasLanguges("German", false, "de", R.drawable.flag_de))
        list.add(ModelClasLanguges("Korean", false, "Ko", R.drawable.flag_ko))
        list.add(ModelClasLanguges("Pakistan", false, "ur", R.drawable.flag_pa))
        list.add(ModelClasLanguges("Japanese", false, "ja", R.drawable.flag_jw))
        list.add(ModelClasLanguges("Persian", false, "fa", R.drawable.flag_fa))
        list.add(ModelClasLanguges("Portugues", false, "pt", R.drawable.flag_pt_pt))
        list.add(ModelClasLanguges("Russian", false, "ru", R.drawable.flag_ru))
        list.add(ModelClasLanguges("Spanish", false, "es", R.drawable.flag_es_es))
        list.add(ModelClasLanguges("Turkish", false, "tr", R.drawable.flag_tr))
    }

}