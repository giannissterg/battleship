package gr.ste.domain.base;

import java.util.ArrayList;
import java.util.List;

public class ListMapper<Domain, Data> implements Mapper<List<Domain>, List<Data>> {
    private final Mapper<Domain, Data> mapper;

    public ListMapper(Mapper<Domain, Data> mapper) {
        this.mapper = mapper;
    }

    @Override
    public List<Domain> toDomain(List<Data> data) {
        final List<Domain> domainResult = new ArrayList<>();
        for(Data datum : data) {
            domainResult.add(mapper.toDomain(datum));
        }
        return domainResult;
    }

    @Override
    public List<Data> toData(List<Domain> entities) {
        final List<Data> dataResult = new ArrayList<>();
        for(Domain entity : entities) {
            dataResult.add(mapper.toData(entity));
        }
        return dataResult;
    }
}
