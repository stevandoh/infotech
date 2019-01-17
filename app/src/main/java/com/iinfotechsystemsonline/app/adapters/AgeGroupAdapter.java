package com.iinfotechsystemsonline.app.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.iinfotechsystemsonline.app.R;
import com.iinfotechsystemsonline.app.models.AgeGroupMDL;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import io.realm.Realm;


public class AgeGroupAdapter extends RecyclerView.Adapter<AgeGroupAdapter.AgeListListViewHolder> {
    Context context;
    List<AgeGroupMDL> agelist;
    Realm realm;

    public AgeGroupAdapter(Context context, Realm realm, List<AgeGroupMDL> agelist) {
        this.context = context;
        this.realm = realm;
        this.agelist = agelist;

    }

    @Override
    public AgeListListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_age, parent, false);
        context = parent.getContext();
        return new AgeListListViewHolder(v);
    }


    //
//    @Override
//    public void onBindViewHolder(SubCategoryViewHolder subCategoryViewHolder, int position) {
//        final SubCategoryViewHolder mvh = subCategoryViewHolder;
//        final SubCategoryModel subCategory = getItem(position);
//        subCategoryViewHolder.mtxtSubCategoryId.setText(subCategory.getId());
//        subCategoryViewHolder.mtxtSubCategoryName.setText(subCategory.getName());
//        subCategoryViewHolder.mtxtSubCategoryPosition.setText(String.valueOf(position + 1) );
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return subCategories.size();
//    }
//
//    public void setFilter( List<SubCategoryModel> subCategoriesFiltered ) {
//        subCategories = new ArrayList<>();
//        subCategories.addAll(subCategoriesFiltered);
//        notifyDataSetChanged();
//    }
    public AgeGroupMDL getItem(int position) {
        if (this.agelist == null || this.agelist.get(position) == null) {
            return null;
        }
        return this.agelist.get(position);
    }


    @Override
    public void onBindViewHolder(AgeListListViewHolder holder, int position) {
        final AgeListListViewHolder mvh = holder;
        final AgeGroupMDL ageMDL = getItem(position);
//        holder.data = ageMDL;

        StringBuilder sb = new StringBuilder();
        sb.append(ageMDL.getMinage());
        sb.append("-");
        sb.append(ageMDL.getMaxage());
        holder.tvName.setText(ageMDL.getName());
        holder.tvId.setText(String.format("%d", ageMDL.getId()));
        holder.tvAge.setText(sb.toString());

//        holder.imageViewProfile;

//        Glide.with(context)
//                .load(ageMDL.getPhotoUri())
//                .placeholder(R.drawable.ic_person)
//                .into(imageView);
    }

    @Override
    public int getItemCount() {
        return agelist.size();
    }

//    @Override
//    public void onBindViewHolder(SubCategoryViewHolder subCategoryViewHolder, int position) {
//        final SubCategoryViewHolder mvh = subCategoryViewHolder;
//        final SubCategoryModel subCategory = getItem(position);
//        subCategoryViewHolder.mtxtSubCategoryId.setText(subCategory.getId());
//        subCategoryViewHolder.mtxtSubCategoryName.setText(subCategory.getName());
//        subCategoryViewHolder.mtxtSubCategoryPosition.setText(String.valueOf(position + 1) );
//    }
//
//
//    @Override
//    public int getItemCount() {
//        return subCategories.size();
//    }

    //    @Override
//    public Filter getFilter() {
//        SellerFilter filter = new SellerFilter(this);
//        return filter;
//    }
//
//    public void filterResults(String text) {
////        text = text == null ? null : text.toLowerCase().trim();
////        if(text == null || "".equals(text)) {
////            updateData(realm.where(AgeGroupMDL.class).findAll());
////        } else {
//            updateData(realm.where(AgeGroupMDL.class)
//                    .contains("sellerType", text, Case.INSENSITIVE) // TODO: change field
//                    .findAll());
////        }
//    }
//    private class SellerFilter
//            extends Filter {
//        private final AgeGroupAdapter adapter;
//
//        private SellerFilter(AgeGroupAdapter adapter) {
//            super();
//            this.adapter = adapter;
//        }
//
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            return new FilterResults();
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            adapter.filterResults(constraint.toString());
//        }
//    }
//
    public void setFilter(List<AgeGroupMDL> agelistFiltered) {
        agelist = new ArrayList<>();
        agelist.addAll(agelistFiltered);
        notifyDataSetChanged();
    }


//    public void filter(String text) {
//        items.clear();
//        if(text.isEmpty()){
//            items.addAll(itemsCopy);
//        } else{
//            text = text.toLowerCase();
//            for(PhoneBookItem item: itemsCopy){
//                if(item.name.toLowerCase().contains(text) || item.phone.toLowerCase().contains(text)){
//                    items.add(item);
//                }
//            }
//        }
//        notifyDataSetChanged();
//    }

//    public void setFilter( List<SubCategoryModel> subCategoriesFiltered ) {
//        subCategories = new ArrayList<>();
//        subCategories.addAll(subCategoriesFiltered);
//        notifyDataSetChanged();
//    }

    public class AgeListListViewHolder extends RecyclerView.ViewHolder {
        public AgeGroupMDL data;
        TextView tvName;
        TextView tvAge;
        TextView tvId;

//        Button buttonViewSellerDetail;

        public AgeListListViewHolder(View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvAge = itemView.findViewById(R.id.tvAge);
            tvId = itemView.findViewById(R.id.tvId);


        }


    }
}
