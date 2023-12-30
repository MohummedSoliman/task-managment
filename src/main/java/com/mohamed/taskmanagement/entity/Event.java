package com.mohamed.taskmanagement.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "event")
@Getter @Setter
public class Event {
    @Id
    private String id;

    @Column(name = "summary")
    private String summary;

    @Column(name = "description")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "start_id")
    private EventDateTime start;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "end_id")
    private EventDateTime end;
}
