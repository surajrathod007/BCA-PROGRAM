package com.surajrathod.bcaprogram.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.TableLayout
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.firestore.FirebaseFirestore
import com.surajrathod.bcaprogram.BuildConfig
import com.surajrathod.bcaprogram.MainViewPagerAdapter
import com.surajrathod.bcaprogram.R
import com.surajrathod.bcaprogram.model.AppUpdate
import com.surajrathod.bcaprogram.network.NetworkService
import com.surajrathod.bcaprogram.ui.fragments.DashboardFragment
import com.surajrathod.bcaprogram.ui.fragments.FavouritesFragment
import com.surajrathod.bcaprogram.ui.fragments.ShareFragment
import com.surajrathod.bcaprogram.utils.DataStoreConstants
import com.surajrathod.bcaprogram.utils.TapTargetMaker
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.update_dialog_layout.*
import kotlinx.android.synthetic.main.welcome_screen.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
val Context.preferenceDataStore : DataStore<Preferences> by preferencesDataStore(DataStoreConstants.DATA_STORE_NAME)
var shareLink : String? = null
class MainActivity : AppCompatActivity() {
    var thisVersion = BuildConfig.VERSION_NAME.toFloat()
    val tapTargetBuilder = TapTargetMaker()
    lateinit var viewpager : ViewPager2
    lateinit var tabs : TableLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // App Tutorial for new Users  --------------------------------------------------------------------------------------
        CoroutineScope(Dispatchers.IO).launch {
            val preferences = preferenceDataStore.data.first()
            val isTutorialDone = preferences[booleanPreferencesKey(DataStoreConstants.DS_KEY_IS_TUTORIAL_DONE)]
            shareLink = preferences[stringPreferencesKey(DataStoreConstants.DS_KEY_APP_SHARING_LINK)]
            println("UPDATE LINK : $shareLink")
            if(isTutorialDone != true){
                setUpTutorial()
                preferenceDataStore.edit {
                    it[booleanPreferencesKey("isTutorialDone")] = true
                }
            }
        }
        // App Updates Setup    ---------------------------------------------------------------------------------------------
        var intent = Intent(this,MainActivity::class.java)

        val update = FirebaseFirestore.getInstance().collection("newPrograms").document("appUpdate").get()
        update.addOnSuccessListener {
            val data = it.data
            val update = AppUpdate(
                id = 1,
                link = it.get("link").toString(),
                version = it.get("version").toString().toFloat(),
                message = it.get("message").toString(),
            )
            if(update.version>thisVersion){
                msg.text = msg.text.toString() + update.message
                GlobalScope.launch {
                    preferenceDataStore.edit {
                        it[stringPreferencesKey(DataStoreConstants.DS_KEY_APP_SHARING_LINK)] = update.link
                    }
                }
                intent = setUpLink(update.link)
                toggleUpdateDialog()
            }
            if(shareLink==null){
                GlobalScope.launch {
                    preferenceDataStore.edit {
                        it[stringPreferencesKey(DataStoreConstants.DS_KEY_APP_SHARING_LINK)] = update.link
                    }
                }
                shareLink = update.link
            }
        }
            .addOnFailureListener {
                Toast.makeText(this@MainActivity,"Update check failed !",Toast.LENGTH_LONG).show()
            }
        /*
       val checkUpdates = NetworkService.networkInstance.checkForUpdates()
        checkUpdates.enqueue(object : Callback<AppUpdate> {
            override fun onResponse(call: Call<AppUpdate>, response: Response<AppUpdate>) {
                println("Response is ${response.body()}")
                val data = response.body()!!
                if(data.version>thisVersion){
                    println("Update available")
                    msg.text = msg.text.toString() + data.message
                    GlobalScope.launch {
                        preferenceDataStore.edit {
                            it[stringPreferencesKey(DataStoreConstants.DS_KEY_APP_SHARING_LINK)] = data.link
                        }
                    }
                    intent = setUpLink(data.link)
                        toggleUpdateDialog()
                }
                if(shareLink==null){
                    GlobalScope.launch {
                        preferenceDataStore.edit {
                            it[stringPreferencesKey(DataStoreConstants.DS_KEY_APP_SHARING_LINK)] = data.link
                        }
                    }
                    shareLink = data.link
                }
            }
            override fun onFailure(call: Call<AppUpdate>, t: Throwable) {
               println("RETROFIT ERROR WHILE CHECKING UPDATES : $t")
            }
        })
         */


