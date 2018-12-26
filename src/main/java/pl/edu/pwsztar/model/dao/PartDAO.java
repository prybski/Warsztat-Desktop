package pl.edu.pwsztar.model.dao;

import pl.edu.pwsztar.entity.Part;

import java.util.List;

public interface PartDAO {

    List<Part> findAll();
}
