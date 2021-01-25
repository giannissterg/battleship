package gr.ste.domain.base;

public interface Mapper<Domain, Data> {
    Domain toDomain(Data data);
    Data toData(Domain domain);
}
