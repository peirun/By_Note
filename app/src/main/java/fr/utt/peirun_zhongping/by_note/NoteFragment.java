package fr.utt.peirun_zhongping.by_note;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NoteFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NoteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoteFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private ListView lv;
    private MyAdapter adapter;
    private NoteDB noteDB;
    private SQLiteDatabase dbReader;
    private Cursor cursor;
    private View view;

    // TODO: Rename and change types of parameters
    private String mParam1;

    private OnFragmentInteractionListener mListener;

    public NoteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoteFragment newInstance(String param1, String param2) {
        NoteFragment fragment = new NoteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString("params");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_note, container, false);
        initView();
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    // ini view
    public void initView() {
        lv = (ListView) view.findViewById(R.id.list);
        noteDB = new NoteDB(getActivity());
        dbReader = noteDB.getReadableDatabase();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                cursor.moveToPosition(position);
                Intent i = new Intent(getActivity(), ModifyNote.class);
                i.putExtra(NoteDB.NOTE_ID,
                        cursor.getInt(cursor.getColumnIndex(NoteDB.NOTE_ID)));
                i.putExtra(NoteDB.STARRED,
                        cursor.getString(cursor.getColumnIndex(NoteDB.STARRED)));
                i.putExtra(NoteDB.FOLDER_NAME,
                        cursor.getString(cursor.getColumnIndex(NoteDB.FOLDER_NAME)));
                startActivity(i);
            }
        });
    }

    // choose data by different data
    public void selectDB() {
        if(mParam1 == "Note" || mParam1 == null){
            cursor = dbReader.query(NoteDB.TABLE_NAME, null, null, null, null,
                    null, null);
        }else if(mParam1 == "Starred"){
            cursor = dbReader.query(NoteDB.TABLE_NAME, null, NoteDB.STARRED + "=?", new String[] { "true" }, null,
                    null, null);
        }else{
            cursor = dbReader.query(NoteDB.TABLE_NAME, null, NoteDB.FOLDER_NAME + "=?", new String[] { mParam1 }, null,
                    null, null);
        }
        adapter = new MyAdapter(getActivity(), cursor);
        lv.setAdapter(adapter);
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        selectDB();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
