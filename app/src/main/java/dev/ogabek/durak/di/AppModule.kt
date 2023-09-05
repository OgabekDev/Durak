package dev.ogabek.durak.di

import android.app.Application
import android.content.Context
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    private val application = Application()

    @Singleton
    @Provides
    fun getReference(): DatabaseReference {
        return FirebaseDatabase.getInstance().getReferenceFromUrl("https://durak-ogabekdev-default-rtdb.firebaseio.com")
    }

    @Singleton
    @Provides
    fun getContext(): Context {
        return application
    }

}