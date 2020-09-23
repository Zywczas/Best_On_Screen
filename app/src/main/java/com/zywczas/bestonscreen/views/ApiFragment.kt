package com.zywczas.bestonscreen.views

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import com.zywczas.bestonscreen.R
import com.zywczas.bestonscreen.viewmodels.factories.ApiVMFactory

//todo pousuwac niepotrzebne layouty

//todo ogarnac back stack, sprawdzic czy jak jest sie w Api i zminimaluzuje to po czasie wraca do tego czy od nowa wlacza aktivity i DB

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ApiFragment (
    private val viewModelFactory : ApiVMFactory,
    private val picasso : Picasso
) : Fragment() {

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //todo zamiast intent chyba mamy arguments
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_api_and_db, container, false)
    }


}