package com.altangerel.wallmart

import android.app.Activity
import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var users = ArrayList<User>();

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        textView2.paintFlags = Paint.UNDERLINE_TEXT_FLAG

        fillInitialData()

        button_signin.setOnClickListener {
            if (isValidated(email.text.toString(), password.text.toString())) {
                val shoppingIntent = Intent(this, ShoppingCategory::class.java)
                shoppingIntent.putExtra("email", email.text.toString())
                startActivity(shoppingIntent)
            } else {
                Toast.makeText(applicationContext, "Wrong credential", Toast.LENGTH_LONG).show()
            }
        }

        button2.setOnClickListener {
            val signUpIntent = Intent(this, RegisterActivity::class.java)
            resultLauncher.launch(signUpIntent)
        }
    }

    private var resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val serUser = data!!.getSerializableExtra("user")
            val newUser = serUser as User
            users.add(newUser)
        }
    }

    private fun isValidated(email: String, password: String): Boolean {
        for (user in users) {
            if (user.userName == email && user.password == password) {
                return true
            }
        }
        return false
    }

    private fun findUserByEmail(email: String): User {
        for (user in users) {
            if (user.userName == email) {
                return user
            }
        }
        return User()
    }

    private fun fillInitialData() {
        var user1 = User("Altangerel", "Ganbaatar", "aganbaatar@miu.edu", "1234")
        var user2 = User("Admin", "Ganbaatar", "admin@miu.edu", "1234")

        users.add(user1)
        users.add(user2)
    }

    fun forgotPassword(view: View) {
        val inputEmail = email.text.toString()
        val foundUser = findUserByEmail(inputEmail)

        if(foundUser.password.isNotEmpty()) {
            val sendIntent = Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"))
            sendIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(inputEmail))
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, this.getString(R.string.password_recovery))
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Your password is: ${foundUser.password}")
            startActivity(Intent.createChooser(sendIntent, this.getString(R.string.send_email)))
        } else {
            Toast.makeText(applicationContext, "Can not found user with email: $inputEmail", Toast.LENGTH_LONG).show()
        }
    }
}