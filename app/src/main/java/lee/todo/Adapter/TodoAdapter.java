package lee.todo.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;
import lee.todo.R;

/**
 * @Author lee
 * @Time 5/15/17.
 */

public class TodoAdapter extends RecyclerView.Adapter<TodoAdapter.ViewHolder>{
    private List<TodoList> mTodoList;
    private Context mContext;

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view,int pos);
        void onItemLongClick(View view,int pos);
    }

    //内部类ViewHolder
    static class ViewHolder extends RecyclerView.ViewHolder{
        View fView;
        TextView edit_title;
        TextView edit_note;
        public ViewHolder(View view){
            super(view);
            fView=view;
            edit_title=(TextView)view.findViewById(R.id.title) ;
            edit_note=(TextView)view.findViewById(R.id.note);
        }
    }
    /**
     *构造方法,外部传入List
     * @param TodoList
     */
    public TodoAdapter(Context context,List<TodoList> TodoList){
        this.mTodoList =TodoList;
        this.mContext=context;
    }
    //创建实例
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view= LayoutInflater.from(mContext).inflate(R.layout.todolist_item,parent,false);
        ViewHolder holder=new ViewHolder(view);
        return holder;
    }



    //RecyclerView子项赋值
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position){
        TodoList todoList= mTodoList.get(position);
        holder.edit_title.setText(todoList.getTitle());
        holder.edit_note.setText(todoList.getNote());
        if (onItemClickListener !=null){
            holder.fView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //int pos=holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.fView,position);
                }
            });
            holder.fView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    onItemClickListener.onItemLongClick(holder.fView,position);
                    return false;
                }
            });
        }

    }
    @Override
    public int getItemCount(){
        return mTodoList.size();
    }



}
