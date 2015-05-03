package com.locol.locol.fragments;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.locol.locol.R;
import com.locol.locol.adapters.CommentAdapter;
import com.locol.locol.pojo.Account;
import com.parse.FindCallback;
import com.parse.FunctionCallback;
import com.parse.ParseCloud;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class CommentFragment extends Fragment {

    private ListView listView;

    public CommentAdapter getAdapter() {
        return adapter;
    }

    private CommentAdapter adapter;

    private ProgressBar progressBar;

    public void setEvent(ParseObject event) {
        this.event = event;
    }

    public ParseObject getEvent() {
        return event;
    }

    private ParseObject event = new ParseObject("Event");

    public CommentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_comment, container, false);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        progressBar = (ProgressBar) view.findViewById(R.id.progress_bar);

        ParseQueryAdapter.QueryFactory<ParseObject> parseQuery = new ParseQueryAdapter.QueryFactory<ParseObject>() {
            public ParseQuery create() {
                ParseQuery query = new ParseQuery("Comment");
                query.whereEqualTo("event", getEvent());
                query.orderByAscending("createdAt");
                return query;
            }
        };
        adapter = new CommentAdapter(getActivity(), parseQuery);
        listView = (ListView) view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
//        adapter.loadObjects();
        adapter.addOnQueryLoadListener(new ParseQueryAdapter.OnQueryLoadListener<ParseObject>() {
            @Override
            public void onLoading() {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoaded(List<ParseObject> list, Exception e) {
                progressBar.setVisibility(View.GONE);
            }
        });

        final EditText commentText = (EditText) view.findViewById(R.id.comment_text);

        ImageButton btnSend = (ImageButton) view.findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (commentText.getText().toString().isEmpty()) {
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                    alertDialogBuilder
                            .setMessage("Please enter your comment!")
                            .setCancelable(true)
                            .setPositiveButton("OK", null);
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                } else {
                    final ParseUser user = ParseUser.getCurrentUser();
                    ParseObject comment = new ParseObject("Comment");
                    comment.put("comment", commentText.getText().toString());
                    comment.put("author_name", Account.getUserName());
                    comment.put("author", user);
                    comment.put("author_avatar_url", user.getString("avatar_url"));
                    comment.put("event", getEvent());
                    comment.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            Toast.makeText(getActivity(), "Save your comment successfully!", Toast.LENGTH_SHORT).show();
                            commentText.getText().clear();

                            adapter.loadObjects();

                            hideKeyboard();

                            // Assume ParseObject myPost was previously created.
                            ParseQuery<ParseObject> query = ParseQuery.getQuery("Comment");
                            query.whereEqualTo("event", event);

                            query.findInBackground(new FindCallback<ParseObject>() {
                                public void done(List<ParseObject> objects, ParseException e) {
                                    HashSet<String> authorList = new HashSet<String>();
                                    for (ParseObject object: objects) {
                                        String authorId = object.getParseObject("author").getObjectId();
//                                        if (!authorId.equals(user.getObjectId())) {
                                            authorList.add(authorId);
//                                        }
                                    }

                                    Log.wtf("authorList", authorList.toString());

                                    for (String author: authorList) {
                                        HashMap<String, Object> params = new HashMap<>();
                                        params.put("authorId", author);

                                        params.put("message", Account.getUserName() + " commented on an event that you are following.");
                                        params.put("title", event.getString("title"));
                                        params.put("event_id", event.getObjectId());

                                        ParseCloud.callFunctionInBackground("pushWhenComment", params, new FunctionCallback<String>() {
                                            @Override
                                            public void done(String s, ParseException e) {
                                                Toast.makeText(getActivity(), s + event.getString("title") + event.getObjectId(), Toast.LENGTH_SHORT).show();
                                                Log.wtf("pushWhencomment", s + " " +  event.getString("title") + " " + event.getObjectId());
                                            }
                                        });
                                    }

                                }
                            });
                        }
                    });
                }
            }
        });

        return view;
    }

    private void hideKeyboard() {
        // Check if no view has focus:
        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
