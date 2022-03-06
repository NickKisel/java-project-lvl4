package hexlet.code.domain;

import io.ebean.annotation.WhenCreated;
import io.ebean.annotation.WhenModified;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import java.time.Instant;

@Entity
public final class Article {

    @Id
    private long id;

    private String name;

    @Lob
    private String description;

    @WhenCreated
    private Instant created_at;

    @WhenModified
    private  Instant updated_at;

    public Article() {
    }

    public Article(String name, String description) {
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

    public Instant getCreated_at() {
        return created_at;
    }

    public Instant getUpdated_at() {
        return updated_at;
    }
}
