package com.example.mycalldemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ContactsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    Context mContext;
    ArrayList<ContactsBean> list;

    public ContactsAdapter(Context context){
        this.mContext=context;
    }
    public void setData(ArrayList<ContactsBean> list){
        this.list=list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(mContext).inflate(R.layout.item_contacts,parent,false);
        return new ItemHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ItemHolder itemHolder= (ItemHolder) holder;
        ContactsBean bean=list.get(position);
        itemHolder.iv.setImageBitmap(bean.getBm());
        itemHolder.name.setText("姓名:"+bean.getName());
        List<ContactsBean.phoneBean> phones = bean.getPhoneBeans();
        if (phones!=null&&phones.size()!=0){
            itemHolder.num1.setText(phones.get(0).getPhone());
            if (phones.size()>1){
                itemHolder.num2.setText(phones.get(1).getPhone());
            }
        }
        List<ContactsBean.addBean> address = bean.getAddressBeans();
        if (address!=null&&address.size()!=0){
            itemHolder.address1.setText(address.get(0).getAddress());
            if (address.size()>1){
                itemHolder.address2.setText(address.get(1).getAddress());
            }
            if (address.size()>2){
                itemHolder.address3.setText(address.get(2).getAddress());
            }
        }
        List<ContactsBean.emailBean> emails = bean.getEmailBeans();
        if (emails!=null&&emails.size()!=0){
            itemHolder.email1.setText(emails.get(0).getEmail());
            if (emails.size()>1){
                itemHolder.email2.setText(emails.get(1).getEmail());
            }
            if (emails.size()>2){
                itemHolder.email3.setText(emails.get(2).getEmail());
            }
        }
        itemHolder.company.setText("公司:"+bean.getCompany());
        itemHolder.job.setText("职位:"+bean.getJob());
    }

    @Override
    public int getItemCount() {
        if (list==null||list.size()==0){
            return 0;
        }
        return list.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    static class ItemHolder extends RecyclerView.ViewHolder{
        TextView name,num1,num2,email1,email2,email3,address1,address2,address3,company,job;
        ImageView iv;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            iv=itemView.findViewById(R.id.iv);
            name=itemView.findViewById(R.id.name);
            num1=itemView.findViewById(R.id.num1);
            num2=itemView.findViewById(R.id.num2);
            email1=itemView.findViewById(R.id.email1);
            email2=itemView.findViewById(R.id.email2);
            email3=itemView.findViewById(R.id.email3);
            address1=itemView.findViewById(R.id.address1);
            address2=itemView.findViewById(R.id.address2);
            address3=itemView.findViewById(R.id.address3);
            company=itemView.findViewById(R.id.company);
            job=itemView.findViewById(R.id.job);
        }
    }
}
