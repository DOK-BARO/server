package kr.kro.dokbaro.server.core.bookquiz.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.dto.option.SortDirection
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.CountBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizAnswerPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizSummaryPort
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuestionException
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizSummarySortOption
import kr.kro.dokbaro.server.core.bookquiz.query.Question
import kr.kro.dokbaro.server.fixture.domain.bookQuizAnswerFixture

class BookQuizQueryServiceTest :
	StringSpec({
		val readBookQuizQuestionPort = mockk<ReadBookQuizQuestionPort>()
		val readBookQuizAnswerPort = mockk<ReadBookQuizAnswerPort>()
		val countBookQuizPort = mockk<CountBookQuizPort>()
		val readBookQuizSummaryPort = mockk<ReadBookQuizSummaryPort>()

		val bookQuizQueryService =
			BookQuizQueryService(
				readBookQuizQuestionPort,
				readBookQuizAnswerPort,
				countBookQuizPort,
				readBookQuizSummaryPort,
			)

		"퀴즈를 조회한다" {
			every { readBookQuizQuestionPort.findBookQuizQuestionsBy(any()) } returns
				BookQuizQuestions(
					1,
					"java 정석 1차",
					60,
					listOf(
						Question(
							1,
							"조정석의 아내 이름은?",
							QuizType.MULTIPLE_CHOICE,
							listOf(
								SelectOption("거미"),
								SelectOption("개미"),
								SelectOption("고미"),
							),
						),
					),
				)

			bookQuizQueryService.findBookQuizQuestionsBy(1) shouldNotBe null
		}

		"ID에 해당하는 퀴즈가 없으면 예외를 발생한다" {
			every { readBookQuizQuestionPort.findBookQuizQuestionsBy(any()) } returns null

			shouldThrow<NotFoundQuizException> {
				bookQuizQueryService.findBookQuizQuestionsBy(5)
			}
		}

		"question id 에 해당하는 답변을 가져온다" {
			every { readBookQuizAnswerPort.findBookQuizAnswerBy(any()) } returns bookQuizAnswerFixture()

			bookQuizQueryService.findBookQuizAnswer(4) shouldNotBe null
		}

		"답변 조회 시 question id에 해당하는 question이 없으면 예외를 반환한다" {
			every { readBookQuizAnswerPort.findBookQuizAnswerBy(any()) } returns null

			shouldThrow<NotFoundQuestionException> {
				bookQuizQueryService.findBookQuizAnswer(5)
			}
		}

		"퀴즈 요약본을 조회한다" {
			every { countBookQuizPort.countBookQuizBy(any()) } returns 100
			every { readBookQuizSummaryPort.findAllBookQuizSummary(any(), any(), any()) } returns listOf()

			bookQuizQueryService.findAllBookQuizSummary(
				1,
				1,
				1,
				BookQuizSummarySortOption.CREATED_AT,
				SortDirection.ASC,
			) shouldNotBe null
		}
	})