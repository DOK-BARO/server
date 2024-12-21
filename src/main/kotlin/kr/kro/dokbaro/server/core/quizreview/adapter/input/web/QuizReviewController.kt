package kr.kro.dokbaro.server.core.quizreview.adapter.input.web

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.common.dto.response.IdResponse
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.quizreview.adapter.input.web.dto.CreateQuizReviewRequest
import kr.kro.dokbaro.server.core.quizreview.adapter.input.web.dto.UpdateQuizReviewRequest
import kr.kro.dokbaro.server.core.quizreview.application.port.input.CreateQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.DeleteQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.FindQuizReviewSummaryUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.FindQuizReviewTotalScoreUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.UpdateQuizReviewUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.CreateQuizReviewCommand
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.FindQuizReviewSummaryCommand
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.UpdateQuizReviewCommand
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummarySortKeyword
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewTotalScore
import kr.kro.dokbaro.server.security.annotation.Login
import kr.kro.dokbaro.server.security.details.DokbaroUser
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
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
@RequestMapping("/quiz-reviews")
class QuizReviewController(
	private val createQuizReviewUseCase: CreateQuizReviewUseCase,
	private val findQuizReviewTotalScoreUseCase: FindQuizReviewTotalScoreUseCase,
	private val findQuizReviewSummaryUseCase: FindQuizReviewSummaryUseCase,
	private val updateQuizReviewUseCase: UpdateQuizReviewUseCase,
	private val deleteQuizReviewUseCase: DeleteQuizReviewUseCase,
) {
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun createQuizReview(
		@RequestBody body: CreateQuizReviewRequest,
		@Login user: DokbaroUser,
	): IdResponse<Long> =
		IdResponse(
			createQuizReviewUseCase.create(
				CreateQuizReviewCommand(
					starRating = body.starRating,
					difficultyLevel = body.difficultyLevel,
					comment = body.comment,
					creatorId = user.id,
					quizId = body.quizId,
				),
			),
		)

	@PutMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun updateQuizReview(
		@PathVariable id: Long,
		@RequestBody body: UpdateQuizReviewRequest,
	) {
		updateQuizReviewUseCase.update(
			UpdateQuizReviewCommand(
				id = id,
				starRating = body.starRating,
				difficultyLevel = body.difficultyLevel,
				comment = body.comment,
			),
		)
	}

	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun deleteQuizReview(
		@PathVariable id: Long,
	) {
		deleteQuizReviewUseCase.delete(id)
	}

	@GetMapping("/total-score")
	fun getTotalScore(
		@RequestParam quizId: Long,
	): QuizReviewTotalScore = findQuizReviewTotalScoreUseCase.findTotalScoreBy(quizId)

	@GetMapping
	fun getReviews(
		@RequestParam page: Long,
		@RequestParam size: Long,
		@RequestParam quizId: Long,
		@RequestParam sort: QuizReviewSummarySortKeyword,
		@RequestParam direction: SortDirection,
	): PageResponse<QuizReviewSummary> =
		findQuizReviewSummaryUseCase.findAllQuizReviewSummaryBy(
			FindQuizReviewSummaryCommand(
				quizId = quizId,
			),
			PageOption.of(
				page = page,
				size = size,
				sort = sort,
				direction = direction,
			),
		)
}