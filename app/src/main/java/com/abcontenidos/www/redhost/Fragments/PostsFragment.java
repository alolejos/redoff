package com.abcontenidos.www.redhost.Fragments;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.abcontenidos.www.redhost.Dbases.MyDbHelper;
import com.abcontenidos.www.redhost.MyRecyclerViewAdapter;
import com.abcontenidos.www.redhost.Objets.Post;
import com.abcontenidos.www.redhost.Dbases.PostDao;
import com.abcontenidos.www.redhost.Activities.PostInfo;
import com.abcontenidos.www.redhost.R;
import com.abcontenidos.www.redhost.Objets.User;
import com.abcontenidos.www.redhost.Dbases.UserDao;

import java.util.ArrayList;

public class PostsFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    MyRecyclerViewAdapter adapter;
    Intent i;
    User user;
    UserDao userDao;
    PostDao postDao;
    ArrayList<Post> list;
    RecyclerView recyclerView;
    private String mParam1;
    private String mParam2;

    public PostsFragment() {
        // Required empty public constructor
    }


    public static PostsFragment newInstance(String param1, String param2) {
        PostsFragment fragment = new PostsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_posts, container, false);

        // carga elementos visuales

        // lectura del usuario de la base de datos
        MyDbHelper helper = new MyDbHelper(getActivity(), "user");
        SQLiteDatabase db = helper.getWritableDatabase();
        userDao = new UserDao(db);
        user = userDao.get();

        // lectura de los posts
        MyDbHelper helperPosts = new MyDbHelper(getActivity(), "posts");
        SQLiteDatabase db1 = helperPosts.getWritableDatabase();
        postDao= new PostDao(db1);
        list = postDao.getall();

        // carga del Recyclerview
        recyclerView = view.findViewById(R.id.recycler_main);
        int numberOfColumns = 4;
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), numberOfColumns));
        adapter = new MyRecyclerViewAdapter(getActivity(), list);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onItemClick(View view, int position) {
        i = new Intent(getActivity(), PostInfo.class);
        i.putExtra("key", Integer.valueOf(list.get(position).getId()));
        Log.d("key", list.get(position).getId());
        startActivity(i);
    }

}



