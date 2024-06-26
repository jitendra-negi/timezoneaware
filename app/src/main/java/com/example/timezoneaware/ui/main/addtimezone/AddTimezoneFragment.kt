package com.example.timezoneaware.ui.main.addtimezone

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.timezoneaware.R
import com.example.timezoneaware.data.model.InputTimezone
import com.example.timezoneaware.databinding.FragmentAddtimezoneBinding
import com.example.timezoneaware.utils.DataResult
import com.example.timezoneaware.utils.DateUtils
import com.example.timezoneaware.utils.Prefs
import com.example.timezoneaware.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_addtimezone.*
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class AddTimezoneFragment : Fragment() {

    private lateinit var binding: FragmentAddtimezoneBinding
    private val viewModel: AddTimezoneViewModel by viewModels()
    var prefs: Prefs? = null
    private var current: Calendar? = null
    private var miliSeconds: Long = 0
    private var sdf: SimpleDateFormat? = null
    private var resultdate: Date? = null
    var idArray = TimeZone.getAvailableIDs()
    var gmtDifference: String? = null
    var timeZoneCity: String? = null
    var userId: Int = 0
    var timeZoneId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentAddtimezoneBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = Prefs(context!!)
        setBackHomeList()
        setUpTimezoneSniperAdapter()
        sdf = SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss")
        getGMTTime()
        setSpinnerFeatures()
        setUpEnterCityTimezoneData()
        attachApiResponse()
        attachProgressBar()
        bundleData()

    }

    private fun bundleData() {
        if (arguments != null) {
            val timezoneId = arguments!!.getInt(TIMEZONE_ID)
            val cityName = arguments!!.getString(CITY_NAME)
            val cityTimeZone = arguments!!.getString(CITY_TIMEZONE)
            timeZoneId = timezoneId
            etCityName.setText(cityName)
            binding.spinnerTimezone.setSelection(
                (binding.spinnerTimezone.adapter as ArrayAdapter<String?>).getPosition(
                    cityTimeZone
                )
            )
            binding.btnSubmit.text= "Update Timezone"
        }

    }

    private fun setUpTimezoneSniperAdapter() {
        val idAdapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_item, idArray
        )
        // Set layout to use when the list of choices appear
        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        // Set Adapter to Spinner
        binding.spinnerTimezone.adapter = idAdapter
    }

    private fun setBackHomeList() {
        binding.apply {
            btn_back.setOnClickListener {
                navToTimezoneList()
            }
        }
    }

    //Get Current Gmt Time
    private fun getGMTTime() {
        current = Calendar.getInstance()
        binding.tvGmtTime.text = current!!.time.toString()
        miliSeconds = current!!.timeInMillis
        val tzCurrent: TimeZone = current!!.timeZone
        var offset: Int = tzCurrent.rawOffset
        if (tzCurrent.inDaylightTime(Date())) {
            offset += tzCurrent.dstSavings
        }
        miliSeconds -= offset
        resultdate = Date(miliSeconds)
        println(sdf!!.format(resultdate))
    }

    private fun setSpinnerFeatures() {
        binding.apply {
            spinnerTimezone.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>,
                    view: View?, position: Int, id: Long
                ) {
                    getGMTTime()
                    val selectedId = parent
                        .getItemAtPosition(position) as String
                    tvCurrentTime.text = DateUtils.getTimezoneGMT(selectedId)

                    Log.e(TAG, "onItemSelected: $selectedId")

                    val timezone = TimeZone.getTimeZone(selectedId)

                    val TimeZoneName = timezone.displayName
                    timeZoneCity = selectedId

                    val timeZoneOffset = (timezone.rawOffset / (60 * 1000)) //return GMT Values
                    val hrs = timeZoneOffset / 60 //to get the hours
                    val mins = timeZoneOffset % 60 //to get minutes

                    miliSeconds += timezone.rawOffset
                    resultdate = Date(miliSeconds)
                    tvCurrentTime.text = (TimeZoneName + " : GMT " + hrs + "." + mins)
                    gmtDifference = buildString {
                        append(hrs)
                        append(".")
                        append(mins)
                    }
                    miliSeconds = 0
                }
                override fun onNothingSelected(arg0: AdapterView<*>?) {}
            }
        }
    }


    private fun setUpEnterCityTimezoneData() {
        binding.apply {

            etCityName.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnSubmit.performClick()
                    return@setOnEditorActionListener true
                }
                false
            }

            btnSubmit.setOnClickListener {
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val timeZoneCityName = etCityName.text.toString().trim()
                val timeZoneCityNameTimeZone = timeZoneCity!!.trim()
                val timeZoneCityGMTDifference = gmtDifference.toString()
                val userId = prefs!!.userIdPref.toString()

                val isCitynameValid = validateCityName(timeZoneCityName)
                if (!isCitynameValid) cityNameContainer.error = getString(R.string.invalid_city)

                if (isCitynameValid) {
                    val cityTimezoneData = InputTimezone(
                        timeZoneCityName,
                        timeZoneCityNameTimeZone,
                        timeZoneCityGMTDifference,
                        userId
                    )
                    if (timeZoneId!=0){
                        viewModel.updateTimezoneData(timeZoneId.toString(),cityTimezoneData)
                    }else{
                        viewModel.addTimezoneData(cityTimezoneData)
                    }

                    cityNameContainer.error = null

                } else {
                    Toast.makeText(
                        context,
                        "City input is not valid",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                imm.hideSoftInputFromWindow(it.windowToken, 0)
                it.clearFocus()
            }
        }
    }


    private fun validateCityName(city: String): Boolean {
        var valid = false
        if (city.isNotEmpty() && city.length >= 2) {
            valid = true
        }
        return valid
    }

    private fun attachProgressBar() {
        viewModel.loading.observe(viewLifecycleOwner, Observer { show ->
            binding.progressBar.show(show)
        })
    }

    private fun attachApiResponse() {
        viewModel.data.observe(viewLifecycleOwner, Observer { response ->
            response?.let { result ->
                when (result) {
                    is DataResult.GenericError -> {
                        Log.d(
                            "AddTimezoneFragment",
                            "code- ${result.code} error message- ${result.errorMessages}"
                        )
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            context,
                            "Something Went Wrong!!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataResult.NetworkError -> {
                        Log.d(
                            "AddTimezoneFragment",
                            "network error message- ${result.networkError}"
                        )
                        Toast.makeText(
                            context,
                            "No Internet - ${result.networkError}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    is DataResult.Success -> {
                        if (result.value.success) {
                            navToTimezoneList()
                        }
                    }
                }
            }
        })
    }

    private fun navToTimezoneList() {
        findNavController().navigate(R.id.action_addTimezoneFragment_to_timezoneListFragment)
    }

}

const val TIMEZONE_ID = "TIMEZONE_ID"
const val CITY_NAME = "CITY_NAME"
const val CITY_TIMEZONE = "CITY_TIMEZONE"
private const val TAG = "AddTimezoneFragment"