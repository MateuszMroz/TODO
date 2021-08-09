package com.example.todo.ui.list_todo

import com.example.todo.R
import com.example.todo.data.FakeToDoRepository
import com.example.todo.data.models.ToDo
import com.example.todo.util.BaseUnitTest
import com.example.todo.util.Event
import getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.core.Is.`is`
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class ListToDoViewModelTest : BaseUnitTest() {
    private lateinit var listToDoViewModel: ListToDoViewModel
    private lateinit var todoFakeRepository: FakeToDoRepository

    private val todo1 = ToDo(
        title = "title1",
        description = "desc1",
        pictureUrl = null,
        timestamp = System.currentTimeMillis()
    )
    private val todo2 = ToDo(
        title = "title2",
        description = "desc2",
        pictureUrl = "pictureUrl2",
        timestamp = System.currentTimeMillis()
    )
    private val todo3 = ToDo(
        title = "title3",
        description = "desc3",
        pictureUrl = null,
        timestamp = System.currentTimeMillis()
    )
    private val todo4 = ToDo(
        title = "title4",
        description = "desc4",
        pictureUrl = "pictureUrl4",
        timestamp = System.currentTimeMillis()
    )
    private val todo5 = ToDo(
        title = "title5",
        description = "desc5",
        pictureUrl = null,
        timestamp = System.currentTimeMillis()
    )

    @Before
    fun setup() = runBlockingTest {
        todoFakeRepository = FakeToDoRepository()

        todoFakeRepository.addToDo(todo1)
        todoFakeRepository.addToDo(todo2)
        todoFakeRepository.addToDo(todo3)
        todoFakeRepository.addToDo(todo4)
        todoFakeRepository.addToDo(todo5)
    }

    @After
    fun tearDown() {
        todoFakeRepository.clearData()
    }

    @Test
    fun `remove todo success`() = runBlockingTest {
        listToDoViewModel = ListToDoViewModel(todoFakeRepository)

        listToDoViewModel.onRemoveTask(todo1.id)

        val successMsg: Event<Int> = listToDoViewModel.successMsg.getOrAwaitValue()
        assertThat(successMsg.getContentIfNotHandled(), `is`(R.string.msg_todo_remove_success))
    }

    @Test
    fun `remove todo failure`() = runBlockingTest {
        todoFakeRepository.shouldThrowError = true

        listToDoViewModel = ListToDoViewModel(todoFakeRepository)

        listToDoViewModel = ListToDoViewModel(todoFakeRepository)
        listToDoViewModel.onRemoveTask(todo1.id)

        val errorMsg: Event<String?> = listToDoViewModel.errorMsg.getOrAwaitValue()
        assertThat(errorMsg.getContentIfNotHandled(), `is`(todoFakeRepository.exceptionMsg))
    }
}


