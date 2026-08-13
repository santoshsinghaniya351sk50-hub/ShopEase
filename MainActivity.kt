package com.shopease.app

import android.os.Bundle
import android.widget.*
import android.graphics.Color
import android.view.Gravity
import android.view.ViewGroup
import android.content.Context

data class Product(val name: String, val price: Int, val icon: String)

class MainActivity : android.app.Activity() {
    private val products = listOf(
        Product("Smart Watch", 1699, "⌚"),
        Product("Running Shoes", 1749, "👟"),
        Product("Wireless Headphones", 1299, "🎧"),
        Product("Backpack", 999, "🎒"),
        Product("Hair Dryer", 999, "💨"),
        Product("Bluetooth Speaker", 1499, "🔊")
    )
    private val cart = mutableListOf<Product>()
    private lateinit var cartButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        showHome()
    }

    private fun dp(v: Int) = (v * resources.displayMetrics.density).toInt()

    private fun showHome() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setBackgroundColor(Color.rgb(246,247,249))
        }

        val header = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            gravity = Gravity.CENTER_VERTICAL
            setPadding(dp(16), dp(12), dp(16), dp(12))
            setBackgroundColor(Color.WHITE)
        }
        val logo = TextView(this).apply {
            text = "ShopEase"
            textSize = 24f
            setTextColor(Color.rgb(255,90,31))
            setTypeface(null, 1)
        }
        header.addView(logo, LinearLayout.LayoutParams(0, dp(55), 1f))

        cartButton = Button(this).apply {
            text = "🛒 Cart (0)"
            setOnClickListener { showCart() }
        }
        header.addView(cartButton, LinearLayout.LayoutParams(dp(125), dp(55)))
        root.addView(header)

        val search = EditText(this).apply {
            hint = "Search products..."
            setSingleLine(true)
            setPadding(dp(16), 0, dp(16), 0)
        }
        root.addView(search, LinearLayout.LayoutParams(-1, dp(55)).apply {
            setMargins(dp(16), dp(12), dp(16), dp(4))
        })

        val scroll = ScrollView(this)
        val content = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(dp(16), dp(8), dp(16), dp(24))
        }

        val banner = TextView(this).apply {
            text = "🔥 Big Deals, Easy Shopping\nTop products पर शानदार offers"
            textSize = 22f
            setTextColor(Color.WHITE)
            setPadding(dp(20), dp(25), dp(20), dp(25))
            setBackgroundColor(Color.rgb(255,90,31))
        }
        content.addView(banner, LinearLayout.LayoutParams(-1, dp(125)).apply {
            setMargins(0, dp(8), 0, dp(18))
        })

        val title = TextView(this).apply {
            text = "Deals of the Day"
            textSize = 21f
            setTextColor(Color.DKGRAY)
            setTypeface(null, 1)
        }
        content.addView(title)

        val grid = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL }
        fun render(list: List<Product>) {
            grid.removeAllViews()
            list.chunked(2).forEach { rowProducts ->
                val row = LinearLayout(this@MainActivity).apply { orientation = LinearLayout.HORIZONTAL }
                rowProducts.forEach { p ->
                    val card = LinearLayout(this@MainActivity).apply {
                        orientation = LinearLayout.VERTICAL
                        setPadding(dp(10), dp(10), dp(10), dp(10))
                        setBackgroundColor(Color.WHITE)
                    }
                    val icon = TextView(this@MainActivity).apply {
                        text = p.icon
                        textSize = 48f
                        gravity = Gravity.CENTER
                    }
                    card.addView(icon, LinearLayout.LayoutParams(-1, dp(80)))
                    val name = TextView(this@MainActivity).apply {
                        text = p.name
                        textSize = 16f
                        setTypeface(null, 1)
                    }
                    card.addView(name)
                    val price = TextView(this@MainActivity).apply {
                        text = "₹${p.price}"
                        textSize = 18f
                        setTextColor(Color.rgb(255,90,31))
                        setTypeface(null, 1)
                    }
                    card.addView(price)
                    val add = Button(this@MainActivity).apply {
                        text = "Add to Cart"
                        setOnClickListener {
                            cart.add(p)
                            cartButton.text = "🛒 Cart (${cart.size})"
                            Toast.makeText(this@MainActivity, "${p.name} cart में जोड़ दिया", Toast.LENGTH_SHORT).show()
                        }
                    }
                    card.addView(add)
                    row.addView(card, LinearLayout.LayoutParams(0, dp(210), 1f).apply {
                        setMargins(dp(4), dp(8), dp(4), dp(8))
                    })
                }
                if (rowProducts.size == 1) row.addView(Space(this@MainActivity), LinearLayout.LayoutParams(0, 1, 1f))
                grid.addView(row)
            }
        }
        render(products)
        content.addView(grid)

        search.setOnEditorActionListener { _, _, _ ->
            val q = search.text.toString().trim().lowercase()
            render(if (q.isEmpty()) products else products.filter { it.name.lowercase().contains(q) })
            true
        }
        scroll.addView(content)
        root.addView(scroll, LinearLayout.LayoutParams(-1, 0, 1f))
        setContentView(root)
    }

    private fun showCart() {
        val total = cart.sumOf { it.price }
        val names = if (cart.isEmpty()) "Cart अभी खाली है।" else
            cart.joinToString("\n") { "• ${it.name} — ₹${it.price}" } + "\n\nTotal: ₹$total"
        AlertDialog.Builder(this)
            .setTitle("🛒 Your Cart")
            .setMessage(names)
            .setPositiveButton("OK", null)
            .show()
    }
}
