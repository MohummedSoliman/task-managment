package com.mohamed.taskmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "event")
@Getter @Setter
public class Event {
    @Id
    private String id;

    @NotEmpty(message = "The Summary can not be empty or null")
    @Size(min = 3, max = 20, message = "The Summary field should have characters between 3 and 20")
    @Column(name = "summary")
    private String summary;

    @NotEmpty(message = "Description filed can not be empty or null")
    @Column(name = "description")
    @Size(min = 5 , max = 20, message = "The Description field should have characters between 3 and 20")
    private String description;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "start_id")
    private EventDateTime start;
    
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "end_id")
    private EventDateTime end;
}
