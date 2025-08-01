package ticket_system.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import ticket_system.enums.TicketStatus;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
@Table(name = "tickets")
public class Ticket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status = TicketStatus.OPEN;

    private String resolution;

    private String clientEvidence;

    private String supportEvidence;

    @Column(name = "total_progress_minutes")
    private Long totalProgressMinutes = 0L;

    @Column(name = "current_progress_start")
    private LocalDateTime currentProgressStart;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "project_id", nullable = false)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "support_type_id", nullable = false)
    private SupportType supportType;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "client_id", nullable = false)
    private User client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "support_id")
    private User support;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TicketStatusHistory> statusHistory;

    @CreatedDate
    @Column(nullable = false)
    private LocalDate startDate;

    private LocalDate endDate;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Helper para obtener duración total actualizada
     */
    public Long getCurrentTotalProgressMinutes() {
        long baseMinutes = this.totalProgressMinutes != null ? this.totalProgressMinutes : 0L;

        // Si está en progreso, suma el tiempo actual
        if (this.status == TicketStatus.IN_PROGRESS && this.currentProgressStart != null) {
            long currentSessionMinutes = Duration.between(
                    this.currentProgressStart,
                    LocalDateTime.now()
            ).toMinutes();

            return baseMinutes + currentSessionMinutes;
        }

        return baseMinutes;
    }
}
