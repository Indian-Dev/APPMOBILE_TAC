package com.example.libeery.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libeery.R;
import com.example.libeery.model.Beer;
import com.example.libeery.view.DetailsBeerView;
import com.example.libeery.viewModel.BeersViewModel;
import com.example.libeery.model.BeerRoom;
import com.squareup.picasso.Picasso;

import java.util.List;

public class SearchBeerAdapter extends RecyclerView.Adapter<SearchBeerAdapter.ViewHolder> {

    private final List<BeerRoom> beers;
    private List<BeerRoom> filteredBeers;
    private final BeersViewModel viewModel;

    public SearchBeerAdapter(BeersViewModel viewModel, List<BeerRoom> beers) {
        this.viewModel = viewModel;
        this.beers = beers;
        this.filteredBeers = beers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searchbeer_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchBeerAdapter.ViewHolder holder, int position) {
        holder.display(beers.get(position));
    }

    @Override
    public int getItemCount() {
        return beers.size();
    }

    public void filter(String text) {
        filteredBeers.clear();
        if(text.isEmpty()){
            filteredBeers.addAll(viewModel.getBeerList().getValue());
        } else{
            text = text.toLowerCase();
            for(BeerRoom beer: viewModel.getBeerList().getValue()){
                if(beer.getName().toLowerCase().contains(text))
                    filteredBeers.add(beer);
            }
        }
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private BeerRoom beer;
        public TextView nameTextView;
        public TextView catNameTextView;
        public TextView countryTextView;
        public ImageView favoriteImage;
        public ImageView beerImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.nameTextView = itemView.findViewById(R.id.nameTextView);
            this.catNameTextView = itemView.findViewById(R.id.catNameTextView);
            this.countryTextView = itemView.findViewById(R.id.countryTextView);
            this.favoriteImage = itemView.findViewById(R.id.favoriteImage);
            this.beerImage = itemView.findViewById(R.id.beerImage);
            this.favoriteImage.setOnClickListener(v -> {
                beer.setFavorite(beer.getFavorite()!=1?1:0);
                display(beer);
                if(beer.getFavorite()==1)
                    viewModel.insert(beer);
                else
                    viewModel.delete(beer);
            });

           itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, DetailsBeerView.class);
                    intent.putExtra("beer", beer);
                    context.startActivity(intent);
                }
            });

        }

        public void display(BeerRoom beer) {
            this.beer = beer;
            nameTextView.setText(beer.getName());
            catNameTextView.setText(beer.getCatName());
            /*if(beer.getStyle() != null)
                countryTextView.setText(beer.getStyle().getShortName());*/
            if(beer.getImageURL() != null && !beer.getImageURL().isEmpty()){
                Picasso.get().load(beer.getImageURL()).into(this.beerImage);
            }
            if(beer.getFavorite()==1)
                favoriteImage.setImageResource(R.drawable.ic_lover);
            else
                favoriteImage.setImageResource(R.drawable.ic_like);
        }
    }
}