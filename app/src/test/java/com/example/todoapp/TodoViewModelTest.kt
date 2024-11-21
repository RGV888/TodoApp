package com.example.todoapp

import com.example.todoapp.repository.TodoRepository
import com.example.todoapp.roomdb.entitys.TodoEntity
import com.example.todoapp.viewmodels.TodoViewModel
import com.example.todoapp.viewmodels.UiState
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test


@ExperimentalCoroutinesApi
class UserViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var todoViewModel: TodoViewModel
    private val todoRepository: TodoRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        todoViewModel = TodoViewModel(todoRepository)
    }


    @Test
    fun `test getTodos should emit Success when data is loaded`() = runBlocking {
        val todoList = listOf(TodoEntity(1, "Task 1"), TodoEntity(2, "Task 2"))
        coEvery { todoRepository.getAllTodos() } returns flow { emit(todoList) }

        val uiStateList = mutableListOf<UiState<Unit>>()

        todoViewModel.uiState.take(2).collect { uiState ->
            uiStateList.add(uiState)
        }

        assert(uiStateList[0] is UiState.Loading)
        assert(uiStateList[1] is UiState.Success<*>)
        assert(todoViewModel.todos.value == todoList)
    }

        @Test
    fun `test getTodos should emit Error when an exception occurs`() = runBlocking {

        val exceptionMessage = "Failed to fetch data"
        coEvery { todoRepository.getAllTodos() } throws Exception(exceptionMessage)

        val uiStateList = mutableListOf<UiState<Unit>>()

        todoViewModel.uiState.collect { uiState ->
            uiStateList.add(uiState)
        }

        assert(uiStateList[0] is UiState.Loading)
        val errorState = uiStateList[1] as UiState.Error
        assert(errorState.message == "Failed to get Todos")
    }

    @After
    fun tearDown() {
        clearAllMocks()
        Dispatchers.resetMain() // Reset Main dispatcher
    }
}