package just.somebody.gigtern.domain.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import just.somebody.gigtern.domain.enums.ApplicationStatus
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.enums.VerificationStatus
import org.hibernate.annotations.IdGeneratorType
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "applications")
data class ApplicationEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
		name      = "id",
		updatable = false,
		nullable  = false,
		unique    = true)
	val id: Long = 0,

	// - - - Many-to-One relationship with Gig
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name      = "gig_id",
		updatable = false,
		nullable  = false,
		unique    = false)
	val gig: GigEntity,

	// - - - Many-to-One relationship with Student
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name      = "student_id",
		updatable = false,
		nullable  = false,
		unique    = false)
	val student: StudentEntity,

	@Column(
		name              = "cover_letter",
		updatable         = false,
		nullable          = true,
		unique            = false,
		columnDefinition  = "TEXT")
	val coverLetter: String? = null,

	@Enumerated(EnumType.STRING)
	@Column(
		name      = "status",
		updatable = true,
		nullable  = false,
		unique    = false)
	var status: ApplicationStatus = ApplicationStatus.APPLIED,

	@Column(
		name      = "applied_date",
		updatable = false,
		nullable  = false,
		unique    = false)
	val appliedDate: LocalDateTime = LocalDateTime.now(),

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
	var updatedAt: LocalDateTime = LocalDateTime.now())
