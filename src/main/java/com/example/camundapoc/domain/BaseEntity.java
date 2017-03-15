/**
 *
 */
package com.example.camundapoc.domain;

import com.example.camundapoc.config.database.MappingConfig;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.JsonProcessingException;

import javax.persistence.*;
import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.Date;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.springframework.data.jpa.domain.AbstractPersistable;

/**
 * Base class for all entities that have common mapping functions
 *
 * @author Spencer
 */
@MappedSuperclass
@JsonIgnoreProperties(ignoreUnknown = true)
public abstract class BaseEntity<ID> implements Serializable {

    private static final long serialVersionUID = 2535090450811888936L;

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;

    public String getId() {
        return id;
    }

    protected void setId(final String id) {
        this.id = id;
    }

    public boolean isNew() {
        return this.id == null;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }

        if (this == obj) {
            return true;
        }

        if (!getClass().equals(obj.getClass())) {
            return false;
        }

        AbstractPersistable rhs = (AbstractPersistable) obj;
        return this.id == null ? false : this.id.equals(rhs.getId());
    }


    @Override
    public int hashCode() {
        int hashCode = 17;
        hashCode += (this.id == null) ? 0 : this.id.hashCode() * 31;
        return hashCode;
    }

    @Column(name = "creation_time", nullable = false, updatable = false)
    @Type(type = "java.util.Date")
    @JsonIgnore
    protected Date creationTime;

    @Column(name = "modification_time", nullable = false)
    @Type(type = "java.util.Date")
    @JsonIgnore
    private Date modificationTime;

    @Column(name = "updated_by_user", nullable = true)
    @JsonIgnore
    private Long updatedByUserDbId;


    @PrePersist
    public void prePersist() {
        this.creationTime = new Date();
        this.modificationTime = new Date();
    }

    @PreUpdate
    public void preUpdate() {
        this.modificationTime = new Date();
    }

    @Override
    public String toString() {
        try {
            return toJson();
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error occurred rendering object as JSON: " + e.getMessage(), e);
        }
    }

    /**
     * Implementing classes should return a text string
     * that is a human readable way of identifying
     * the entity.
     *
     * @return
     */
    public abstract String getDisplayText();

    /**
     * Produces a JSON string
     *
     * @return
     * @throws JsonProcessingException
     */
    public String toJson() throws JsonProcessingException {
        return MappingConfig.getObjectMapper().writeValueAsString(this);
    }

    /**
     * Returns the corresponding getter method for a field from this object
     *
     * @param field
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    @JsonIgnore
    public Method getGetterMethodForField(Field field) throws NoSuchMethodException, SecurityException {
        String fieldName = field.getName();
        String getterMethodName = "get" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
        try
        {
            return this.getClass().getMethod(getterMethodName);
        }catch( NoSuchMethodException e ) {
            // If that fails try with "is" instead
            getterMethodName = "is" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
            return this.getClass().getMethod(getterMethodName);
        }
    }

    /**
     * Returns the corresponding getter method for a field from the specified object
     *
     * @param field
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    @JsonIgnore
    public static Method getGetterMethodForField(Field field, Object obj) throws NoSuchMethodException, SecurityException {
        String fieldName = field.getName();
        String getterMethodName = "get" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
        try
        {
            return obj.getClass().getMethod(getterMethodName);
        }catch( NoSuchMethodException e ) {
            // If that fails try with "is" instead
            getterMethodName = "is" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
            return obj.getClass().getMethod(getterMethodName);
        }
    }

    /**
     * Returns the corresponding getter method for a field from the specified class
     *
     * @param field
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    @JsonIgnore
    public static Method getGetterMethodForField(Field field, Class clazz) throws NoSuchMethodException, SecurityException {
        String fieldName = field.getName();
        String getterMethodName = "get" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
        try
        {
            return clazz.getMethod(getterMethodName);
        }catch( NoSuchMethodException e ) {
            // If that fails try with "is" instead
            getterMethodName = "is" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);
            return clazz.getMethod(getterMethodName);
        }
    }

    /**
     * Returns the corresponding the Class of the parameter for the setter method of this field
     *
     * @param field
     * @return
     * @throws NoSuchMethodException
     * @throws SecurityException
     */
    @JsonIgnore
    public Class<?> getSetterMethodParameterTypeForField(Field field) throws SecurityException, NoSuchMethodException {
        String fieldName = field.getName();
        String setterMethodName = "set" + fieldName.toUpperCase().charAt(0) + fieldName.substring(1);

        for (Method method : this.getClass().getMethods()) {
            if (method.getName().equals(setterMethodName)) {
                return method.getParameterTypes()[0];
            }
        }

        throw new NoSuchMethodException(setterMethodName);
    }

    public Class<? extends BaseEntity> getEntityType() {
        return this.getClass();
    }


    /**
     * Returns the name of the member field in the
     * entity that is annotated with a @Id
     *
     * @return
     */
    //@JsonIgnore
    public String getEntityIdFieldName() {
        for (Field field : this.getClass().getDeclaredFields()) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Id.class)) {
                    return field.getName();
                }
            }
        }
        return "";
    }

    /**
     * Returns the getter method that returns the entities
     * identifier value
     * @return
     * @throws NoSuchMethodException
     */
    @JsonIgnore
    public Method getEntityIdGetterMethod() throws NoSuchMethodException {
        for (Field field : this.getClass().getDeclaredFields()) {
            Annotation[] annotations = field.getDeclaredAnnotations();
            for (Annotation annotation : annotations) {
                if (annotation.annotationType().equals(Id.class)) {
                    return getGetterMethodForField(field);
                }
            }
        }
        return null;
    }

    public Object getEntityIdValue() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        return getEntityIdGetterMethod().invoke(this);
    }



    public Date getCreationTime() {
        return creationTime;
    }

    public Date getModificationTime() {
        return modificationTime;
    }

    public void setModificationTime(Date modificationTime) {
        this.modificationTime = modificationTime;
    }



}
