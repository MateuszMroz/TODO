package com.example.todo.ui.todo

import com.example.todo.R
import com.example.todo.data.FakeToDoRepository
import com.example.todo.data.models.ToDo
import com.example.todo.util.BaseUnitTest
import com.example.todo.util.Event
import com.example.todo.util.ToDoMapper
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ToDoViewModelTest : BaseUnitTest() {
    private lateinit var todoViewModel: ToDoViewModel
    private lateinit var todoFakeRepository: FakeToDoRepository
    private lateinit var mapper: ToDoMapper

    private val todo1 = ToDo(
        title = "title1",
        description = "desc1",
        pictureUrl = null,
        timestamp = System.currentTimeMillis()
    )

    @Before
    fun setup() = runBlockingTest {
        mapper = ToDoMapper()
        todoFakeRepository = FakeToDoRepository()
        todoFakeRepository.addToDo(todo1)
    }

    @After
    fun tearDown() {
        todoFakeRepository.clearData()
    }

    @Test
    fun `save new todo success`() = runBlockingTest {
        todoViewModel = ToDoViewModel(todoFakeRepository, mapper, null)

        todoViewModel.title.value = "title NEW"
        todoViewModel.description.value = "description NEW"
        todoViewModel.pictureUrl.value = "pictureUrl NEW"

        todoViewModel.saveToDo()

        val successMsg: Event<Int> = todoViewModel.successMsg.getOrAwaitValue()
        assertThat(successMsg.getContentIfNotHandled(), `is`(R.string.todo_save_success))
    }

    @Test
    fun `save new todo failure`() = runBlockingTest {
        todoFakeRepository.shouldThrowError = true

        todoViewModel = ToDoViewModel(todoFakeRepository, mapper, null)

        todoViewModel.title.value = "title NEW"
        todoViewModel.description.value = "description NEW"
        todoViewModel.pictureUrl.value = "pictureUrl NEW"

        todoViewModel.saveToDo()

        val errorMsg: Event<String?> = todoViewModel.errorMsg.getOrAwaitValue()
        assertThat(errorMsg.getContentIfNotHandled(), `is`(todoFakeRepository.exceptionMsg))
    }

    @Test
    fun `fetch todo to edit success`() = runBlockingTest {
        todoViewModel = ToDoViewModel(todoFakeRepository, mapper, todo1.id)

        todoViewModel.fetchToDo()

        assertThat(todoViewModel.title.getOrAwaitValue(), `is`(todo1.title))
        assertThat(todoViewModel.description.getOrAwaitValue(), `is`(todo1.description))
        assertThat(todoViewModel.pictureUrl.getOrAwaitValue(), `is`(todo1.pictureUrl))
    }

    @Test
    fun `fetch todo to edit failure`() = runBlockingTest {
        todoFakeRepository.shouldThrowErrorFetchToDo = true
        todoViewModel = ToDoViewModel(todoFakeRepository, mapper, todo1.id)

        todoViewModel.fetchToDo()

        val errorMsg: Event<String?> = todoViewModel.errorMsg.getOrAwaitValue()
        assertThat(errorMsg.getContentIfNotHandled(), `is`(todoFakeRepository.exceptionMsg))
    }

    @Test
    fun `save edit todo success`() = runBlockingTest {
        todoViewModel = ToDoViewModel(todoFakeRepository, mapper, todo1.id)

        todoViewModel.title.value = "title EDIT"
        todoViewModel.description.value = "description EDIT"
        todoViewModel.pictureUrl.value = "pictureUrl EDIT"

        todoViewModel.saveToDo()

        val successMsg: Event<Int> = todoViewModel.successMsg.getOrAwaitValue()
        assertThat(successMsg.getContentIfNotHandled(), `is`(R.string.todo_updated_success))
    }

    @Test
    fun `save edit todo failure`() = runBlockingTest {
        todoFakeRepository.shouldThrowErrorFetchToDo = false
        todoFakeRepository.shouldThrowError = true

        todoViewModel = ToDoViewModel(todoFakeRepository, mapper, todo1.id)

        todoViewModel.title.value = "title EDIT"
        todoViewModel.description.value = "description EDIT"
        todoViewModel.pictureUrl.value = "pictureUrl EDIT"

        todoViewModel.saveToDo()

        val errorMsg: Event<String?> = todoViewModel.errorMsg.getOrAwaitValue()
        assertThat(errorMsg.getContentIfNotHandled(), `is`(todoFakeRepository.exceptionMsg))
    }
}