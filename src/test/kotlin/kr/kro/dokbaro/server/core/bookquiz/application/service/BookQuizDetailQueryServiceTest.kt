package kr.kro.dokbaro.server.core.bookquiz.application.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadBookQuizDetailQuestionsPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.ReadQuestionElementPort
import kr.kro.dokbaro.server.core.bookquiz.application.port.out.dto.BookQuizDetailQuestions
import kr.kro.dokbaro.server.core.bookquiz.application.service.exception.NotFoundQuizException
import kr.kro.dokbaro.server.core.bookquiz.domain.AccessScope
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType

class BookQuizDetailQueryServiceTest :
	StringSpec({

		val readBookQuizDetailQuestionsPort: ReadBookQuizDetailQuestionsPort = mockk()
		val readQuestionElementPort: ReadQuestionElementPort = mockk()

		val bookQuizDetailQueryService: BookQuizDetailQueryService =
			BookQuizDetailQueryService(
				readBookQuizDetailQuestionsPort,
				readQuestionElementPort,
			)

		beforeEach {
			every { readBookQuizDetailQuestionsPort.findBookQuizDetailBy(any()) } returns
				BookQuizDetailQuestions(
					id = 1L,
					title = "Kotlin Programming Quiz",
					description = "A quiz to test your knowledge of Kotlin programming concepts.",
					bookId = 101L,
					questions =
						listOf(
							BookQuizDetailQuestions.Question(
								id = 1L,
								content = "What is the primary purpose of a data class in Kotlin?",
								answerExplanationContent = "A data class in Kotlin is used to hold data and .",
								answerType = QuizType.MULTIPLE_CHOICE_MULTIPLE_ANSWER,
							),
							BookQuizDetailQuestions.Question(
								id = 2L,
								content = "Which keyword is used to declare an immutable variable in Kotlin?",
								answerExplanationContent = "In Kotlin, the 'val' keyword is used to declare variables .",
								answerType = QuizType.MULTIPLE_CHOICE_MULTIPLE_ANSWER,
							),
						),
					studyGroupId = 201L,
					timeLimitSecond = 1800,
					viewScope = AccessScope.EVERYONE,
					editScope = AccessScope.EVERYONE,
					temporary = true,
				)
		}

		"퀴즈 상세를 조회한다" {
			every { readQuestionElementPort.findSelectOptionBy(any()) } returns
				mapOf(1L to listOf("hello"), 2L to listOf("world"))
			every { readQuestionElementPort.findAnswerExplanationImageBy(any()) } returns
				mapOf(1L to listOf("hello"), 2L to listOf("world"))
			every { readQuestionElementPort.findAnswersBy(any()) } returns
				mapOf(1L to listOf("hello"), 2L to listOf("world"))

			bookQuizDetailQueryService.findBookQuizDetailBy(1) shouldNotBe null
		}

		"quiz에 해당하는 값이 없으면 예외를 반환한다" {
			every { readBookQuizDetailQuestionsPort.findBookQuizDetailBy(any()) } returns null
			shouldThrow<NotFoundQuizException> {
				bookQuizDetailQueryService.findBookQuizDetailBy(1L)
			}
		}

		"question element에 값이 없으면 빈 컬렉션을 제공한다" {
			every { readQuestionElementPort.findSelectOptionBy(any()) } returns emptyMap()
			every { readQuestionElementPort.findAnswerExplanationImageBy(any()) } returns emptyMap()
			every { readQuestionElementPort.findAnswersBy(any()) } returns emptyMap()

			val result = bookQuizDetailQueryService.findBookQuizDetailBy(1)

			result.questions
				.first()
				.selectOptions
				.shouldBeEmpty()
			result.questions
				.first()
				.answerExplanationImages
				.shouldBeEmpty()
			result.questions
				.first()
				.answers
				.shouldBeEmpty()
		}
	})