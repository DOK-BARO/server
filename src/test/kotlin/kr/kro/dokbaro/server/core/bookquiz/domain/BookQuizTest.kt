package kr.kro.dokbaro.server.core.bookquiz.domain

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.IllegalSubmitSheetFormatException
import kr.kro.dokbaro.server.core.bookquiz.domain.exception.NotFoundQuestionException
import kr.kro.dokbaro.server.fixture.domain.bookQuizFixture
import kr.kro.dokbaro.server.fixture.domain.quizQuestionFixture

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

		"정답 및 설명을 반환한다" {
			val bookQuiz =
				bookQuizFixture(
					questions =
						listOf(
							quizQuestionFixture(id = 1),
						),
				)

			bookQuiz.getAnswer(1) shouldNotBe null
		}

		"퀴즈 질문 수정을 진행한다." {
			val bookQuiz =
				bookQuizFixture(
					questions =
						listOf(
							quizQuestionFixture(id = 1),
						),
				)

			bookQuiz.updateQuestions(
				listOf(
					QuizQuestion(
						content = "it.content",
						selectOptions = listOf(),
						answer =
							QuestionAnswer(
								explanationContent = "it.answerExplanationContent",
								explanationImages = listOf(),
								gradeSheet =
									GradeSheetFactory.create(
										type = QuizType.OX,
										sheet = AnswerSheet(listOf("O")),
									),
							),
					),
				),
				1,
			)

			bookQuiz.questions.getQuestions().size shouldBe 1
			bookQuiz.questions
				.getQuestions()
				.first()
				.answer.explanationImages
				.shouldBeEmpty()
		}
		"설명 조회 시 question에 해당하는 ID가 없으면 예외를 반환한다" {
			val bookQuiz = bookQuizFixture()

			shouldThrow<NotFoundQuestionException> {
				bookQuiz.getAnswer(100)
			}
		}

		"단일 채점을 수행한다" {
			val bookQuiz =
				bookQuizFixture(
					questions =
						listOf(
							quizQuestionFixture(
								id = 1,
								answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("O"))),
							),
						),
				)

			bookQuiz.grade(1, AnswerSheet(listOf("O"))) shouldBe GradeResult(true)
			bookQuiz.grade(1, AnswerSheet(listOf("X"))) shouldBe GradeResult(false)
		}

		"단일 채점 수행 시 id 에 해당하는 question이 없으면 예외를 반환한다" {
			val bookQuiz =
				bookQuizFixture(
					questions =
						listOf(
							quizQuestionFixture(
								id = 1,
								answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("O"))),
							),
						),
				)

			shouldThrow<NotFoundQuestionException> {
				bookQuiz.grade(10, AnswerSheet(listOf("X")))
			}
		}

		"전체 채점을 수행한다" {
			val bookQuiz =
				bookQuizFixture(
					questions =
						listOf(
							quizQuestionFixture(
								id = 1,
								answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("O"))),
							),
							quizQuestionFixture(
								id = 2,
								answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("X"))),
							),
						),
				)

			bookQuiz.gradeAll(
				mapOf(
					1L to AnswerSheet(listOf("O")),
					2L to AnswerSheet(listOf("O")),
				),
			) shouldBe mapOf(1L to GradeResult(true), 2L to GradeResult(false))
		}

		"전체 채점 시 제출 문항 수와 문제 수가 다르면 예외를 반환한다" {
			val bookQuiz =
				bookQuizFixture(
					questions =
						listOf(
							quizQuestionFixture(
								id = 1,
								answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("O"))),
							),
							quizQuestionFixture(
								id = 2,
								answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("X"))),
							),
						),
				)

			shouldThrow<IllegalSubmitSheetFormatException> {
				bookQuiz.gradeAll(
					mapOf(
						1L to AnswerSheet(listOf("O")),
					),
				)
			}

			shouldThrow<IllegalSubmitSheetFormatException> {
				bookQuiz.gradeAll(
					mapOf(
						1L to AnswerSheet(listOf("O")),
						2L to AnswerSheet(listOf("O")),
						3L to AnswerSheet(listOf("O")),
					),
				)
			}
		}

		"전체 채점 시 quiz에 속한 question id가 아니면 예외를 반환한다" {
			val bookQuiz =
				bookQuizFixture(
					questions =
						listOf(
							quizQuestionFixture(
								id = 1,
								answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("O"))),
							),
							quizQuestionFixture(
								id = 2,
								answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("X"))),
							),
						),
				)

			shouldThrow<NotFoundQuestionException> {
				bookQuiz.gradeAll(
					mapOf(
						1L to AnswerSheet(listOf("O")),
						3L to AnswerSheet(listOf("O")),
					),
				)
			}
		}

		"질문 개수를 반환한다" {
			val bookQuiz =
				bookQuizFixture(
					questions =
						listOf(
							quizQuestionFixture(
								id = 1,
								answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("O"))),
							),
							quizQuestionFixture(
								id = 2,
								answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("X"))),
							),
						),
				)

			bookQuiz.getQuestionCount() shouldBe 2
		}

		"book quiz update 시 본인이 작성한 퀴즈가 아니면 contributor에 기재한다" {
			val creatorId = 5L
			val modifierId = 50L
			val questions =
				listOf(
					quizQuestionFixture(
						id = 1,
						answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("O"))),
					),
					quizQuestionFixture(
						id = 2,
						answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("X"))),
					),
				)

			val bookQuiz =
				bookQuizFixture(
					creatorId = creatorId,
					questions =
					questions,
				)

			bookQuiz.updateQuestions(
				newQuestions = questions,
				modifierId = modifierId,
			)

			bookQuiz.contributorIds shouldContain modifierId
		}

		"book quiz update 시 본인이 만든 퀴즈를 업데이트 할 때는 contributor에 기재하지 않는다" {
			val creatorId = 5L
			val questions =
				listOf(
					quizQuestionFixture(
						id = 1,
						answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("O"))),
					),
					quizQuestionFixture(
						id = 2,
						answer = GradeSheetFactory.create(QuizType.OX, AnswerSheet(listOf("X"))),
					),
				)

			val bookQuiz =
				bookQuizFixture(
					creatorId = creatorId,
					questions =
					questions,
				)

			bookQuiz.updateQuestions(
				newQuestions = questions,
				modifierId = creatorId,
			)

			bookQuiz.contributorIds.shouldBeEmpty()
		}
	})