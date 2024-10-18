package kr.kro.dokbaro.server.core.bookquiz.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto.CreateBookQuizRequest
import kr.kro.dokbaro.server.core.bookquiz.adapter.input.web.dto.UpdateBookQuizRequest
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.CreateBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizAnswerUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.FindBookQuizQuestionUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.UpdateBookQuizUseCase
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.CreateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.application.port.input.dto.UpdateBookQuizCommand
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizAnswer
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/book-quizzes")
class BookQuizController(
	private val createBookQuizUseCase: CreateBookQuizUseCase,
	private val findBookQuizQuestionUseCase: FindBookQuizQuestionUseCase,
	private val updateBookQuizUseCase: UpdateBookQuizUseCase,
	private val findBookQuizAnswerUseCase: FindBookQuizAnswerUseCase,
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
					timeLimitSecond = body.timeLimitSecond,
					viewScope = body.viewScope,
					editScope = body.editScope,
				),
			),
		)

	@GetMapping("/{id}/questions")
	fun getBookQuizQuestions(
		@PathVariable id: Long,
	): BookQuizQuestions = findBookQuizQuestionUseCase.findBookQuizQuestionsBy(id)

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun updateQuiz(
		@PathVariable id: Long,
		@RequestBody body: UpdateBookQuizRequest,
	) {
		updateBookQuizUseCase.update(
			UpdateBookQuizCommand(
				id = id,
				title = body.title,
				description = body.description,
				bookId = body.bookId,
				timeLimitSecond = body.timeLimitSecond,
				viewScope = body.viewScope,
				editScope = body.editScope,
				studyGroupId = body.studyGroupId,
				questions = body.questions,
			),
		)
	}

	@GetMapping("/answer")
	fun getQuestionAnswer(
		@RequestParam questionId: Long,
	): BookQuizAnswer = findBookQuizAnswerUseCase.findBookQuizAnswer(questionId)
}