package com.tahsinalsayeed.codeviewer;

import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by sayeed on 12/25/17.
 */

public class NavigationFragment extends DialogFragment implements AdapterView.OnItemClickListener {


    private Repository repository;
    private ListView navigationListView;
    private List<DirectoryEntryDto> entries;
    private ProgressBar progressBar;
    private String currentPath = "";
    private Context context;

    static NavigationFragment github(String username, String repositoryName){
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle(2);
        args.putString("username", username);
        args.putString("repositoryName", repositoryName);
        fragment.setArguments(args);
        return fragment;
    }

    static NavigationFragment local(String path){
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle(2);
        args.putBoolean("local", true);
        args.putString("path", path);
        fragment.setArguments(args);
        return fragment;
    }

    static NavigationFragment offline(){
        NavigationFragment fragment = new NavigationFragment();
        Bundle args = new Bundle(1);
        args.putBoolean("offline", true);
        fragment.setArguments(args);
        return fragment;
    }

    public NavigationFragment() {

    }

    public String getCurrentPath() {
        return currentPath;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.MyDialogFragmentStyle);
        if (savedInstanceState != null)
            currentPath = savedInstanceState.getString("path");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        getDialog().setTitle("Navigation");
        View rootView = inflater.inflate(R.layout.navigation_fragment, container, false);
        progressBar = rootView.findViewById(R.id.fetch_entry_progress);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        navigationListView = rootView.findViewById(R.id.repository_navigation_list);
        navigationListView.setOnItemClickListener(this);


        fetchDirectoryEntries();
        return rootView;
    }

    private void fetchDirectoryEntries() {
        Bundle args = getArguments();
        if (args.containsKey("local")){
            repository = Repositories.local(args.getString("path"), getActivity());
        } else if (args.containsKey("offline")){
            Log.d("OFFLINE", "found");
            repository = Repositories.offline(getActivity());
        }
        else {
            String username = args.getString("username");
            String repositoryName = args.getString("repositoryName");
            repository = Repositories.github(username, repositoryName);
        }
        FetchEntryTask fetcher = new FetchEntryTask();
        System.out.println(currentPath + "fetch");
        fetcher.execute(currentPath);
    }

    private void populateListView() {
        List<String> fileNames = new ArrayList<>(15);
        for (DirectoryEntryDto entry: entries)
            fileNames.add(entry.fileName);

        ArrayAdapter<DirectoryEntryDto> adapter = new DirectoryEntryAdapter(getActivity(), R.layout.navigation_item, entries);
        navigationListView.setAdapter(adapter);

    }

    private void displayCode(Code code){
        ((CodeDisplayer)getActivity()).displayCode(code);
        dismiss();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("path", currentPath);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        DirectoryEntryDto entry = (DirectoryEntryDto)navigationListView.getItemAtPosition(position);

        if (entry.isDir()){
            navigationListView.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            new FetchEntryTask().execute(entry.relativePath);
            currentPath = entry.relativePath;
        } else {
            new FetchContentTask().execute(entry.relativePath);
        }
    }

    public void setCurrentPath(String currentPath) {
        this.currentPath = currentPath;
    }


    private class DirectoryEntryAdapter extends ArrayAdapter<DirectoryEntryDto>{
        private final List<DirectoryEntryDto> entries;
        @NonNull
        private final Context context;
        private int layoutResourceId;


        public DirectoryEntryAdapter(@NonNull Context context, int resource, @NonNull DirectoryEntryDto[] objects) {
            super(context, resource, objects);
            this.context = context;
            layoutResourceId = resource;
            entries = Arrays.asList(objects);
        }

        public DirectoryEntryAdapter(@NonNull Context context, int resource, @NonNull List<DirectoryEntryDto> objects) {

            super(context, resource, objects);
            this.context = context;
            layoutResourceId = resource;
            entries = objects;
        }

        @Nullable
        @Override
        public DirectoryEntryDto getItem(int position) {
            return entries.get(position);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = LayoutInflater.from(context);
                convertView = inflater.inflate(layoutResourceId, null);
            }
            DirectoryEntryDto entry = entries.get(position);
            if (entry != null) {
                ImageView imageView = (ImageView)convertView.findViewById(R.id.item_image);
                TextView textView = (TextView) convertView.findViewById(R.id.navigation_item);
                imageView.setImageResource(entry.isDir()? R.drawable.ic_folder_black_24dp: R.drawable.ic_code_black_24dp);
                textView.setText(entry.fileName);
            }

            return convertView;
        }
    }



    private class FetchEntryTask extends AsyncTask<String, Integer, List<DirectoryEntryDto>>{

        @Override
        protected List<DirectoryEntryDto> doInBackground(String... strings) {
            Log.d("OFFLINE", repository.toString());
            return repository.getDirectoryEntry(strings[0]);
        }

        @Override
        protected void onPostExecute(List<DirectoryEntryDto> directoryEntryDtos) {
            super.onPostExecute(directoryEntryDtos);
            entries = directoryEntryDtos;
            Log.d("OFFLINE", entries.size() + "");
            progressBar.setVisibility(View.GONE);
            navigationListView.setVisibility(View.VISIBLE);
            populateListView();
        }
    }

    private class FetchContentTask extends AsyncTask<String, Integer, Code>{

        @Override
        protected Code doInBackground(String... strings) {
            return repository.getCode(strings[0]);
        }

        @Override
        protected void onPostExecute(Code code) {
            super.onPostExecute(code);
            displayCode(code);
        }
    }
}
