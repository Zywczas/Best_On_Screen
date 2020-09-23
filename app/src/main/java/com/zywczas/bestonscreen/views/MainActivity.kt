package com.zywczas.bestonscreen.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.R
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var movieFragmentFactory: MovieFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        //todo to musi byc przed onCreate
        App.moviesComponent.inject(this)
        supportFragmentManager.fragmentFactory = movieFragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //todo poukladac to w lambdy zaleznosci
            startDBFragment()

    }

    private fun startDBFragment(){
        //todo supportFragmentManager jest dla wszystkich fragmentow w tym activity wiec jak go zmienie w trakcie to nie wiem czy sie nie pomiesza wszystko
        //todo ale fragment factory chyba mozna zmieniac, wiec powinno byc ok
        //todo mozliwe ze to powinno byc przed seper.onCreate

        if (supportFragmentManager.fragments.size == 0){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DBFragment::class.java, null)
                .commit()
        }


    }


}