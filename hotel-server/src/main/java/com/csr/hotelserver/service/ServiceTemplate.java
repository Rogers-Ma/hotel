package com.csr.hotelserver.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface ServiceTemplate<T,M,F extends JpaRepository<T,M> & JpaSpecificationExecutor<T>>{
    F getRepository();

    default List<T> findAll(){
        return this.getRepository().findAll();
    }

    default Page findByPage(Integer pageNo, Integer pageSize, Map<String, Object> condition){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return getRepository().findAll(buildJpaSpecification(condition), pageable);
    }
    Specification<T> buildJpaSpecification(Map<String, Object> conditions);

    default void save(T t) throws Exception {
        getRepository().save(t);
    }
    default void deleteById(M m){
        getRepository().deleteById(m);
    }

    default void update(T t){
        getRepository().save(t);
    }

    default T getOne(M id){
        return this.getRepository().getOne(id);
    }
}
