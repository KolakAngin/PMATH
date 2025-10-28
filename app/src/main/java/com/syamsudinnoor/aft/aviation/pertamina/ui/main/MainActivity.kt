    package com.syamsudinnoor.aft.aviation.pertamina.ui.main

    import android.app.Dialog
    import android.content.Intent
    import android.os.Bundle
    import android.text.SpannableString
    import android.text.style.ForegroundColorSpan
    import android.view.Menu
    import android.view.MenuItem
    import android.view.Window
    import android.widget.Button
    import android.widget.TextView
    import android.widget.Toast
    import androidx.activity.viewModels
    import androidx.appcompat.app.AppCompatActivity
    import androidx.appcompat.widget.SearchView
    import androidx.core.content.ContextCompat
    import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen

    import androidx.core.view.ViewCompat
    import androidx.core.view.WindowCompat
    import androidx.core.view.WindowInsetsCompat
    import androidx.recyclerview.widget.GridLayoutManager
    import com.syamsudinnoor.aft.aviation.pertamina.R
    import com.syamsudinnoor.aft.aviation.pertamina.adapter.MainAdapter
    import com.syamsudinnoor.aft.aviation.pertamina.adapter.ItemMenu
    import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityMainBinding
    import com.syamsudinnoor.aft.aviation.pertamina.ui.aircraft.AircraftActivity
    import com.syamsudinnoor.aft.aviation.pertamina.ui.density.DensityActivity
    import com.syamsudinnoor.aft.aviation.pertamina.ui.dipping_tank.DippingTankActivity
    import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.activity.ToppingUpActivity
    import androidx.lifecycle.lifecycleScope
    import com.syamsudinnoor.aft.aviation.pertamina.ui.calculator.CalculatorActivity
    import com.syamsudinnoor.aft.aviation.pertamina.ui.converter.ConverterActivity
    import com.syamsudinnoor.aft.aviation.pertamina.ui.flashlight.FlashLightActivity
    import com.syamsudinnoor.aft.aviation.pertamina.ui.main.viewmodel.MainLoginViewModel
    import com.syamsudinnoor.aft.aviation.pertamina.ui.stopwatch.StopWatchActivity
    import com.syamsudinnoor.aft.aviation.pertamina.utility.openWhatsApp
    import com.syamsudinnoor.aft.aviation.pertamina.utility.settingDialogGlobal
    import kotlinx.coroutines.launch
    import androidx.core.view.size
    import androidx.core.view.get
    import com.itextpdf.kernel.colors.Color
    import com.syamsudinnoor.aft.aviation.pertamina.ui.stadis.StadisActivity

    class MainActivity : AppCompatActivity() {
        private lateinit var binding: ActivityMainBinding
        private lateinit var menuAdapter: MainAdapter
        private val mainViewModel: MainLoginViewModel by viewModels()

        override fun onCreate(savedInstanceState: Bundle?) {
            // 1. Install Splash Screen
            val splashScreen = installSplashScreen()
            super.onCreate(savedInstanceState)
            setupMainActivityUI()

            // 2. Tahan Splash Screen selama ViewModel melakukan pengecekan
            splashScreen.setKeepOnScreenCondition {
                mainViewModel.isLoading.value
            }

            // 3. Amati hasil pengecekan sesi
            lifecycleScope.launch {
                mainViewModel.isSessionValid.collect { isSessionValid ->
                    // Jika hasil sudah ada (bukan null awal)
                    if (isSessionValid != null) {
                        if (isSessionValid) {
                            // SESI VALID: Lanjutkan setup MainActivity
                            rvInitialize()
                        } else {
                            // SESI TIDAK VALID: Arahkan ke LoginActivity
                            navigateToLogin()
                        }
                    }
                }
            }
        }

        private fun setupMainActivityUI() {
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)

            WindowCompat.setDecorFitsSystemWindows(window, false)
            window.statusBarColor = android.graphics.Color.TRANSPARENT
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = false
            binding.topAppBar.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary))
            setSupportActionBar(binding.topAppBar)
        }

        private fun navigateToLogin() {
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }


        private fun rvInitialize(){
            menuAdapter = MainAdapter(getItemMenu())
            binding.rvHolder.adapter = menuAdapter
            menuAdapter.adapterPassItemCallback { itemMenu ->
                when(itemMenu.name){
                    "Penerimaan Bridger" -> {
                        val intent = Intent(this, DensityActivity::class.java)
                        intent.putExtra(DensityActivity.APP_NAME, itemMenu.name)
                        startActivity(intent)
                    }
                    "Topping Up Refueller" ->{
                        val intent = Intent(this,ToppingUpActivity::class.java)
                        intent.putExtra(ToppingUpActivity.APP_NAME, itemMenu.name)
                        startActivity(intent)
                    }
                    "Cek Stock" -> {
                        val intent = Intent(this, DippingTankActivity::class.java)
                        intent.putExtra(DippingTankActivity.APP_NAME, itemMenu.name)
                        startActivity(intent)
                    }
                    "Aircraft Fuel Remain" ->{
                        val intent = Intent(this, AircraftActivity::class.java)
                        intent.putExtra(AircraftActivity.APP_NAME, itemMenu.name)
                        startActivity(intent)
                    }

                    "Calculator" ->{
                        val intent = Intent(this, CalculatorActivity::class.java)
                        startActivity(intent)
                    }
                    "Stopwatch" ->{
                        val intent = Intent(this, StopWatchActivity::class.java)
                        startActivity(intent)
                    }
                    "Flashlight" -> {
                        val intent = Intent(this, FlashLightActivity::class.java)
                        startActivity(intent)
                    }
                    "Converter" -> {
                        val intent = Intent(this, ConverterActivity::class.java)
                        startActivity(intent)
                    }
                    "Call Developer" -> openWhatsApp(this,"6285173005241","Halo Developer Saya Ingin Tambah Fitur Baru")

                    "Calculator Dopping Stadis" -> {
                        val intent = Intent(this, StadisActivity::class.java)
                        startActivity(intent)
                    }
                }

            }
            setSupportActionBar(binding.topAppBar)
            binding.rvHolder.setHasFixedSize(true)
            binding.rvHolder.layoutManager = GridLayoutManager(this, 2)
        }

        private fun settingDialog(massage: String){
            val dialog = Dialog(this)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.costum_delete_quality_control)
            dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
            val titleText : TextView = dialog.findViewById(R.id.txt_title)
            titleText.text = "Konfirmasi Keluar Aplikasi"
            val massageText  : TextView = dialog.findViewById(R.id.txt_massage)
            massageText.text = massage


            val yesButton : Button = dialog.findViewById(R.id.btn_yes)
            yesButton.setOnClickListener {
                val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
                with(sharedPref.edit()) {
                    clear()
                    apply()
                }

                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                startActivity(intent)
                finish()
            }
            val noButton : Button = dialog.findViewById(R.id.btn_no)
            noButton.setOnClickListener {
                dialog.dismiss()
            }
            dialog.show()
        }


        private fun getItemMenu(): ArrayList<ItemMenu> {
            val menuName = resources.getStringArray(R.array.holder_name)
            val menuImage = resources.obtainTypedArray(R.array.holder_image)
            val menuList = ArrayList<ItemMenu>()
            for (i in menuName.indices){
                val itemMenu = ItemMenu(menuName[i], menuImage.getResourceId(i, -1))
                menuList.add(itemMenu)
            }
            return menuList
        }

        override fun onCreateOptionsMenu(menu: Menu?): Boolean {
            menuInflater.inflate(R.menu.main_menu,menu)
            val searchMenu = menu?.findItem(R.id.search_item)
            val searchView = searchMenu?.actionView as? SearchView
            searchView?.queryHint = resources.getString(R.string.search_hint)
            searchView?.isSubmitButtonEnabled = true
            val c = object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    filteredList(newText)
                    return true
                }

            }
            searchView?.setOnQueryTextListener(c)

            if (menu != null){
                for(i in 0..menu.size - 1){
                    val menuItem = menu[i]
                    val spannable = SpannableString(menuItem.title.toString())
                    spannable.setSpan(ForegroundColorSpan(resources.getColor(R.color.black)),
                        0,
                        spannable.length,
                        0)

                    menuItem.title = spannable

                }
            }

            return true
        }

        override fun onOptionsItemSelected(item: MenuItem): Boolean {
            when(item.itemId){
                R.id.about_item -> startActivity(Intent(this, PrivacyPolicyActivity::class.java))
                R.id.logout_item -> {
                    settingDialogGlobal("Konfirmasi Keluar Aplikasi","Apakah Anda yakin keluar aplikasi",
                        this){
                        val sharedPref = getSharedPreferences("app_prefs", MODE_PRIVATE)
                        with(sharedPref.edit()) {
                            clear()
                            apply()
                        }

                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                        startActivity(intent)
                        finish()
                    }
                }
            }
            return super.onOptionsItemSelected(item)
        }

        private fun filteredList(newText: String?) {
            val filteredList = ArrayList<ItemMenu>()
            if (newText != null){
                for (i in getItemMenu()){
                    if (i.name.lowercase().contains(newText.lowercase())){
                        filteredList.add(i)
                    }
                }

                if (!filteredList.isEmpty()){
                    menuAdapter.setFilteredList(filteredList)
                }
            }
        }
    }