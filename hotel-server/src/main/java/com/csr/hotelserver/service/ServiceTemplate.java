package com.csr.hotelserver.service;

import com.csr.hotelserver.util.exception.MyException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

public interface ServiceTemplate<T,M,F extends JpaRepository<T,M> & JpaSpecificationExecutor<T>>{
    F getRepository();

    default List<T> findAll(Map<String, Object> condition){
        return this.getRepository().findAll(buildJpaSpecification(condition));
    }

    default Page findByPage(Integer pageNo, Integer pageSize, Map<String, Object> condition){
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return getRepository().findAll(buildJpaSpecification(condition), pageable);
    }

    Specification<T> buildJpaSpecification(Map<String, Object> conditions);

    @Transactional(rollbackFor = Exception.class)
    default void save(T t) {
        getRepository().save(t);
    }

    @Transactional(rollbackFor = Exception.class)
    default void deleteById(M m){
        getRepository().deleteById(m);
    }

    @Transactional(rollbackFor = Exception.class)
    default void update(T t){
        getRepository().save(t);
    }

    default T getOne(M id){
        return this.getRepository().getOne(id);
    }
}
