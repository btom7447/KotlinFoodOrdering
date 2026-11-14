/*
 * Tests for formatNaira, the single place naira amounts are formatted for display.
 */
package foodordering

import kotlin.test.Test
import kotlin.test.assertEquals

class MoneyTest {
    @Test
    fun `naira amounts use the naira sign and comma separators`() {
        assertEquals("₦1,500", formatNaira(1_500))
        assertEquals("₦400", formatNaira(400))
        assertEquals("₦1,234,500", formatNaira(1_234_500))
    }
}
