package com.mehmetbaloglu.learnconnect

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mehmetbaloglu.learnconnect.data.repository.Repository
import com.mehmetbaloglu.learnconnect.ui.viewmodels.UserDetailViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class UserDetailViewModelTest {

    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule() // LiveData ve StateFlow'u senkron çalıştırır.

    private lateinit var viewModel: UserDetailViewModel
    private val mockRepository: Repository = mockk() // Repository mocklanır.
    private val testDispatcher = UnconfinedTestDispatcher()

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        viewModel = UserDetailViewModel(mockRepository)
    }

    @Test
    fun `getUserRegisteredCourses should fetch courses from repository`() = runTest {
        // Mock davranış
        val mockCourses = listOf("Course1", "Course2", "Course3")
        coEvery { mockRepository.getUserRegisteredCoursesFromFirestore("test@example.com") } returns mockCourses

        // Test edilen fonksiyon
        viewModel.getUserRegisteredCourses("test@example.com")

        // Sonuçları kontrol et
        assertEquals(mockCourses, viewModel.userRegisteredCourses.first())
        coVerify { mockRepository.getUserRegisteredCoursesFromFirestore("test@example.com") }
    }

    @Test
    fun `removeCourseFromRegisteredCourses should call repository and update list`() = runTest {
        // Mock davranış
        val updatedCourses = listOf("Course2", "Course3") // Kaldırma sonrası kurslar
        coJustRun { mockRepository.removeCourseFromRegisteredCoursesFirestore("test@example.com", "Course1") }
        coEvery { mockRepository.getUserRegisteredCoursesFromFirestore("test@example.com") } returns updatedCourses

        // Test edilen fonksiyon
        viewModel.removeCourseFromRegisteredCourses("test@example.com", "Course1")

        // Sonuçları kontrol et
        assertEquals(updatedCourses, viewModel.userRegisteredCourses.first())
        coVerify {
            mockRepository.removeCourseFromRegisteredCoursesFirestore("test@example.com", "Course1")
            mockRepository.getUserRegisteredCoursesFromFirestore("test@example.com")
        }
    }

    @Test
    fun `getUserFavCourses should fetch favorite courses from repository`() = runTest {
        // Mock davranış
        val mockFavCourses = listOf("FavCourse1", "FavCourse2")
        coEvery { mockRepository.getUserFavCoursesFromFirestore("test@example.com") } returns mockFavCourses

        // Test edilen fonksiyon
        viewModel.getUserFavCourses("test@example.com")

        // Sonuçları kontrol et
        assertEquals(mockFavCourses, viewModel.userFavCourses.first())
        coVerify { mockRepository.getUserFavCoursesFromFirestore("test@example.com") }
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
