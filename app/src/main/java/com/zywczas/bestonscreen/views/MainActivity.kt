package com.zywczas.bestonscreen.views

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zywczas.bestonscreen.R
import dagger.android.*

import javax.inject.Inject
//chyba mozna usunac Has android injector
class MainActivity : AppCompatActivity()
//    , HasAndroidInjector
{
////cos
//    @Inject
//    lateinit var androidInjector: DispatchingAndroidInjector<Any>
    @Inject
    lateinit var moviesFragmentsFactory: MoviesFragmentsFactory

//    override fun androidInjector(): AndroidInjector<Any> {
//        return androidInjector
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        supportFragmentManager.fragmentFactory = moviesFragmentsFactory
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initDBFragment()
    }

    private fun initDBFragment(){
        if (supportFragmentManager.fragments.size == 0){
            supportFragmentManager.beginTransaction()
                    //todo moze dac add?
                .replace(R.id.fragmentContainer, DBFragment::class.java, null)
                .commit()
        }
    }

}