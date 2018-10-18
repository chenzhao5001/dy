package com.guidesound.Service;

import com.guidesound.models.Video;


import java.util.List;

public interface IVideoService {

    public void addVideo(Video video);
    public List<Video> getVideoList(int userId);
    public Video getVideo(int id);
    public void setVideoStatus(int id,int user_id);
    public void deleteVideo(int id,int userId);
}
