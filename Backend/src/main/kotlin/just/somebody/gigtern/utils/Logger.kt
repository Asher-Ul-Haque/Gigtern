package just.somebody.gigtern.utils

import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


object Logger
{
	// - - - Configuration
	private const val ENABLE_FILE_LOG    : Boolean = true
	private const val ENABLE_TIME_STAMPS : Boolean = true
	private const val ENABLE_TRACE_LOGS  : Boolean = false
	private const val ENABLE_DEBUG_LOGS  : Boolean = true
	private const val LOG_DIRECTORY      : String  = "logs"
	private const val LATEST_FILE_NAME   : String  = "latest.log"

	// - - - ANSI color codes
	private const val ANSI_RESET      = "\u001B[0m"
	private const val ANSI_RED_BG     = "\u001B[41m"
	private const val ANSI_RED        = "\u001B[31m"
	private const val ANSI_PASTEL_RED = "\u001B[38;5;216m"
	private const val ANSI_BLUE       = "\u001B[34m"
	private const val ANSI_GREEN      = "\u001B[32m"
	private const val ANSI_PURPLE     = "\u001B[35m"

	// - - - Date and time formatting
	private val dateFormatter     = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
	private val fileNameFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm")

	// - - - Internal State and Initialization ---
	private val logDir          : File = File(LOG_DIRECTORY)
	private val sessionLogFile  : File?
	private val startTime       : String = LocalDateTime.now().format(fileNameFormatter)

	// - - - Initialization block runs when the Logger object is first accessed
	init
	{
		if (!logDir.exists()) logDir.mkdirs()

		sessionLogFile = if (ENABLE_FILE_LOG)
		{
			val sessionFile = File(logDir, "$startTime.log")
			val latestFile  = File(logDir, LATEST_FILE_NAME)

			try
			{
				if (latestFile.exists()) latestFile.delete()
				Files.copy(sessionFile.toPath(), latestFile.toPath(), StandardCopyOption.REPLACE_EXISTING)
			}
			catch (_: IOException) { /* Ignore file copy error */ }

			try
			{
				FileWriter(sessionFile, true).use()
				{ file ->
					file.appendLine(" - - - Log Session Started - - -")
					file.appendLine("Session Time: $startTime")
					file.appendLine()
				}
				sessionFile
			}
			catch (e: IOException)
			{
				println("[LOGGER ERROR]: Failed to initialize log file: ${e.message}")
				null
			}
		} else null

		LOG_INFO("Logger initialized successfully.")
	}

	// - - - Logging Methods - - -

	fun LOG_FATAL   (vararg ARGS: Any) = logWithLevel("FATAL", ANSI_RED_BG, *ARGS)
	fun LOG_ERROR   (vararg ARGS: Any) = logWithLevel("ERROR", ANSI_RED, *ARGS)
	fun LOG_WARNING (vararg ARGS: Any) = logWithLevel("WARNING", ANSI_PASTEL_RED, *ARGS)

	fun LOG_DEBUG(vararg ARGS: Any)
	{
		if (ENABLE_DEBUG_LOGS) logWithLevel("DEBUG", ANSI_BLUE, *ARGS)
	}

	fun LOG_TRACE(vararg ARGS: Any)
	{
		if (ENABLE_TRACE_LOGS) logWithLevel("TRACE", ANSI_PURPLE, *ARGS)
	}

	fun LOG_INFO(vararg ARGS: Any) = logWithLevel("INFO", ANSI_GREEN, *ARGS)


	// - - - Core message formatter - - -

	private fun logWithLevel(LEVEL: String, COLOR: String, vararg ARGS: Any)
	{
		val msg = constructMessage(LEVEL, *ARGS)
		println(COLOR + msg + ANSI_RESET)
		writeToFile(msg)
	}

	private fun constructMessage(LEVEL: String, vararg ARGS: Any): String
	{
		val baseMessage = ARGS.joinToString("") { it.toString() }
		return if (ENABLE_TIME_STAMPS)
			"${LocalDateTime.now().format(dateFormatter)} [$LEVEL]: $baseMessage"
		else
			"[$LEVEL]:\t$baseMessage"
	}

	private fun writeToFile(MESSAGE: String)
	{
		if (!ENABLE_FILE_LOG || sessionLogFile == null) return
		try
		{
			FileWriter(sessionLogFile, true).use { file -> file.appendLine(MESSAGE) }
			val latestPath: Path = File(logDir, LATEST_FILE_NAME).toPath()
			Files.copy(sessionLogFile.toPath(), latestPath, StandardCopyOption.REPLACE_EXISTING)
		}
		catch (e: IOException)
		{ println(ANSI_RED + "[LOGGER ERROR]: Failed to write to log file: ${e.message}" + ANSI_RESET) }
	}
}
