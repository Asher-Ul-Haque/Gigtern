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
@Table(name = "students")
data class StudentEntity(
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
		name      = "university",
		updatable = true,
		nullable  = true,
		unique    = false)
	var university: String? = null,

	// - - - Stores skills as a comma-separated string or array in PostgreSQL
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name        = "student_skills",
		joinColumns = [JoinColumn(name = "student_id")])
	@Column(name = "skill")
	val skills: List<String> = emptyList(),

	// - - - WARN: Not counting for MVP
	@Column(columnDefinition = "TEXT")
	val studentIdProofUrl: String? = null,

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