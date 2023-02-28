package com.demitrist.advices.data.cache

import com.demitrist.advices.data.AdviceData
import com.demitrist.advices.data.entities.AdviceRoomEntity
import com.demitrist.advices.data.entities.QueryRoomEntity

/**
 * @author Demitrist on 27.02.2023
 **/

interface AdviceCacheDataSource {

    suspend fun fetch(query: String): List<AdviceData>

    suspend fun contains(query: String): Boolean

    suspend fun save(query: String, advices: List<AdviceData>)

    class Base(
        private val advicesDao: AdvicesDao,
        private val queriesDao: QueriesDao,
        private val mapper: AdviceData.Mapper<AdviceRoomEntity>,
    ) : AdviceCacheDataSource {

        override suspend fun fetch(query: String): List<AdviceData> {
            val d = queriesDao.query(query)
            if (d != null) {
                val ids = d.adviceId.split(",")
                val advices = mutableListOf<AdviceData>()
                ids.forEach {
                    val entity = advicesDao.findById(it.toInt())
                    advices.add(AdviceData(id = entity.id,
                        text = entity.text,
                        isFavourite = entity.isFavourite))
                }
                return advices
            } else throw IllegalStateException()
        }

        override suspend fun contains(query: String): Boolean {
            val q = queriesDao.query(query)
            return q != null
        }

        override suspend fun save(query: String, advices: List<AdviceData>) {
            val advicesString = mutableSetOf<Int>()
            advices.forEach {
                it.provideId(advicesString)
                advicesDao.save(it.map(mapper))
            }
            queriesDao.save(QueryRoomEntity(query, advicesString.joinToString(",")))
        }
    }
}