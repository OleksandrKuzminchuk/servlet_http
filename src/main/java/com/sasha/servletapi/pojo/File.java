package com.sasha.servletapi.pojo;

import com.google.gson.annotations.Expose;

import javax.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "file")
public class File {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Expose
    @Column(name = "id")
    private Integer id;
    @Expose
    @Column(name = "name")
    private String name;
    @Expose
    @Column(name = "file_path")
    private String filePath;

    public File(){}

    public File(String name, String filePath) {
        this.name = name;
        this.filePath = filePath;
    }

    public File(Integer id, String name, String filePath) {
        this.id = id;
        this.name = name;
        this.filePath = filePath;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof File)) return false;
        File file = (File) o;
        return Objects.equals(getId(), file.getId()) && Objects.equals(getName(), file.getName()) && Objects.equals(getFilePath(), file.getFilePath());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getFilePath());
    }

    @Override
    public String toString() {
        return "File{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
