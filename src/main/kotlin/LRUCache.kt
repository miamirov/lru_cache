class LRUCache<K, V>(private val capacity: Int) : Cache<K, V> {
    private val cache: MutableMap<K, Node>
    private var head: Node?
    private var tail: Node?

    init {
        assert(capacity > 0)

        cache = HashMap(capacity)
        head = null
        tail = null
    }

    override operator fun set(key: K, value: V) {
        val sizeBefore = cache.size -
                if (key in cache || cache.size == capacity) 1 else 0

        if (key in cache)
            remove(cache.remove(key)!!)

        if (cache.size >= capacity)
            remove(cache.remove(tail!!.key)!!)

        val node = Node(key, value)
        add(node)
        cache[key] = node

        assert(head == node)
        assert(sizeBefore + 1 == cache.size)
        assert(sizeBefore + 1 <= capacity)
    }

    override operator fun get(key: K): V? {
        val sizeBefore: Int = cache.size

        val node: Node = cache[key] ?: return null
        remove(node)
        add(node)

        assert(head == node)
        assert(sizeBefore == listSize())
        assert(sizeBefore == cache.size)
        return node.value
    }

    override fun values(): List<Pair<K, V>> {
        return mutableListOf<Pair<K, V>>().apply {
            var cur = head
            while (cur != null) {
                cur = cur.apply { add(key to value) }.next
            }

            assert(size == cache.size)
        }
    }


    private inner class Node(
        val key: K,
        val value: V,
        var prev: Node? = null,
        var next: Node? = null
    )

    private fun remove(node: Node) {
        if (node.prev != null) {
            node.prev!!.next = node.next
        } else {
            head = node.next
        }

        if (node.next != null) {
            node.next!!.prev = node.prev
        } else {
            tail = node.prev
        }

        assert(head != node)
        assert(tail != node)
        assert(node.prev == null || node.prev!!.next == node.next)
        assert(node.next == null || node.next!!.prev == node.prev)
    }

    private fun add(node: Node) {
        node.next = head
        node.prev = null
        head?.prev = node
        head = node
        if (tail == null)
            tail = head

        assert(head == node)
        assert(tail != null)
    }

    private fun listSize(): Int {
        var size = 0
        var cur = head
        while (cur != null) {
            cur = cur.next
            size++
        }
        return size
    }
}
