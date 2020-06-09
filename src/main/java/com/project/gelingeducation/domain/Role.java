package com.project.gelingeducation.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "role")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id")
public class Role implements Serializable {

    private static final long serialVersionUID = 9205096691157148000L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", length = 24, nullable = false)
    private String name;

    //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "create_time")
    @CreationTimestamp
    private Date createDate;

    @Column(name = "remark")
    private String remark;
    //0为不是默认注册时的身份,1为默认
    @Column(name = "is_default")
    private Integer isDefault = 0;

    @OneToMany(mappedBy = "role")
    private Set<User> users = new HashSet<>();


    @ManyToMany(targetEntity = Permission.class
            ,cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "t_role_permission",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions = new HashSet<>();

}
