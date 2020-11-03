package study.querydsl.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;

/*
    순수 JPA 에서 Auditing 을 하는 방법
 */
@MappedSuperclass
@Getter
public class JpaBaseEntity {

    @Column(updatable = false)
    private LocalDateTime createDate;
    private LocalDateTime updatedDate;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        this.createDate = now;
        this.updatedDate = now;
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedDate = LocalDateTime.now();
    }

}
