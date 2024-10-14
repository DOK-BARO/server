package kr.kro.dokbaro.server.core.bookquiz.domain

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture

class BookQuizTest :
	StringSpec({

		"기본 옵션을 수정한다" {
			val bookQuiz = bookQuizFixture()

			bookQuiz.updateBasicOption(
				title = "new title",
				description = "new description",
				bookId = 4,
				timeLimitSecond = 5,
				viewScope = AccessScope.EVERYONE,
				editScope = AccessScope.CREATOR,
			)

			bookQuiz.title shouldBe "new title"
			bookQuiz.description shouldBe "new description"
			bookQuiz.bookId shouldBe 4
			bookQuiz.timeLimitSecond shouldBe 5
			bookQuiz.viewScope shouldBe AccessScope.EVERYONE
			bookQuiz.editScope shouldBe AccessScope.CREATOR
		}

		"기본 옵션 수정 시 아무 값을 넣지 않으면 기존 값을 그대로 유지한다" {
			val bookQuiz = bookQuizFixture()

			val beforeTitle = bookQuiz.title
			val beforeDescription = bookQuiz.description
			val beforeBookId = bookQuiz.bookId
			val beforeTimeLimitSecond = bookQuiz.timeLimitSecond
			val beforeViewScope = bookQuiz.viewScope
			val beforeEditScope = bookQuiz.editScope

			bookQuiz.updateBasicOption()

			bookQuiz.title shouldBe beforeTitle
			bookQuiz.description shouldBe beforeDescription
			bookQuiz.bookId shouldBe beforeBookId
			bookQuiz.timeLimitSecond shouldBe beforeTimeLimitSecond
			bookQuiz.viewScope shouldBe beforeViewScope
			bookQuiz.editScope shouldBe beforeEditScope
		}

		"스터디 그룹을 수정한다" {
			val bookQuiz = bookQuizFixture()
			val newGroup = listOf<Long>(1, 2, 4)

			bookQuiz.updateStudyGroups(newGroup)

			bookQuiz.studyGroups shouldBe newGroup
		}
	})