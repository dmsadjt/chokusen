package com.dmsadjt.chokusen.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "channel_read_states",
    uniqueConstraints = @UniqueConstraint(columnNames = { "user_id", "channel_id" })
)
@Getter
@Setter
@NoArgsConstructor
public class ChannelReadState {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID readStateId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "channel_id", nullable = false)
    private Channel channel;

    private LocalDateTime lastReadAt;
}
