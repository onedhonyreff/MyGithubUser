package com.fikri.submissiongithubuserbfaa.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.fikri.submissiongithubuserbfaa.BuildConfig
import com.fikri.submissiongithubuserbfaa.R
import com.fikri.submissiongithubuserbfaa.databinding.ActivitySplashScreenBinding
import com.fikri.submissiongithubuserbfaa.other_class.SettingPreferences
import com.fikri.submissiongithubuserbfaa.view_model.SettingsFactory
import com.fikri.submissiongithubuserbfaa.view_model.SplashScreenViewModel

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Toast.makeText(this, BuildConfig.GITHUB_TOKEN, Toast.LENGTH_SHORT).show()

        val pref = SettingPreferences.getInstance(dataStore)

        val viewModel = ViewModelProvider(this, SettingsFactory(pref)).get(
            SplashScreenViewModel::class.java
        )

        viewModel.getThemeSettings().observe(this, { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        })

        window.navigationBarColor = ContextCompat.getColor(this, R.color.backgroundColor1)

        val fadeInAnimation: Animation = AnimationUtils.loadAnimation(this, R.anim.fade_in_anim)
        val upAnim: Animation = AnimationUtils.loadAnimation(this, R.anim.up_anim)

        binding.ivGithubLogo.startAnimation(fadeInAnimation)
        binding.ivGithubText.startAnimation(upAnim)

        viewModel.isTimeOut.observe(this, {
            if (it) {
                startActivity(Intent(this@SplashScreen, ListUserActivity::class.java))
                overridePendingTransition(R.anim.fade_in_anim, R.anim.fade_out_anim)
                finish()
            }
        })
    }
}





// cara menyimpan variable pada BuildConfig.java

// 1. ubah tampilan struktur project menjadi -project-
// 2. buat variable baru pada file local.properties. misalnya -> GITHUB_TOKEN=token_anda (nama variabel ditulis langsung dan nilai string tanpa petik)
// 3. buka build.gradle lalu pada bagian -defaultConfig{}- tambahkan kode dibawah ini.
//      Properties properties = new Properties()
//      properties.load(project.rootProject.file("local.properties").newDataInputStream())
//      buildConfigField "String", "GITHUB_TOKEN", "\"${properties.getProperty("GITHUB_TOKEN")}\""
// 4. build ulang project dengan cara klik menu Build->Rebuild Project
// 5. jika sudah masuk ke file BuildConvig.java kemudian variabel dapat diakses dengan cara BuildConfig.NAMA_VARIABEL




// cara membuat callback pada fungsi. (seperti di javascript)

//    private fun ambilNama(): String{
//        return "Dhony"
//    }
//
//    private fun coba(){
//        var greeting = "Hello"
//        getName { result ->
//            greeting = "$greeting $result, namaku ${ambilNama()}"
//        }
//
//        Toast.makeText(this, greeting, Toast.LENGTH_SHORT).show()
//    }
//
//    private fun getName(myCallback: (result: String?) -> Unit) {
//        val ind = "Dunia"
//        myCallback.invoke(ind)
//    }





// cara mendapatkan fragment yang sedang aktif juka menggunakan navController. pastikan sudah di-attach (menggunakan interface untuk memicu pemberitahuan)

//private fun getFragment(): Fragment? {
//    val navHostFragment: Fragment? = supportFragmentManager.primaryNavigationFragment
//    return navHostFragment?.childFragmentManager?.fragments?.get(0)
//}




// cara untuk menambahkan filter warna (ex: imageview tint) secara terprogram

//     binding.ivThemeMode.colorFilter = PorterDuffColorFilter(
//         ContextCompat.getColor(this, R.color.secondary),
//         PorterDuff.Mode.SRC_IN
//     )