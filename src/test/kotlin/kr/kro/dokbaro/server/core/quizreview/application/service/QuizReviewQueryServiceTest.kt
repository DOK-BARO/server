package kr.kro.dokbaro.server.core.quizreview.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.FindQuizReviewSummaryCommand
import kr.kro.dokbaro.server.core.quizreview.application.port.out.CountQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewSummaryPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewTotalScorePort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement

class QuizReviewQueryServiceTest :
	StringSpec({

		val readQuizReviewTotalScorePort = mockk<ReadQuizReviewTotalScorePort>()
		val countQuizReviewPort = mockk<CountQuizReviewPort>()
		val readQuizReviewSummaryPort = mockk<ReadQuizReviewSummaryPort>()

		val quizReviewQueryService =
			QuizReviewQueryService(readQuizReviewTotalScorePort, countQuizReviewPort, readQuizReviewSummaryPort)

		"퀴즈 총 점수를 조회한다" {
			every { readQuizReviewTotalScorePort.findBy(any()) } returns
				listOf(
					QuizReviewTotalScoreElement(1, 3, 3),
					QuizReviewTotalScoreElement(1, 2, 2),
					QuizReviewTotalScoreElement(1, 1, 1),
				)

			quizReviewQueryService.findTotalScoreBy(1) shouldNotBe null
		}

		"퀴즈 총 점수 조회 시 quiz id에 해당하는 quiz가 없으면 예외를 반환한다" {
			every { readQuizReviewTotalScorePort.findBy(any()) } returns emptyList()

			shouldThrow<NotFoundQuizException> {
				quizReviewQueryService.findTotalScoreBy(1)
			}
		}

		"요약본을 조회한다" {
			every { countQuizReviewPort.countBy(any()) } returns 100
			every { readQuizReviewSummaryPort.findAllQuizReviewSummaryBy(any(), any()) } returns listOf()

			quizReviewQueryService.findAllQuizReviewSummaryBy(
				FindQuizReviewSummaryCommand(
					quizId = 10,
				),
				PageOption.of(),
			) shouldNotBe null
		}
	})