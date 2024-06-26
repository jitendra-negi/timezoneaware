package com.example.timezoneaware.ui.main.editusers

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.timezoneaware.R
import com.example.timezoneaware.databinding.FragmentEditUserBinding
import com.example.timezoneaware.utils.DataResult
import com.example.timezoneaware.utils.Prefs
import com.example.timezoneaware.utils.show
import kotlinx.android.synthetic.main.fragment_addtimezone.*
import java.util.*
import androidx.lifecycle.Observer
import com.example.timezoneaware.data.model.InputUserData
import com.example.timezoneaware.ui.main.user.USER_ID
import com.example.timezoneaware.ui.main.user.USER_NAME
import com.example.timezoneaware.ui.main.user.USER_TYPE
import com.example.timezoneaware.utils.HelperUtils
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_addtimezone.btn_back
import kotlinx.android.synthetic.main.fragment_edit_user.*


@AndroidEntryPoint
class EditUserFragment : Fragment() {

    private lateinit var binding: FragmentEditUserBinding
    private val viewModel: EditUserViewModel by viewModels()
    var prefs: Prefs? = null
    var userId: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = FragmentEditUserBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = Prefs(context!!)
        setBackHomeList()
        setUpUserData()
        attachApiResponse()
        attachProgressBar()
        bundleData()

    }

    private fun bundleData() {
        if (arguments != null) {
            val userID = arguments!!.getInt(USER_ID)
            val userName = arguments!!.getString(USER_NAME)
            val userType = arguments!!.getInt(USER_TYPE)

            this.userId=userID
            binding.btnSubmit.text= "Update User"
            binding.tvUsertype.text= "User Role: " + HelperUtils.getUserRoles(userType)
            etUserName.setText( userName.toString())
        }

    }


    private fun setBackHomeList() {
        binding.apply {
            btn_back.setOnClickListener {
                navToUserList()
            }
        }
    }


    private fun setUpUserData() {
        binding.apply {

            etUserName.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    btnSubmit.performClick()
                    return@setOnEditorActionListener true
                }
                false
            }

            btnSubmit.setOnClickListener {
                val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                val username = etUserName.text.toString().trim()

                val id = prefs!!.userIdPref.toInt()

                val isUserNameValid = validateUserName(username)
                if (!isUserNameValid) userNameContainer.error = getString(R.string.invalid_user)

                if (isUserNameValid) {
                    val userData = InputUserData(
                        username
                    )
                    if (userId!=0){
                        viewModel.updateUserData(id,userId,userData)
                    }/*else{
                        viewModel.addTimezoneData(cityTimezoneData)
                    }*/

                    userNameContainer.error = null

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




    private fun validateUserName(userName: String): Boolean {
        var valid = false
        if (userName.isNotEmpty() && userName.length >= 6) {
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
                            navToUserList()
                        }
                    }
                }
            }
        })
    }

    private fun navToUserList() {
        findNavController().navigate(R.id.action_editUserFragment_to_userListFragment)
    }

}