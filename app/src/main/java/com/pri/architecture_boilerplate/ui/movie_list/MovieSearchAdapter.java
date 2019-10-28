package com.pri.architecture_boilerplate.ui.movie_list;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.pri.architecture_boilerplate.vo.Movie;
import com.pri.architecture_boilerplate.R;
import com.pri.architecture_boilerplate.databinding.ItemSearchBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.pri.architecture_boilerplate.api.ApiConstantsKt.IMAGE_BASE_URL;

public class MovieSearchAdapter extends RecyclerView.Adapter<MovieSearchAdapter.ViewHolder> {

    private List<Movie> data;

    public MovieSearchAdapter(List<Movie> data) {
        this.data = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemSearchBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_search, parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ItemSearchBinding binding = holder.binding;
        Movie movie = data.get(position);
        binding.setData(movie);
        String url = IMAGE_BASE_URL + movie.getBackdropPath();
        Picasso.get().load(url).into(binding.ivThumb);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ItemSearchBinding binding;

        ViewHolder(ItemSearchBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
