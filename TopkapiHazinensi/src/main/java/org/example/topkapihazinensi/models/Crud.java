package org.example.topkapihazinensi.models;

import java.util.List;
import java.util.Objects;

public interface Crud<model> {

    public model findById(int id);
    public List<model> getAllData();
    public void save(model model);
    public void update(model model);
    public void delete(int id);
}
// yapılacak tüm controller bunun ana yapı alsın diye