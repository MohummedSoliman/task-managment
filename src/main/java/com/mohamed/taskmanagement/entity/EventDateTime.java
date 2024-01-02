package com.mohamed.taskmanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "event_date")
@Getter
@Setter
public class EventDateTime {

    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private int id;

    @Column(name = "date")
    private LocalDate date;

    @NotEmpty(message = "Date Filed can not be empty or null")
    @Column(name = "date_time")
    private String dateTime;

    @Column(name = "time_zone")
    private String timeZone;
}
