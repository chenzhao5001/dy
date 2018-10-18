package com.guidesound.Service.impl;

import com.guidesound.Service.IVideoService;
import com.guidesound.dao.IVideo;
import com.guidesound.models.Video;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VideoServiceImpl implements IVideoService{

    @Autowired
    private IVideo iVideo;

    @Override
    public void addVideo(Video video) {
        iVideo.addVideo(video);
    }

    @Override
    public List<Video> getVideoList(int userId) {
        return iVideo.getVideoList(userId);
    }

    @Override
    public Video getVideo(int id) {
        return iVideo.getVideo(id);
    }

    @Override
    public void setVideoStatus(int id,int status) {
        iVideo.setVideoStatus(id,status);
    }

    @Override
    public void deleteVideo(int id, int userId) {
        iVideo.deleteVideo(id,userId);
    }
}
