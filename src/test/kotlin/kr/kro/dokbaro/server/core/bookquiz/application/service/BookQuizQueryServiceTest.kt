package kr.kro.dokbaro.server.core.bookquiz.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.common.dto.option.PageOption
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.CountBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizAnswerPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizExplanationPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizSummaryPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadMyBookQuizSummaryPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadUnsolvedGroupBookQuizPort
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.NotFoundQuestionException
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizExplanation
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.MyBookQuizSummary
import kr.kro.dokbaro.server.core.bookquiz.query.condition.MyBookQuizSummaryFilterCondition
import kr.kro.dokbaro.server.fixture.domain.bookQuizAnswerFixture
import java.time.LocalDateTime

class BookQuizQueryServiceTest :
	StringSpec({
		val readBookQuizQuestionPort = mockk<ReadBookQuizQuestionPort>()
		val readBookQuizAnswerPort = mockk<ReadBookQuizAnswerPort>()
		val countBookQuizPort = mockk<CountBookQuizPort>()
		val readBookQuizSummaryPort = mockk<ReadBookQuizSummaryPort>()
		val findUnsolvedGroupBookQuizPort = mockk<ReadUnsolvedGroupBookQuizPort>()
		val readMyBookQuizSummaryPort = mockk<ReadMyBookQuizSummaryPort>()
		val readBookQuizExplanationPort = mockk<ReadBookQuizExplanationPort>()

		val bookQuizQueryService =
			BookQuizQueryService(
				readBookQuizQuestionPort,
				readBookQuizAnswerPort,
				countBookQuizPort,
				readBookQuizSummaryPort,
				findUnsolvedGroupBookQuizPort,
				readMyBookQuizSummaryPort,
				readBookQuizExplanationPort,
			)

		"퀴즈를 조회한다" {
			every { readBookQuizQuestionPort.findBookQuizQuestionsBy(any()) } returns
				BookQuizQuestions(
					1,
					"java 정석 1차",
					60,
					null,
					listOf(
						BookQuizQuestions.Question(
							1,
							"조정석의 아내 이름은?",
							QuizType.MULTIPLE_CHOICE_MULTIPLE_ANSWER,
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
			every { readBookQuizSummaryPort.findAllBookQuizSummary(any(), any()) } returns listOf()

			bookQuizQueryService.findAllBookQuizSummary(
				1,
				PageOption.of(),
			) shouldNotBe null
		}

		"스터디 그룹 퀴즈 중 본인이 안 푼 문제 목록을 조회한다" {
			every { findUnsolvedGroupBookQuizPort.findAllUnsolvedQuizzes(any(), any(), any()) } returns listOf()

			bookQuizQueryService.findAllUnsolvedQuizzes(3, 1, PageOption.of()) shouldNotBe null
		}

		"내가 제작한 퀴즈 목록을 조회한다" {
			every { readMyBookQuizSummaryPort.findAllMyBookQuiz(any(), any(), any()) } returns
				listOf(
					MyBookQuizSummary(
						id = 1L,
						bookImageUrl = "https://example.com/book_image.jpg",
						title = "Effective Kotlin",
						description = "Description",
						updatedAt = LocalDateTime.now(),
						temporary = true,
					),
				)

			bookQuizQueryService.findMyBookQuiz(
				1,
				PageOption.of(),
				MyBookQuizSummaryFilterCondition(),
			) shouldNotBe
				null
		}

		"퀴즈 설명을 조회한다" {
			every { readBookQuizExplanationPort.findExplanationBy(any()) } returns null

			shouldThrow<NotFoundQuizException> {
				bookQuizQueryService.findExplanationBy(1)
			}

			every { readBookQuizExplanationPort.findExplanationBy(any()) } returns
				BookQuizExplanation(
					id = 1L,
					title = "Sample Quiz Explanation",
					description = "This is a detailed explanation for a sample book quiz.",
					createdAt = LocalDateTime.now(),
					creator =
						BookQuizExplanation.Creator(
							id = 101L,
							nickname = "QuizMaster",
							profileImageUrl = "https://example.com/profile.jpg",
						),
					book =
						BookQuizExplanation.Book(
							id = 201L,
							title = "Effective Kotlin",
							imageUrl = "https://example.com/book.jpg",
						),
				)

			bookQuizQueryService.findExplanationBy(1) shouldNotBe null
		}
	})