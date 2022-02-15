package repositories;

import java.util.UUID;

public interface BaseRepository<T, R1, R2>{

    T get(UUID id);
    R1 add(T t);
    R2 getList();
    R2 getListById(UUID id);
    R1 editById(UUID id, T t);



}
