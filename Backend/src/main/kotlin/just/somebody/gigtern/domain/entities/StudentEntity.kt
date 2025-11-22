package just.somebody.gigtern.domain.entities

import jakarta.persistence.*
import org.hibernate.annotations.UuidGenerator
import java.util.UUID

@Entity
@Table(name = "students")
data class StudentEntity(
	@Id
	@UuidGenerator
	@Column(
		name      = "id",
		updatable = false,
		nullable  = false,
		unique    = true)
	val id: UUID = UUID.randomUUID(),

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
	val studentIdProofUrl: String? = null)