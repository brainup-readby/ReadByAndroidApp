package com.brainup.readbyapp.com.brainup.readbyapp.library.common

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brainup.readbyapp.auth.login.UserSelectedChapters
import com.brainup.readbyapp.com.brainup.readbyapp.library.CategoryChildAdapter
import com.squareup.picasso.Picasso


@BindingAdapter(value = ["setBooks"])
fun RecyclerView.setRowBook(books: List<UserSelectedChapters>?) {
    if (books != null) {
        val bookAdapter = CategoryChildAdapter()
        bookAdapter.submitList(books)
        adapter = bookAdapter
    }
}

@BindingAdapter("imageUrl")
fun setImageUrl(view: ImageView, poserPath: String?) {
    if (!poserPath.isNullOrEmpty())
        Picasso.get().load(poserPath).into(view)
}
/*
@BindingAdapter(value = ["setVendorList"])
fun RecyclerView.setRowVendor(vendors: List<VendorModel>) {
    if (vendors != null) {
        val bookAdapter = CategoryChildAdapter()
        bookAdapter.submitList(vendors)
        adapter = bookAdapter
    }
}*/
