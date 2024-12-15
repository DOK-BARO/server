package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.GradeSheetFactory
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalRegisterSheetFormatException
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalSubmitSheetFormatException

class MultipleChoiceSingleAnswerTest :
	StringSpec({

		"답변의 타입을 확인한다" {
			val correct = AnswerSheet(listOf("1"))
			val answer =
				GradeSheetFactory
					.create(QuizType.MULTIPLE_CHOICE_SINGLE_ANSWER, correct)

			answer.getType() shouldBe QuizType.MULTIPLE_CHOICE_SINGLE_ANSWER
		}

		"답변 목록을 가져온다" {
			val correct = AnswerSheet(listOf("1"))
			val answer = MultipleChoiceSingleAnswer.from(correct)

			answer.getAnswers().size shouldBe 1
			answer.getAnswers() shouldBe correct.answer
		}

		"일치 여부를 확인한다" {
			val correct = AnswerSheet(listOf("1"))
			val answer = MultipleChoiceSingleAnswer.from(correct)

			answer.isCorrect(correct) shouldBe true
			answer.isCorrect(AnswerSheet(listOf("3"))) shouldBe false
		}

		"답변 제출 시 숫자가 아닌 값이 입력되면 예외를 반환한다" {
			val correct = AnswerSheet(listOf("1"))
			val answer = MultipleChoiceSingleAnswer.from(correct)

			shouldThrow<IllegalSubmitSheetFormatException> {
				answer.isCorrect(AnswerSheet(listOf("N")))
			}
		}

		"답변 제출 시 객관식 답이 하나가 아니면 예외를 반환한다" {
			val correct = AnswerSheet(listOf("1"))
			val answer = MultipleChoiceSingleAnswer.from(correct)

			shouldThrow<IllegalSubmitSheetFormatException> {
				answer.isCorrect(AnswerSheet(listOf("1", "2")))
			}
		}

		"답변 제출 시 숫자가 아닌 값을 제출하면 예외를 반환한다" {
			shouldThrow<IllegalRegisterSheetFormatException> {
				MultipleChoiceSingleAnswer.from(AnswerSheet(listOf("N")))
			}
		}

		"답변 제출 시 답이 하나가 아니면 예외를 반환한다" {

			shouldThrow<IllegalRegisterSheetFormatException> {
				MultipleChoiceSingleAnswer.from(AnswerSheet(listOf("1", "2")))
			}
		}
	})