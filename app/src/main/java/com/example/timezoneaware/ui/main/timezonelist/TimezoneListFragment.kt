package com.example.timezoneaware.ui.main.timezonelist

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timezoneaware.R
import com.example.timezoneaware.data.model.TimeZoneInfo
import com.example.timezoneaware.databinding.FragmentTimezoneListBinding
import com.example.timezoneaware.prefs
import com.example.timezoneaware.utils.DataResult
import com.example.timezoneaware.utils.Prefs
import com.example.timezoneaware.utils.show
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TimezoneListFragment : Fragment() {

    private lateinit var cityList: List<TimeZoneInfo>
    private lateinit var binding: FragmentTimezoneListBinding
    private val viewModel: TimezoneListViewModel by viewModels()
    var userId: Int = 0
    val bundle = Bundle()
    private lateinit var rvAdapter: CustomAdapter

    companion object {
        private const val TAG = "TimezoneListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentTimezoneListBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setAddTimezone()
        getUserId()
        setLogout()
        showHideAdminFeature()
        setupSearchView()
        //load api data
        getTimeZoneData()
        attachApiResponse()
        attachSimpleApiResponse()
        attachProgressBar()
    }

    private fun showHideAdminFeature() {
        val roleId = prefs!!.userTypePref

        binding.apply {
            textAdmin.setOnClickListener {
               navToUserList()
            }
            if(roleId==0 )
            {
                fab.visibility=View.VISIBLE
                textAdmin.visibility= View.GONE
            } else {
                fab.visibility=View.GONE
                textAdmin.visibility= View.VISIBLE
            }
        }



    }

    private fun setupSearchView() {
        // searchByCity queryListener
        binding.apply {
//            searchByCity.isIconified = false;
            searchByCity.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    if (query.isNotEmpty()) {
                        getTimeZoneByCity(query)
                    }
                    return false
                }
                override fun onQueryTextChange(newText: String): Boolean {
                    if (newText.isEmpty()) {
                        getTimeZoneData()
                    }
                    return false
                }
            })
        }

    }

    private fun setUpRecyclerView() {

        if (this::rvAdapter.isInitialized) {

            rvAdapter.update(cityList)
        } else {
            // create  layoutManager
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!)
            // pass it to rvLists layoutManager
            binding.recycler.layoutManager = layoutManager
            // initialize the adapter,
            // and pass the required argument
            rvAdapter = CustomAdapter(cityList)
            // attach adapter to the recycler view
            binding.recycler.adapter = rvAdapter

            rvAdapter.onItemClick = { pos, view ->
                when(view.id)
                {
                   R.id.btn_delete->{
                       Log.e("delete",pos.toString())
                       deleteTimeZoneData(cityList.get(pos).timeZoneId)
                   }

                    R.id.btn_edit->{

                        Log.e("Edit",cityList[pos].timeZoneId)
                        navToEditTimezone(cityList[pos].timeZoneId.toInt(),
                            cityList[pos].cityName,
                            cityList[pos].timeZoneCityName,
                        )
                    }
                }
            }
        }
    }

    private fun getTimeZoneData() {
        binding.apply {
            val userId = prefs!!.userIdPref.toString()
            viewModel.getTimezonesList(userId)
        }
    }

    private fun deleteTimeZoneData(id: String) {
        binding.apply {
            val userId = prefs!!.userIdPref.toString()
            viewModel.deleteTimeZone(id)
        }
    }

    private fun getTimeZoneByCity(inputCity : String) {
        binding.apply {
            val userId = prefs!!.userIdPref.toString()
            viewModel.searchCity(userId, inputCity)
        }
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
                            "TimezoneListFragment",
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
                            "TimezoneListFragment",
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
                            if (result.value.data != null) {
                                cityList = result.value.data
                                setUpRecyclerView()
                            }

                        } else {
                            if (result.value.data == null) {
                                cityList = arrayListOf<TimeZoneInfo>()
                                setUpRecyclerView()
                            }

                            Toast.makeText(
                                context,
                                result.value.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


            }
        })
    }

    private fun attachSimpleApiResponse() {
        viewModel.simpledata.observe(viewLifecycleOwner, Observer { response ->
            response?.let { result ->
                when (result) {
                    is DataResult.GenericError -> {
                        Log.d(
                            "TimezoneListFragment",
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
                            "TimezoneListFragment",
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
                            getTimeZoneData()
                        } else {
                            Toast.makeText(
                                context,
                                result.value.error,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }


            }
        })
    }

    private fun getUserId() {
        userId = Prefs(context!!).userIdPref
    }

    private fun setLogout() {
        binding.textLogout.setOnClickListener {
            prefs.isLoggedIn = false
            prefs.userTypePref = 0
            prefs.userIdPref = 0
            navToStartScreen()
        }
    }

    private fun navToAddTimezone() {
        bundle.putString(USER_ID, userId.toString())
        findNavController().navigate(R.id.action_timezoneFragment_to_addtimezoneFragment, bundle)
    }

    private fun setAddTimezone() {
        binding.apply {
            fab.setOnClickListener {
                navToAddTimezone()
            }
        }
    }

    private fun navToEditTimezone(timezoneId: Int,cityName: String, cityTimeZone: String) {
        bundle.putInt(TIMEZONE_ID, timezoneId)
        bundle.putString(CITY_NAME, cityName)
        bundle.putString(CITY_TIMEZONE, cityTimeZone)
        findNavController().navigate(R.id.action_timezoneFragment_to_addtimezoneFragment, bundle)
    }

    private fun navToStartScreen() {
        findNavController().navigate(R.id.action_timezoneFragment_to_authFragment)
    }

    private fun navToUserList() {
        findNavController().navigate(R.id.action_timezoneFragment_to_userListFragment)
    }

}

const val USER_ID = "USER_ID"
const val TIMEZONE_ID = "TIMEZONE_ID"
const val CITY_NAME = "CITY_NAME"
const val CITY_TIMEZONE = "CITY_TIMEZONE"