package com.cache;

import java.util.List;

public interface ITrainCaching {
    void clear() throws InterruptedException;

    void Save(List<SeatEntity> seatEntities) throws InterruptedException;
}
