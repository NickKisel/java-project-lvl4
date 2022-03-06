package hexlet.code.domain;

import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.Instant;

@Entity
public final class Url {

    @Id
    private long id;

    private String name;

    @Lob
    private String description;

    @WhenCreated
    private Instant createdAt;

    @WhenModified
    private  Instant updatedAt;

    public Url() {
    }

    public Url(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }
}
