package kr.kro.dokbaro.server.core.bookquiz.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.QuizTypeMissMatchException
import kr.kro.dokbaro.server.fixture.domain.quizQuestionFixture

class QuizQuestionsTest :
	StringSpec({

		"질문 목록을 수정한다" {
			val quizQuestions = QuizQuestions(mutableListOf(quizQuestionFixture(id = 1)))

			quizQuestions.updateQuestions(listOf(quizQuestionFixture(id = 1), quizQuestionFixture(id = 2)))

			quizQuestions.getQuestions().size shouldBe 2
		}

		"질문 수정 시 타입을 변경하면 예외를 반환한다" {
			val quizQuestions =
				QuizQuestions(
					mutableListOf(
						quizQuestionFixture(
							id = 1,
							answer =
								GradeSheetFactory.create(
									QuizType.MULTIPLE_CHOICE,
									AnswerSheet(listOf("2", "4")),
								),
						),
					),
				)

			shouldThrow<QuizTypeMissMatchException> {
				quizQuestions.updateQuestions(
					listOf(
						quizQuestionFixture(
							id = 1,
							answer =
								GradeSheetFactory.create(
									QuizType.OX,
									AnswerSheet(listOf("O")),
								),
						),
					),
				)
			}
		}
	})