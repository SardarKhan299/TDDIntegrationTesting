package com.raywenderlich.android.wishlist.persistance

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.raywenderlich.android.wishlist.Wishlist
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@RunWith(AndroidJUnit4::class)
class WishlistDaoTest {

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var wishlistDao: WishlistDao
    private lateinit var wishlistDatabase: WishlistDatabase

    @Before
    fun initDB(){
        // 1
        wishlistDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            WishlistDatabase::class.java).build()
        // 2
        wishlistDao = wishlistDatabase.wishlistDao()
    }

    @Test
    fun getAllReturnsEmptyWhenNothingSaved(){
        val testObserver : Observer<List<Wishlist>> = mock()
        wishlistDao.getAll().observeForever(testObserver)
        verify(testObserver).onChanged(emptyList())
    }

    @Test
    fun saveWishlistSavesData(){
        // 1
        val wishlist1 = Wishlist("Victoria", listOf(), 1)
        val wishlist2 = Wishlist("Tyler", listOf(), 2)
        wishlistDao.save(wishlist1, wishlist2)

        // 2
        val testObserver: Observer<List<Wishlist>> = mock()
        wishlistDao.getAll().observeForever(testObserver)

        val listClass = ArrayList::class.java as Class<ArrayList<Wishlist>>

        val argumentCaptor = ArgumentCaptor.forClass(listClass)

        verify(testObserver).onChanged(argumentCaptor.capture())

        Assert.assertTrue(argumentCaptor.value.size>0)
    }

    @Test
    fun getAllRetrievesData(){
        // 1
        val wishlist1 = Wishlist("Victoria", listOf(), 1)
        val wishlist2 = Wishlist("Tyler", listOf(), 2)
        wishlistDao.save(wishlist1, wishlist2)


        // 2
        val testObserver: Observer<List<Wishlist>> = mock()
        wishlistDao.getAll().observeForever(testObserver)

        val listClass =
            ArrayList::class.java as Class<ArrayList<Wishlist>>
        val argumentCaptor = ArgumentCaptor.forClass(listClass)
        verify(testObserver).onChanged(argumentCaptor.capture())
        val capturedArgument = argumentCaptor.value
        Assert.assertTrue(capturedArgument.containsAll(listOf(wishlist1,wishlist2)))

    }

    @Test
    fun findByIdRetrievesCorrectData(){
        // 1
        val wishlist1 = Wishlist("Victoria", listOf(), 1)
        val wishlist2 = Wishlist("Tyler", listOf(), 2)
        wishlistDao.save(wishlist1, wishlist2)


        // 2
        val testObserver: Observer<Wishlist> = mock()
        wishlistDao.findById(wishlist2.id).observeForever(testObserver)

        verify(testObserver).onChanged(wishlist2)


    }


    @After
    fun closeDb() {
        wishlistDatabase.close()
    }

}