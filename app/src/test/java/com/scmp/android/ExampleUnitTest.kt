package com.scmp.android

import android.util.Patterns
import androidx.core.util.PatternsCompat
import com.scmp.android.util.Constant
import org.junit.Test
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(JUnit4::class)
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun checkPassWordValid() {
        assert(Constant.PASSWORD_PATTERN.toRegex().matches("test112222"))
//        assert(Constant.PASSWORD_PATTERN.toRegex().matches("test"))
//        assert(Constant.PASSWORD_PATTERN.toRegex().matches("testtesttest"))
    }

    @Test
    fun checkEmailValid() {
        assert(PatternsCompat.EMAIL_ADDRESS.toRegex().matches("eve.holt@reqres.in"))
//        assert(PatternsCompat.EMAIL_ADDRESS.toRegex().matches("eve.hol"))
    }

    
}