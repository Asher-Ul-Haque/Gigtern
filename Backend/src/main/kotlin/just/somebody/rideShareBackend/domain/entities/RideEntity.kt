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
import just.somebody.rideShareBackend.domain.enums.RideStatus
import just.somebody.rideShareBackend.domain.enums.UserRole
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

@Entity
@EntityListeners(AuditingEntityListener::class)
@Table(name = "rides")
data class RideEntity(
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(
		name      = "id",
		updatable = false,
		nullable  = false,
		unique    = true)
	val id: Long = 0,

	@Column(
		name      = "driver_id",
		updatable = false,
		nullable  = false,
		unique    = false)
	val driverId: Int = 0,

	@Column(
		name      = "origin_location",
		updatable = false,
		nullable  = false,
		unique    = false)
	val originLocation: String = "",

	@Column(
		name      = "departure_time",
		updatable = false,
		nullable  = false,
		unique    = false)
	val departureTime: LocalDateTime = LocalDateTime.now(),

	@Column(
		name      = "max_seats",
		updatable = false,
		nullable  = false,
		unique    = false)
	val maxSeats: Byte = 0,

	@Column(
		name      = "booked_seats",
		updatable = true,
		nullable  = false,
		unique    = false)
	var bookedSeats: Byte = 0,

	@Enumerated(EnumType.STRING)
	@Column(
		name      = "status",
		updatable = true,
		nullable  = false,
		unique    = false)
	var status: RideStatus = RideStatus.ACTIVE,

	// - - - One-to-Many relationship with Stops: Defines the full route
	@OneToMany(
		mappedBy      = "ride",
		cascade       = [CascadeType.ALL],
		orphanRemoval = true,
		fetch         = FetchType.LAZY)
	var stops: MutableList<StopEntity> = mutableListOf(),

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
{
	fun addStop(STOP: StopEntity)
	{
		stops.add(STOP)
		STOP.ride = this
	}
}