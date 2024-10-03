package kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto

import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateQuizQuestionCommand

data class CreateBookQuizRequest(
	val title: String,
	val description: String,
	val bookId: Long,
	val questions: Collection<CreateQuizQuestionCommand>,
)