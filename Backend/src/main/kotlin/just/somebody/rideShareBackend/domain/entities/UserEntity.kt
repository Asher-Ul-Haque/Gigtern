package just.somebody.rideShareBackend.domain.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.Table
import just.somebody.rideShareBackend.domain.enums.UserGender
import just.somebody.rideShareBackend.domain.enums.UserRole
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "users")
data class UserEntity(
	@Id
	@Column(
		name      = "roll_no",
		updatable = false,
		nullable  = false,
		unique    = true)
	val rollNo: Int = 0,

	@Column(
		name      = "name",
		nullable  = false,
		updatable = true,
		unique    = false)
	val name: String = "",

	@Column(
		name      = "email",
		nullable  = false,
		updatable = false,
		unique    = true)
	val email: String = "",

	@Column(
		name      = "phone",
		nullable  = false,
		updatable = false,
		unique    = true)
	val phoneNumber: Long = 0L,

	@Column(
		name      = "gender",
		nullable  = false,
		updatable = false,
		unique    = false)
	val gender: UserGender = UserGender.MALE,

	@Enumerated(EnumType.STRING)
	@Column(
		name      = "role",
		nullable  = false,
		updatable = true,
		unique    = false)
	var role: UserRole = UserRole.PASSENGER,

	@Column(
		name      = "reputation",
		nullable  = false,
		updatable = true,
		unique    = false)
	var reputationScore: Float = 0.0f,

	@Column(
		name      = "cancellation_rate",
		nullable  = false,
		updatable = true,
		unique    = false)
	var cancellationRate: Float = 0.0f,

	@Column(
		name      = "on_time_rate",
		nullable  = false,
		updatable = true,
		unique    = false)
	var onTimeRate: Float = 0.0f,

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
