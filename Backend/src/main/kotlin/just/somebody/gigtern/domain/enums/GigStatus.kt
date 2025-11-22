package just.somebody.gigtern.domain.enums

enum class GigStatus
{
	OPEN,           // - - - Posted and available for applications
	MATCHED,        // - - - Candidate accepted, ready to start
	IN_PROGRESS,    // - - - Work is ongoing
	COMPLETED,      // - - - Work finished
	CLOSED          // - - - Finalized
}