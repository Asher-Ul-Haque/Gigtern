package just.somebody.gigtern.domain.entities

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID


@EntityListeners(AuditingEntityListener::class)
@Entity
@Table(name = "employers")
data class EmployerEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
		name      = "id",
		updatable = false,
		nullable  = false,
		unique    = true)
	val id: Long = 0,

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name                  = "user_id",
		referencedColumnName  = "id",
		nullable              = false,
		unique                = true)
	val user: UserEntity = UserEntity(),

	@Column(
		name      = "poc",
		updatable = true,
		nullable  = false,
		unique    = true)
	var pointOfContact: String = "",

	// - - - WARN: We keep the column but won't use it in MVP logic
	@Column(columnDefinition = "TEXT")
	val aadharKycUrl: String? = null,

	@CreatedDate
	@Column(
		name      = "created_at",
		nullable  = false,
		updatable = false,
		unique    = false)
	val createdAt: LocalDateTime = LocalDateTime.now(),

	@LastModifiedDate
	@Column(
		name      = "updated_at",
		nullable  = false,
		updatable = true,
		unique    = false)
	var updatedAt: LocalDateTime = LocalDateTime.now()
)
