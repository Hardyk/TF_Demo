package com.app.tfdemo.objectBox;

import io.objectbox.annotation.Entity;
import io.objectbox.annotation.Id;

/**
 * Created by hardik on 20/2/19.
 */

@Entity
public class SelectedVideo {

    @Id
    long id;
    private String videoID;
    private int upVote;
    private int downVote;

    public SelectedVideo(String videoID, int upVote, int downVote) {
        this.videoID = videoID;
        this.upVote = upVote;
        this.downVote = downVote;
    }

    public SelectedVideo() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
    }

    public int getUpVote() {
        return upVote;
    }

    public void setUpVote(int upVote) {
        this.upVote = upVote;
    }

    public int getDownVote() {
        return downVote;
    }

    public void setDownVote(int downVote) {
        this.downVote = downVote;
    }
}
