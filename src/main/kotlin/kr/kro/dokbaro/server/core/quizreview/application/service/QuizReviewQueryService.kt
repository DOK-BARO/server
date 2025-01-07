package kr.kro.dokbaro.server.core.quizreview.application.service

import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.common.dto.response.PageResponse
import kr.kro.dokbaro.server.core.quizreview.application.port.input.FindQuizReviewSummaryUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.FindQuizReviewTotalScoreUseCase
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.FindQuizReviewSummaryCommand
import kr.kro.dokbaro.server.core.quizreview.application.port.out.CountQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewSummaryPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewTotalScorePort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.CountQuizReviewCondition
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.ReadQuizReviewSummaryCondition
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummary
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewSummarySortKeyword
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewTotalScore
import org.springframework.stereotype.Service

@Service
class QuizReviewQueryService(
	private val readQuizReviewTotalScorePort: ReadQuizReviewTotalScorePort,
	private val countQuizReviewPort: CountQuizReviewPort,
	private val readQuizReviewSummaryPort: ReadQuizReviewSummaryPort,
) : FindQuizReviewTotalScoreUseCase,
	FindQuizReviewSummaryUseCase {
	override fun findTotalScoreBy(quizId: Long): QuizReviewTotalScore {
		val elements: Collection<QuizReviewTotalScoreElement> = readQuizReviewTotalScorePort.findBy(quizId)

		if (elements.isEmpty()) {
			return QuizReviewTotalScore(
				quizId = quizId,
				averageStarRating = null,
				difficulty = null,
			)
		}

		val difficultyGroup: Map<Int, Int> =
			elements
				.map {
					it.difficultyLevel
				}.groupBy { it }
				.mapValues { (_, v) -> v.count() }
				.toMutableMap()
				.apply {
					getOrPut(1) { 0 }
					getOrPut(2) { 0 }
					getOrPut(3) { 0 }
				}

		return QuizReviewTotalScore(
			quizId = quizId,
			averageStarRating = elements.map { it.starRating }.average(),
			difficulty =
				difficultyGroup.mapValues { (_, v) ->
					QuizReviewTotalScore.DifficultyScore(v, v.toDouble() / difficultyGroup.values.sum())
				},
		)
	}

	override fun findAllQuizReviewSummaryBy(
		command: FindQuizReviewSummaryCommand,
		pageOption: PageOption<QuizReviewSummarySortKeyword>,
	): PageResponse<QuizReviewSummary> {
		val totalCount: Long = countQuizReviewPort.countBy(CountQuizReviewCondition(command.quizId))

		return PageResponse.of(
			totalElementCount = totalCount,
			pageSize = pageOption.size,
			data =
				readQuizReviewSummaryPort.findAllQuizReviewSummaryBy(
					condition = ReadQuizReviewSummaryCondition(command.quizId),
					pageOption = pageOption,
				),
		)
	}
}