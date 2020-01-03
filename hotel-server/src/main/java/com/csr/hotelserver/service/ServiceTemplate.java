package com.csr.hotelserver.service;

import com.csr.hotelserver.util.exception.MyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.transaction.Transactional;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public interface ServiceTemplate<T,M,F extends JpaRepository<T,M> & JpaSpecificationExecutor<T>>{
    F getRepository();

    default List<T> findAll(){
        return this.getRepository().findAll();
    }

    default List<T> findAll(Map<String, Object> condition){
        return this.getRepository().findAll(buildJpaSpecification(condition));
    }

    default Page findByPage(Integer pageNo, Integer pageSize, Map<String, Object> condition){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return getRepository().findAll(buildJpaSpecification(condition), pageable);
    }

    Specification<T> buildJpaSpecification(Map<String, Object> conditions);

    @Transactional
    default void save(T t) throws MyException {
        getRepository().save(t);
    }

    @Transactional
    default void deleteById(M m){
        getRepository().deleteById(m);
    }

    @Transactional
    default void update(T t){
        getRepository().save(t);
    }

    default T getOne(M id){
        return this.getRepository().getOne(id);
    }
}
