package codes.walid4444.notes.Fragment.ViewModel

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat.startActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import codes.walid4444.notes.Adpaters.HomeNotesAdapter
import codes.walid4444.notes.Fragment.HomeFragment
import codes.walid4444.notes.Helper.Database.Model.Note
import codes.walid4444.notes.Helper.FireStore.FireStoreUtility
import codes.walid4444.notes.Helper.NoteRepository
import codes.walid4444.notes.Helper.SharedPrefManger
import codes.walid4444.notes.R
import me.everything.android.ui.overscroll.OverScrollDecoratorHelper


class HomeViewModel : ViewModel() {
    private lateinit var repository: NoteRepository;
    private lateinit var noteList: LiveData<List<Note>>
    private lateinit var mContext: Context;
    lateinit var note_home_adapter: HomeNotesAdapter;
    private var navController: NavController? = null;
    private lateinit var notesLiveData: LiveData<PagedList<Note>>
    private lateinit var gridLayoutManager: StaggeredGridLayoutManager;
    private lateinit var note_home_recyclerview: RecyclerView;
    public lateinit var mMenu: Menu
    public lateinit var homeFragment: HomeFragment;
    lateinit var sharedPrefManger : SharedPrefManger;
    var fireStoreUtility = FireStoreUtility()
    fun Init(v: View?) {
        this.mContext = v!!.context;
        this.navController = Navigation.findNavController(v!!);
        repository = NoteRepository(mContext)
        noteList = repository.myNotes!!
        sharedPrefManger = SharedPrefManger(mContext)
        fireStoreUtility
    }

    lateinit var callback: OnBackPressedCallback ;
    fun setFragment(homeFragment: HomeFragment){
        this.homeFragment = homeFragment;
        changeBackFunction(false)
    }

    private fun changeBackFunction(isActivated: Boolean) {
        if (!::callback.isInitialized){
            callback = object : OnBackPressedCallback(isActivated) {
                override fun handleOnBackPressed() {
                    closeSelectMode()
                    this.isEnabled = false
                }
            }
        }else
            callback.isEnabled=isActivated
        homeFragment.requireActivity()
            .onBackPressedDispatcher.addCallback(homeFragment.viewLifecycleOwner, callback)
    }

    fun RecyclerViewInit(note_home_recyclerview: RecyclerView) {
        this.note_home_recyclerview = note_home_recyclerview
        note_home_adapter = HomeNotesAdapter(mContext, ArrayList())
        val gridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        val factory: DataSource.Factory<Int, Note> = repository.PagedNotes

        val pagedListBuilder: LivePagedListBuilder<Int, Note> =
            LivePagedListBuilder<Int, Note>(factory, 5)
        notesLiveData = pagedListBuilder.build()

        var animShake: Animation = AnimationUtils.loadAnimation(mContext, R.anim.shake);


        note_home_adapter.mSelectModeObservable.map {
            onRecyclerViewSelectedModeChanged(it, animShake)
            return@map
        }.subscribe({ Log.i("mSelectMode", "changed") })
        note_home_recyclerview.layoutManager = gridLayoutManager
        note_home_recyclerview.setHasFixedSize(true)
        note_home_recyclerview.adapter = note_home_adapter;
        OverScrollDecoratorHelper.setUpOverScroll(note_home_recyclerview, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

    }

    private fun onRecyclerViewSelectedModeChanged(
        isActivated: Boolean,
        animShake: Animation
    ) {
        fun CheckisSharable() {
            if (note_home_adapter.numberOfSelected() == 1)
                mMenu.findItem(R.id.action_share).setVisible(true)
            else
                mMenu.findItem(R.id.action_share).setVisible(false)
        }

        if (isActivated) {
            changeBackFunction(true)
            CheckisSharable()
            mMenu.findItem(R.id.action_close).setVisible(true)
            mMenu.findItem(R.id.action_delete).setVisible(true)

        } else {
            changeBackFunction(false)
            mMenu.findItem(R.id.action_delete).setVisible(false)
            mMenu.findItem(R.id.action_close).setVisible(false)
            CheckisSharable()
            (mContext as Activity).runOnUiThread(Runnable {
                note_home_recyclerview.startAnimation(animShake);
            })
        }
    }


    fun getMyNotes(): LiveData<List<Note>> {
        return noteList;
    }


    fun onClick(v: View?) {
        when (v!!.id) {
            R.id.add_float_btn -> navController!!.navigate(R.id.action_homeFragment_to_noteDetailsFragment)
        }
    }


    fun onOptionsItemSelected(item: MenuItem, sharedPrefManger: SharedPrefManger) {
        when (item.itemId) {
            R.id.logout_app -> {
                sharedPrefManger.Logout()
                navController!!.navigate(R.id.action_homeFragment_to_loginFragment)
            }
            R.id.action_delete -> deleteSelectedItems()
            R.id.action_share ->shareItem()
            R.id.action_close ->closeSelectMode()
        }
    }

    fun deleteSelectedItems() {
        note_home_adapter.getSelectedItems().forEach({
            note_home_adapter.notifyItemRemoved(note_home_adapter.getItemPosition(it))
            repository.delete(it)
            if (sharedPrefManger.isLoggedIn)
                fireStoreUtility.deleteNote(it)
            note_home_adapter.mSelectModeObservable.onNext(false)
        })
    }

    fun shareItem() {
        if (note_home_adapter.getSelectedItems().size == 1) {

            val sharingIntent = Intent(Intent.ACTION_SEND)
            sharingIntent.type = "text/plain"
            sharingIntent.putExtra(
                Intent.EXTRA_SUBJECT,
                note_home_adapter.getSelectedItems()[0].title
            )
            sharingIntent.putExtra(
                Intent.EXTRA_TEXT,
                note_home_adapter.getSelectedItems()[0].description
            )
            startActivity(mContext, Intent.createChooser(sharingIntent, "Share via"), null)
        } else {
            if (note_home_adapter.getSelectedItems().size == 0)
                note_home_adapter.mSelectModeObservable.onNext(false)
            else
                note_home_adapter.mSelectModeObservable.onNext(true)
        }
    }

    fun closeSelectMode(){
        note_home_adapter.closeSelectMode()
        note_home_adapter.mSelectModeObservable.onNext(false)
    }
}


