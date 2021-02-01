import org.junit.Before
import org.junit.Test

class LRUCacheTest {
    private lateinit var cache: LRUCache<Int, Int>

    @Before
    fun init() {
        cache = LRUCache(4)
    }

    @Test
    fun `Correct order after put`() {
        (1 until 4).forEach { cache[it] = it }

        assert(cache.values() == listOf(3 to 3, 2 to 2, 1 to 1))
    }

    @Test
    fun `Correct get`() {
        (1 until 4).forEach { cache[it] = it }

        assert(cache[1] == 1)
        assert(cache[2] == 2)
        assert(cache[3] == 3)
    }

    @Test
    fun `Correct order after get`() {
        (1 until 5).forEach { cache[it] = it }
        cache[1]
        assert(cache.values() == listOf(1 to 1, 4 to 4, 3 to 3, 2 to 2))
    }

    @Test
    fun `Correctly capacity limit`() {
        (1 until 6).forEach { cache[it] = it }

        assert(cache[1] == null)
        assert(cache.values().size == 4)
    }

    @Test
    fun `Correct order after set and get`() {
        (1 until 5).forEach { cache[it] = it }
        (4 downTo 1).forEach { cache[it] }

        assert(cache.values() == listOf(1 to 1, 2 to 2, 3 to 3, 4 to 4))
    }

    @Test
    fun `Return null if no value`() {
        (1 until 5).forEach { cache[it] = it }

        assert(cache[9] == null)
    }


    @Test(expected = AssertionError::class)
    fun `Assert capacity less than 1`() {
        LRUCache<Int, Int>(0)
    }
}
