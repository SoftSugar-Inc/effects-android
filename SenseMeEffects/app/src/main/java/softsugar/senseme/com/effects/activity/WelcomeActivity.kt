package softsugar.senseme.com.effects.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import softsugar.senseme.com.effects.R
import softsugar.senseme.com.effects.utils.MultiLanguageUtils

class WelcomeActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(MultiLanguageUtils.appContext)
    }
}