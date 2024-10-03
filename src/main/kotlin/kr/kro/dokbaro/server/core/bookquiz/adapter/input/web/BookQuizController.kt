package kr.kro.dokbaro.server.core.bookquiz.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto.CreateBookQuizRequest
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.CreateBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateBookQuizCommand
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book-quizzes")
class BookQuizController(
	private val createBookQuizUseCase: CreateBookQuizUseCase,
) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun createQuiz(
		@RequestBody body: CreateBookQuizRequest,
		auth: Authentication,
	): IdResponse<Long> =
		IdResponse(
			createBookQuizUseCase.create(
				CreateBookQuizCommand(
					title = body.title,
					description = body.description,
					bookId = body.bookId,
					creatorAuthId = UUIDUtils.stringToUUID(auth.name),
					questions = body.questions,
				),
			),
		)
}