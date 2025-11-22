package just.somebody.rideShareBackend.domain.enums

enum class PaymentStatus
{
	PENDING_PAYMENT,
	ESCROW,
	DRIVER_PAID,
	PLATFORM_COMMISSIONED,
	REFUNDED
}