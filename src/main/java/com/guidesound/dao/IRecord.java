package com.guidesound.dao;

import com.guidesound.dto.RecordDTO;

public interface IRecord {

    void add(RecordDTO recordDTO);
    void update(RecordDTO recordDTO);
}
