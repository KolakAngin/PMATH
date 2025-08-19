package com.syamsudinnoor.aft.aviation.pertamina.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView

import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.syamsudinnoor.aft.aviation.pertamina.R
import com.syamsudinnoor.aft.aviation.pertamina.adapter.MainAdapter
import com.syamsudinnoor.aft.aviation.pertamina.adapter.ItemMenu
import com.syamsudinnoor.aft.aviation.pertamina.databinding.ActivityMainBinding
import com.syamsudinnoor.aft.aviation.pertamina.ui.aircraft.AircraftActivity
import com.syamsudinnoor.aft.aviation.pertamina.ui.density.DensityActivity
import com.syamsudinnoor.aft.aviation.pertamina.ui.dipping_tank.DippingTankActivity
import com.syamsudinnoor.aft.aviation.pertamina.ui.topping_up.ToppingUpActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var menuAdapter: MainAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        rvInitialize()
        setSupportActionBar(binding.topAppBar)

    }

    private fun rvInitialize(){
        menuAdapter = MainAdapter(getItemMenu())
        binding.rvHolder.adapter = menuAdapter
        menuAdapter.adapterPassItemCallback { itemMenu ->
            Toast.makeText(this,"Menu Clicked : ${itemMenu.name}", Toast.LENGTH_SHORT).show()
            when(itemMenu.name){
                "Penerimaan Bridger" -> {
                    val intent = Intent(this, DensityActivity::class.java)
                    intent.putExtra(DensityActivity.APP_NAME, itemMenu.name)
                    startActivity(intent)
                }
                "Topping Up Refeuller" ->{
                    val intent = Intent(this,ToppingUpActivity::class.java)
                    intent.putExtra(ToppingUpActivity.APP_NAME, itemMenu.name)
                    startActivity(intent)
                }
                "Dipping Tank" -> {
                    val intent = Intent(this, DippingTankActivity::class.java)
                    intent.putExtra(DippingTankActivity.APP_NAME, itemMenu.name)
                    startActivity(intent)
                }
                "Aircraft Fuel Remain" ->{
                    val intent = Intent(this, AircraftActivity::class.java)
                    intent.putExtra(AircraftActivity.APP_NAME, itemMenu.name)
                    startActivity(intent)
                }

            }

        }
        setSupportActionBar(binding.topAppBar)
        binding.rvHolder.setHasFixedSize(true)
        binding.rvHolder.layoutManager = GridLayoutManager(this, 2)
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
                TODO("Not yet implemented")
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filteredList(newText)
                return true
            }

        }
        searchView?.setOnQueryTextListener(c)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.about_item -> Toast.makeText(this,"About Clicked", Toast.LENGTH_SHORT).show()
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