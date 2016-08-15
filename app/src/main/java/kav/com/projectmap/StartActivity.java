package kav.com.projectmap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

public class StartActivity extends AppCompatActivity {
    public DbHelper dbhelp;
    ArrayList<DbHelper.data> array1 = new ArrayList<>();
    ArrayList<String> list = new ArrayList<>();
    ArrayList<String> address = new ArrayList<>();

    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,MapsActivity.class);
                startActivity(intent);
            }
        });
        dbhelp = new DbHelper(this);
        dbhelp.getWritableDatabase();

        array1 = dbhelp.getAllLabels();
        for (int i = 0; i < array1.size(); i++) {
            list.add("Location : " + array1.get(i).a + "\n"+"Message : " + array1.get(i).s);
            Log.d("TAG", "onCreate: " + array1.get(i).a);
            address.add(array1.get(i).a);
        }

        setUpRecView();
    }

    private void setUpRecView() {
//        recyclerView = (RecyclerView) findViewById(R.id.recView);
        recyclerView = (RecyclerView) findViewById(R.id.recView);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StartActivity.this);
//        RecyclerView.LayoutManager grid = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(new MyAdapter());


    }


    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {

        @Override
        public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
//
//            View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.single_view,parent,false);
//            Holder mHolder= new Holder(view);
//
//            return mHolder;
//
            return new Holder(LayoutInflater.from(StartActivity.this).inflate(R.layout.single_view, parent, false));
        }

        @Override
        public void onBindViewHolder(Holder holder, final int position) {
            Log.d("TAG", "onBindViewHolder: "+list.get(position));
            holder.textView.setText(list.get(position));
            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dbhelp.delete(address.get(position));
                }
            });

        }

        @Override
        public int getItemCount() {
            return list.size();
        }

        public class Holder extends RecyclerView.ViewHolder {

            TextView textView;
            Button button;


            public Holder(View itemView) {
                super(itemView);

                textView = (TextView) itemView.findViewById(R.id.text);
                button = (Button) itemView.findViewById(R.id.del);

            }
        }

    }

}
