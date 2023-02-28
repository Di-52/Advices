package com.demitrist.advices.data.cache

import androidx.room.Dao
import androidx.room.Query
import com.demitrist.advices.data.entities.QueryRoomEntity

/**
 * @author Demitrist on 27.02.2023
 **/

@Dao
interface QueriesDao {

    @Query("SELECT * FROM queries WHERE query IN (:query)")
    fun query(query: String): QueryRoomEntity
}