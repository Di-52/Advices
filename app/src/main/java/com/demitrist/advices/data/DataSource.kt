package com.demitrist.advices.data

/**
 * @author Demitrist on 26.02.2023
 **/

interface DataSource {

    suspend fun fetch(query: String): List<AdviceData>
    suspend fun random(): AdviceData
}