package lee.todo.Activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;
import java.util.ArrayList;
import java.util.List;

import lee.todo.R;
import lee.todo.Adapter.TodoAdapter;
import lee.todo.Adapter.TodoList;
import lee.todo.Service.ReminderService;
import lee.todo.Util.LogUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout swipeRefresh;
    private RecyclerView recyclerView;
    private StaggeredGridLayoutManager manager;
    private List<TodoList> todoLists=new ArrayList<>();
    private TodoAdapter adapter;
    private int row=2;
    private final static String TAG="MainActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        swipeRefresh=(SwipeRefreshLayout)findViewById(R.id.swipe_layout);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        recyclerView=(RecyclerView)findViewById(R.id.recycleView);
        FloatingActionButton floatingButton=(FloatingActionButton)findViewById(R.id.float_button);
        //GridLayoutManager manager=new GridLayoutManager(this,row);
        manager=new StaggeredGridLayoutManager(row,StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(manager);
        adapter=new TodoAdapter(MainActivity.this,todoLists);
        recyclerView.setAdapter(adapter);
        //开启reminderService
        Intent serviceIntent=new Intent(this, ReminderService.class);
        startService(serviceIntent);
        LogUtil.d(TAG,"service start");
        //初始化数据
        initalLits();
        floatingButton.setOnClickListener(this);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshList();
            }
        });
        adapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
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
                /*String note=todoLists.get(pos).getNote();
                LogUtil.d("note",pos+" "+note);
                DataSupport.deleteAll(TodoList.class,"note=?",note);
                todoLists.remove(pos);
                adapter.notifyDataSetChanged();*/
                startMyActivity(pos,MarkdownActivity.class);
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch ((v.getId())){
            case R.id.float_button:

                Intent intent=new Intent(this,EditActivity.class);
                startActivity(intent);
                TodoList todo=new TodoList();
                //DataSupport.deleteAll(TodoList.class);
                //Toast.makeText(this,"hi",Toast.LENGTH_SHORT).show();
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
                if ((row == 2)) {
                    row = 1;
                } else {
                    row = 2;
                }
                switchIcon(item);

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
            item.setIcon(getResources().getDrawable(R.drawable.ic_span_3));
        } else {
            item.setIcon(getResources().getDrawable(R.drawable.ic_span_1));
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycleView);
                //GridLayoutManager manager=new GridLayoutManager(MainActivity.this,row);
                manager=new StaggeredGridLayoutManager(row,StaggeredGridLayoutManager.VERTICAL);
                recyclerView.setLayoutManager(manager);
                adapter=new TodoAdapter(MainActivity.this,todoLists);
                recyclerView.setAdapter(adapter);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                initalLits();
                adapter.setOnItemClickListener(new TodoAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int pos) {
                        //Toast.makeText(MainActivity.this, pos+"", Toast.LENGTH_SHORT).show();
                        String title=todoLists.get(pos).getTitle();
                        String note=todoLists.get(pos).getNote();
                        Intent updataIntent=new Intent(MainActivity.this,UpdateActivity.class);
                        updataIntent.putExtra("title",title);
                        updataIntent.putExtra("note",note);
                        startActivity(updataIntent);
                    }

                    @Override
                    public void onItemLongClick(View view, int pos) {
                        //Toast.makeText(MainActivity.this, "Deleted!", Toast.LENGTH_SHORT).show();
                        String note=todoLists.get(pos).getNote();
                        LogUtil.d(TAG,"note"+note);
                        DataSupport.deleteAll(TodoList.class,"note=?",note);
                        todoLists.remove(pos);
                        adapter.notifyDataSetChanged();
                    }
                });

            }
        });
        adapter.notifyItemRangeChanged(0,adapter.getItemCount());
        LogUtil.d(TAG,"switch");
    }

    private  void initalLits(){
        todoLists.clear();
        Connector.getDatabase();
        //查询数据库
        List<TodoList>todolists=DataSupport.findAll(TodoList.class);
        try {
            for (int i=0;i<todolists.size();i++){
                //添加到最前面
                //for (int j=i;j<todolists.size()){
                //    if (todolists.get(j).getTime()todolists.get(j).getTime())
                //}
                todoLists.add(0,todolists.get(i));
                LogUtil.d(TAG,"initial List:"+todolists.get(i).getTitle()+i);
            }
            todolists.clear();
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshList(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(1000);
                }catch (InterruptedException e){
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //initalLits();
                        swipeRefresh.setRefreshing(false);
                    }
                });
            }
        }).start();
    }

    private void startMyActivity(int pos,Class activity){
        LogUtil.d(TAG,pos+"");
        String title=todoLists.get(pos).getTitle();
        String note=todoLists.get(pos).getNote();
        LogUtil.d("MainActivity",note);
        Intent intent=new Intent(MainActivity.this,activity);
        intent.putExtra("title",title);
        intent.putExtra("note",note);
        startActivity(intent);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        todoLists.clear();
        List<TodoList>todolists=DataSupport.findAll(TodoList.class);
        try {
            for (int i=0;i<todolists.size();i++){
                //添加到最前面
                todoLists.add(0,todolists.get(i));
                LogUtil.d(TAG,"Title:"+todolists.get(i).getTitle()+i);
            }
            todolists.clear();
            adapter.notifyDataSetChanged();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
