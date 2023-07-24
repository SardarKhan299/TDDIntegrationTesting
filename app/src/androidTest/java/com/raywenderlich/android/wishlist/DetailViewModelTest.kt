package com.raywenderlich.android.wishlist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
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
import org.mockito.kotlin.mock
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

    @Test
    fun saveNewItemSavesData(){
        // 1 \
        val wishList = Wishlist("Victoria", listOf("RW Android Apprentice Book", "Android phone"),1)
        val name = "Smart Watch"
        detailViewModel.saveNewItem(wishList,name)

        val mockObserver = mock<Observer<Wishlist>>()

        wishListDao.findById(wishList.id)
            .observeForever(mockObserver)

        verify(mockObserver).onChanged(wishList.copy(wishes = wishList.wishes + name))

    }

    @Test
    fun getWishListCallDatabase(){
        detailViewModel.getWishlist(1)
        verify(wishListDao).findById(1)
    }



}