package just.somebody.rideShareBackend.domain.entities

import com.fasterxml.jackson.annotation.JsonIgnore
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
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import just.somebody.rideShareBackend.domain.enums.RideStatus
import just.somebody.rideShareBackend.domain.enums.UserRole
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "stops")
data class StopEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
		name      = "roll_no",
		updatable = false,
		nullable  = false,
		unique    = true)
	val stopId: Long = 0,

	@Column(
		name      = "location",
		updatable = false,
		nullable  = false,
		unique    = false)
	val location: String = "",

	@Column(
		name      = "sequence",
		updatable = false,
		nullable  = false,
		unique    = false)
	val sequence: Byte = 0,

	@Column(
		name      = "estimated_arrival_time",
		updatable = true,
		nullable  = false,
		unique    = false)
	var estimatedArrivalTime: LocalDateTime = LocalDateTime.now(),

	// - - - Many-to-One relationship back to the parent Ride
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(
		name      = "ride_id",
		updatable = true,
		nullable  = false,
		unique    = false)
	@JsonIgnore
	var ride: RideEntity = RideEntity(),

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