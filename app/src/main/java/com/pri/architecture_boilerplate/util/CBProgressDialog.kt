package com.pri.architecture_boilerplate.util

import android.app.Dialog
import android.content.Context
import android.view.Window
import android.widget.ProgressBar


/**
 * Created by Priyanka on 13/11/18.
 */
class CBProgressDialog//..we need the context else we can not create the dialog so get context in constructor
(internal var context: Context) {
    internal var dialog: Dialog? = null

    val isShowing: Boolean
        get() = dialog?.isShowing ?: false

    init {


        dialog = Dialog(context)
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        //...set cancelable false so that it's never get hidden
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(true)
        //...that's the layout i told you will inflate later
        dialog?.setContentView(ProgressBar(context))
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)

        //...initialize the imageView form infalted layout

        /* *//*
        it was never easy to load gif into an ImageView before Glide or Others library
        and for doing this we need DrawableImageViewTarget to that ImageView
        *//*
        GlideDrawableImageViewTarget imageViewTarget = new GlideDrawableImageViewTarget(gifImageView);

        //...now load that gif which we put inside the drawble folder here with the help of Glide

        Glide.with(activity).load(R.drawable.loading_new).placeholder(R.drawable.loading_new).centerCrop().crossFade().into(imageViewTarget);*/

        //...finaly show it

    }

    fun showDialog() {
        dialog?.show()
    }

    //..also create a method which will hide the dialog when some work is done
    fun hideDialog() {
        dialog?.dismiss()
    }
}