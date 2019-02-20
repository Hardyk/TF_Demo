package com.app.tfdemo.ui.component.videoPlay;

import android.app.Activity;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;


import com.app.tfdemo.GiphyDemo;
import com.app.tfdemo.objectBox.SelectedVideo;
import com.app.tfdemo.objectBox.SelectedVideo_;

import java.util.List;

import io.objectbox.Box;
import io.objectbox.BoxStore;
import io.objectbox.query.Query;

import static com.app.tfdemo.GiphyDemo.getApplication;


/**
 * Created by hardik on 20/2/19.
 */

public class VideoPlayPresenter  {

    public static final int UPDATE_UP_VOTE = 1;
    public static final int UPDATE_DOWN_VOTE = 2;

    private SelectedVideo selection;

    private Box<SelectedVideo> selectionBox;
    private Query<SelectedVideo> selectionQuery;
    private Activity activity;
    private BoxStore boxStore;

    public VideoPlayPresenter(Activity activity) {
        this.activity = activity;
        boxStore = ((GiphyDemo) getApplication()).getBoxStore();
        selectionBox = boxStore.boxFor(SelectedVideo.class);

        selectionQuery = selectionBox.query().order(SelectedVideo_.videoID).build();
    }

    public SelectedVideo checkVideVotes(String videoID) {
        List<SelectedVideo> selection = selectionBox.find(SelectedVideo_.videoID, videoID);
        if (selection != null && selection.size() > 0) {
            return selection.get(0);
        } else {
            return null;
        }
    }

    public SelectedVideo updateVideoVote(String videoID, int type) {
        selection = checkVideVotes(videoID);
        if (selection != null) {
            if (type == UPDATE_UP_VOTE) {
                selection.setUpVote(selection.getUpVote() + 1);
            } else {
                selection.setDownVote(selection.getDownVote() + 1);
            }
            selectionBox.put(selection);
            return selection;
        } else {
            selection = new SelectedVideo(videoID, 0, 0);
            if (type == UPDATE_UP_VOTE) {
                selection.setUpVote(1);
            } else {
                selection.setDownVote(1);
            }
            selectionBox.put(selection);
        }
        return selection;
    }

    public void shoeAlertDialog(String message) {
        new AlertDialog.Builder(activity)
                .setTitle("Playback Error")
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        activity.finish();
                    }
                })
                .show();
    }

    public void data(String videoID){}

}
