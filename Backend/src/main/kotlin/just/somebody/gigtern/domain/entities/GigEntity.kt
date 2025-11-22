package just.somebody.gigtern.domain.entities

import jakarta.persistence.CollectionTable
import jakarta.persistence.Column
import jakarta.persistence.ElementCollection
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import just.somebody.gigtern.domain.enums.ApplicationStatus
import just.somebody.gigtern.domain.enums.GigStatus
import just.somebody.gigtern.domain.enums.PaymentType
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.enums.VerificationStatus
import org.hibernate.annotations.UuidGenerator
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime
import java.util.UUID

/*
@EntityListeners(AuditingEntityListener::class)
@Entity
@Table(name = "gigs")
data class GigEntity(
	@Id
	@UuidGenerator
	@Column(
		name      = "id",
		updatable = false,
		nullable  = false,
		unique    = true)
	val id: UUID = UUID.randomUUID(),

	// - - - Many-to-One relationship with Employer
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name      = "employer_id",
		updatable = false,
		nullable  = false,
		unique    = true)
	val employer: EmployerEntity = EmployerEntity(),

	@Column(
		name      = "title",
		updatable = false,
		nullable  = false,
		length    = 100)
	val title: String = "",

	@Column(
		name              = "description",
		updatable         = false,
		nullable          = false,
		columnDefinition  = "TEXT")
	val description: String = "",

	@Enumerated(EnumType.STRING)
	@Column(
		name      = "payment_type",
		updatable = false,
		nullable  = false)
	val paymentType: PaymentType = PaymentType.FIXED,

	@Column(
		name      = "pay_rate",
		updatable = false,
		nullable  = false)
	val payRate: Float = 0.0f,

	@Column(
		name      = "estimated_duration_hours",
		updatable = false,
		nullable  = true)
	val durationEstimateHours: Int? = null,

	@Enumerated(EnumType.STRING)
	@Column(
		name      = "status",
		updatable = true,
		nullable  = false)
	var status: GigStatus = GigStatus.OPEN,

	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(
		name        = "gig_skills_required",
		joinColumns = [JoinColumn(name = "gig_id")])
	@Column(
		name      = "skill",
		updatable = true,
		nullable  = false)
	var skillsRequired: List<String> = emptyList(),

	@Column(
		name      = "posted_date",
		nullable  = false,
		updatable = false)
	val postedDate: LocalDateTime = LocalDateTime.now(),

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

 */