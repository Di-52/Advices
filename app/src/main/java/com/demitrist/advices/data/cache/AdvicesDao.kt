package com.demitrist.advices.data.cache

import androidx.room.Dao
import androidx.room.Query
import com.demitrist.advices.data.entities.AdviceRoomEntity

/**
 * @author Demitrist on 27.02.2023
 **/

@Dao
interface AdvicesDao {

    @Query("SELECT * FROM advices WHERE id IN (:id)")
    fun findById(id:Int):AdviceRoomEntity

    @Query("SELECT * FROM advices")
    fun allAdvices():List<AdviceRoomEntity>
}