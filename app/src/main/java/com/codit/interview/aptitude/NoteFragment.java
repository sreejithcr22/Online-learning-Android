package com.codit.interview.aptitude;



import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 *
 */
public class NoteFragment extends DialogFragment {
    Toolbar notesTitleBar;
    EditText notesText;
    NoteInterface interfaceObj;
    SharedPreferences sharedPref;

    public interface NoteInterface
    {
        public void sendNote(String note);
    }


    public NoteFragment() {
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);


        if(sharedPref.getBoolean("note_save",true)) {
            String text = String.valueOf(notesText.getText());
            interfaceObj.sendNote(text);
        }



    }

    public void saveNotes()
    {

        String text=String.valueOf(notesText.getText());
        interfaceObj.sendNote(text);


    }


    @NonNull
    @Override

    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            interfaceObj=(NoteInterface) activity;
        }

        catch (ClassCastException e)
        {
            Toast.makeText(getContext(),"Sorry, Something went wrong !",Toast.LENGTH_SHORT).show();
        }
    }



    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //notes body
        View view = inflater.inflate(R.layout.note_layout, null);
        notesText=(EditText)view.findViewById(R.id.noteText);
        notesText.setText(this.getTag());

        //notes title view
        View title=inflater.inflate(R.layout.notes_title,null);

        //notes title toolbar
        notesTitleBar=(Toolbar)title.findViewById(R.id.notesTitle);
        notesTitleBar.setTitle("Notes");

        notesTitleBar.inflateMenu(R.menu.notes_title_menu);

        notesTitleBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.buttonNotesClear:
                        notesText.setText("");
                        break;

                    case R.id.buttonNotesSave:
                        saveNotes();
                       Toast.makeText(getContext(),"Notes Saved !",Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });

        notesTitleBar.setNavigationIcon(R.drawable.ic_notes_back);
        notesTitleBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        builder.setCustomTitle(title);

        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.getWindow().getAttributes().windowAnimations=R.style.CalcAnimation;

        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPref= PreferenceManager.getDefaultSharedPreferences(getContext());

    }
}
