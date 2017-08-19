package lee.todo.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import lee.todo.Adapter.SimpleNote;
import lee.todo.R;
import lee.todo.Adapter.NoteAdapter;
import lee.todo.Service.ReminderService;
import lee.todo.Util.LogUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private GridLayoutManager manager;
    private List<SimpleNote> noteLists=new ArrayList<>();
    private NoteAdapter adapter;
    private FloatingActionButton floatingActionButton;
    private int row=2;
    private final static String TAG="MainActivity";
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //start reminderService
        Intent serviceIntent=new Intent(this, ReminderService.class);
        startService(serviceIntent);
        LogUtil.d(TAG,"service start");
        realm=Realm.getDefaultInstance();
        initUI();
        initList();
    }


    private void initUI(){
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        floatingActionButton=(FloatingActionButton)findViewById(R.id.float_button);
        manager=new GridLayoutManager(this,row);
        //manager=new StaggeredGridLayoutManager(row,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter=new NoteAdapter(MainActivity.this,noteLists);
        recyclerView.setAdapter(adapter);
        floatingActionButton.setOnClickListener(this);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
        adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
            //自定义的接口,点击编辑
            @Override
            public void onItemClick(View view, int pos) {
                //Toast.makeText(MainActivity.this, pos+"", Toast.LENGTH_SHORT).show();
                startMyActivity(pos,UpdateActivity.class);
            }
            //长按Markdown预览
            @Override
            public void onItemLongClick(View view, int pos) {
                //Toast.makeText(MainActivity.this, pos+" long", Toast.LENGTH_SHORT).show();
                /*String note=noteLists.get(pos).getNote();
                LogUtil.d("note",pos+" "+note);
                DataSupport.deleteAll(Note.class,"note=?",note);
                noteLists.remove(pos);
                adapter.notifyDataSetChanged();*/
                startMyActivity(pos,MarkdownActivity.class);
            }
        });
    }

    private void initList(){
        noteLists.clear();
        RealmQuery<SimpleNote> query=realm.where(SimpleNote.class);
        RealmResults<SimpleNote> results=query.findAll().sort("id");
        for (int i=0;i<results.size();i++){
            noteLists.add(0,results.get(i));
        }
        adapter.notifyItemChanged(0);
    }

    @Override
    public void onClick(View v) {
        switch ((v.getId())){
            case R.id.float_button:
                Intent intent=new Intent(this,AddEditActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.switch_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.switch_item:
                switchIcon(item);
                switchLayout();
                break;
            case R.id.about_item:
                Intent aboutIntent=new Intent(this,AboutActivity.class);
                startActivity(aboutIntent);
                break;
            case R.id.search_item:
                break;
            case R.id.setting_item:
                //Intent settingIntent=new Intent(this,SettingsActivity.class);
                //startActivity(settingIntent);
                break;
            default:
                break;
        }
        return true;
    }

    private void switchIcon(MenuItem item) {
        if (row==1) {
            item.setIcon(getResources().getDrawable(R.drawable.ic_span_1));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_span_2));
        }
    }

    private void switchLayout(){
        if (row==1){
            manager.setSpanCount(row=2);
        }else {
            manager.setSpanCount(row=1);
        }
        adapter.notifyItemChanged(0,adapter.getItemCount());
    }


    private void refreshList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(600);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initList();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void startMyActivity(int pos,Class activity){
        LogUtil.d(TAG,pos+"");
        String title=noteLists.get(pos).getTitle();
        String note=noteLists.get(pos).getNote();
        int id=noteLists.get(pos).getId();
        LogUtil.d("MainActivity",note);
        Intent intent=new Intent(MainActivity.this,activity);
        intent.putExtra("title",title);
        intent.putExtra("note",note);
        intent.putExtra("id",id);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        initList();
    }
}
