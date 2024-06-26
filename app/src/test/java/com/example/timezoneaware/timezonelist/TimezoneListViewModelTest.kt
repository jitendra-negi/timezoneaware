package com.example.timezoneaware.timezonelist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.timezoneaware.TestDispatchers
import com.example.timezoneaware.data.model.TimeZonesResponse
import com.example.timezoneaware.data.repository.timezone.TimeZoneRepository
import com.example.timezoneaware.ui.main.timezonelist.TimezoneListViewModel
import com.example.timezoneaware.utils.DataResult.*
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import io.mockk.MockKAnnotations
import junit.framework.Assert.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4


@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class TimezoneListViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    private val repository = mock<TimeZoneRepository>()
    private lateinit var viewModel: TimezoneListViewModel
    private val id = "1"

    @Before
    fun setUpTest() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this)
        viewModel = TimezoneListViewModel(TestDispatchers, repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    /***
     * Test cases:
     * Getting TimeZone list Successfully
     */
    @Test
    fun `when getting list of timezones it should return the add success as result`() = runTest {
        val result = mock<TimeZonesResponse>()
        whenever(repository.getTimezoneListData(id)).thenReturn(Success(result))
        viewModel.getTimezonesList(id)
        assertEquals(Success(result), viewModel.data.value)
    }

    /***
     * Test cases:
     * Getting timezone list throws IOException as NetworkError
     */
    @Test
    fun `when getting list of timezones it should return throws IOException then it should emit the result as NetworkError`() =
        runTest {
            whenever(repository.getTimezoneListData(id)).thenReturn(NetworkError("Network error"))
            viewModel.getTimezonesList(id)
            assertEquals(NetworkError("Network error"), viewModel.data.value)
        }

    /***
     * Test cases:
     * Editing timezone and throws throws IOException as GenericError
     */
    @Test
    fun `when getting list of timezone should return throws IOException then it should emit the result as GenericError`() =
        runTest {
            whenever(repository.getTimezoneListData(id)).thenReturn(
                GenericError(
                    433,
                    "error message"
                )
            )
            viewModel.getTimezonesList(id)
            assertEquals(
                GenericError(
                    433,
                    "error message"
                ), viewModel.data.value
            )
        }
}