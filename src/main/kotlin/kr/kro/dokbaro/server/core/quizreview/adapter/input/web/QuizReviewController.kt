package kr.kro.dokbaro.server.core.quizreview.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.quizreview.adapter.input.web.dto.CreateQuizReviewRequest
import kr.kro.dokbaro.server.core.quizreview.application.port.input.CreateQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.CreateQuizReviewCommand
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/quiz-reviews")
class QuizReviewController(
	private val createQuizReviewUseCase: CreateQuizReviewUseCase,
) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun createQuizReview(
		@RequestBody body: CreateQuizReviewRequest,
		auth: Authentication,
	): IdResponse<Long> =
		IdResponse(
			createQuizReviewUseCase.create(
				CreateQuizReviewCommand(
					score = body.score,
					difficultyLevel = body.difficultyLevel,
					comment = body.comment,
					authId = UUIDUtils.stringToUUID(auth.name),
					quizId = body.quizId,
				),
			),
		)
}