package com.cspl.common.gen_ai.speechaiengine.models.entities;

import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public abstract class AbstractBaseAuditableEntity<PK extends Serializable> implements Serializable, Persistable<PK> {
    private static final long serialVersionUID = 6219787514778472672L;

    @Id
    private PK id;

    @CreatedDate
    @Field(targetType = FieldType.DATE_TIME)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Field(targetType = FieldType.DATE_TIME)
    private LocalDateTime updatedAt;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String updatedBy;

    private boolean deleted = false;

    @Version
    private Long version;

    @Transient
    public boolean isNew() {
        return null == this.getId();
    }

    @Override
    public PK getId() { return this.id; }
}