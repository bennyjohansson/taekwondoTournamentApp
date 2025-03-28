package com.taekwondo.tournament.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "clubs")
@Data
@NoArgsConstructor
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Club name is required")
    private String name;

    private String location;

    @OneToMany(mappedBy = "club", cascade = CascadeType.ALL)
    private List<Participant> participants = new ArrayList<>();
} 