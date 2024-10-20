package kr.kro.dokbaro.server.core.quizreview.adapter.input.web

import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.common.util.UUIDUtils
import kr.kro.dokbaro.server.core.quizreview.adapter.input.web.dto.CreateQuizReviewRequest
import kr.kro.dokbaro.server.core.quizreview.application.port.input.CreateQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.FindQuizReviewTotalScoreUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.CreateQuizReviewCommand
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewTotalScore
import org.springframework.http.HttpStatus
import org.springframework.security.core.Authentication
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/quiz-reviews")
class QuizReviewController(
	private val createQuizReviewUseCase: CreateQuizReviewUseCase,
	private val findQuizReviewTotalScoreUseCase: FindQuizReviewTotalScoreUseCase,
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

	@GetMapping("/total-score")
	fun getTotalScore(
		@RequestParam quizId: Long,
	): QuizReviewTotalScore = findQuizReviewTotalScoreUseCase.findTotalScoreBy(quizId)

	@GetMapping
	fun getReviews(
		@RequestParam quizId: Long,
		@RequestParam sort: String,
	): PageResponse<QuizReviewSummary> = TODO()
}