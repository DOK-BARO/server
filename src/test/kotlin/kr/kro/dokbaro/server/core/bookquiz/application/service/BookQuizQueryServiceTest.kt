package kr.kro.dokbaro.server.core.bookquiz.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizQuestionPort
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.SelectOption
import kr.kro.dokbaro.server.core.bookquiz.query.BookQuizQuestions
import kr.kro.dokbaro.server.core.bookquiz.query.Question

class BookQuizQueryServiceTest :
	StringSpec({
		val readBookQuizQuestionPort = mockk<ReadBookQuizQuestionPort>()

		val bookQuizQueryService = BookQuizQueryService(readBookQuizQuestionPort)

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
	})