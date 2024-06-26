package com.example.timezoneaware.addtimezone

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.timezoneaware.TestDispatchers
import com.example.timezoneaware.data.model.InputTimezone
import com.example.timezoneaware.data.model.TimeZoneResponse
import com.example.timezoneaware.data.repository.timezone.TimeZoneRepository
import com.example.timezoneaware.ui.main.addtimezone.AddTimezoneViewModel
import com.example.timezoneaware.utils.DataResult.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.MockKAnnotations
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class AddTimezoneViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mock<TimeZoneRepository>()
    private lateinit var viewModel: AddTimezoneViewModel
    private val id = "1"

    private val inputTimeZone = InputTimezone(
        timeZoneCityName = "POP",
        timeZoneCityNameTimeZone = "Africa/Malabo",
        timeZoneCityGMTDifference = "1.0",
        userId = "1"
    )

    @Before
    fun setUpTest() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        viewModel = AddTimezoneViewModel(TestDispatchers, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /***
     * Test cases:
     * Add Timezone Successfully
     */
    @Test
    fun `when adding Timezone and it should return the edit success as result`() = runTest {
        val result = mock<TimeZoneResponse>()
        whenever(repository.addTimezone(inputTimeZone)).thenReturn(Success(result))
        viewModel.addTimezoneData(inputTimeZone)
        assertEquals(Success(result), viewModel.data.value)
    }

    /***
     * Test cases:
     * Adding Timezone throws IOException as NetworkError
     */
    @Test
    fun `when adding Timezone it should return throws IOException then it should emit the result as NetworkError`() =
        runTest {
            whenever(repository.addTimezone(inputTimeZone)).thenReturn(
                NetworkError(
                    "Network error"
                )
            )
            viewModel.addTimezoneData(inputTimeZone)
            assertEquals(NetworkError("Network error"), viewModel.data.value)
        }

    /***
     * Test cases:
     * Adding Timezone throws IOException as GenericError
     */
    @Test
    fun `when adding Timezone it should return throws IOException then it should emit the result as GenericError`() =
        runTest {
            whenever(repository.addTimezone(inputTimeZone)).thenReturn(
                GenericError(
                    433,
                    "error message"
                )
            )
            viewModel.addTimezoneData(inputTimeZone)
            assertEquals(
                GenericError(
                    433,
                    "error message"
                ), viewModel.data.value
            )
        }


}