package com.codit.interview.aptitude.data.repository

import com.codit.interview.aptitude.core.coroutines.DispatcherProvider
import com.codit.interview.aptitude.data.local.InterviewDatabase
import com.codit.interview.aptitude.data.mapper.toDomain
import com.codit.interview.aptitude.domain.model.Tip
import com.codit.interview.aptitude.domain.repository.TipRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

/** Interview tips and formulas backed by `interview.db`. */
@Singleton
class TipRepositoryImpl @Inject constructor(
    private val database: InterviewDatabase,
    private val dispatchers: DispatcherProvider,
) : TipRepository {

    private val revision = MutableStateFlow(0L)

    private fun changes(): Flow<Long> = revision.onStart { emit(revision.value) }

    override fun observeTips(table: String): Flow<List<Tip>> =
        changes().map { database.readTips(table).map { it.toDomain() } }.flowOn(dispatchers.io)

    override suspend fun getTip(table: String, number: Int): Tip? = withContext(dispatchers.io) {
        database.readTip(table, number)?.toDomain()
    }

    override suspend fun getTipByTitle(table: String, title: String): Tip? =
        withContext(dispatchers.io) { database.readTipByTitle(table, title)?.toDomain() }

    override suspend fun addToFavourites(
        table: String,
        tipNumber: Int,
        favouritesTable: String,
    ): Boolean = withContext(dispatchers.io) {
        val added = database.copyToFavourites(table, tipNumber, favouritesTable)
        revision.value = revision.value + 1
        added
    }

    override suspend fun lastTipNumber(table: String): Int = withContext(dispatchers.io) {
        database.lastTipNumber(table)
    }
}
