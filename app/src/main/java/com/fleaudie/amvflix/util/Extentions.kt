package com.fleaudie.amvflix.util

import android.view.View
import androidx.navigation.Navigation

fun Navigation.utilNavigate(it: View, id:Int){
    findNavController(it).navigate(id)
}