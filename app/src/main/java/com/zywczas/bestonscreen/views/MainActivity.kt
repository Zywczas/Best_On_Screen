package com.zywczas.bestonscreen.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zywczas.bestonscreen.App
import com.zywczas.bestonscreen.R
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var movieFragmentFactory: MovieFragmentFactory

    override fun onCreate(savedInstanceState: Bundle?) {
        App.moviesComponent.inject(this)
        supportFragmentManager.fragmentFactory = movieFragmentFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDBFragment()
    }

    private fun initDBFragment(){
        //todo supportFragmentManager jest dla wszystkich fragmentow w tym activity wiec jak go zmienie w trakcie to nie wiem czy sie nie pomiesza wszystko
        //todo ale fragment factory chyba mozna zmieniac, wiec powinno byc ok
        if (supportFragmentManager.fragments.size == 0){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragmentContainer, DBFragment::class.java, null)
                .commit()
        }
    }

}