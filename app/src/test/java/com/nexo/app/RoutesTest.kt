package com.nexo.app

import com.nexo.app.ui.navigation.Routes
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RoutesTest {
    @Test
    fun routesAreUniqueAndNotBlank() {
        assertEquals(Routes.all.size, Routes.all.toSet().size)
        assertTrue(Routes.all.all { it.isNotBlank() })
    }
}
