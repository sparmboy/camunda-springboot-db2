package com.example.camundapoc.domain;

import javax.persistence.*;
import java.util.UUID;

/**
 * Created by spencer on 14/03/2017.
 */
@Entity
public class ResultEntity extends BaseEntity<UUID> {

    private String result;

    public ResultEntity(){}

    public ResultEntity(String result) {
        this.result = result;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ResultEntity) {
            ResultEntity otherResult = (ResultEntity) other;
            return (otherResult.getResult() == null ? this.getResult() == null : otherResult.getResult().equals(this.getResult()))
                    ;
        }
        return false;
    }

    @Override
    public String getDisplayText() {
        return "";
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
