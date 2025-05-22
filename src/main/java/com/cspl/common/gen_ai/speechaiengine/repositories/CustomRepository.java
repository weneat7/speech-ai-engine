package com.cspl.common.gen_ai.speechaiengine.repositories;


public interface CustomRepository<I,T> {

    void markEntityAsDeleted(I entityId, T entityName);

}
