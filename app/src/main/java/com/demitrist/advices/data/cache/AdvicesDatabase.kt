package com.demitrist.advices.data.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.demitrist.advices.data.entities.AdviceRoomEntity
import com.demitrist.advices.data.entities.QueryRoomEntity

/**
 * @author Demitrist on 01.03.2023
 **/

@Database(entities = [AdviceRoomEntity::class, QueryRoomEntity::class], version = 1)
abstract class AdvicesDatabase : RoomDatabase() {

    abstract fun advicesDao(): AdvicesDao
    abstract fun queriesDao(): QueriesDao
}