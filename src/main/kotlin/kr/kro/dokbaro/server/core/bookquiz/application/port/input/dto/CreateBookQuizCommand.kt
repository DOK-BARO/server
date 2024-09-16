package kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto

import java.util.UUID

data class CreateBookQuizCommand(
	val title: String,
	val description: String,
	val bookId: Long,
	val creatorAuthId: UUID,
)