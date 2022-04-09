package com.miled.marvel.utils

import com.miled.marvel.utils.TestUtil.generatedApiHash
import com.miled.marvel.utils.TestUtil.timeStumpForTest
import generateHash
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test

class FunctionsKtTest {
    @Before
    fun init() {

    }
    //*test generateHash
    @Test
    fun generateHashShouldReturnValidHashKey() {
        //act
        val hashApi = generateHash(timeStump = timeStumpForTest)
        //assert
        MatcherAssert.assertThat(hashApi, Matchers.`is`(Matchers.notNullValue()))
        MatcherAssert.assertThat(hashApi, Matchers.`is`(Matchers.equalTo(generatedApiHash)))
    }

    @Test
    fun generateHashShouldReturnInValidHashKey() {
        //act
        val hashApi = generateHash(System.currentTimeMillis())
        //assert
        MatcherAssert.assertThat(hashApi, Matchers.`is`(Matchers.notNullValue()))
        MatcherAssert.assertThat(hashApi, Matchers.not(Matchers.equalTo(generatedApiHash)))
    }
}