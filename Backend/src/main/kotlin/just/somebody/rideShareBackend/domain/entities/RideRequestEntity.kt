package just.somebody.rideShareBackend.domain.entities

import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EntityListeners
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import just.somebody.rideShareBackend.domain.enums.PaymentStatus
import just.somebody.rideShareBackend.domain.enums.RequestStatus
import just.somebody.rideShareBackend.domain.enums.RideStatus
import just.somebody.rideShareBackend.domain.enums.UserRole
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "ride_requests")
data class RideRequestEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
		name      = "id",
		updatable = false,
		nullable  = false,
		unique    = true)
	val id: Long = 0,

	@Column(
		name      = "ride_id",
		updatable = false,
		nullable  = false,
		unique    = false)
	val rideId: Long = 0,

	@Column(
		name      = "passenger_id",
		updatable = false,
		nullable  = false,
		unique    = false)
	val passengerId: Int = 0,

	@Column(
		name      = "pickup_stop_id",
		updatable = false,
		nullable  = false,
		unique    = false)
	val pickupStopID: Long = 0,

	@Column(
		name      = "drop_off_stop_id",
		updatable = false,
		nullable  = false,
		unique    = false)
	val dropOffStopID: Long = 0,

	@Column(
		name      = "quoted_contribution",
		updatable = false,
		nullable  = false,
		unique    = false)
	val quotedContribution: Float = 0.0f,

	@Enumerated(EnumType.STRING)
	@Column(
		name      = "request_status",
		updatable = true,
		nullable  = false,
		unique    = false)
	var requestStatus: RequestStatus = RequestStatus.PENDING,

	@Enumerated(EnumType.STRING)
	@Column(
		name      = "payment_status",
		updatable = true,
		nullable  = false,
		unique    = false)
	var paymentStatus: PaymentStatus = PaymentStatus.PENDING_PAYMENT,

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