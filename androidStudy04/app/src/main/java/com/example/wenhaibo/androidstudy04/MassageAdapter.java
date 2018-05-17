package com.example.wenhaibo.androidstudy04;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class MassageAdapter extends RecyclerView.Adapter<MassageAdapter.ViewHolder> {




    private List<Message> messageList;

    static  class  ViewHolder extends  RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftmsg;
        TextView rightmsg;


        public ViewHolder(View itemView) {
            super( itemView );

            //布局LinearLayout 左右

            leftLayout=(LinearLayout) itemView.findViewById( R.id.left_layout );
            rightLayout=(LinearLayout) itemView.findViewById( R.id.right_layout );

            //控件  id的绑定

            leftmsg=itemView.findViewById( R.id.left_mesage );
            rightmsg=itemView.findViewById( R.id.right_massage );





        }
    }


    public MassageAdapter(List<Message> messageList) {
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MassageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from( parent.getContext() ).inflate( R.layout.mesage_item,parent,false );

        return  new ViewHolder( view );

        //return null;


    }

    @Override
    public void onBindViewHolder(@NonNull MassageAdapter.ViewHolder holder, int position) {

        Message message=messageList.get( position );

        if (message.getType()==Message.TYPE_RECEIVED){

            //加载左边布局
            holder.leftLayout.setVisibility( View.VISIBLE );
            holder.rightLayout.setVisibility( View.GONE );
            holder.leftmsg.setText( message.getContent() );

        }else if(message.getType()==Message.TYPE_SENT) {

            //加载右边布局
            holder.rightLayout.setVisibility( View.VISIBLE );
            holder.rightLayout.setVisibility( View.GONE );
            holder.rightmsg.setText( message.getContent() );




        }





    }

    @Override
    public int getItemCount() {
        return messageList.size();
    }
}
