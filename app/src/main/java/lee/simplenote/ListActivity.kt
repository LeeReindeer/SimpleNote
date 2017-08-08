package lee.simplenote

import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.RequiresApi
import android.support.design.widget.FloatingActionButton
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import org.jetbrains.anko.intentFor
import org.jetbrains.anko.singleTop
import org.jetbrains.anko.startActivity
import org.litepal.crud.DataSupport
import org.litepal.tablemanager.Connector
import java.util.ArrayList

class ListActivity : AppCompatActivity(),View.OnClickListener {

    var noteList = ArrayList<Note>()
    var noteAdapter :NoteAdapter?=null
    var listView :ListView?=null
    var swipeRefresh:SwipeRefreshLayout?=null


    override  fun onPostResume() {
        super.onPostResume()
        runOnUiThread { initUI() }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.list_layout)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        val floatingButton=findViewById(R.id.float_button) as FloatingActionButton
        swipeRefresh=findViewById(R.id.swipe_layout) as SwipeRefreshLayout
        swipeRefresh!!.setColorSchemeColors(R.color.colorPrimary)
        swipeRefresh!!.setOnRefreshListener { refreshList() }
        initUI()
        //DetailActivity
        listView!!.setOnItemClickListener { parent, view, position, id ->
            var note:Note= noteList[position]
            val id=note.mId
            Toast.makeText(this, note.mNote, Toast.LENGTH_SHORT).show()
            startActivity(intentFor<DetailActivity>("id" to id).singleTop())
        }
        //Delete item
        listView!!.setOnItemLongClickListener { parent, view, position, id ->
            var noteId:String= noteList[position].mId.toString()
            DataSupport.deleteAll(Note ::class.java,"mId=?",noteId)
            initNote()
            Toast.makeText(this@ListActivity,"Deleted",Toast.LENGTH_SHORT).show()
            false
        }
        floatingButton.setOnClickListener(this)
    }


    fun initUI(){
        noteAdapter=NoteAdapter(this,R.layout.note_item,noteList)
        listView=findViewById(R.id.list_view) as ListView
        listView!!.adapter=noteAdapter
        initNote()
    }

    fun refreshList(){
        Thread(Runnable {
            try {
                initNote()
                Thread.sleep(500)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            runOnUiThread {
                swipeRefresh!!.isRefreshing=false
                initUI()
            }
        }).start()
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.float_button -> {
                //add new note
                startActivity(intentFor<AddEditActivity>().singleTop())
            }
            else -> {
            }
        }
    }

    private fun initNote(){
        //(1..3).mapTo(noteList) { Note(it,"hi", "this is test"+ it) }
        Connector.getDatabase()
        noteList.clear()
        var mNoteList =DataSupport.findAll(Note::class.java)
        try {
            //(mNoteList.size..0).mapTo(noteList){mNoteList[it]}
            for (i in 0..mNoteList.size){
                noteList.add(0,mNoteList[i])
                Log.d("noteList",mNoteList[i].toString())
            }
            //noteList.addAll(mNoteList)
            mNoteList.clear()
            noteAdapter!!.notifyDataSetChanged()
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}

class NoteAdapter(context: Context?, resource: Int, objects:List<Note> ) : ArrayAdapter<Note>(context, resource,objects){

    private val resourceId=resource

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {

        val note:Note=getItem(position)
        val view:View
        val viewHold:ViewHolder

        if (convertView==null){
            view = LayoutInflater.from(context).inflate(resourceId, parent, false)
            //viewHolder=new ViewHolder();
            viewHold = ViewHolder()
            viewHold.mTitle=view.findViewById(R.id.title_item) as TextView
            viewHold.mDescrip=view.findViewById(R.id.description_item) as TextView
            view.tag = viewHold
        }else{
            view=convertView
            viewHold=view.tag as ViewHolder
        }
        viewHold.mTitle?.text = note.mTitle
        viewHold.mDescrip?.text=if (note.mNote!!.length>15) (note.mNote!!.subSequence(0,9).toString()+"\n  See more...") else note.mNote
        return view
    }

    internal inner class ViewHolder {
        var mTitle: TextView ?= null
        var mDescrip: TextView? = null
    }
}