        updateBtn.setOnClickListener{
            Toast.makeText(this@MainActivity, "updating..", Toast.LENGTH_SHORT).show()
            toggleUpdateDialog()
            startActivity(intent)
        }
        laterBtn.setOnClickListener{
            toggleUpdateDialog()
        }
        // Bottom Navigation ----------------------------------------------------------------------------------------------------
//        bottomNavigationView.setupWithNavController(findNavController(R.id.fragmentContainerView))
        setUpViewPager()

    }
    // Class Functions ----------------------------------------------------------------------------------------------------------
    private fun setUpTutorial(){
        bottomNavigationView.visibility = GONE
        welcomeScreen.visibility = VISIBLE

        getStartedBtn.setOnClickListener {
            val tutorial = createTapTargetSequence()!!
            tutorial.start()
            bottomNavigationView.visibility = VISIBLE
            welcomeScreen.visibility = GONE
        }
        skipBtn.setOnClickListener {
            bottomNavigationView.visibility = VISIBLE
            welcomeScreen.visibility = GONE
            GlobalScope.launch {
                preferenceDataStore.edit {
                    it[booleanPreferencesKey(DataStoreConstants.DS_KEY_IS_TUTORIAL_DONE)] = true
                }
            }
        }
    }

    private fun setUpLink(link: String) : Intent {
        return Intent(Intent.ACTION_VIEW, Uri.parse(link))
    }

    private fun setUpViewPager(){
        val fragmentList = listOf(DashboardFragment(),FavouritesFragment(),ShareFragment())
        fragmentContainerView.adapter = MainViewPagerAdapter(fragmentList,this)
        bottomNavigationView.setupWithViewPager2(fragmentContainerView)
      /*  TabLayoutMediator(bottomNavigationView,fragmentContainerView){tab,pos->
            tab.icon=when(pos){
                1-> resources.getDrawable(R.drawable.ic_baseline_favorite_24)
                2->  resources.getDrawable(R.drawable.ic_baseline_share_24)
                else ->  resources.getDrawable(R.drawable.ic_baseline_home_24)
            }
        }.attach()*/
    }

    private fun toggleUpdateDialog() {
        if(updateDialog.isVisible){
            updateDialog.visibility = GONE
            bottomNavigationView.visibility = VISIBLE

        }else{

            updateDialog.visibility = VISIBLE
            bottomNavigationView.visibility = GONE
        }
    }

    private fun createTapTargetSequence(): TapTargetSequence? {
        return TapTargetSequence(this).targets(
            tapTargetBuilder.createTapTarget(findViewById(R.id.dashboardFragment), "Home ","Here You will Get All Programs for Your BCA Journey"),
            tapTargetBuilder.createTapTarget(searchButton,"Search","Find Programs for your different Semesters and Subjects"),
            tapTargetBuilder.createTapTarget(findViewById(R.id.ttFavBtn),"Make It Favourite","Mark Programs with ❤ to Find Them Quickly"),
            tapTargetBuilder.createTapTarget(findViewById(R.id.favouritesFragment),"Favourites","Your ❤ Programs will Appear here Offline for You "),
            tapTargetBuilder.createTapTarget(findViewById(R.id.shareFragment),"Sharing Is Caring", "Share this App with Your \nBCA mates , Happy Coding !"),
        ).listener(object : TapTargetSequence.Listener{
            override fun onSequenceFinish() {

            }
            override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}
            override fun onSequenceCanceled(lastTarget: TapTarget?) {}
        }).continueOnCancel(true)
    }

}