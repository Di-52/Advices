package com.demitrist.advices.presentation.viewmodels

import com.demitrist.advices.R
import com.demitrist.advices.core.DispatchersList
import com.demitrist.advices.core.ProvideResources
import com.demitrist.advices.domain.*
import com.demitrist.advices.presentation.AdviceUi
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * @author Demitrist on 09.02.2023
 */

class SearchViewModelTest {

    private lateinit var communication: FakeSearchCommunication
    private lateinit var interactor: FakeSearchInteractor
    private lateinit var validator: FakeSearchRequestValidator
    private lateinit var dispatchers: FakeDispatchersList
    private lateinit var resources: FakeProvideResources
    private lateinit var viewModel: SearchViewModel

    @Before
    fun setUp() {
        communication = FakeSearchCommunication()
        interactor = FakeSearchInteractor()
        validator = FakeSearchRequestValidator()
        dispatchers = FakeDispatchersList()
        resources = FakeProvideResources()
        viewModel = SearchViewModel(
            communication = communication,
            interactor = interactor,
            validator = validator,
            dispatchers = dispatchers,
            resources = resources,
        )
    }

    @Test
    fun `search with invalid query`() = runBlocking {
        validator.validationResult = false
        viewModel.advices(query = "a")

        assertEquals("a", validator.isValidCalledList[0])
        assertEquals(1, validator.isValidCalledList.size)
        assertEquals(0, validator.mapCallList.size)
        assertEquals(0, interactor.queries.size)
        assertEquals(0, interactor.randomAdviceCalledCount)
        assertEquals(0, dispatchers.ioCalledCount)
        assertEquals(0, dispatchers.uiCalledCount)
        assertEquals(SearchUiState.Error(message = "invalid input string"), communication.list[0])
        assertEquals(1, communication.list.size)
    }

    @Test
    fun `search with valid query and error result`() {
        validator.validationResult = true
        validator.mapResult = "b"
        viewModel.advices(query = "a")
        interactor.result = SearchAdviceResult.Error(exception = ServiceException())

        assertEquals("a", validator.isValidCalledList[0])
        assertEquals(1, validator.isValidCalledList.size)
        assertEquals(1, validator.mapCallList.size)
        assertEquals(SearchUiState.Progress, communication.list[0])
        assertEquals(1, dispatchers.ioCalledCount)
        assertEquals("b", interactor.queries[0])
        assertEquals(1, interactor.queries.size)
        assertEquals(0, interactor.randomAdviceCalledCount)
        assertEquals(SearchUiState.Error(message = "service unavailable"), communication.list[1])
        assertEquals(2, communication.list.size)
    }

    @Test
    fun `search with valid query and success result`() = runBlocking {
        validator.validationResult = true
        validator.mapResult = "b"
        interactor.result = SearchAdviceResult.Success(
            listOf(AdviceDomain(
                id = 1,
                text = "text",
                isFavourite = false
            ))
        )
        viewModel.advices(query = "a")

        assertEquals("a", validator.isValidCalledList[0])
        assertEquals(1, validator.isValidCalledList.size)
        assertEquals(1, validator.mapCallList.size)
        assertEquals(SearchUiState.Progress, communication.list[0])
        assertEquals(1, dispatchers.ioCalledCount)
        assertEquals("b", interactor.queries[0])
        assertEquals(1, interactor.queries.size)
        assertEquals(0, interactor.randomAdviceCalledCount)
        assertEquals(
            SearchUiState.Success(listOf(
                AdviceUi(
                    id = 1,
                    text = "text",
                    isFavourite = false
                ))), communication.list[1])
        assertEquals(2, communication.list.size)
    }

    @Test
    fun `search random and error result`() = runBlocking {

        viewModel.adviceRandom()
        assertEquals(0, validator.isValidCalledList.size)
        assertEquals(0, validator.mapCallList.size)
        assertEquals(SearchUiState.Progress, communication.list[0])
        assertEquals(1, dispatchers.ioCalledCount)
        assertEquals(0, interactor.queries.size)
        assertEquals(1, interactor.randomAdviceCalledCount)
        assertEquals(SearchUiState.Error(message = "service unavailable"), communication.list[1])
        assertEquals(2, communication.list.size)
    }

    @Test
    fun `search random and success result`() = runBlocking {
        interactor.randomAdviceResult = SearchAdviceResult.Success(
            listOf(AdviceDomain(
                id = 1,
                text = "text",
                isFavourite = false
            ))
        )
        viewModel.adviceRandom()
        assertEquals(0, validator.isValidCalledList.size)
        assertEquals(0, validator.mapCallList.size)
        assertEquals(SearchUiState.Progress, communication.list[0])
        assertEquals(1, dispatchers.ioCalledCount)
        assertEquals(0, interactor.queries.size)
        assertEquals(1, interactor.randomAdviceCalledCount)
        assertEquals(
            SearchUiState.Success(listOf(
                AdviceUi(
                    id = 1,
                    text = "text",
                    isFavourite = false
                ))), communication.list[1])
        assertEquals(2, communication.list.size)
    }


}

private class FakeSearchInteractor : SearchInteractor {
    val queries = ArrayList<String>()
    var result: SearchAdviceResult? = null

    override suspend fun advices(query: String): SearchAdviceResult {
        queries.add(query)
        return result
    }

    var randomAdviceCalledCount = 0
    var randomAdviceResult: SearchAdviceResult? = null
    override suspend fun randomAdvice(): SearchAdviceResult {
        randomAdviceCalledCount++
        return randomAdviceResult
    }
}

private class FakeSearchCommunication : SearchCommunication {
    val list = ArrayList<SearchUiState>()
    override fun map(data: SearchUiState) {
        list.add(data)
    }
}

private class FakeSearchRequestValidator() : SearchRequestValidator {
    var validationResult: Boolean? = null
    val isValidCalledList = arrayListOf<String>()

    override fun isValid(request: String): Boolean {
        isValidCalledList.add(request)
        return validationResult!!
    }

    val mapCallList = arrayListOf<String>()
    var mapResult: String? = null
    override fun map(data: String): String {
        mapCallList.add(data)
        return mapResult!!
    }
}

private class FakeDispatchersList : DispatchersList {
    private val dispatcher = TestCoroutineDispatcher()
    var ioCalledCount = 0
    var uiCalledCount = 0

    override fun io(): CoroutineDispatcher {
        ioCalledCount++
        return dispatcher
    }

    override fun ui(): CoroutineDispatcher {
        uiCalledCount++
        return dispatcher
    }
}

private class FakeProvideResources : ProvideResources {
    override fun resource(id: Int): String {
        return when (id) {
            R.strings.invalid_input_string -> "invalid input string"
            R.strings.unavailable_service_string -> "service unavailable"
            else -> throw IllegalStateException()
        }
    }
}