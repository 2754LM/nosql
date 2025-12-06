package com.cczu.nosql.entity;

import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = false)
@Entity
@Data
@Table(name = "md_user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseModel {
  @Id private Long id;

  private String name;

  private String password;

  @OneToMany(mappedBy = "fromUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserFollow> userFollows;

  @OneToMany(mappedBy = "toUser", cascade = CascadeType.ALL, orphanRemoval = true)
  private List<UserFollow> followers;

  @OneToMany(mappedBy = "author")
  private List<Article> articles;

  public User(String name, String password) {
    this.name = name;
    this.password = password;
  }
}
