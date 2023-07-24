package com.raywenderlich.android.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raywenderlich.android.wishlist.persistance.RepositoryImpl
import com.raywenderlich.android.wishlist.persistance.WishlistDao
import com.raywenderlich.android.wishlist.persistance.WishlistDaoImpl
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class DetailViewModelTest{

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val wishListDao: WishlistDao = Mockito.spy(WishlistDaoImpl())

    private val detailViewModel = DetailViewModel(RepositoryImpl(wishListDao))

    @Test
    fun saveNewItemCallsDatabase(){
        detailViewModel.saveNewItem(Wishlist("Victoria",
                listOf("RW Android Apprentice Book", "Android phone"), 1),
        "Smart watch")
        verify(wishListDao).save(any())
    }
}