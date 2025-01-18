package kr.kro.dokbaro.server.core.quizreview.application.service

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.quizreview.application.port.input.dto.FindQuizReviewSummaryCommand
import kr.kro.dokbaro.server.core.quizreview.application.port.out.CountQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadMyQuizReviewPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewSummaryPort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.ReadQuizReviewTotalScorePort
import kr.kro.dokbaro.server.core.quizreview.application.port.out.dto.QuizReviewTotalScoreElement
import kr.kro.dokbaro.server.core.quizreview.query.MyQuizReview
import kr.kro.dokbaro.server.core.quizreview.query.QuizReviewTotalScore

class QuizReviewQueryServiceTest :
	StringSpec({

		val readQuizReviewTotalScorePort = mockk<ReadQuizReviewTotalScorePort>()
		val countQuizReviewPort = mockk<CountQuizReviewPort>()
		val readQuizReviewSummaryPort = mockk<ReadQuizReviewSummaryPort>()
		val readMyQuizReviewPort = mockk<ReadMyQuizReviewPort>()

		val quizReviewQueryService =
			QuizReviewQueryService(
				readQuizReviewTotalScorePort,
				countQuizReviewPort,
				readQuizReviewSummaryPort,
				readMyQuizReviewPort,
			)

		"퀴즈 총 점수를 조회한다" {
			every { readQuizReviewTotalScorePort.findBy(any()) } returns
				listOf(
					QuizReviewTotalScoreElement(1, 3, 3),
					QuizReviewTotalScoreElement(1, 2, 2),
					QuizReviewTotalScoreElement(1, 1, 1),
				)

			quizReviewQueryService.findTotalScoreBy(1) shouldNotBe null
		}

		"퀴즈 총 점수 조회 시 선택한 난이도가 없으면 0개로 보여준다" {
			every { readQuizReviewTotalScorePort.findBy(any()) } returns
				listOf(QuizReviewTotalScoreElement(1, 3, 1))

			quizReviewQueryService.findTotalScoreBy(1).difficulty!![2] shouldBe
				QuizReviewTotalScore.DifficultyScore(selectCount = 0, selectRate = 0.0)
			quizReviewQueryService.findTotalScoreBy(1).difficulty!![3] shouldBe
				QuizReviewTotalScore.DifficultyScore(selectCount = 0, selectRate = 0.0)

			every { readQuizReviewTotalScorePort.findBy(any()) } returns
				listOf(QuizReviewTotalScoreElement(1, 3, 2))
			quizReviewQueryService.findTotalScoreBy(1).difficulty!![1] shouldBe
				QuizReviewTotalScore.DifficultyScore(selectCount = 0, selectRate = 0.0)
		}

		"퀴즈 총 점수 조회 시 quiz id에 해당하는 quiz가 없으면 난이도 및 별점에 null을 반환한다" {
			every { readQuizReviewTotalScorePort.findBy(any()) } returns emptyList()

			quizReviewQueryService.findTotalScoreBy(1) shouldBe
				QuizReviewTotalScore(
					quizId = 1,
					averageStarRating = null,
					difficulty = null,
				)
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

		"내가 작성한 퀴즈 리뷰를 조회한다" {
			every { readMyQuizReviewPort.findMyReviewBy(any(), any()) } returns
				MyQuizReview(
					id = 1L,
					starRating = 5,
					difficultyLevel = 3,
					comment = "This quiz was well-designed and challenging!",
					quizId = 101L,
				)

			quizReviewQueryService.findMyReviewBy(1, 1) shouldNotBe null
		}
	})