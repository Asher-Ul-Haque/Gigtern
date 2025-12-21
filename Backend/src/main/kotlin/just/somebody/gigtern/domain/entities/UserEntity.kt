package just.somebody.gigtern.domain.entities

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import just.somebody.gigtern.domain.enums.Role
import just.somebody.gigtern.domain.enums.ValidationProvider
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "users")
data class UserEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
		name      = "id",
		updatable = false,
		nullable  = false,
		unique    = true)
	val id: Long = 0,

	@Column(
		name      = "name",
		updatable = true,
		nullable  = false,
		unique    = false)
	var name: String = "",

	@Column(
		name      = "email",
		updatable = true,
		nullable  = false,
		unique    = true)
	var email: String = "",

	@Column(
		name      = "onliner",
		updatable = true,
		nullable  = true,
		unique    = false)
	var oneLiner: String? = null,

	@Column(
		name      = "password_hash",
		updatable = true,
		nullable  = false,
		unique    = false)
	var passwordHash: String = "",

	@Enumerated(EnumType.STRING)
	@Column(
		name      = "role",
		updatable = false,
		nullable  = false,
		unique    = false)
	val role: Role = Role.STUDENT,

	@Enumerated(EnumType.STRING)
	@Column(
		name      = "validation_provider",
		updatable = true,
		nullable  = false,
		unique    = false)
	var validationProvider: ValidationProvider = ValidationProvider.LOCAL,

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
