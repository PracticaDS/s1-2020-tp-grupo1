package ar.edu.unq.pdes.grupo1.myprivateblog.di

import android.content.Context
import androidx.lifecycle.ViewModel
import ar.edu.unq.pdes.grupo1.myprivateblog.BaseApplication
import ar.edu.unq.pdes.grupo1.myprivateblog.MainActivity
import ar.edu.unq.pdes.grupo1.myprivateblog.MainActivityViewModel
import ar.edu.unq.pdes.grupo1.myprivateblog.data.AppDatabase
import ar.edu.unq.pdes.grupo1.myprivateblog.data.BlogEntriesRepository
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.post_create.PostCreateFragment
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.post_create.PostCreateViewModel
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.post_detail.PostDetailFragment
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.post_detail.PostDetailViewModel
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.post_edit.PostEditFragment
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.post_edit.PostEditViewModel
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.posts_listing.PostsListingFragment
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.posts_listing.PostsListingViewModel
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.sign_in.SignInFragment
import ar.edu.unq.pdes.grupo1.myprivateblog.screens.sign_in.SignInViewModel
import ar.edu.unq.pdes.grupo1.myprivateblog.services.BlogEntriesService
import ar.edu.unq.pdes.grupo1.myprivateblog.services.CryptoService
import dagger.*
import dagger.android.AndroidInjector
import dagger.android.ContributesAndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import dagger.multibindings.IntoMap
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidSupportInjectionModule::class,
        ApplicationModule::class,
        MainActivityModule::class
    ])
interface ApplicationComponent : AndroidInjector<BaseApplication> {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance applicationContext: Context): ApplicationComponent
    }
}

@Module
open class ApplicationModule {

    @Singleton
    @Provides
    fun provideAppDatabase(context: Context): AppDatabase {
        return AppDatabase.generateDatabase(context)
    }

    @Singleton
    @Provides
    fun provideBlogEntriesRepository(appDatabase: AppDatabase): BlogEntriesRepository {
        return BlogEntriesRepository(
            appDatabase
        )
    }

    @Singleton
    @Provides
    fun provideCryptoService(): CryptoService {
        return CryptoService()
    }

    @Singleton
    @Provides
    fun provideBlogEntriesService(blogEntriesRepository: BlogEntriesRepository, context: Context, cryptoService: CryptoService): BlogEntriesService {
        return BlogEntriesService(
            blogEntriesRepository,
            context,
            cryptoService
        )
    }
}

@Module(
    includes = [
        SignInModule::class,
        PostsListingModule::class,
        PostDetailModule::class,
        PostEditModule::class,
        PostCreateModule::class
    ]
)
abstract class MainActivityModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun mainActivity(): MainActivity

    @Binds
    @IntoMap
    @ViewModelKey(MainActivityViewModel::class)
    abstract fun bindViewModel(viewmodel: MainActivityViewModel): ViewModel
}

@Module
abstract class PostsListingModule {

    @ContributesAndroidInjector(modules = [
        ViewModelBuilder::class
    ])
    internal abstract fun postsListingFragment(): PostsListingFragment

    @Binds
    @IntoMap
    @ViewModelKey(PostsListingViewModel::class)
    abstract fun bindViewModel(viewmodel: PostsListingViewModel): ViewModel
}

@Module
abstract class PostDetailModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun postDetailFragment(): PostDetailFragment

    @Binds
    @IntoMap
    @ViewModelKey(PostDetailViewModel::class)
    abstract fun bindViewModel(viewmodel: PostDetailViewModel): ViewModel
}

@Module
abstract class SignInModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun signInFragment(): SignInFragment

    @Binds
    @IntoMap
    @ViewModelKey(SignInViewModel::class)
    abstract fun bindViewModel(viewmodel: SignInViewModel): ViewModel
}

@Module
abstract class PostEditModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun postEditFragment(): PostEditFragment

    @Binds
    @IntoMap
    @ViewModelKey(PostEditViewModel::class)
    abstract fun bindViewModel(viewmodel: PostEditViewModel): ViewModel
}

@Module
abstract class PostCreateModule {

    @ContributesAndroidInjector(
        modules = [
            ViewModelBuilder::class
        ]
    )
    internal abstract fun postCreateFragment(): PostCreateFragment

    @Binds
    @IntoMap
    @ViewModelKey(PostCreateViewModel::class)
    abstract fun bindViewModel(viewmodel: PostCreateViewModel): ViewModel
}