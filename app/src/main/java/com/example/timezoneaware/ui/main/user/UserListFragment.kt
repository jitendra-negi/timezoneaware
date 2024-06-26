package com.example.timezoneaware.ui.main.user

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.timezoneaware.R
import com.example.timezoneaware.data.model.User
import com.example.timezoneaware.databinding.FragmentUserListBinding
import com.example.timezoneaware.prefs
import com.example.timezoneaware.utils.DataResult
import com.example.timezoneaware.utils.Prefs
import com.example.timezoneaware.utils.show
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UserListFragment : Fragment() {

    private lateinit var usersList: List<User>
    private lateinit var binding: FragmentUserListBinding
    private val viewModel: UserListViewModel by viewModels()
    var userId: Int = 0
    val bundle = Bundle()
    private lateinit var rvAdapter: UsersAdapter

    companion object {
        private const val TAG = "UserListFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentUserListBinding.inflate(inflater)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //  setAddTimezone()  //Add User option Enable
        getUserId()
        setBackHomeList()
        getUserListData()  //fetch users data
        attachApiResponse()
        attachSimpleApiResponse()
        attachProgressBar()
    }

    private fun setUpRecyclerView() {

        if (this::rvAdapter.isInitialized) {

            rvAdapter.update(usersList)
        } else {
            // create  layoutManager
            val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(activity!!)
            // pass it to rvLists layoutManager
            binding.recycler.layoutManager = layoutManager
            // initialize the adapter,
            // and pass the required argument
            rvAdapter = UsersAdapter(usersList)
            // attach adapter to the recycler view
            binding.recycler.adapter = rvAdapter

            rvAdapter.onItemClick = { pos, view ->
                when (view.id) {
                    R.id.btn_delete -> {
                        Log.e("delete", pos.toString())
                         deleteUserData(usersList.get(pos).userId)
                    }

                    R.id.btn_edit -> {
                        Log.e("Edit", pos.toString())

                        navToEditUser(usersList[pos].userId,
                            usersList[pos].userName,
                            usersList[pos].userType!!
                        )
                    }
                }
            }
        }
    }


    private fun navToEditUser(userId: Int,userName: String, userType: Int) {
        bundle.putInt(USER_ID, userId)
        bundle.putString(USER_NAME, userName)
        bundle.putInt(USER_TYPE, userType)
        findNavController().navigate(R.id.action_userListFragment_to_editUserFragment, bundle)
    }

    private fun deleteUserData(userId:Int) {
        binding.apply {
            val id = prefs!!.userIdPref.toString()
            viewModel.deleteUserAccount(id,userId)
        }
    }

    private fun getUserListData() {
        binding.apply {
            val userId = prefs!!.userIdPref.toString()
            viewModel.getUserList(userId)
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
                                usersList = result.value.data
                                setUpRecyclerView()
                            }

                        } else {

                            if (result.value.data == null) {
                                usersList = arrayListOf<User>()
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
                            "UserListFragment",
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
                            "UserListFragment",
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
                            getUserListData()
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

    private fun navToStartScreen() {
        findNavController().navigate(R.id.action_timezoneFragment_to_authFragment)
    }

    private fun setBackHomeList() {
        binding.txtBack.setOnClickListener {
            findNavController().navigate(R.id.action_userListFragment_to_timezoneListFragment)
        }
    }


}

const val USER_ID = "USER_ID"
const val USER_TYPE = "USER_TYPE"
const val USER_NAME = "USER_NAME"