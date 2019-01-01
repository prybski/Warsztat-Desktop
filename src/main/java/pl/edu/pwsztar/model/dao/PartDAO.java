package pl.edu.pwsztar.model.dao;

import pl.edu.pwsztar.entity.Part;

import java.util.List;

public interface PartDAO {

    void add(Part part);
    List<Part> findAll();
}
