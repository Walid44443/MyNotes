package codes.walid4444.notes.Fragment.ViewModel

import android.content.Context
import android.view.View
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
import codes.walid4444.notes.Helper.Database.Model.Note
import codes.walid4444.notes.Helper.NoteRepository
import codes.walid4444.notes.R


class HomeViewModel : ViewModel() {
    private lateinit var repository : NoteRepository;
    private lateinit var noteList: LiveData<List<Note>>
    private lateinit var mContext: Context;
    lateinit var note_home_adapter : HomeNotesAdapter;
    private var navController : NavController? = null;
    private lateinit var notesLiveData: LiveData<PagedList<Note>>
    private lateinit var gridLayoutManager : StaggeredGridLayoutManager;
    private lateinit var note_home_recyclerview : RecyclerView;
    fun Init(v: View?){
        this.mContext = v!!.context;
        this.navController = Navigation.findNavController(v!!);
        repository = NoteRepository(mContext)
        noteList = repository.myNotes!!
    }

    fun RecyclerViewInit(note_home_recyclerview : RecyclerView){
        this.note_home_recyclerview =note_home_recyclerview
        note_home_adapter = HomeNotesAdapter(mContext,ArrayList())
        val gridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)

        val factory: DataSource.Factory<Int, Note> = repository.PagedNotes

        val pagedListBuilder: LivePagedListBuilder<Int, Note>  = LivePagedListBuilder<Int, Note>(factory, 5)
        notesLiveData = pagedListBuilder.build()



        note_home_recyclerview.layoutManager = gridLayoutManager
        note_home_recyclerview.setHasFixedSize(true)
        note_home_recyclerview.adapter = note_home_adapter;
    }

    fun getNoteLiveData() = notesLiveData

    fun getMyNotes() : LiveData<List<Note>>{
        return noteList;
    }


    fun onClick(v : View?){
        when(v!!.id){
            R.id.add_float_btn -> navController!!.navigate(R.id.action_homeFragment_to_noteDetailsFragment)
        }
    }

}
