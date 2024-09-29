package kr.kro.dokbaro.server.core.bookquiz.domain.answerstyle

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet
import kr.kro.dokbaro.server.core.bookquiz.domain.QuizType
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalRegisterSheetFormatException
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalSubmitSheetFormatException

class MultipleChoiceAnswerTest :
	StringSpec({

		"답변의 타입을 확인한다" {
			MultipleChoiceAnswer.from(AnswerSheet(listOf("1", "2", "3"))).getType() shouldBe QuizType.MULTIPLE_CHOICE
		}

		"답변 목록을 가져온다" {
			val correct = listOf("1", "2", "3")
			MultipleChoiceAnswer.from(AnswerSheet(correct)).getAnswers() shouldBe correct
		}

		"일치 여부를 확인한다" {
			val correct = AnswerSheet(listOf("1", "2", "3"))
			val answer = MultipleChoiceAnswer.from(correct)

			answer.match(correct) shouldBe true
			answer.match(AnswerSheet(listOf("4"))) shouldBe false
			answer.match(AnswerSheet(listOf("1"))) shouldBe false
			answer.match(AnswerSheet(listOf("1", "2"))) shouldBe false
			answer.match(AnswerSheet(listOf("1", "2", "4"))) shouldBe false
			answer.match(AnswerSheet(listOf("1", "2", "3", "4"))) shouldBe false
		}

		"답변 제출 시 숫자 형태가 아닌 값을 제출 시 예외를 반환한다" {
			shouldThrow<IllegalSubmitSheetFormatException> {
				MultipleChoiceAnswer.from(AnswerSheet(listOf("1"))).match(AnswerSheet(listOf("A")))
			}
		}

		"숫자 타입이 아닌 값이 생성되면 예외를 반환한다" {
			shouldThrow<IllegalRegisterSheetFormatException> {
				MultipleChoiceAnswer.from(AnswerSheet(listOf("hello")))
			}
		}
	})