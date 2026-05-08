package co.com.monkeymobile.fakestore.domain.usecase

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

abstract class FlowUseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    operator fun invoke(parameters: P): Flow<Result<R>> {
        return execute(parameters)
            .map { Result.success(it) }
            .catch { exception -> emit(Result.failure(exception)) }
            .flowOn(coroutineDispatcher)
    }

    @Throws(RuntimeException::class)
    protected abstract fun execute(parameters: P): Flow<R>
}
