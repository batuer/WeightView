package com.gusi.example;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.gusi.weight.SizeUtils;
import com.gusi.weight.WeightView;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    RecyclerView rcv = findViewById(R.id.rcv);
    rcv.setLayoutManager(new LinearLayoutManager(this));
    rcv.setAdapter(new Adapter());
  }

  class Adapter extends RecyclerView.Adapter<ViewHolder> {
    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = getLayoutInflater().inflate(R.layout.item, parent, false);
      return new ViewHolder(view);
    }

    @Override public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
      holder.tv1.setText("Tv1 : " + position);
      holder.tv2.setText("Tv2: " + position);
      holder.tv3.setText("Tv3 : " + position);
      holder.tv4.setText("Tv4 : " + position);
      WeightView itemView = (WeightView) holder.itemView;
      itemView.setTopBorder(position == 0 ? SizeUtils.dp2px(MainActivity.this, 1) : 0);
    }

    @Override public int getItemCount() {
      return 5;
    }
  }

  class ViewHolder extends RecyclerView.ViewHolder {
    private TextView tv1, tv2, tv3, tv4;

    public ViewHolder(View itemView) {
      super(itemView);
      tv1 = itemView.findViewById(R.id.tv1);
      tv2 = itemView.findViewById(R.id.tv2);
      tv3 = itemView.findViewById(R.id.tv3);
      tv4 = itemView.findViewById(R.id.tv4);
    }
  }
}

