package kr.kro.dokbaro.server.core.solvingquiz.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.core.bookquiz.domain.AnswerSheet

class SolvingQuizTest :
	StringSpec({
		"sheet를 추가한 후 sheet 목록을 반환한다" {
			val solvingQuiz = SolvingQuiz(playerId = 1, quizId = 1)
			solvingQuiz.addSheet(3, AnswerSheet(listOf("asdf")))

			solvingQuiz.getSheets()[3] shouldNotBe null
		}
	})