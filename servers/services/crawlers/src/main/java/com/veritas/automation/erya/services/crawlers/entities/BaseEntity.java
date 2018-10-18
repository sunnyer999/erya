package com.veritas.automation.erya.services.crawlers.entities;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;

/**
 * Created by Dean.Zou on 2/7/2017.
 */
public abstract class BaseEntity {


    @Id
    private String id;


    private LocalDateTime createTime;

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
